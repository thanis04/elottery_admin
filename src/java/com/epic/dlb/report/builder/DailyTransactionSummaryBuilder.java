/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.report.service.CoomonService;
import com.epic.dlb.report.service.TransactionReportService;
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
@Component("dailyTransactionSummaryBuilder")
public class DailyTransactionSummaryBuilder {

    @Autowired
    private TransactionReportService transactionReportService;

    @Transactional
    public JSONArray dailyTransactionSummaryReportData(String fromDate, String toDate) throws Exception {

        JSONArray table = new JSONArray();
        DecimalFormat decimalFormat = new DecimalFormat("**00.00");
        Iterator<Object[]> list = transactionReportService.getTransactonSummeryByDate(fromDate, toDate).iterator();

        while (list.hasNext()) {
            Object[] next = list.next();

            JSONObject row = new JSONObject();

            row.put("date", next[0]);
            Double tot_collection = (Double) next[2];
            row.put("tot_collection", tot_collection);

            Double totalCommision = tot_collection * SystemVarList.TRANSACTION_COMMESION;

            row.put("tot_commission", totalCommision);
            row.put("net_amt_transfered", tot_collection - totalCommision);
            row.put("win_amt_transfered", "0.00");
            row.put("tkt_value", tot_collection);

            Double card = (Double) next[3];
            Double casa = (Double) next[4];
            Double totalBankCh = card + casa;
            row.put("tot_bank_charges", totalBankCh);
            System.out.println(totalBankCh);

            table.add(row);

        }

        return table;
    }

    @Transactional
    public File generateExcel(String fromDate, String toDate) throws Exception {

        File file = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "daily_transaction_summary_" + fromDate.replaceAll(":", "") + "_" + toDate.replaceAll(":", "") + sdf2.format(new Date()) + ".xls";

           file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Daily transaction summary", 0);

            s.addCell(new Label(0, 0, "Collection Date"));
            s.addCell(new Label(1, 0, "Totally Collection"));
            s.addCell(new Label(2, 0, "Total Commission (income)"));
            s.addCell(new Label(3, 0, "Net Amount to be Transfered to DLB"));
            s.addCell(new Label(4, 0, "Winning Amount to be Transfered to DLB operating A/C	"));
            s.addCell(new Label(5, 0, "Totally Bank & Other Charges (Cost)"));

            Iterator<Object[]> iterator = transactionReportService.getTransactonSummeryByDate(fromDate, toDate).iterator();
            int genCount = 1;
            while (iterator.hasNext()) {

                Object[] next = iterator.next();

                JSONObject row = new JSONObject();

                row.put("date", next[0]);
                Double tot_collection = (Double) next[2];
                row.put("tot_collection", tot_collection);

                Double totalCommision = tot_collection * SystemVarList.TRANSACTION_COMMESION;

                row.put("tot_commission", totalCommision);
                row.put("net_amt_transfered", tot_collection - totalCommision);
                row.put("win_amt_transfered", "0.00");
                row.put("tkt_value", tot_collection);

                Double card = (Double) next[3];
                Double casa = (Double) next[4];
                Double totalBankCh = card + casa;
                row.put("tot_bank_charges", totalBankCh);

                s.addCell(new Label(0, genCount, next[0].toString()));
                s.addCell(new Label(1, genCount, decimalFormat.format(tot_collection)));
                s.addCell(new Label(2, genCount, decimalFormat.format(totalCommision)));
                s.addCell(new Label(3, genCount, decimalFormat.format(tot_collection - totalCommision)));
                s.addCell(new Label(4, genCount, "0.00"));
                s.addCell(new Label(5, genCount, decimalFormat.format(totalBankCh)));
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
