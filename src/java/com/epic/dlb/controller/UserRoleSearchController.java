/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.SystemUserBuilder;
import com.epic.dlb.builder.UserRoleBuilder;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.service.ActivityLogService;
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
@RequestMapping("user_role_search")
public class UserRoleSearchController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleBuilder userRoleBuilder;

    @Autowired
    private ActivityLogService activityLogService;
    
    @Autowired
    private SystemUserBuilder systemUserBuilder;
    
    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/user_role_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/user_role_search";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }
    
    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session, DlbWbUserRole userRole) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_role_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (userRole == null && userRole.getUserrolecode().isEmpty() && userRole.getDescription().isEmpty()) {
                    //list all records
                    search = userRoleService.listAll();
                    searchResult = userRoleBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    List<WhereStatement> searchCriterias = new ArrayList<>();
                    if (!userRole.getUserrolecode().isEmpty()) {
                        WhereStatement searchCriteria = new WhereStatement("userrolecode", userRole.getUserrolecode(), SystemVarList.LIKE);
                        searchCriterias.add(searchCriteria);
                    }
                    if (!userRole.getDescription().isEmpty()) {
                        WhereStatement searchCriteria = new WhereStatement("description", userRole.getDescription(), SystemVarList.LIKE);
                        searchCriterias.add(searchCriteria);
                    }

                    search = userRoleService.search(searchCriterias);
                    searchResult = userRoleBuilder.buildSearchResult(search);
                }

                //set to response
                response.put("search_result", searchResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(UserRoleSearchController.class.getName()).log(Level.SEVERE, null, ex);
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
            List checkAuthorization = userService.checkAuthorization(session, "/user_role_search/show_page.htm");
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
