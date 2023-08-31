/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.builder.UserRoleBuilder;
import com.epic.dlb.service.UserRoleService;
import com.epic.dlb.builder.EmployeeBuilder;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductListId;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.DayService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ridmi_g
 */
@Controller
@RequestMapping("user_role")
public class UserRoleController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleBuilder userRoleBuilder;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/user_role/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/user_role";

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
    public String search(HttpSession session, DlbWbUserRole userRole) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_role/show_page.htm");

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
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "delete.htm", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRecord(HttpSession session,
            @RequestParam("id") String id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_role/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbUserRole userRole = userRoleService.get(id);
                //check is exsting (when record is  not found return null object)
                if (userRole != null) {
                    //record found
                    //delete
                    userRoleService.delete(userRole);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Role", userRole.getUserrolecode());
                    jSONObject.put("User Role Description", userRole.getDescription());
                    jSONObject.put("Status", userRole.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "DELETE", jSONObject, "URM", user));
                    //set to response              
                    msg = "User Role" + MessageVarList.DEL_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "User Role" + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            if (ex.getClass().equals(DataIntegrityViolationException.class)) {
                MySQLIntegrityConstraintViolationException ex1 = (MySQLIntegrityConstraintViolationException) ex.getCause().getCause();
                if (ex1.getErrorCode() == MysqlErrorNumbers.ER_ROW_IS_REFERENCED_2) {
                    msg = "Can't delete.Item" + MessageVarList.ALREADY_USED;
                }
            }
            System.out.println(ex.getCause());
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

    @RequestMapping(value = "get.htm", method = RequestMethod.POST)
    @ResponseBody
    public String getRecord(HttpSession session,
            @RequestParam("id") String id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_role/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbUserRole userRole = userRoleService.get(id);
                //check is exsting (when record is  not found return null object)
                if (userRole != null) {
                    //record found
                    //create JSON object
                    record = userRoleBuilder.buildJSONObject(userRole);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "User Role " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session, DlbWbUserRole userRole) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_role/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                if (userRoleService.get(userRole.getUserrolecode()) == null) {
                    //save new record
                    userRoleService.save(userRole);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Role", userRole.getUserrolecode());
                    jSONObject.put("User Role Description", userRole.getDescription());
                    jSONObject.put("Status", userRole.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "URM", user));

                    //set to response              
                    msg = "User Role" + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record is already exsits
                    //set to response              
                    msg = "User Role" + MessageVarList.ALREADY_EXSITS;
                    status = SystemVarList.WARNING;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updateRecord(HttpSession session, DlbWbUserRole userRole) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_role/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                if (userRoleService.get(userRole.getUserrolecode()) != null) {
                    DlbWbUserRole role = userRoleService.get(userRole.getUserrolecode());
                    //update new record
                    userRoleService.update(userRole);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Role", userRole.getUserrolecode());
                    jSONObject.put("User Role Description", userRole.getDescription());
                    jSONObject.put("Status", userRole.getDlbStatus().getStatusCode());

                    jSONObject.put("Previous User Role", role.getUserrolecode());
                    jSONObject.put("Previous User Role Description", role.getDescription());
                    jSONObject.put("Previous Status", role.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "UPDATE", jSONObject, "URM", user));

                    //set to response              
                    msg = "User Role " + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "User Role " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

}
