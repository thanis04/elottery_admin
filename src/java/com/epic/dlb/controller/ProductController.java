/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductBuilder;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
@RequestMapping("/product")
@Controller
public class ProductController {

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
        List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/product";
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
            List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");

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

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session, DlbWbProduct product, @RequestParam("productIcon") String productIcon) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                product.setProductIcon(productIcon);
//                //check is exsiting
                if (productService.get(product.getProductCode()) == null) {
                    //save new record
                    String res = productService.save(product);

                    //set to response              
                    msg = "Lottery " + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product Code", product.getProductCode());
                    jSONObject.put("Product Description", product.getDescription());
                    jSONObject.put("Product Status", product.getDlbStatus().getDescription());
                    jSONObject.put("Product Icon URL", productIcon);
                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "PM", user));

                } else {
                    //record is already exsits
                    //set to response              
                    msg = "Lottery " + MessageVarList.ALREADY_EXSITS;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY + AuditTraceVarList.ADDED + AuditTraceVarList.PARAMTERS + product.getDescription();
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
    public String updateRecord(HttpSession session, DlbWbProduct product, @RequestParam("productIcon") String productIcon) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                product.setProductIcon(productIcon);

                //check is exsiting
                if (productService.get(product.getProductCode()) != null) {
                    //update new record
                    DlbWbProduct dwp = productService.get(product.getProductCode());
                    productService.update(product);

                    //set to response              
                    msg = "Lottery " + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product Code", product.getProductCode());
                    jSONObject.put("Product Description", product.getDescription());
                    jSONObject.put("Product Status", product.getDlbStatus().getStatusCode());
                    jSONObject.put("Product Icon URL", productIcon);
                    
                    jSONObject.put("Previous Product Code", dwp.getProductCode());
                    jSONObject.put("Previous Product Description", dwp.getDescription());
                    jSONObject.put("Previous Product Status", dwp.getDlbStatus().getDescription());
                    jSONObject.put("Previous Product Icon URL", dwp.getProductIcon());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "UPDATE", jSONObject, "PM", user));

                } else {
                    //record not found
                    //set to response              
                    msg = "Lottery " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY + AuditTraceVarList.UPDATED + AuditTraceVarList.PARAMTERS + product.getDescription();
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
    public String getRecord(HttpSession session, @RequestParam("code") String code) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");

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

    @RequestMapping(value = "delete.htm", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRecord(HttpSession session, @RequestParam("code") String code) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.WARNING;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProduct product = productService.get(code);
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //delete
                    productService.delete(product);

                    //set to response              
                    msg = "Lottery " + MessageVarList.DEL_SUC;
                    status = SystemVarList.SUCCESS;

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product", product.getProductCode());
                    jSONObject.put("Product Description", product.getDescription());
                    jSONObject.put("Product Status", product.getDlbStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "DELETE", jSONObject, "PM", user));

                } else {
                    //record not found
                    //set to response              
                    msg = "Product " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

                //audit trace log save
                String activity
                        = AuditTraceVarList.LOTTERY + AuditTraceVarList.DELETED + AuditTraceVarList.PARAMTERS + product.getDescription();
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
            System.out.println(ex.getCause());
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

    //search 
    @RequestMapping(value = "/search_all.htm", method = RequestMethod.GET)
    public String listAll(HttpSession session, Model model) {

        //declare variable common varibles   
        List search = null;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                search = productService.search();
                model.addAttribute("search", search);

            } else {
                //Authorization fail
                String msg = (String) checkAuthorization.get(0);
                model.addAttribute(SystemVarList.MESSAGE, msg);
                return SystemVarList.LOGIN_PAGE;

            }

        } catch (Exception ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "pages/product_search";

    }

}
