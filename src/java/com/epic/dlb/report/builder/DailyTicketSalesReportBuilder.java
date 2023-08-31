/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.SalesTicket;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbWbProduct;
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
import java.util.List;
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
@Component("dailyTicketSalesReportBuilder")
public class DailyTicketSalesReportBuilder {

    @Autowired
    private SalesReportService reportService;

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public JSONArray dailyTicketSalesReportReportData(Date fromDate, Date toDate,
            String lottey, ReportSearchCriteriaDto dto) throws Exception {
        JSONArray table = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        Iterator<SalesTicket> iterator = reportService.
                getPaginatedTicketSalesReport(fromDate, toDate, lottey, dto).iterator();

        while (iterator.hasNext()) {
            JSONObject row = new JSONObject();

            SalesTicket next = iterator.next();

            row.put("lottery_name", next.getProductDescription());
            row.put("purchase_date", next.getCreatedDate());
            row.put("draw_no", next.getDrawNo());
            row.put("draw_date", next.getDrawDate());
            row.put("tkt_reff", next.getSerialNo());
            row.put("cus_reff", next.getFirstName() + " " + next.getLastName());
            row.put("tkt_value", "20");
            row.put("mode_purchase", next.getPayMethod());
            row.put("currency", "LKR");
            row.put("tr_id", next.getTxId());

            table.add(row);

        }

        return table;
    }

    @Transactional
    public File generateExcel(Date fromDate, Date toDate, String lottery,
            ReportSearchCriteriaDto dto) throws Exception {

        File file = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "ticket_sales_report_" + dateFormat.format(fromDate) + "_" + dateFormat.format(toDate) + sdf2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Daily ticket sales report", 0);

            s.addCell(new Label(0, 0, "Lottery"));
            s.addCell(new Label(1, 0, "Draw No"));
            s.addCell(new Label(2, 0, "Draw Date"));
            s.addCell(new Label(3, 0, "Ticket Reference No"));
            s.addCell(new Label(4, 0, "Purchase Date"));
            s.addCell(new Label(5, 0, "Customer Details"));
            s.addCell(new Label(6, 0, "Ticket Value"));
            s.addCell(new Label(7, 0, "Mode of Purchase"));
            s.addCell(new Label(8, 0, "Currency"));
            s.addCell(new Label(9, 0, "Transaction ID"));

            Iterator<SalesTicket> iterator = reportService.getPaginatedTicketSalesReport(fromDate, toDate, lottery, dto).iterator();
            int genCount = 1;
            while (iterator.hasNext()) {

                SalesTicket next = iterator.next();

                s.addCell(new Label(0, genCount, next.getProductDescription()));
                s.addCell(new Label(1, genCount, next.getDrawNo()));
                s.addCell(new Label(2, genCount, next.getDrawDate()));
                s.addCell(new Label(2, genCount, next.getDrawDate()));
                s.addCell(new Label(3, genCount, next.getSerialNo()));
                s.addCell(new Label(4, genCount, next.getCreatedDate()));
                s.addCell(new Label(5, genCount, next.getFirstName() + " " + next.getLastName()));
                s.addCell(new Label(6, genCount, next.getTicketPrice()));
                s.addCell(new Label(7, genCount, next.getPayMethod()));
                s.addCell(new Label(8, genCount, "LKR"));
                s.addCell(new Label(9, genCount, next.getTxId()));
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
