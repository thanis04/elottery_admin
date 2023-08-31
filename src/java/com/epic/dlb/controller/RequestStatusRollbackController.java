/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.PasswordChangeDto;
import com.epic.dlb.dto.StatusRollbackDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.RequestStatusRollbackService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nipuna_k
 */
@Controller
@RequestMapping("reverseClaimedTicket")
public class RequestStatusRollbackController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestStatusRollbackService requestStatusRollbackService;

    @RequestMapping("/show_page.htm")
    public String showRequestPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/reverseClaimedTicket/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                model.addAttribute("list", requestStatusRollbackService.getData(SystemVarList.DLB_CLAIMED_1));

            } catch (Exception ex) {
                Logger.getLogger(RequestStatusRollbackController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/status_rollback_request";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "request_rollback.htm", method = RequestMethod.POST)
    @ResponseBody
    public String requestRollback(HttpSession session, Model model, StatusRollbackDto statusRollbackDto) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reverseClaimedTicket/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                DlbWbSystemUser user = userService.get(systemUser.getUsername());
                requestStatusRollbackService.requestSaveData(statusRollbackDto, user);
                msg = "Request has been submitted successfully";
                status = SystemVarList.SUCCESS;
                model.addAttribute("list", requestStatusRollbackService.getData(SystemVarList.DLB_CLAIMED_1));
            } else {
                return "common/login";
            }
        } catch (Exception ex) {
            msg = "Somthing Went Wrong";
            status = SystemVarList.ERROR;
            Logger.getLogger(RequestStatusRollbackController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping("/show_page_approve.htm")
    public String showApprovePage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session,
                "/reverseClaimedTicket/show_page_approve.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                model.addAttribute("list", requestStatusRollbackService.getRollbackData());
            } catch (Exception ex) {
                Logger.getLogger(RequestStatusRollbackController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/status_rollback_approve";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "approve_rollback.htm", method = RequestMethod.POST)
    @ResponseBody
    public String approveRollback(HttpSession session, Model model,
            HttpServletRequest request) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        String id = request.getParameter("id");
        String purchaseHistoryId = request.getParameter("purchaseHistoryId");

        try {
            List checkAuthorization = userService.checkAuthorization(session, "/reverseClaimedTicket/show_page_approve.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                
                DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                DlbWbSystemUser user = userService.get(systemUser.getUsername());
                
                StatusRollbackDto statusRollbackDto = new StatusRollbackDto();
                statusRollbackDto.setId(Integer.parseInt(id));
                statusRollbackDto.setPurchaseHistoryId(Integer.parseInt(purchaseHistoryId));
                requestStatusRollbackService.approveData(statusRollbackDto, user);
                msg = "Updated successfully";
                status = SystemVarList.SUCCESS;
            } else {
                return "common/login";
            }
        } catch (Exception ex) {
            msg = "Somthing Went Wrong";
            status = SystemVarList.ERROR;
            Logger.getLogger(RequestStatusRollbackController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }
}
