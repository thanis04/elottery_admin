/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.PaginatedDataDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.report.service.SalesReportService;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
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
@Component("ticketReturnedBuilder")
public class TicketReturnedBuilder {

    @Autowired
    private SalesReportService salesReportService;

    public PaginatedDataDto ticketReturnedReportData(String fromDate, String toDate, String product, Integer start, Integer length) throws Exception {
        JSONArray table = new JSONArray();

//        Iterator<DlbWbTicket> iterator = salesReportService.getTicketNotSalesReport(fromDate, toDate,product, SystemVarList.RETURNED+"").iterator();
        PaginatedDataDto paginatedDataDto = salesReportService.getReturnedTicketsReport(fromDate, toDate, product, "17", start, length);
        Iterator<DlbWbTicket> iterator = paginatedDataDto.getList().iterator();
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
            row.put("ticket_upload_date", dateFormat.format(next.getCreatedDate()));
            row.put("ticket_ret_date", dateFormat.format(next.getDrawDate()));

            table.add(row);

        }
        paginatedDataDto.setArray(table);
        return paginatedDataDto;
    }

    @Transactional
    public File getGenerateTicketReturnedExcel(ReportSearchCriteriaDto dto) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONArray jSONArray = new JSONArray();

        PaginatedDataDto paginatedDataDto = salesReportService.getReturnedTicketsReport(
                dto.getFromDate(), dto.getToDate(), dto.getGameType(), "17", null, null);

        Iterator<DlbWbTicket> iterator = paginatedDataDto.getList().iterator();

//        Iterator<DlbWbTicket> iterator = salesReportService.getTicketNotSalesReport(
//                dto.getFromDate(), dto.getToDate(), dto.getGameType(), "17").iterator();
        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Ticket Returned Report From - " + dto.getFromDate().replace("00:00:00", "")
                + " To - " + dto.getToDate().replace("23:59:59", "") + " "
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Agent Finance Report", 0);

        s.addCell(new Label(0, 0, "Lottery Name"));
        s.addCell(new Label(1, 0, "Draw Number"));
        s.addCell(new Label(2, 0, "Ticket Reference Number"));
        s.addCell(new Label(3, 0, "Ticket Value"));
        s.addCell(new Label(4, 0, "Currency"));
        s.addCell(new Label(5, 0, "Ticket Uploaded Date"));
        s.addCell(new Label(6, 0, "Ticket Returned Date"));

        int genCount = 1;
        while (iterator.hasNext()) {
            DlbWbTicket obj = iterator.next();
            s.addCell(new Label(0, genCount, obj.getProductDescription() == null ? "-" : obj.getProductDescription()));
            s.addCell(new Label(1, genCount, obj.getDrawNo() == null ? "-" : obj.getDrawNo()));
            s.addCell(new Label(2, genCount, obj.getSerialNumber() == null ? "-" : obj.getSerialNumber()));
            s.addCell(new Label(3, genCount, "20"));
            s.addCell(new Label(4, genCount, "LKR"));
            s.addCell(new Label(5, genCount, obj.getCreatedDate() == null ? "-" : dateFormat.format(obj.getCreatedDate())));
            s.addCell(new Label(6, genCount, obj.getDrawDate() == null ? "-" : dateFormat.format(obj.getDrawDate())));
            genCount++;
        }
        w.write();
        w.close();
        return file;
    }
}
