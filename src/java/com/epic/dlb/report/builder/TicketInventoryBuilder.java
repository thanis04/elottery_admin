/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbWbTicket;
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
@Component("ticketInventoryBuilder")
public class TicketInventoryBuilder {

    @Autowired
    private SalesReportService salesReportService;

    @Autowired
    private GenericRepository genericRepository;

    public JSONArray ticketInventoryReportData(String fromDate, String toDate, String product) throws Exception {
        JSONArray table = new JSONArray();

        Iterator<DlbWbTicket> iterator = salesReportService.getTicketNotSalesReport(fromDate, toDate, product, SystemVarList.READY_TO_CHECKOUT).iterator();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();

            JSONObject row = new JSONObject();

            row.put("lottery_name", next.getProductDescription());
            row.put("draw_no", next.getDrawNo());
            row.put("draw_date", dateFormat.format(next.getDrawDate()));
            row.put("tkt_reff", next.getSerialNumber());
            row.put("tkt_value", "20");
            row.put("currency", "LKR");
            row.put("ticket_pur_date", dateFormat.format(next.getCreatedDate()));

            table.add(row);

        }

        return table;
    }

    @Transactional
    public File generateExcel(String fromDate, String toDate, String lottery) throws Exception {

        File file = null;

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "ticket_sales_report_" + dateFormat.format(fromDate) + "_" + dateFormat.format(toDate) + sdf2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Daily ticket sales report", 0);

            s.addCell(new Label(0, 0, "Lottery"));
            s.addCell(new Label(1, 0, "Draw No"));
            s.addCell(new Label(2, 0, "Draw Date"));
            s.addCell(new Label(3, 0, "Ticket Reference No"));
            s.addCell(new Label(4, 0, "Ticket Value"));
            s.addCell(new Label(5, 0, "Currency"));
            s.addCell(new Label(6, 0, "Purchase Date"));

            Iterator<DlbWbTicket> iterator = salesReportService.getTicketNotSalesReport(fromDate, toDate, lottery, SystemVarList.READY_TO_CHECKOUT).iterator();
            int genCount = 1;
            while (iterator.hasNext()) {

                DlbWbTicket next = iterator.next();

                s.addCell(new Label(0, genCount, next.getProductDescription()));
                s.addCell(new Label(1, genCount, next.getDrawNo()));
                s.addCell(new Label(2, genCount, dateFormat.format(next.getDrawDate())));
                s.addCell(new Label(3, genCount, next.getSerialNumber()));
                s.addCell(new Label(4, genCount, "20.00"));
                s.addCell(new Label(5, genCount, "LKR"));
                s.addCell(new Label(6, genCount, dateFormat.format(next.getCreatedDate())));
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
