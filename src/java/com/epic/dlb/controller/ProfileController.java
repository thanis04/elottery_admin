/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.report.controller_agent.AgentDetailsReportController;
import com.epic.dlb.service.ProvinceDistrictService;
import com.epic.dlb.service.SubAgentManagementService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nipuna_k
 */
@RequestMapping("/profile")
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubAgentManagementService subAgentManagementService;

    @Autowired
    private ProvinceDistrictService provinceDistrictService;

    @RequestMapping(value = "/show_page.htm", method = RequestMethod.GET)
    public String showPage(HttpSession session, Model model) {
        if (session != null) {
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (systemUser != null) {
                if (subAgentManagementService.isValid(systemUser.getUsername())) {
                    model.addAttribute("province_list", provinceDistrictService.getBuildProvinces());
                    return "common/profile_view";
                } else {
                    return "common/not_availble";
                }
            }
        }

        return "common/login";
    }

    @RequestMapping(value = "/getSubAgent.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getSubAgent(HttpSession session, Model model,
            HttpServletRequest request) {

        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        JSONObject response = new JSONObject();

        try {
            response.put(SystemVarList.RECORD, subAgentManagementService.
                    getSubAgent(systemUser.getUsername()));
            response.put("msg", "Fetching Successfull");
            response.put("status", SystemVarList.SUCCESS);

        } catch (Exception ex) {
            Logger.getLogger(AgentDetailsReportController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return response.toJSONString();
    }

    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updateRecord(HttpSession session, HttpServletRequest request,
            @RequestParam(value = "nicFile", required = false) MultipartFile nicFile,
            @RequestParam(value = "scanCopyPassbookFile", required = false) MultipartFile scanCopyPassbookFile,
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
            response = subAgentManagementService.startUpdate(formData, nicFile,
                    scanCopyPassbookFile, proofOfAddressFile,
                    businessRegFile, systemUser);

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
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
