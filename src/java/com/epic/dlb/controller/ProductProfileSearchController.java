/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductListBuilder;
import com.epic.dlb.builder.ProductProfileBuilder;
import com.epic.dlb.builder.SpecialGameProfileBuilder;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.DayService;
import com.epic.dlb.service.GameProfileService;
import com.epic.dlb.service.ProductItemService;
import com.epic.dlb.service.ProductListService;
import com.epic.dlb.service.ProductProfileService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.ResultService;
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
@RequestMapping("product_profile_search")
public class ProductProfileSearchController {
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
    private ProductProfileService productProfileService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private ProductProfileBuilder productProfileBuilder;

    @Autowired
    private ProductListBuilder productListBuilder;

    @Autowired
    private GameProfileService gameProfileService;

    @Autowired
    private SpecialGameProfileBuilder gameProfileBuilder;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/product_profile_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                List daySelectBox = dayService.loadSelectBox(SystemVarList.ACTIVE);
                List productItemList = productItemService.listByStatus();
                List gameProfileList = gameProfileService.listAllActive();

                model.addAttribute("product_select_box", productSelectBox);
                model.addAttribute("day_select_box", daySelectBox);
                model.addAttribute("product_item_list", productItemList);
                model.addAttribute("game_profile_list", gameProfileList);

            } catch (Exception ex) {
                Logger.getLogger(ProductProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/product_profile_search";
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
    public String search(HttpSession session,
            @RequestParam("productCode") String p_code,
            @RequestParam("dayCode") String d_code) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (p_code.equals("0") && d_code.equals("0")) {
                    //list all records
                    search = productProfileService.listAll();
                    searchResult = productProfileBuilder.buildSearchResult(search);

                } else {
                    //search using criteria
                    List<WhereStatement> searchCriterias = new ArrayList<>();
                    if (!p_code.equals("0")) {
                        WhereStatement searchCriteria = new WhereStatement("dlbWbProduct.productCode", p_code, SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }
                    if (!d_code.equals("0")) {
                        WhereStatement searchCriteria = new WhereStatement("dlbWbWeekDay.dayCode", d_code, SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }

                    search = productProfileService.search(searchCriterias);
                    searchResult = productProfileBuilder.buildSearchResult(search);
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
    
    @RequestMapping(value = "/get.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getRecord(HttpSession session,
            @RequestParam("id") int id) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProductProfile product = productProfileService.get(id);
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //create JSON object
                    record = productProfileBuilder.buildJSONObject(product);

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
    
    @RequestMapping(value = "/view.htm", method = RequestMethod.POST)
    @ResponseBody
    public String viewRecord(HttpSession session,
            @RequestParam("id") int id) {
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                DlbWbProductProfile product = productProfileService.get(id);
                //check is exsting (when record is  not found return null object)
                if (product != null) {
                    //record found
                    //create JSON object
                    record = productProfileBuilder.buildObject(product);

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
}
