/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.SystemUserBuilder;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.UserPrivilegeService;
import com.epic.dlb.service.UserRoleService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
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
@RequestMapping("system_user_search")
public class SystemUserSearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private SystemUserBuilder systemUserBuilder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/system_user_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //set attr
                WhereStatement whereStatement = new WhereStatement("dlbStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
                List userRoleList = userRoleService.listAll(whereStatement);
                List employeeList = employeeService.listAll(whereStatement);
                model.addAttribute("user_role_select_box", userRoleList);
                model.addAttribute("employee_select_box", employeeList);
            } catch (Exception ex) {
                Logger.getLogger(SystemUserController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "pages/system_user_search";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session,
            @RequestParam("username") String username,
            @RequestParam("userRoleCode") String userRoleCode) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user_search/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //set to response
                response.put("search_result", userService.
                        getSystemUser(username, userRoleCode));
                response.put("total_count", userService.
                        getTotalSystemUsers(userRoleCode));
                response.put("active_count", userService.
                        getTotalActiveInactiveSystemUsers(username, userRoleCode, SystemVarList.ACTIVE));
                response.put("inactive_count", userService.
                        getTotalActiveInactiveSystemUsers(username, userRoleCode, SystemVarList.INACTIVE));
                msg = SystemVarList.SUCCESS;
                status = true;
            } else {
                msg = (String) checkAuthorization.get(0);
            }

        } catch (Exception ex) {
            Logger.getLogger(SystemUserSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "/view_privilege.htm", method = RequestMethod.GET)
    @ResponseBody
    public String viewPrivilege(HttpSession session, @RequestParam("userRoleCode") String userRoleCode) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = true;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user_search/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                response.put("search_result", systemUserBuilder.buildPrivilages(userRoleCode));
                msg = SystemVarList.SUCCESS;
                status = true;
            } else {
                msg = (String) checkAuthorization.get(0);
            }

        } catch (Exception ex) {
            Logger.getLogger(SystemUserSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }
}
