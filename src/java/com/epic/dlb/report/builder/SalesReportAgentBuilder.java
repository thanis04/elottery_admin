/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.AgentSalesByDto;
import com.epic.dlb.dto.AgentSalesDto;
import com.epic.dlb.dto.AgentSearchCriteriaDto;
import com.epic.dlb.dto.AgentUserDto;
import com.epic.dlb.util.common.Configuration;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Component("salesReportAgentBuilder")
public class SalesReportAgentBuilder {

    @Transactional
    public JSONArray salesReportData(List<AgentSalesDto> list) throws Exception {
        JSONArray table = new JSONArray();
        list.forEach(dto -> {
            JSONObject row = new JSONObject();
            row.put("gameType", dto.getGameType());
            row.put("noOfTickets", dto.getNoOfTickets());

            table.add(row);
        });
        return table;
    }

    @Transactional
    public JSONArray salesReportDataByUser(List<AgentSalesByDto> list) throws Exception {
        JSONArray table = new JSONArray();
        list.forEach(dto -> {
            JSONObject row = new JSONObject();
            row.put("nameOfUser", dto.getNameOfUser());
            row.put("noOfTickets", dto.getNoOfTickets());

            table.add(row);
        });
        return table;
    }

    @Transactional
    public File generateExcel(List<AgentSalesDto> list,
            AgentSearchCriteriaDto agentSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Sales Report " + agentSearchCriteriaDto.getFromDate().replaceAll("00:00:00", "")
                + "- " + agentSearchCriteriaDto.getToDate().replaceAll("23:59:59", "")
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Sales report", 0);

        s.addCell(new Label(0, 0, "Lottery Name"));
        s.addCell(new Label(1, 0, "Number of Tickets Sold"));

        int genCount = 1;
        int total = 0;
        for (AgentSalesDto dto : list) {

            s.addCell(new Label(0, genCount, dto.getGameType()));
            s.addCell(new Label(1, genCount, dto.getNoOfTickets()));
            total = total + Integer.parseInt(dto.getNoOfTickets());
            genCount++;

        }
        genCount++;
        s.addCell(new Label(0, genCount, "Total"));
        s.addCell(new Label(1, genCount, String.valueOf(total)));

        w.write();
        w.close();

        return file;

    }

    @Transactional
    public File generateExcelByUser(List<AgentSalesByDto> list,
            AgentSearchCriteriaDto agentSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Sales By Customer Report " + agentSearchCriteriaDto.getFromDate().replaceAll("00:00:00", "")
                + "- " + agentSearchCriteriaDto.getToDate().replaceAll("23:59:59", "")
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Sales by customer report", 0);

        s.addCell(new Label(0, 0, "Name of the Customer"));
        s.addCell(new Label(1, 0, "No of Tickets Purchased"));

        int genCount = 1;
        int total = 0;
        for (AgentSalesByDto dto : list) {

            s.addCell(new Label(0, genCount, dto.getNameOfUser()));
            s.addCell(new Label(1, genCount, dto.getNoOfTickets()));
            total = total + Integer.parseInt(dto.getNoOfTickets());
            genCount++;

        }

        genCount++;
        s.addCell(new Label(0, genCount, "Total"));
        s.addCell(new Label(1, genCount, String.valueOf(total)));
        
        w.write();
        w.close();

        return file;

    }
}
