/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductItemBuilder;
import com.epic.dlb.builder.ProductListBuilder;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductListId;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.DayService;
import com.epic.dlb.service.ProductItemService;
import com.epic.dlb.service.ProductListService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
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
@RequestMapping("/product_item")
public class ProductItemController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;
    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private ProductItemBuilder productItemBuilder;

    @Autowired
    private AuditTraceService auditTraceService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/product_item/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/product_item";
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
    public String search(HttpSession session, DlbWbProductItem productItem) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_item/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (productItem == null && productItem.getItemCode().isEmpty() && productItem.getDescription().isEmpty()) {

                    //list all records
                    search = productItemService.listAll();
                    searchResult = productItemBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    WhereStatement searchCriteria1 = new WhereStatement("itemCode", productItem.getItemCode(), SystemVarList.LIKE);
                    WhereStatement searchCriteria2 = new WhereStatement("description", productItem.getDescription(), SystemVarList.LIKE);

                    search = productItemService.search(searchCriteria1, searchCriteria2);
                    searchResult = productItemBuilder.buildSearchResult(search);
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY_ITEM + AuditTraceVarList.SEARCHED + AuditTraceVarList.PARAMTERS + productItem.getDescription();
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
    public String saveRecord(HttpSession session, DlbWbProductItem productItem) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_item/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                if (productItemService.get(productItem.getItemCode()) == null) {
                    //save new record
                    productItemService.save(productItem);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product Item Code", productItem.getItemCode());
                    jSONObject.put("Product Item Description", productItem.getDescription());
                    jSONObject.put("Product Item Status", productItem.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "PIM", user));

                    //set to response              
                    msg = "Lottery Item " + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record is already exsits
                    //set to response              
                    msg = "Lottery Item" + MessageVarList.ALREADY_EXSITS;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY_ITEM + AuditTraceVarList.ADDED + AuditTraceVarList.PARAMTERS + productItem.getDescription();
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
    public String updateRecord(HttpSession session, DlbWbProductItem productItem) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_item/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                if (productItemService.get(productItem.getItemCode()) != null) {
                    //update new record
                    
                    DlbWbProductItem item = productItemService.get(productItem.getItemCode());
                    
                    productItemService.update(productItem);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product Item Code", productItem.getItemCode());
                    jSONObject.put("Product Item Description", productItem.getDescription());
                    jSONObject.put("Product Item Status", productItem.getDlbStatus().getStatusCode());
                    
                    jSONObject.put("Previous Product Item Code", item.getItemCode());
                    jSONObject.put("Previous Product Item Description", item.getDescription());
                    jSONObject.put("Previous Product Item Status", item.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "UPDATE", jSONObject, "PIM", user));

                    //set to response              
                    msg = "Product Item" + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Product Item" + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY_ITEM + AuditTraceVarList.UPDATED + AuditTraceVarList.PARAMTERS + productItem.getDescription();
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
            @RequestParam("code") String code) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_item/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProductItem productItem = productItemService.get(code);
                //check is exsting (when record is  not found return null object)
                if (productItem != null) {
                    //record found
                    //create JSON object
                    record = productItemBuilder.buildJSONObject(productItem);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Product Item" + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY_ITEM + AuditTraceVarList.VIEWED + AuditTraceVarList.PARAMTERS + productItem.getDescription();
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
            @RequestParam("code") String code) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_item/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProductItem product = productItemService.get(code);
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //delete
                    productItemService.delete(product);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("productItemCode", product.getItemCode());
                    jSONObject.put("Product Item Description", product.getDescription());
                    jSONObject.put("Product Item Status", product.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "DELETE", jSONObject, "PIM", user));

                    //set to response              
                    msg = "Product item" + MessageVarList.DEL_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Product item" + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY_ITEM + AuditTraceVarList.DELETED + AuditTraceVarList.PARAMTERS + code;
                auditTraceService.save(activity, session);

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            if (ex.getClass().equals(DataIntegrityViolationException.class)) {
                MySQLIntegrityConstraintViolationException ex1 = (MySQLIntegrityConstraintViolationException) ex.getCause().getCause();
                if (ex1.getErrorCode() == MysqlErrorNumbers.ER_ROW_IS_REFERENCED_2) {
                    msg = "Can't delete. Product Item" + MessageVarList.ALREADY_USED;
                }
            }
            Logger.getLogger(ProductItemController.class.getName()).log(Level.SEVERE, null, ex);

        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

}
