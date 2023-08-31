/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.EmployeeBuilder;
import com.epic.dlb.dto.ActivityLogDto;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductListId;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.DayService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
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
@RequestMapping("employee")
public class EmployeeController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeBuilder employeeBuilder;

    @Autowired
    private AuditTraceService auditTraceService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/employee/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/employee";

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
    public String search(HttpSession session, HttpServletResponse servletResponse, DlbWbEmployee employee) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/employee/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (employee.getEmployeeid().isEmpty() && employee.getName().isEmpty()) {
                    //list all records
                    search = employeeService.listAll();
                    searchResult = employeeBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    List<WhereStatement> searchCriterias = new ArrayList<>();
                    if (!employee.getEmployeeid().isEmpty()) {
                        WhereStatement searchCriteria = new WhereStatement("employeeid", employee.getEmployeeid(), SystemVarList.LIKE);
                        searchCriterias.add(searchCriteria);
                    }
                    if (!employee.getName().isEmpty()) {
                        WhereStatement searchCriteria = new WhereStatement("name", employee.getName(), SystemVarList.LIKE);
                        searchCriterias.add(searchCriteria);
                    }

                    search = employeeService.search(searchCriterias);
                    searchResult = employeeBuilder.buildSearchResult(search);
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.EMPLOYEE + AuditTraceVarList.SEARCHED + AuditTraceVarList.PARAMTERS + employee.getName();
                auditTraceService.save(activity, session);

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
    public String saveRecord(HttpSession session, DlbWbEmployee employee) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/employee/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                if (employeeService.get(employee.getEmployeeid()) == null) {
                    //save new record
                    employeeService.save(employee);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Employee Id", employee.getEmployeeid());
                    jSONObject.put("Employee Name", employee.getName());
                    jSONObject.put("NIC", employee.getNic());
                    jSONObject.put("Address", employee.getAddress());
                    jSONObject.put("Email", employee.getEmail());
                    jSONObject.put("Contact No", employee.getContactno());
                    jSONObject.put("Status", employee.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "EM", systemUser));
                    //set to response              
                    msg = "Employee " + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record is already exsits
                    //set to response              
                    msg = "Employee" + MessageVarList.ALREADY_EXSITS;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.EMPLOYEE + AuditTraceVarList.ADDED + AuditTraceVarList.PARAMTERS + employee.getName();
                auditTraceService.save(activity, session);

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
    public String updateRecord(HttpSession session, DlbWbEmployee employee) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/employee/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                if (employeeService.get(employee.getEmployeeid()) != null) {

                    DlbWbEmployee previous = employeeService.get(employee.getEmployeeid());

                    //update new record
                    

                    JSONObject jSONObject = activityLogService.
                            buildEmployeeUpdateJson(employee, previous);
                    
                    employeeService.update(employee);
                    
                    activityLogService.save(activityLogService.buildActivityLog(
                            "UPDATE", jSONObject, "EM", systemUser));

                    //set to response              
                    msg = "Employee " + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Employee " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.EMPLOYEE + AuditTraceVarList.UPDATED + AuditTraceVarList.PARAMTERS + employee.getName();
                auditTraceService.save(activity, session);

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
            List checkAuthorization = userService.checkAuthorization(session, "/employee/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbEmployee employee = employeeService.get(id);
                //check is exsting (when record is  not found return null object)
                if (employee != null) {
                    //record found
                    //create JSON object
                    record = employeeBuilder.buildJSONObject(employee);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Employee " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.EMPLOYEE + AuditTraceVarList.VIEWED + AuditTraceVarList.PARAMTERS + employee.getName();
                auditTraceService.save(activity, session);

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
        String status = SystemVarList.ERROR;;
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/employee/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbEmployee employee = employeeService.get(id);
                //check is exsting (when record is  not found return null object)
                if (employee != null) {
                    //record found
                    //delete
                    employeeService.delete(employee);

                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Employee Id", employee.getEmployeeid());
                    jSONObject.put("Employee Name", employee.getName());
                    jSONObject.put("NIC", employee.getNic());
                    jSONObject.put("Address", employee.getAddress());
                    jSONObject.put("Email", employee.getEmail());
                    jSONObject.put("ContactNo", employee.getContactno());
//                    Hibernate.initialize(employee.getDlbStatus());
//                    jSONObject.put("Status", employee.getDlbStatus().getDescription());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "DELETE", jSONObject, "EM", systemUser));
                    //set to response              
                    msg = "Employee" + MessageVarList.DEL_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Employee" + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.EMPLOYEE + AuditTraceVarList.DELETED + AuditTraceVarList.PARAMTERS + employee.getName();
                auditTraceService.save(activity, session);

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
