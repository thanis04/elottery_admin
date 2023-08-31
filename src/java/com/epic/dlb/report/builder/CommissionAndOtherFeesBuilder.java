/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.report.service.CoomonService;
import com.epic.dlb.report.service.SalesReportService;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Component("commissionAndOtherFeesBuilder")
public class CommissionAndOtherFeesBuilder {

    @Autowired
    private SalesReportService reportService;

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public JSONArray commissionAndOtherFeesReportData(Date fromDate, Date toDate, String lottery) throws Exception {
        JSONArray table = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("#00.00");

        Iterator<DlbSwtStPurchaseHistory> iterator = reportService.getTicketSalesReport(fromDate, toDate, lottery).iterator();

        while (iterator.hasNext()) {
            JSONObject row = new JSONObject();

            DlbSwtStPurchaseHistory next = iterator.next();

            row.put("lottery_name", next.getProductDescription());
            row.put("draw_no", next.getDrawNo());
            row.put("draw_date", dateFormat.format(next.getDrawDate()));
            row.put("tkt_reff", next.getSerialNo());
            row.put("cus_reff", next.getDlbSwtStWallet().getFirstName() + " " + next.getDlbSwtStWallet().getLastName());

            DlbSwtStTransaction stTransaction
                    = (DlbSwtStTransaction) genericRepository.get(next.getTxnId(), DlbSwtStTransaction.class);

            Double bacnkChrg = 0.0;
            if (stTransaction.getDlbSwtMtPaymentMethod().getCode() == SystemVarList.PAY_METHOD_CREDIT_CARD
                    || stTransaction.getDlbSwtMtPaymentMethod().getCode() == SystemVarList.PAY_METHOD_DEBIT_CARD) {
                bacnkChrg = 20 * (3.25 / 100);
            } else if (stTransaction.getDlbSwtMtPaymentMethod().getCode() == SystemVarList.PAY_METHOD_CASA) {
                bacnkChrg = 20 * (1.5 / 100);
            }

            row.put("bank_charge", decimalFormat.format(bacnkChrg));
            row.put("tkt_value", "20");
            row.put("cost", "16.25");
            row.put("commision", "3.75");
            row.put("mode_purchase", next.getPaymentMethod());
            row.put("currency", "LKR");

            table.add(row);

        }
        return table;

    }

    @Transactional
    public File generateExcel(Date fromDate, Date toDate, String lottery) throws Exception {
        File file = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "commission_report_" + dateFormat.format(fromDate) + "_" + dateFormat.format(toDate) + sdf2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Commission & other fees report", 0);

            s.addCell(new Label(0, 0, "lottery_name"));
            s.addCell(new Label(1, 0, "Draw No"));
            s.addCell(new Label(2, 0, "Draw Date"));
            s.addCell(new Label(3, 0, "Ticket Reference No"));
            s.addCell(new Label(4, 0, "Bank Charge"));
            s.addCell(new Label(5, 0, "Ticket Value"));
            s.addCell(new Label(6, 0, "Cost"));
            s.addCell(new Label(7, 0, "Commision"));
            s.addCell(new Label(8, 0, "Currency"));

            Iterator<DlbSwtStPurchaseHistory> iterator = reportService.getTicketSalesReport(fromDate, toDate, lottery).iterator();
            int genCount = 1;
            while (iterator.hasNext()) {

                DlbSwtStPurchaseHistory next = iterator.next();

                DlbSwtStTransaction stTransaction
                        = (DlbSwtStTransaction) genericRepository.get(next.getTxnId(), DlbSwtStTransaction.class);

                Double bacnkChrg = 0.0;
                if (stTransaction.getDlbSwtMtPaymentMethod().getCode() == SystemVarList.PAY_METHOD_CREDIT_CARD
                        || stTransaction.getDlbSwtMtPaymentMethod().getCode() == SystemVarList.PAY_METHOD_DEBIT_CARD) {
                    bacnkChrg = 20 * (3.25 / 100);
                } else if (stTransaction.getDlbSwtMtPaymentMethod().getCode() == SystemVarList.PAY_METHOD_CASA) {
                    bacnkChrg = 20 * (1.5 / 100);
                }

                s.addCell(new Label(0, genCount, next.getProductDescription()));
                s.addCell(new Label(1, genCount, next.getDrawNo()));
                s.addCell(new Label(2, genCount, dateFormat.format(next.getDrawDate())));
                s.addCell(new Label(3, genCount, next.getSerialNo()));
                s.addCell(new Label(4, genCount, decimalFormat.format(bacnkChrg)));
                s.addCell(new Label(5, genCount, "20.00"));
                s.addCell(new Label(6, genCount, "16.25"));
                s.addCell(new Label(7, genCount, "3.75"));
                s.addCell(new Label(8, genCount, "LKR"));
                genCount++;

            }

            w.write();
            w.close();

            return file;

        } catch (Exception e) {
            return null;
        } finally {
            CoomonService.deleteTmpFile(file.getName());
        }

    }
}
