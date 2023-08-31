/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ResultBuilder;
import com.epic.dlb.builder.SpecialGameProfileBuilder;
import com.epic.dlb.model.DlbWbGameResult;
import com.epic.dlb.model.DlbWbResult;
import com.epic.dlb.model.DlbWbResultDetails;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.DayService;
import com.epic.dlb.service.GameProfileService;
import com.epic.dlb.service.ProductListService;
import com.epic.dlb.service.ProductProfileService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.ResultService;
import com.epic.dlb.service.TicketService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.service.WiningLogicService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SecurityService;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping("result_search")
public class ResultSearchController {
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
    private ResultBuilder resultBuilder;

    @Autowired
    private ResultService resultService;

    @Autowired
    private AuditTraceService auditTraceService;

    private static SimpleDateFormat dateFormat;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private WiningLogicService winingLogicService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProductProfileService productProfileService;

    @Autowired
    private SpecialGameProfileBuilder specialGameProfileBuilder;

    @Autowired
    private GameProfileService gameProfileService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/result_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                List daySelectBox = dayService.loadSelectBox(SystemVarList.ACTIVE);
                List logics = winingLogicService.listAll();
                model.addAttribute("product_select_box", productSelectBox);
                model.addAttribute("day_select_box", daySelectBox);
                model.addAttribute("logics", logics);
                DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                model.addAttribute(SystemVarList.USER, systemUser.getUsername());

            } catch (Exception ex) {
                Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/result_search";
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
            DlbWbResult dlbWbResult,
            @RequestParam("draw_no") String drawNoStr,
            @RequestParam("draw_date_str") String drawDateStr) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //check search criteria is null
                if (drawNoStr == null || drawNoStr.isEmpty()
                        && (dlbWbResult.getDlbWbProduct().getProductCode().equals("0"))
                        && (drawDateStr == null || drawDateStr.isEmpty())) {
                    msg = "Search field(s)" + MessageVarList.CAN_NOT_EMPTY;

                } else {
                    //search using criteria
                    List<WhereStatement> searchCriterias = new ArrayList<>();
                    if (!dlbWbResult.getDlbWbProduct().getProductCode().equals("0")) {
                        WhereStatement searchCriteria = new WhereStatement("dlbWbProduct.productCode", dlbWbResult.getDlbWbProduct().getProductCode(), SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }
                    if (drawNoStr != null && !drawNoStr.isEmpty()) {
                        int drawNo = Integer.parseInt(drawNoStr);
                        WhereStatement searchCriteria = new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }
                    if (!drawDateStr.isEmpty()) {
                        Date drawDate = dateFormat.parse(drawDateStr);
                        WhereStatement searchCriteria = new WhereStatement("date", drawDate, SystemVarList.EQUAL);
                        searchCriterias.add(searchCriteria);
                    }

                    search = resultService.search(searchCriterias);
                    searchResult = resultBuilder.buildSearchResult(search);

                    //set to response
                    response.put("search_result", searchResult);
                    msg = SystemVarList.SUCCESS;
                    status = true;

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
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result_search/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                List resultList = resultService.get(id);

                List<DlbWbGameResult> resultByResultId = gameProfileService.getResultByResultId(id);

                DlbWbResult result = (DlbWbResult) resultList.get(0);
                List<DlbWbResultDetails> resultLines = (List) resultList.get(1);
                //check is exsting (when record is  not found return null object)
                if (result != null) {
                    //record found
                    //create JSON object
                    record = resultBuilder.buildJSONObject(result, resultLines);

                    if (!resultByResultId.isEmpty()) {
                        JSONArray buildResult = specialGameProfileBuilder.buildResult(resultByResultId);
                        record.put("sp_items", buildResult);
                    }

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Result " + MessageVarList.NOT_FOUND;
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
