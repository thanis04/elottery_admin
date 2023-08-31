/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.UserClaimedRequestDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.UserClaimPendingService;
import com.epic.dlb.service.UserService;
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
@RequestMapping("user_claim_request")
public class UserClaimPendingController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserClaimPendingService claimPendingService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/user_claim_request/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/user_claim_request";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/show_approve_page.htm")
    public String showApprovePage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/user_claim_request/show_approve_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/user_claim_request_approve";
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
            @RequestParam("txnId") String txnId,
            @RequestParam("serialNo") String serialNo,
            @RequestParam("purchaseId") String purchaseId) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/user_claim_request/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", claimPendingService.search(serialNo, txnId, purchaseId));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(UserClaimPendingController.class.getName()).log(Level.SEVERE, null, ex);
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
            List checkAuthorization = userService.checkAuthorization(session, "/user_claim_request/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", claimPendingService.getAllRequests());
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(UserClaimPendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

    @RequestMapping(value = "request.htm", method = RequestMethod.POST)
    @ResponseBody
    public String request(HttpSession session, Model model, UserClaimedRequestDto userClaimedRequestDto) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(
                    session, "/user_claim_request/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (authStatus) {
                claimPendingService.saveRequest(userClaimedRequestDto, systemUser);
                msg = "Successfully Requested";
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(UserClaimPendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

    @RequestMapping(value = "get.htm", method = RequestMethod.GET)
    @ResponseBody
    public String get(HttpSession session, Model model,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/user_claim_request/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("result", claimPendingService.getById(Integer.parseInt(id)));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(UserClaimPendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    @RequestMapping(value = "approveReject.htm", method = RequestMethod.POST)
    @ResponseBody
    public String approveReject(HttpSession session, Model model, UserClaimedRequestDto userClaimedRequestDto) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(
                    session, "/user_claim_request/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (authStatus) {
                claimPendingService.saveApproveReject(userClaimedRequestDto, systemUser);
                msg = "Successfully Submitted";
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(UserClaimPendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
}
