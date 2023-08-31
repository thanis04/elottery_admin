/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.SystemUserBuilder;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.UserRoleService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MD5Security;
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
 * @author kasun_n
 */
@Controller
@RequestMapping("system_user")
public class SystemUserController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
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

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");
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

            return "pages/system_user";

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
    public String search(HttpSession session, DlbWbSystemUser systemUser) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (systemUser.getUsername().isEmpty() && systemUser.getDlbWbUserRole().getUserrolecode().equals("0")) {
                    //list all records
                    search = userService.listAll();
                    searchResult = systemUserBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    List<WhereStatement> searchCriterias = new ArrayList<>();
                    if (!systemUser.getUsername().isEmpty()) {
                        WhereStatement searchCriteria = new WhereStatement("username", systemUser.getUsername(), SystemVarList.LIKE);
                        searchCriterias.add(searchCriteria);
                    }
                    if (!systemUser.getDlbWbUserRole().getUserrolecode().equals("0")) {
                        WhereStatement searchCriteria = new WhereStatement("dlbWbUserRole", systemUser.getDlbWbUserRole(), SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }

                    search = userService.search(searchCriterias);
                    searchResult = systemUserBuilder.buildSearchResult(search);
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

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session, DlbWbSystemUser systemUser) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                if (userService.get(systemUser.getUsername()) == null) {
                    //save new record
                    //encript password
                    systemUser.setPassword(MD5Security.MD5(systemUser.getPassword()));
                    userService.save(systemUser);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Name", systemUser.getUsername());
                    jSONObject.put("Employee Id", systemUser.getDlbWbEmployee().getEmployeeid());
                    jSONObject.put("Status", systemUser.getDlbStatus().getStatusCode());
                    jSONObject.put("User Role", systemUser.getDlbWbUserRole().getUserrolecode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "UM", user));
                    //set to response              
                    msg = "System User" + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record is already exsits
                    //set to response              
                    msg = "System User" + MessageVarList.ALREADY_EXSITS;
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
    public String updateRecord(HttpSession session, DlbWbSystemUser systemUser) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;
        DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                DlbWbSystemUser dbUser = userService.get(systemUser.getUsername());
                if (dbUser != null) {
                    //update new record
                    systemUser.setPassword(dbUser.getPassword());
                    userService.update(systemUser);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Name", systemUser.getUsername());
                    jSONObject.put("Employee Id", systemUser.getDlbWbEmployee().getEmployeeid());
                    jSONObject.put("Status", systemUser.getDlbStatus().getStatusCode());
                    jSONObject.put("User Role", systemUser.getDlbWbUserRole().getUserrolecode());

                    jSONObject.put("Previous User Name", dbUser.getUsername());
                    jSONObject.put("Previous Employee Id", dbUser.getDlbWbEmployee().getEmployeeid());
                    jSONObject.put("Previous Status", dbUser.getDlbStatus().getStatusCode());
                    jSONObject.put("Previous User Role", dbUser.getDlbWbUserRole().getUserrolecode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "UPDATE", jSONObject, "UM", user));

                    //set to response              
                    msg = "System User " + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "System User " + MessageVarList.NOT_FOUND;
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

    @RequestMapping(value = "update_password.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updatePassword(HttpSession session, DlbWbSystemUser systemUser, @RequestParam("new_password") String newPassword) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                DlbWbSystemUser dbUser = userService.get(systemUser.getUsername());
                if (dbUser != null) {

                    //check current password  with db record
                    if (!MD5Security.MD5(systemUser.getPassword()).equals(dbUser.getPassword())) {
                        //match pass
                        //set new password to user
                        dbUser.setPassword(MD5Security.MD5(newPassword));

                        //update password
                        userService.update(dbUser);

                        DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("Owner of the account", systemUser.getUsername());
                        jSONObject.put("Updated by", user.getDlbWbEmployee().getName());
                        activityLogService.save(activityLogService.buildActivityLog(
                                "UPDATE_PASSWORD", jSONObject, "UM", user));

                        //set to response              
                        msg = "Password updated succesfully";
                        status = SystemVarList.SUCCESS;

                    } else {
                        //not match
                        msg = "The password you have entered does not mach your current one ";
                        status = SystemVarList.ERROR;

                    }

                } else {
                    //record not found
                    //set to response              
                    msg = "System User " + MessageVarList.NOT_FOUND;
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

    @RequestMapping(value = "update_password_admin.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updatePasswordByAdmin(HttpSession session, DlbWbSystemUser systemUser) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                DlbWbSystemUser dbUser = userService.get(systemUser.getUsername());
                if (dbUser != null) {
                    //set new password to user
                    dbUser.setPassword(MD5Security.MD5(systemUser.getPassword()));
                    //update password
                    userService.update(dbUser);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Owner of the account", systemUser.getUsername());
                    jSONObject.put("Updated by", user.getDlbWbEmployee().getName());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "UPDATE_PASSWORD", jSONObject, "UM", user));

                    //set to response              
                    msg = "Password updated succesfully";
                    status = SystemVarList.SUCCESS;

                } else {
                    //record not found
                    //set to response              
                    msg = "System User " + MessageVarList.NOT_FOUND;
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
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbSystemUser systemUser = userService.get(id);
                //check is exsting (when record is  not found return null object)
                if (systemUser != null) {
                    //record found
                    //create JSON object
                    record = systemUserBuilder.buildJSONObject(systemUser);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "System User" + MessageVarList.NOT_FOUND;
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

    @RequestMapping(value = "delete.htm", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRecord(HttpSession session,
            @RequestParam("id") String id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/system_user/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbSystemUser systemUser = userService.get(id);
                //check is exsting (when record is  not found return null object)
                if (systemUser != null) {
                    //record found
                    //delete

                    //check is logged user 
                    DlbWbSystemUser sessionUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    if (!systemUser.getUsername().equals(sessionUser.getUsername())) {
                        userService.delete(systemUser);

                        //set to response              
                        msg = "System User" + MessageVarList.DEL_SUC;
                        status = SystemVarList.SUCCESS;

                        DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("User Name", systemUser.getUsername());
                        jSONObject.put("Employee Id", systemUser.getDlbWbEmployee().getEmployeeid());
                        jSONObject.put("Status", systemUser.getDlbStatus().getStatusCode());
                        jSONObject.put("User Role", systemUser.getDlbWbUserRole().getUserrolecode());
                        activityLogService.save(activityLogService.buildActivityLog(
                                "DELETE", jSONObject, "UM", user));

                    } else {
                        msg = "Can't delete. System User" + MessageVarList.ALREADY_USED;
                        status = SystemVarList.ERROR;
                    }

                } else {
                    //record not found
                    //set to response              
                    msg = "System User" + MessageVarList.NOT_FOUND;
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
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

}
