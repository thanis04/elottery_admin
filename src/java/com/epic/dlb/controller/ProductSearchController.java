/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductBuilder;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
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
@RequestMapping("/product_sep_search")
@Controller
public class ProductSearchController {
    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductBuilder productBuilder;

    @Autowired
    private AuditTraceService auditTraceService;

    @Autowired
    private ActivityLogService activityLogService;
    
    //load page
    @RequestMapping(value = "/show_page.htm", method = RequestMethod.GET)
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/product_sep_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/product_sep_search";
        } else {
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    //search 
    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session, DlbWbProduct product) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_sep_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (product.getProductCode().isEmpty() && product.getDescription().isEmpty()) {

                    //list all records
                    search = productService.listAll();
                    searchResult = productBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    WhereStatement searchCriteria1 = new WhereStatement("productCode", product.getProductCode(), SystemVarList.LIKE);
                    WhereStatement searchCriteria2 = new WhereStatement("description", product.getDescription(), SystemVarList.LIKE);

                    search = productService.search(searchCriteria1, searchCriteria2);
                    searchResult = productBuilder.buildSearchResult(search);
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY + AuditTraceVarList.SEARCHED + AuditTraceVarList.PARAMTERS + product.getDescription();
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
    
    @RequestMapping(value = "get.htm", method = RequestMethod.POST)
    @ResponseBody
    public String getRecord(HttpSession session, @RequestParam("code") String code) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_sep_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProduct product = productService.get(code);
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //create JSON object
                    record = productBuilder.buildJSONObject(product);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Lottery " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY + AuditTraceVarList.VIEWED + AuditTraceVarList.PARAMTERS + product.getDescription();
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
}
