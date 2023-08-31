/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.service.DashboardService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author kasun_n
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(value = "/load.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getRecord(HttpSession session) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String fromDate = dateFormat.format(date) + " 00:00:00";
            String toDate = dateFormat.format(date) + " 23:59:59";

            Integer totalSales;
            List sales = new ArrayList();
            Integer allcustomer;
            Integer newCustomers;

            totalSales = dashboardService.getSalesByDate(fromDate, toDate, null);
            sales = dashboardService.getSalesByDate(fromDate, toDate);
            allcustomer = dashboardService.allCustomerCount();
            newCustomers = dashboardService.customerCount(fromDate, toDate);
            
            response.put("totalSales", totalSales);
            response.put("sales", sales);
            response.put("allcustomer", allcustomer);
            response.put("newCustomer", newCustomers);

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

}
