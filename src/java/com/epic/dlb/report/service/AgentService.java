/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.AgentDetailSearchDto;
import com.epic.dlb.dto.AgentSalesByDto;
import com.epic.dlb.dto.AgentSalesDto;
import com.epic.dlb.dto.AgentSearchCriteriaDto;
import com.epic.dlb.dto.AgentUserDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.report.repository.AgentRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("agentService")
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<AgentUserDto> getUsers(AgentSearchCriteriaDto dto)
            throws Exception {
        return agentRepository.findUsers(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AgentSalesDto> getSales(AgentSearchCriteriaDto dto)
            throws Exception {
        return agentRepository.findSales(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getSalesTotal(AgentSearchCriteriaDto dto)
            throws Exception {
        return agentRepository.findSalesTotal(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AgentSalesByDto> getSalesByUser(AgentSearchCriteriaDto dto)
            throws Exception {
        return agentRepository.findSalesByUser(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getSalesByUserTotal(AgentSearchCriteriaDto dto)
            throws Exception {
        return agentRepository.findSalesByUserTotal(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getAllAgents(AgentDetailSearchDto dto) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONArray jSONArray = new JSONArray();
        List<Object[]> listObject = agentRepository.findAgents(dto);
        listObject.forEach(row -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("emp_id", row[0] == null ? "-" : row[0].toString());
            jSONObject.put("emp_name", row[1] == null ? "-" : row[1].toString());
            jSONObject.put("emp_contactno", row[2] == null ? "-" : row[2].toString());
            jSONObject.put("emp_nic", row[3] == null ? "-" : row[3].toString());
            jSONObject.put("emp_email", row[4] == null ? "-" : row[4].toString());
            jSONObject.put("emp_address", row[5] == null ? "-" : row[5].toString());
            jSONObject.put("emp_status", row[6] == null ? "-" : row[6].toString());
            jSONObject.put("emp_created", row[7] == null ? "-" : dateFormat.format((Date) row[7]));
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getAllAgentsCount(AgentDetailSearchDto dto) throws Exception {
        return agentRepository.findAgentsCount(dto);
    }

    @Transactional
    public File generateExcelOfAgents(AgentDetailSearchDto dto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Sub Agents Report.xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Sub Agents Report", 0);

        JSONArray jSONArray = getAllAgents(dto);

        s.addCell(new Label(0, 0, "Sub-Agent Id"));
        s.addCell(new Label(1, 0, "Sub-Agent Name"));
        s.addCell(new Label(2, 0, "Contact No"));
        s.addCell(new Label(3, 0, "NIC"));
        s.addCell(new Label(4, 0, "Email"));
        s.addCell(new Label(5, 0, "Address"));
        s.addCell(new Label(6, 0, "Status"));
        s.addCell(new Label(7, 0, "Created Date"));

        int genCount = 1;
        for (Object obj : jSONArray) {
            if (obj instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) obj;
                s.addCell(new Label(0, genCount, (String) jSONObject.get("emp_id")));
                s.addCell(new Label(1, genCount, (String) jSONObject.get("emp_name")));
                s.addCell(new Label(2, genCount, (String) jSONObject.get("emp_contactno")));
                s.addCell(new Label(3, genCount, (String) jSONObject.get("emp_nic")));
                s.addCell(new Label(4, genCount, (String) jSONObject.get("emp_email")));
                s.addCell(new Label(5, genCount, (String) jSONObject.get("emp_address")));
                s.addCell(new Label(6, genCount, (String) jSONObject.get("emp_status")));
                s.addCell(new Label(7, genCount, (String) jSONObject.get("emp_created")));

                genCount++;
            }
        }

        w.write();
        w.close();

        return file;

    }

    public Boolean isValid(String agentCode, String role) {

        if (!role.equals("Agent")) {
            return true;
        } else {
            if (agentCode.equals("00001") || agentCode.equals("E001")
                    || agentCode.equals("EOO1") || agentCode.trim().equals("")) {
                return false;
            } else {
                return true;
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> getAgentFinanceData(ReportSearchCriteriaDto dto) throws Exception {
        return agentRepository.getAgentFinanceData(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getAgentFinanceDataCount(ReportSearchCriteriaDto dto) throws Exception {
        return agentRepository.getAgentFinanceDataCount(dto);
    }
}
