/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.WalletBuilder;
import com.epic.dlb.dto.StatusRollbackDto;
import com.epic.dlb.model.DlbWbResetPinRequest;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.UserService;
import com.epic.dlb.service.WalletService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
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
@RequestMapping("reset_pin_request")
public class ResetPINController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletBuilder walletBuilder;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/reset_pin";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }
    
    @RequestMapping("/show_approve_page.htm")
    public String showApprovePage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_approve_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/reset_approve_pin";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session, Model model,
            @RequestParam("nic") String nic,
            @RequestParam("mobileNo") String mobileNo) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", walletBuilder.buildCustomerDetails(
                        walletService.findByNicOrMobileNo(nic, mobileNo)));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(ResetPINController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    @RequestMapping(value = "search_requests.htm", method = RequestMethod.GET)
    @ResponseBody
    public String searchRequests(HttpSession session, Model model) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", walletService.fetchPINResetRequests());
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(ResetPINController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

    @RequestMapping(value = "get.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getById(HttpSession session, Model model,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("record", walletBuilder.buildWalletDetails(
                        walletService.findById(Integer.parseInt(id))));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(ResetPINController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

    @RequestMapping(value = "get_for_approve.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getForApprove(HttpSession session, Model model,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbResetPinRequest dlbWbResetPinRequest = walletService.getRequestById(Integer.parseInt(id));
                response.put("record", walletBuilder.buildWalletDetails(
                        walletService.findById(dlbWbResetPinRequest.getWalletId())));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(ResetPINController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    @RequestMapping(value = "request.htm", method = RequestMethod.POST)
    @ResponseBody
    public String request(HttpSession session, Model model,
            @RequestParam("id") String id, @RequestParam("remark") String remark) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (authStatus) {

                walletService.saveRquest(Integer.parseInt(id), remark, systemUser.getUsername());
                msg = "PIN Number Reset Request Successfully Submitted";
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(ResetPINController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    @RequestMapping(value = "approve.htm", method = RequestMethod.POST)
    @ResponseBody
    public String approve(HttpSession session, Model model,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reset_pin_request/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (authStatus) {

                walletService.saveApproved(Integer.parseInt(id), systemUser.getUsername());
                msg = "PIN Number Reset Request Successfully Approved";
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(ResetPINController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
}
