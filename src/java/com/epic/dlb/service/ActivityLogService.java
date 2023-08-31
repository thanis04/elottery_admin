/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.ActivityLogDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbActivityLog;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbPage;
import com.epic.dlb.model.DlbWbSection;
import com.epic.dlb.model.DlbWbSystemPriviledge;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.repository.ActivityLogRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.mapping.Collection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("activityLogService")
public class ActivityLogService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private EmployeeService employeeService;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public void save(ActivityLogDto activityLogDto) throws Exception {
        WhereStatement whereStatement = new WhereStatement("pagecode", activityLogDto.getDlbWbPage(), SystemVarList.EQUAL);
        DlbWbPage dlbWbPage = (DlbWbPage) genericRepository.get(DlbWbPage.class, whereStatement);

        WhereStatement whereStatement2 = new WhereStatement("dlbWbPage.pagecode",
                dlbWbPage.getPagecode(), SystemVarList.EQUAL);

        DlbWbSystemPriviledge dlbWbSystemPriviledge = (DlbWbSystemPriviledge) genericRepository.get(DlbWbSystemPriviledge.class, whereStatement2);

        WhereStatement whereStatement1 = new WhereStatement("username", activityLogDto.getDlbWbSystemUser(), SystemVarList.EQUAL);
        DlbWbSystemUser dlbWbSystemUser = (DlbWbSystemUser) genericRepository.get(DlbWbSystemUser.class, whereStatement1);

        DlbWbActivityLog dlbWbActivityLog = new DlbWbActivityLog();
        dlbWbActivityLog.setDescription(activityLogDto.getDescription());
        dlbWbActivityLog.setAction(activityLogDto.getAction());
        dlbWbActivityLog.setDlbWbPage(dlbWbPage);
        dlbWbActivityLog.setDlbWbSystemUser(dlbWbSystemUser);
        dlbWbActivityLog.setCreatedTime(new Date());
        dlbWbActivityLog.setEmployeeid(dlbWbSystemUser.getDlbWbEmployee().getEmployeeid());
        dlbWbActivityLog.setDlbWbSection(dlbWbSystemPriviledge.getDlbWbSection());
        genericRepository.save(dlbWbActivityLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveWithoutSection(ActivityLogDto activityLogDto) throws Exception {
        WhereStatement whereStatement = new WhereStatement("pagecode", activityLogDto.getDlbWbPage(), SystemVarList.EQUAL);
        DlbWbPage dlbWbPage = (DlbWbPage) genericRepository.get(DlbWbPage.class, whereStatement);

        DlbWbSection dlbWbSection = (DlbWbSection) genericRepository.get("BEF", DlbWbSection.class);
        
        WhereStatement whereStatement1 = new WhereStatement("username", activityLogDto.getDlbWbSystemUser(), SystemVarList.EQUAL);
        DlbWbSystemUser dlbWbSystemUser = (DlbWbSystemUser) genericRepository.get(DlbWbSystemUser.class, whereStatement1);

        DlbWbActivityLog dlbWbActivityLog = new DlbWbActivityLog();
        dlbWbActivityLog.setDescription(activityLogDto.getDescription());
        dlbWbActivityLog.setAction(activityLogDto.getAction());
        dlbWbActivityLog.setDlbWbPage(dlbWbPage);
        dlbWbActivityLog.setDlbWbSystemUser(dlbWbSystemUser);
        dlbWbActivityLog.setCreatedTime(new Date());
        dlbWbActivityLog.setEmployeeid(dlbWbSystemUser.getDlbWbEmployee().getEmployeeid());
        dlbWbActivityLog.setDlbWbSection(dlbWbSection);
        genericRepository.save(dlbWbActivityLog);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public DlbWbActivityLog getById(Integer id) {
        return (DlbWbActivityLog) genericRepository.get(id, DlbWbActivityLog.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public String getStatusById(Integer id) {
        DlbStatus dlbStatus = (DlbStatus) genericRepository.get(id, DlbStatus.class);
        return dlbStatus.getDescription();
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject splitJSON(Integer id) {
        DlbWbActivityLog dlbWbActivityLog = getById(id);
        String jsonData = dlbWbActivityLog.getDescription().
                replace("}", "").replace("{", "").replace("\",", "--").replace(",\"", "--").replace("\"", "").
                replace("null,", "null--").replace("[", "").replace("]", "").replace(":", "<b> : </b>");
        String[] splitString = jsonData.split("--");
        Arrays.sort(splitString);
        JSONObject detailList = new JSONObject();
        String pre = "";
        String current = "";
        for (String singleJson : splitString) {

            if (singleJson.contains("tatus<b> : </b>1")) {
                String status = getStatusById(1);
                singleJson = singleJson.replace("tatus<b> : </b>1", "tatus<b> : </b>" + status);
            }
            if (singleJson.contains("tatus<b> : </b>2")) {
                String status = getStatusById(2);
                singleJson = singleJson.replace("tatus<b> : </b>2", "tatus<b> : </b>" + status);
            }

            if (singleJson.contains("Previous") || singleJson.contains("previous")) {
                pre = pre + singleJson + " | ";
            } else {
                current = current + singleJson + " | ";
            }
        }
        String actDate = "Action Date <b>:</b> " + dlbWbActivityLog.getCreatedTime();
        detailList.put("previous", pre);
        detailList.put("current", current);
        detailList.put("actionDate", actDate.replace(".0", ""));
        return detailList;
    }

    @Transactional(rollbackFor = Exception.class)
    public ActivityLogDto buildActivityLog(String action, JSONObject jSONObject,
            String page, DlbWbSystemUser systemUser) {
        ActivityLogDto activityLogDto = new ActivityLogDto();
        activityLogDto.setAction(action);
        activityLogDto.setDescription(jSONObject.toJSONString());
        activityLogDto.setDlbWbPage(page);
        activityLogDto.setDlbWbSystemUser(systemUser.getUsername());
        return activityLogDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getList(ActivityLogDto activityLogDto) throws Exception {
        return activityLogRepository.getList(activityLogDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getCount(ActivityLogDto activityLogDto) throws Exception {
        return activityLogRepository.getCount(activityLogDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject buildEmployeeUpdateJson(DlbWbEmployee employee, DlbWbEmployee previous) throws Exception {
        DlbWbEmployee dlbWbEmployee = new DlbWbEmployee();
        dlbWbEmployee = employeeService.get(previous.getEmployeeid());

        DlbStatus dlbStatus = getDlbStatusById(employee.getDlbStatus());

        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Employee Id", employee.getEmployeeid());
        jSONObject.put("Employee Name", employee.getName());
        jSONObject.put("NIC", employee.getNic());
        jSONObject.put("Address", employee.getAddress());
        jSONObject.put("Email", employee.getEmail());
        jSONObject.put("Contact No", employee.getContactno());
        jSONObject.put("Status", dlbStatus.getDescription());

        dlbWbEmployee = employeeService.get(previous.getEmployeeid());
        DlbStatus dlbStatusPre = getDlbStatusById(previous.getDlbStatus());
//        Hibernate.initialize(previous.getDlbStatus());
        jSONObject.put("Previous Employee Id", previous.getEmployeeid());
        jSONObject.put("Previous Employee Name", previous.getName());
        jSONObject.put("Previous NIC", previous.getNic());
        jSONObject.put("Previous Address", previous.getAddress());
        jSONObject.put("Previous Email", previous.getEmail());
        jSONObject.put("Previous ContactNo", previous.getContactno());
        jSONObject.put("Previous Status", dlbStatusPre.getDescription());
        return jSONObject;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbStatus getDlbStatusById(DlbStatus dlbStatusReq) {
        DlbStatus dlbStatus = (DlbStatus) genericRepository.get(dlbStatusReq.getStatusCode(), DlbStatus.class);
        return dlbStatus;
    }
}
