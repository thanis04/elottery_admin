/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.ActivityLogDto;
import com.epic.dlb.dto.AgentDetailSearchDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.SubAgentDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.report.controller_agent.AgentDetailsReportController;
import com.epic.dlb.report.service.AgentService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.ProvinceDistrictService;
import com.epic.dlb.service.SubAgentManagementService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nipuna_k
 */
@Controller
@RequestMapping("sub-agent-management")
public class SubAgentManagementController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AgentService agentService;
    
    @Autowired
    private ProvinceDistrictService provinceDistrictService;
    
    @Autowired
    private SubAgentManagementService subAgentManagementService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/sub-agent-management/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok  
            model.addAttribute("province_list", provinceDistrictService.getBuildProvinces());
            return "pages/sub_agent_mgt";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
        
    }
    
    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session, HttpServletRequest request,
            @RequestParam("nicFile") MultipartFile nicFile,
            @RequestParam("scanCopyPassbookFile") MultipartFile scanCopyPassbookFile,
            @RequestParam(value = "proofOfAddressFile", required = false) MultipartFile proofOfAddressFile,
            @RequestParam(value = "businessRegFile", required = false) MultipartFile businessRegFile,
            @RequestParam("form_data") String formData) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/sub-agent-management/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response = subAgentManagementService.startRegistration(formData, nicFile,
                        scanCopyPassbookFile, proofOfAddressFile,
                        businessRegFile, systemUser);
            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);
                response.put(SystemVarList.STATUS, status);
                response.put(SystemVarList.MESSAGE, msg);
            }
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response.toJSONString();
    }
    
    @RequestMapping(value = "/show_data.htm", method = RequestMethod.GET)
    @ResponseBody
    public String showPaginatedData(HttpSession session, Model model,
            ActivityLogDto activityLogDto, HttpServletRequest request) {
        List checkAuthorization = userService.checkAuthorization(session, "/sub-agent-management/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {
                
                String start = request.getParameter("start");
                String end = request.getParameter("end");
                String length = request.getParameter("length");
                
                String empId = request.getParameter("empId");
                String empName = request.getParameter("empName");
                
                AgentDetailSearchDto dto = new AgentDetailSearchDto();
                dto.setStart(start);
                dto.setEnd(end);
                dto.setLength(length);
                
                dto.setEmpId(empId);
                dto.setEmpName(empName);
                
                dto.setType("-");
                
                JSONArray data = agentService.getAllAgents(dto);
                Integer count = agentService.getAllAgentsCount(dto);
                
                response.put("search_result", data);
                response.put("data", data);
                response.put("recordsTotal", count);
                response.put("recordsFiltered", count);
                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);
                
            } catch (Exception ex) {
                Logger.getLogger(AgentDetailsReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
        }
        
        return response.toJSONString();
    }
    
    @RequestMapping(value = "/getSubAgent.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getSubAgent(HttpSession session, Model model, HttpServletRequest request,
            @RequestParam("agentCode") String agentCode) {
        List checkAuthorization = userService.checkAuthorization(session, "/sub-agent-management/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {
                response.put(SystemVarList.RECORD, subAgentManagementService.getSubAgent(agentCode));
                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);
                
            } catch (Exception ex) {
                Logger.getLogger(AgentDetailsReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
        }
        
        return response.toJSONString();
    }
    
    @RequestMapping(value = "/getProvince.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getProvinces() {
        JSONObject response = new JSONObject();
        try {
            response.put("province_list", provinceDistrictService.getBuildProvinces());
        } catch (Exception ex) {
            Logger.getLogger(AgentDetailsReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toJSONString();
    }
    
    @RequestMapping(value = "/getDistricts.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getDistricts(@RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        try {
            response.put("district_list", provinceDistrictService.getBuildDistrictsByProvince(Integer.parseInt(id)));
        } catch (Exception ex) {
            Logger.getLogger(AgentDetailsReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toJSONString();
    }
    
    @RequestMapping(value = "/download.htm", method = RequestMethod.GET)
    public void downloadExcel(HttpSession session, HttpServletResponse resp,
            @RequestParam("fileId") Integer fileId,
            @RequestParam("fileName") String fileName) throws ParseException, Exception {
        System.out.println(fileId);
        subAgentManagementService.downloadFile(fileId, fileName, resp);
    }
}
