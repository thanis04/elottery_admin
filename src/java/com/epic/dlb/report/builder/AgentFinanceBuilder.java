/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.AgentSearchCriteriaDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.service.AgentService;
import com.epic.dlb.util.common.Configuration;
import java.io.File;
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
@Component("agentFinanceBuilder")
public class AgentFinanceBuilder {

    @Autowired
    private AgentService agentService;

    @Transactional
    public JSONArray financeReportData(ReportSearchCriteriaDto dto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = agentService.getAgentFinanceData(dto);
        list.forEach(object -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("agent_code", object[0] == null ? "-" : object[0].toString());
            jSONObject.put("agent_name", object[1] == null ? "-" : object[1].toString());
            jSONObject.put("cus_name", object[2] == null ? "-" : object[2].toString());
            jSONObject.put("nic", object[3] == null ? "-" : object[3].toString());
            jSONObject.put("ticket_count", object[4] == null ? "-" : object[4].toString());
            jSONObject.put("last_login_time", object[5] == null ? "-" : object[5].toString().replace(".0", ""));
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }

    @Transactional
    public File getGenerateFinanceReportExcel(ReportSearchCriteriaDto dto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = agentService.getAgentFinanceData(dto);

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Agent Finance Report From - " + dto.getFromDate().replace("00:00:00", "")
                + " To - " + dto.getToDate().replace("23:59:59", "") + " "
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Agent Finance Report", 0);

        s.addCell(new Label(0, 0, "Sub-Agent Code"));
        s.addCell(new Label(1, 0, "Sub-Agent Name"));
        s.addCell(new Label(2, 0, "Customer Name"));
        s.addCell(new Label(3, 0, "NIC"));
        s.addCell(new Label(4, 0, "Ticket Count"));
        s.addCell(new Label(5, 0, "Last Login Time"));

        int genCount = 1;
        for (Object[] obj : list) {
            s.addCell(new Label(0, genCount, obj[0] == null ? "-" : obj[0].toString()));
            s.addCell(new Label(1, genCount, obj[1] == null ? "-" : obj[1].toString()));
            s.addCell(new Label(2, genCount, obj[2] == null ? "-" : obj[2].toString()));
            s.addCell(new Label(3, genCount, obj[3] == null ? "-" : obj[3].toString()));
            s.addCell(new Label(4, genCount, obj[4] == null ? "-" : obj[4].toString()));
            s.addCell(new Label(5, genCount, obj[5] == null ? "-" : obj[5].toString().replace(".0", "")));
            genCount++;
        }
        w.write();
        w.close();
        return file;
    }
}
