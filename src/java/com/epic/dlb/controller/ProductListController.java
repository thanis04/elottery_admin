/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductListBuilder;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductListId;
import com.epic.dlb.model.DlbWbWeekDay;
import com.epic.dlb.service.DayService;
import com.epic.dlb.service.ProductListService;
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
 * @author kasun_n
 */
@Controller 
@RequestMapping("product_list")
public class ProductListController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;
    @Autowired
    private ProductListService productListService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DayService dayService;

    @Autowired
    private ProductListBuilder productListBuilder;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/product_list/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                List daySelectBox = dayService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);
                model.addAttribute("day_select_box", daySelectBox);

            } catch (Exception ex) {
                Logger.getLogger(ProductListController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/product_list";
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
    public String search(HttpSession session, DlbWbProductList productList) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_list/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (productList.getId().getProductCode().equals("0") && productList.getId().getDayCode().equals("0")) {
                    //list all records
                    search = productListService.listAll();
                    searchResult = productListBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    List<WhereStatement> searchCriterias = new ArrayList<>();
                    if (!productList.getId().getProductCode().equals("0")) {
                        WhereStatement searchCriteria = new WhereStatement("id.productCode", productList.getId().getProductCode(), SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }
                    if (!productList.getId().getDayCode().equals("0")) {
                        WhereStatement searchCriteria = new WhereStatement("id.dayCode", productList.getId().getDayCode(), SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }

                    search = productListService.search(searchCriterias);
                    searchResult = productListBuilder.buildSearchResult(search);
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
    public String saveRecord(HttpSession session, DlbWbProductList productList) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_list/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                if (productListService.get(productList.getId()) == null) {
                    //save new record
                    productListService.save(productList);

                    //set to response              
                    msg = "Product List " + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record is already exsits
                    //set to response              
                    msg = "Product List" + MessageVarList.ALREADY_EXSITS;
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
    public String updateRecord(HttpSession session, DlbWbProductList productList) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_list/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                if (productListService.get(productList.getId()) != null) {
                    //update new record
                    productListService.update(productList);

                    //set to response              
                    msg = "Product List" + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Product List" + MessageVarList.NOT_FOUND;
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
            @RequestParam("code") String code,
            @RequestParam("day") String day) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_list/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProductList product = productListService.get(new DlbWbProductListId(code.trim(), day.trim()));
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //create JSON object
                    record = productListBuilder.buildJSONObject(product);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Product List" + MessageVarList.NOT_FOUND;
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
            @RequestParam("code") String code,
            @RequestParam("day") String day) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_list/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProductList product = productListService.get(new DlbWbProductListId(code.trim(), day.trim()));
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //delete
                    productListService.delete(product);

                    //set to response              
                    msg = "Product List" + MessageVarList.DEL_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Product List" + MessageVarList.NOT_FOUND;
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
                    msg = "Can't delete. Item" + MessageVarList.ALREADY_USED;
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
