/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.AgentSearchCriteriaDto;
import com.epic.dlb.dto.AgentUserDto;
import com.epic.dlb.util.common.Configuration;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Component("userReportAgentBuilder")
public class UserReportAgentBuilder {

    @Transactional
    public JSONArray userReportData(List<AgentUserDto> list) throws Exception {
        JSONArray table = new JSONArray();
        list.forEach(dto -> {
            JSONObject row = new JSONObject();
            row.put("nic", dto.getNic());
            row.put("username", dto.getUsername());
            row.put("fullName", dto.getFirstName() + " " + dto.getLastName());
            row.put("agentCode", dto.getAgentCode());
            row.put("lastLoginTime", dto.getLastLoginTime());
            row.put("createdTime", dto.getCreatedTime());
            row.put("transactionDate", dto.getTransactionDate());
            table.add(row);
        });
        return table;
    }

    @Transactional
    public File generateExcel(List<AgentUserDto> list,
            AgentSearchCriteriaDto agentSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Customer Report " + agentSearchCriteriaDto.getFromDate().replaceAll("00:00:00", "")
                + "- " + agentSearchCriteriaDto.getToDate().replaceAll("23:59:59", "")
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Customer report", 0);

        s.addCell(new Label(0, 0, "Name of the Customer"));
        s.addCell(new Label(1, 0, "Created Date/Time"));
        s.addCell(new Label(2, 0, "Last Login Date/Time"));
        s.addCell(new Label(3, 0, "Last Transaction Date/Time"));

        int genCount = 1;
        for (AgentUserDto dto : list) {

            s.addCell(new Label(0, genCount, dto.getFirstName() + " " + dto.getLastName()));
            s.addCell(new Label(1, genCount, dto.getCreatedTime()));
            s.addCell(new Label(2, genCount, dto.getLastLoginTime()));
            s.addCell(new Label(3, genCount, dto.getTransactionDate()));

            genCount++;

        }

        w.write();
        w.close();

        return file;

    }
}
