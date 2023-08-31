/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.AuditTraceBuilder;
import com.epic.dlb.model.DlbWbAudittrace;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import static java.lang.Math.log;
import static java.lang.StrictMath.log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author salinda_r
 */
@Controller
@RequestMapping("audit_trace")

public class AuditTraceController {

   @Autowired
    private UserService userService;

    @Autowired
    private  AuditTraceService auditTraceService;
    
    @Autowired
    private  AuditTraceBuilder traceBuilder;
    
 private static SimpleDateFormat dateFormat;
 
 //private static final Logger log= Logger.getLogger(DeviceController.class);

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/audit_trace/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/audit_trace";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }
    
    
     //search 
    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session, DlbWbAudittrace audit,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/audit_trace/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult = null;
                List search;

                //search using criteria
                List<WhereStatement> searchCriterias = new ArrayList<>();

                if (!audit.getActivity().isEmpty()) {
                    WhereStatement searchCriteria = new WhereStatement("activity", audit.getActivity(), SystemVarList.EQUAL);
                    searchCriterias.add(searchCriteria);
                }
                if (!audit.getUsername().isEmpty()) {
                    WhereStatement searchCriteria = new WhereStatement("username",audit.getUsername(), SystemVarList.EQUAL);
                    searchCriterias.add(searchCriteria);
                }
                if (!audit.getIp().isEmpty()) {
                    WhereStatement searchCriteria = new WhereStatement("ip",audit.getIp(), SystemVarList.EQUAL);
                    searchCriterias.add(searchCriteria);
                }
                

                if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                    //add starting time and end time of the day  - to get all records in day
                    fromDate = fromDate + " 00:00:00";
                    toDate = toDate + " 23:59:59";

                    WhereStatement searchCriteriaFromDate = new WhereStatement("createdtime", dateFormat.parse(fromDate), SystemVarList.GREATER_THAN);
                    WhereStatement searchCriteriaToDate = new WhereStatement("createdtime", dateFormat.parse(toDate), SystemVarList.LESS_THAN);
                    searchCriterias.add(searchCriteriaFromDate);
                    searchCriterias.add(searchCriteriaToDate);
                }

                //check search criterias is empty
                if (searchCriterias.size() > 0) {
                    search = auditTraceService.search(searchCriterias);
                    searchResult = traceBuilder.buildSearchResult(search);
//

                    //set to response
                    response.put("search_result", searchResult);
                    msg = SystemVarList.SUCCESS;
                    status = true;
                    
                    //audit trace log save
//                    String activity=AuditTraceVarList.DEVICE+AuditTraceVarList.SEARCHED+AuditTraceVarList.PARAMTERS+fromDate+toDate+audit.getId();
//                    auditTraceService.save(activity, session);
                    
                } else {
                    msg = "Search criteria" + MessageVarList.CAN_NOT_EMPTY;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);
            }

        } catch (Exception ex) {
//             log.error(ex);
//             log.error("Error");
//             log.info("Error");
//             log.debug("Error");
            Logger.getLogger(DeviceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }
    
}

