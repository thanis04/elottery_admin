/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.WalletBuilder;
import com.epic.dlb.model.DlbWbResetPinRequest;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbWalletDeletionRequest;
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
@RequestMapping("wallet_account_deletion")
public class WalletAccountDeletionController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletBuilder walletBuilder;

    @RequestMapping("/show_request_page.htm")
    public String showRequestPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(
                session, "/wallet_account_deletion/show_request_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/wallet_deletion_req";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/show_approve_page.htm")
    public String showApprovedPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(
                session, "/wallet_account_deletion/show_approve_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/wallet_deletion_approve";
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
            List checkAuthorization = userService.checkAuthorization(
                    session, "/wallet_account_deletion/show_request_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", walletBuilder.buildCustomerDetailsForDeleton(
                        walletService.findByNicOrMobileNo(nic, mobileNo)));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(WalletAccountDeletionController.class.getName()).log(Level.SEVERE, null, ex);
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
            List checkAuthorization = userService.checkAuthorization(
                    session, "/wallet_account_deletion/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", walletService.fetchDeletionRequests());
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
            List checkAuthorization = userService.checkAuthorization(
                    session, "/wallet_account_deletion/show_request_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("record", walletBuilder.buildWalletDetails(
                        walletService.findById(Integer.parseInt(id))));
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(WalletAccountDeletionController.class.getName()).log(Level.SEVERE, null, ex);
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
            List checkAuthorization = userService.checkAuthorization(
                    session, "/wallet_account_deletion/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbWalletDeletionRequest dlbWbWalletDeletionRequest = walletService.getDeletionRequestById(Integer.parseInt(id));
                response.put("record", walletBuilder.buildWalletDetailsForDeletion(
                        walletService.findById(dlbWbWalletDeletionRequest.getWalletId()), dlbWbWalletDeletionRequest));
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
            List checkAuthorization = userService.checkAuthorization(
                    session, "/wallet_account_deletion/show_request_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (authStatus) {

                walletService.saveDeletionRquest(Integer.parseInt(id), remark, systemUser.getUsername());
                msg = "Request Successfully Submitted";
                status = SystemVarList.SUCCESS;
            }
        } catch (Exception ex) {
            Logger.getLogger(WalletAccountDeletionController.class.getName()).log(Level.SEVERE, null, ex);
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
            List checkAuthorization = userService.checkAuthorization(
                    session, "/wallet_account_deletion/show_approve_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (authStatus) {
                JSONObject isValid = walletService.getIsValidToDelete(
                        walletService.getByDeletionRequest(Integer.parseInt(id)));
                if (isValid.get("status").equals("true")) {
                    walletService.saveDeletionApproved(Integer.parseInt(id), systemUser.getUsername());
                    msg = "Request Successfully Approved";
                    status = SystemVarList.SUCCESS;
                } else if (isValid.get("status").equals("false")) {
                    msg = (String) isValid.get("msg");
                    status = SystemVarList.ERROR;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(WalletAccountDeletionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

    @RequestMapping(value = "getPermission.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getPermission(HttpSession session, Model model,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {

            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);

            JSONObject isValid = walletService.getIsValidToDelete(
                    walletService.getByDeletionRequest(Integer.parseInt(id)));
            if (isValid.get("status").equals("true")) {
                status = SystemVarList.SUCCESS;
            } else if (isValid.get("status").equals("false")) {
                msg = (String) isValid.get("msg");
                status = SystemVarList.ERROR;
            }
        } catch (Exception ex) {
            Logger.getLogger(WalletAccountDeletionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    @RequestMapping(value = "getPermissionByWallet.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getPermissionByWallet(HttpSession session, Model model,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {

            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);

            JSONObject isValid = walletService.getIsValidToDelete(
                    walletService.findByWalletId(Integer.parseInt(id)));
            if (isValid.get("status").equals("true")) {
                status = SystemVarList.SUCCESS;
            } else if (isValid.get("status").equals("false")) {
                msg = (String) isValid.get("msg");
                status = SystemVarList.ERROR;
            }
        } catch (Exception ex) {
            Logger.getLogger(WalletAccountDeletionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
}
