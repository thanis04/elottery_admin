/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.DeviceBuilder;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.DeviceService;
import com.epic.dlb.service.UserDetailsManagmentService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
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
 * @author nipuna_k
 */
@Controller
@RequestMapping("user_details")
public class UserDetailsManagmentController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsManagmentService userDetailsManagmentService;
    
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceBuilder deviceBuilder;
    
    @Autowired
    private AuditTraceService auditTraceService;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.
            getLogger(UserDetailsManagmentController.class);

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session,
                "/user_details/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/user_det";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session, DlbSwtStWallet dlbStDevice) {
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        JSONObject response = new JSONObject();
        List checkAuthorization = userService.checkAuthorization(session,
                "/user_details/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            response.put("search_result", userDetailsManagmentService.
                    getUserDetails(dlbStDevice.getMobileNo(), dlbStDevice.getNic()));
            msg = SystemVarList.SUCCESS;
            status = true;

        } else {
            msg = (String) checkAuthorization.get(0);
        }

        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    //request pin
    //load page
    @RequestMapping("/request_pin.htm")
    @ResponseBody
    public String requestPin(HttpSession session, DlbSwtStWallet device) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/user_details/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                DlbSwtStWallet exsitingRecord = deviceService.get(device.getId());
                if (device != null) {//check device is exsits
                    //update request pin  as 1
                    exsitingRecord.setRequest(1);
                    deviceService.update(exsitingRecord);

                    //set to response              
                    msg = "Device PIN requested successfully";
                    status = SystemVarList.SUCCESS;

                    //audit trace log save
                    String activity
                            = AuditTraceVarList.DEVICE + AuditTraceVarList.UPDATED + AuditTraceVarList.ID + device.getId();
                    auditTraceService.save(activity, session);
                } else {
                    //record not found
                    //set to response              
                    msg = "Device " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

            } catch (Exception ex) {
//                Logger.getLogger(DeviceController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
        }
        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    //load page
    @RequestMapping("/view_history.htm")
    @ResponseBody
    public String viewHistory(HttpSession session,
            @RequestParam("type") String type,
            @RequestParam("id") int id) {

        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/user_details/show_page.htm");
        List list = new ArrayList();
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                if (type.trim().equals(SystemVarList.ACTIVITY_HISTORY)) {
                    list = deviceService.viewActivityHistory(id);
                }
                if (type.trim().equals(SystemVarList.PURCHASE_HISTORY)) {
                    list = deviceService.viewPurchaseHistory(id);
                }
                if (type.trim().equals(SystemVarList.TRANSACTION_HISTORY)) {
                    list = deviceService.viewTransactionHistory(id);
                }

                //build json array
                JSONArray buildHistorylist = deviceBuilder.buildHistorylist(type, list);
                //set to response
                response.put("search_result", buildHistorylist);
                response.put(SystemVarList.STATUS, SystemVarList.SUCCESS);

            } catch (Exception ex) {
                //log.error(ex);
                java.util.logging.Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
            response.put(SystemVarList.MESSAGE, msg);
            response.put(SystemVarList.STATUS, status);

        }

        return response.toJSONString();
    }
}
