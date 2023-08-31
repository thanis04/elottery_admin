package com.epic.dlb.report.builder;

import com.epic.dlb.dto.AgentUserDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.BrandwiseSearchReportDto;
import com.epic.dlb.util.common.Configuration;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nipuna_k
 */
@Component("brandWiseReportBuilder")
public class BrandWiseReportBuilder {

    @Transactional
    public JSONArray ticketExpiredReportData(List<BrandwiseSearchReportDto> list) throws Exception {

        JSONArray table = new JSONArray();
        list.forEach(dto -> {
            JSONObject row = new JSONObject();
            row.put("lotteryType", dto.getLotteryType());
            row.put("drawNo", dto.getDrawNo());
            row.put("drawDate", dto.getDrawDate());
            row.put("expDate", dto.getExpiredDate());
            row.put("ticketReference", dto.getTicketReference());
            row.put("purchasedDate", dto.getPurchasedDate());
            row.put("nicNumber", dto.getNicNumber());
            row.put("customerReference", dto.getCustomerReference());
            row.put("ticketValue", "20.00");
            row.put("prizeValue", dto.getPrizeValue() + ".00");
            table.add(row);
        });
        return table;
    }

    @Transactional
    public File ticketExpiredGenerateExcel(List<BrandwiseSearchReportDto> list,
            ReportSearchCriteriaDto brandwiseSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Expired Ticket Report.xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Expired ticket report", 0);

        s.addCell(new Label(0, 0, "Lottery Type"));
        s.addCell(new Label(1, 0, "Draw Number"));
        s.addCell(new Label(2, 0, "Draw Date"));
        s.addCell(new Label(3, 0, "Expiry Date"));
        s.addCell(new Label(4, 0, "Prize Value"));
        s.addCell(new Label(5, 0, "Ticket Reference"));
        s.addCell(new Label(6, 0, "Purchased Date"));
        s.addCell(new Label(7, 0, "NIC Number"));
        s.addCell(new Label(8, 0, "Customer Reference"));
        s.addCell(new Label(9, 0, "Ticket Value"));

        int genCount = 1;
        for (BrandwiseSearchReportDto dto : list) {

            s.addCell(new Label(0, genCount, dto.getLotteryType()));
            s.addCell(new Label(1, genCount, dto.getDrawNo()));
            s.addCell(new Label(2, genCount, dto.getDrawDate()));
            s.addCell(new Label(3, genCount, dto.getExpiredDate()));
            s.addCell(new Label(4, genCount, dto.getPrizeValue() + ".00"));
            s.addCell(new Label(5, genCount, dto.getTicketReference()));
            s.addCell(new Label(6, genCount, dto.getPurchasedDate()));
            s.addCell(new Label(7, genCount, dto.getNicNumber()));
            s.addCell(new Label(8, genCount, dto.getCustomerReference()));
            s.addCell(new Label(9, genCount, "20.00"));

            genCount++;

        }

        w.write();
        w.close();

        return file;

    }

    @Transactional
    public JSONArray drawSummaryReportData(List<BrandwiseSearchReportDto> list) throws Exception {

        JSONArray table = new JSONArray();
        list.forEach(dto -> {
            JSONObject row = new JSONObject();
            row.put("lotteryType", dto.getLotteryType());
            row.put("drawNo", dto.getDrawNo());
            row.put("drawDate", dto.getDrawDate());
            row.put("allocatedTickets", dto.getAllocatedTickets());
            row.put("soldTickets", dto.getSoldTickets());
            row.put("returnTickets", dto.getReturnedTickets());
            row.put("winningTicketCount", dto.getWinningTicketCount());
            row.put("winningTicketTotal", dto.getWinningTicketTotal());
            row.put("claimed", dto.getClaimed());
            row.put("processing", dto.getProcessing());
            row.put("expired", dto.getExpired());
            row.put("unclaimed", dto.getUnclaimed());
            table.add(row);
        });
        return table;
    }

    @Transactional
    public File drawSummaryGenerateExcel(List<BrandwiseSearchReportDto> list,
            ReportSearchCriteriaDto brandwiseSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Draw Summary Report.xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Draw summary report", 0);

        s.addCell(new Label(0, 0, "Lottery Name"));
        s.addCell(new Label(1, 0, "Draw Number"));
        s.addCell(new Label(2, 0, "Draw Date"));
        s.addCell(new Label(3, 0, "Allocated Tickets"));
        s.addCell(new Label(4, 0, "Sold Tickets"));
        s.addCell(new Label(5, 0, "Returned Tickets"));
        s.addCell(new Label(6, 0, "Won Tickets"));
        s.addCell(new Label(7, 0, "Winnings Total"));
        s.addCell(new Label(8, 0, "Claimed Tickets"));
        s.addCell(new Label(9, 0, "Processing Tickets"));
        s.addCell(new Label(10, 0, "Unclaimed Tickets"));
        s.addCell(new Label(11, 0, "Expired Tickets"));

        int genCount = 1;
        for (BrandwiseSearchReportDto dto : list) {

            s.addCell(new Label(0, genCount, dto.getLotteryType()));
            s.addCell(new Label(1, genCount, dto.getDrawNo()));
            s.addCell(new Label(2, genCount, dto.getDrawDate()));
            s.addCell(new Label(3, genCount, dto.getAllocatedTickets()));
            s.addCell(new Label(4, genCount, dto.getSoldTickets()));
            s.addCell(new Label(5, genCount, dto.getReturnedTickets()));
            s.addCell(new Label(6, genCount, dto.getWinningTicketCount()));
            s.addCell(new Label(7, genCount, dto.getWinningTicketTotal() + ".00"));
            s.addCell(new Label(8, genCount, dto.getClaimed()));
            s.addCell(new Label(9, genCount, dto.getProcessing()));
            s.addCell(new Label(10, genCount, dto.getUnclaimed()));
            s.addCell(new Label(11, genCount, dto.getExpired()));

            genCount++;

        }

        w.write();
        w.close();

        return file;

    }
}
