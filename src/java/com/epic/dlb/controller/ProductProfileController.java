/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductListBuilder;
import com.epic.dlb.builder.ProductProfileBuilder;
import com.epic.dlb.builder.SpecialGameProfileBuilder;
import com.epic.dlb.model.DlbWbGame;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductListId;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbProductProfileDetails;
import com.epic.dlb.model.DlbWbSystemUser;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author kasun_n
 */
@Controller
@RequestMapping("product_profile")
public class ProductProfileController {

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
        List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");
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
            return "pages/product_profile";
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
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

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

    @RequestMapping(value = "/save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session,
            @RequestParam("field_data") String productString,
            @RequestParam("url") String url,
            @RequestParam("items") String items,
            @RequestParam("specialDraw") String specialDraw,
            @RequestParam("lastUpdatedUser") String lastUpdatedUser,
            @RequestParam("template") MultipartFile template) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        //---------------This code shows the url of the image-------------
        try {
//            check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                //--------------------------This part is not real commented part------------------
                //still not modify field for url 
                DlbWbProductProfile productProfile = productProfileBuilder.buildObject(productString, url, items, lastUpdatedUser);
                productProfile.setTemplate(template.getBytes());
                if (!productProfileService.checkAlreadyExsits(productProfile)) {
                    //succes
                    Object save = productProfileService.save(productProfile, session);
                    DlbWbProductProfile savedItem = productProfileService.get((int) save);
                    msg = "Lottery profile" + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS; 

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product Profile Id", savedItem.getId());
                    jSONObject.put("Lottery", savedItem.getDlbWbProduct().getDescription());
                    jSONObject.put("Day", savedItem.getDlbWbWeekDay().getDescription());
                    jSONObject.put("Status", savedItem.getDlbStatusByStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "PPM", user));

                } else {
                    msg = "Lottery profile" + MessageVarList.ALREADY_EXSITS;
                    status = SystemVarList.WARNING;
                }

                //----------------------------End of the code-----------------------------------
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
    public String updateRecord(HttpSession session,
            @RequestParam("field_data") String productString,
            @RequestParam("url") String url,
            @RequestParam("items") String items,
            @RequestParam("specialDraw") String specialDraw,
            @RequestParam("lastUpdatedUser") String lastUpdatedUser,
            @RequestParam("template") MultipartFile template) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        //---------------This code shows the url of the image-------------
        try {
//            check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check is exsiting
                //--------------------------This part is not real commented part------------------
                //still not modify field for url 
                DlbWbProductProfile productProfile = productProfileBuilder.buildObject(productString, url, items, lastUpdatedUser);
                productProfile.setTemplate(template.getBytes());
                Object save = productProfileService.update(productProfile, session);
                //----------------------------End of the code-----------------------------------
                //succes

                msg = "Product profile" + MessageVarList.ADD_SUC;
                status = SystemVarList.SUCCESS;

                DlbWbProductProfile savedItem = productProfileService.get((int) save);

                DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("Product Profile Id", savedItem.getId());
                jSONObject.put("Lottery", savedItem.getDlbWbProduct().getDescription());
                jSONObject.put("Day", savedItem.getDlbWbWeekDay().getDescription());
                jSONObject.put("Status", savedItem.getDlbStatusByStatus().getStatusCode());

                jSONObject.put("Previous Product Profile Id", savedItem.getId());
                jSONObject.put("Previous Lottery", savedItem.getDlbWbProduct().getDescription());
                jSONObject.put("Previous Day", savedItem.getDlbWbWeekDay().getDescription());
                jSONObject.put("Previous Status", savedItem.getDlbStatusByStatus().getStatusCode());
                activityLogService.save(activityLogService.buildActivityLog(
                        "UPDATE", jSONObject, "PPM", user));

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
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

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

    @RequestMapping(value = "delete.htm", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRecord(HttpSession session,
            @RequestParam("id") int id
    ) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

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
                    //delete
                    productProfileService.delete(product);

                    //set to response              
                    msg = "Product Profile" + MessageVarList.DEL_SUC;
                    status = SystemVarList.SUCCESS;

                    DlbWbProductProfile savedItem = product;

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Product Profile Id", savedItem.getId());
                    jSONObject.put("Lottery", savedItem.getDlbWbProduct().getDescription());
                    jSONObject.put("Day", savedItem.getDlbWbWeekDay().getDescription());
                    jSONObject.put("Status", savedItem.getDlbStatusByStatus().getStatusCode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "DELETE", jSONObject, "PPM", user));
                } else {
                    //record not found
                    //set to response              
                    msg = "Product Profile" + MessageVarList.NOT_FOUND;
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

    //search 
    @RequestMapping(value = "/load_profile_list.htm", method = RequestMethod.GET)
    @ResponseBody
    public String loadDaysByProduct(HttpSession session, @RequestParam("productCode") String productCode) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray result;
                List dayList;
                DlbWbProduct product = new DlbWbProduct(productCode, null, null);
                dayList = productProfileService.listByProduct(product);

                result = productProfileBuilder.buildDayList(dayList);

                //set to response
                response.put("day_list", result);
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

    //search 
    @RequestMapping(value = "/load_profile.htm", method = RequestMethod.GET)
    @ResponseBody
    public String loadProfile(HttpSession session, @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/product_profile/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray result;
                List itemList;
                String dayCode = null;

                List<DlbWbGame> listGamesByProfile = new ArrayList<>();

                itemList = productProfileService.listByProductProfile(id);

                DlbWbProductProfile dlbWbProductProfile = productProfileService.get(id);

                //load special items ** if exits
                if (dlbWbProductProfile.getDlbStatusBySpecialStatus().getStatusCode() == Integer.parseInt(SystemVarList.YES)) {
                    listGamesByProfile = gameProfileService.listGamesByProfile(dlbWbProductProfile.getDlbWbGameProfile().getId());
                }

                JSONArray buildGameList = gameProfileBuilder.buildGameList(listGamesByProfile);

                //get week day 
                if (itemList.size() > 0) {
                    DlbWbProductProfileDetails details = (DlbWbProductProfileDetails) itemList.get(0);
                    dayCode = details.getDlbWbWeekDay().getDayCode();
                }

                result = productProfileBuilder.buildItemList(itemList);

                Integer nextDrawNoByProduct
                        = resultService.getNextDrawNoByProduct(dlbWbProductProfile.getDlbWbProduct().getProductCode());

                //set to response
                response.put("itemList", result);
                response.put("dayCode", dayCode);
                response.put("game_list", buildGameList);
                response.put("next_draw", nextDrawNoByProduct);
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

}
