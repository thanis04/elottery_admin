/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.ProductProfileBuilder;
import com.epic.dlb.builder.ResultBuilder;
import com.epic.dlb.builder.SpecialGameProfileBuilder;
import com.epic.dlb.dto.resultPublish.ResultPublishDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStWinningLogic;
import com.epic.dlb.model.DlbWbGame;
import com.epic.dlb.model.DlbWbGameResult;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbProductProfileDetails;
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
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SecurityService;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping("result")
public class ResultController {

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

    @Autowired
    private ProductProfileBuilder productProfileBuilder;

    @Autowired
    private SpecialGameProfileBuilder gameProfileBuilder;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/result/show_page.htm");
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
            return "pages/result";
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
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_page.htm");

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

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session, DlbWbResult result,
            @RequestParam("items") String items,
            @RequestParam("spLines") String spLines,
            @RequestParam("draw_date") String drawDate, @RequestParam("public_key") String signKey) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_page.htm");

            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

//                //check is exsiting
                boolean alreadySave = resultService.isAlreadySave(result.getDlbWbProduct(), result.getDrawNo(), drawDate);
                if (!alreadySave) {

                    //verify token                     
//                    byte[] localPublicKeyArry = Base64.decodeBase64(signKey);
//                    boolean verifyTextStatus = securityService.verifyText(session, systemUser.getUsername().getBytes(), localPublicKeyArry);
//                    if (verifyTextStatus) {
                    //save new record
                    DlbWbProductProfile dlbWbProductProfile = productProfileService.get(result.getDlbWbProductProfile().getId());
                    DlbWbResult dlbWbResult = resultBuilder.buildResultObject(result, items, drawDate, dlbWbProductProfile);
                    String weekDay = productProfileService
                            .get(dlbWbResult.getDlbWbProductProfile().getId()).getDlbWbWeekDay().getDescription();

                    if (ticketService.validateDrawDateAndDay(dlbWbResult.getDate(), weekDay)) {

                        List<DlbWbResultDetails> detailses = resultBuilder.buildResultItem(result, items);

                        systemUser.setLastupdateduser(systemUser.getUsername());
                        DlbWbResult savedResult = (DlbWbResult) resultService.save(dlbWbResult, detailses, session);

                        if (dlbWbProductProfile.getDlbStatusBySpecialStatus().getStatusCode() == Integer.parseInt(SystemVarList.YES)) {
                            //special game result
                            List<DlbWbGameResult> buildGameResult = specialGameProfileBuilder.buildSpResultObject(savedResult, spLines);

                            gameProfileService.saveGameResult(buildGameResult);
                        }

                        //audit trace log save
                        String activity
                                = AuditTraceVarList.RESULT + AuditTraceVarList.ADDED + AuditTraceVarList.REF_NO + dlbWbResult.getId();
                        auditTraceService.save(activity, session);

                        //set to response              
                        msg = "Result " + MessageVarList.ADD_SUC;
                        status = SystemVarList.SUCCESS;
                        DlbWbProduct dlbWbProduct = productService.get(
                                savedResult.getDlbWbProduct().getProductCode());
                        DlbSwtStWinningLogic logic = winingLogicService.getById(dlbWbResult.getDlbSwtStWinningLogic().getLogicId());
                        DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("Draw No", dlbWbResult.getDrawNo());
                        jSONObject.put("Product", dlbWbProduct.getDescription());
                        jSONObject.put("Day", dlbWbResult.getDlbWbWeekDay().getDayCode());
                        jSONObject.put("Draw Date", dlbWbResult.getDate());
                        jSONObject.put("Next Draw Date", dlbWbResult.getNextDate());
                        jSONObject.put("Next Jackpot", dlbWbResult.getNextJackpot());
                        jSONObject.put("Winning Logic", dlbWbResult.getDlbSwtStWinningLogic().
                                getLogicId() + " - " + logic.getLogicDescription());
                        activityLogService.save(activityLogService.buildActivityLog(
                                "SAVE", jSONObject, "RUP", user));

                    } else {
                        status = SystemVarList.ERROR;
                        msg = "Draw date and day don't match";
                    }

//                    } else {
//                        //set to response              
//                        msg = MessageVarList.LOGIN_PUBLIC_KEY_ERROR;
//                        status = SystemVarList.ERROR;
//                    }
                } else {
                    //record is already exsits
                    //set to response              
                    msg = MessageVarList.RESULT_ALREADY_EXSITS;
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
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_approval_list.htm");

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

    @RequestMapping("/show_approval_list.htm")
    public String showApprovePage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/result/show_approval_list.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                String msg = MessageVarList.COMMON_ERR;
                boolean status = false;

                JSONArray searchResult;
                List search;

                List<WhereStatement> searchCriterias = new ArrayList<>();

                WhereStatement searchCriteria = new WhereStatement("dlbStatus.statusCode", SystemVarList.SUBMITED, SystemVarList.EQUAL);
                searchCriterias.add(searchCriteria);

                search = resultService.search(searchCriterias);
                searchResult = resultBuilder.buildSearchResult(search);

                //set to response
                model.addAttribute("search_result", searchResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } catch (Exception ex) {
                Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "pages/result_approval";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "get_approval.htm", method = RequestMethod.POST)
    @ResponseBody
    public String getApproveRecord(HttpSession session,
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_approval_list.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //get record
                List resultList = resultService.get(id);
                DlbWbResult result = (DlbWbResult) resultList.get(0);
                List<DlbWbResultDetails> resultLines = (List) (DlbWbResult) resultList.get(1);

                List<DlbWbGameResult> resultByResultId = gameProfileService.getResultByResultId(id);

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

    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updateRecord(HttpSession session,
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();

        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_approval_list.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                List resultList = resultService.get(id);
                DlbWbResult result = (DlbWbResult) resultList.get(0);
                DlbStatus dlbStatus = new DlbStatus(SystemVarList.APPROVED, null, null);
                result.setDlbStatus(dlbStatus);

                //check exsting results
                if (result != null) {

                    resultService.update(result);
                    
                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    resultService.saveAuditLog(result, user, "APPROVE", "RSA");
                    //audit trace log save
                    String activity
                            = AuditTraceVarList.RESULT + AuditTraceVarList.APPROVED + AuditTraceVarList.REF_NO + result.getId();
                    auditTraceService.save(activity, session);

                    //set to response              
                    msg = "Result approved successfully";
                    status = true;
                    
                    
                } else {
                    //record not found
                    //set to response              
                    msg = "Result " + MessageVarList.NOT_FOUND;
                    status = false;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "savejson.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveJSON(HttpSession session,
            @RequestParam("json_file") MultipartFile resultFile,
            @RequestParam("form_data") String formData) {

        JSONObject response = new JSONObject();
        try {
            String jsonData = resultService.getDataFromFile(resultFile);
            String validationStatus = resultService.getValidationStatus(jsonData);
            if (validationStatus.equals("Valid")) {
                resultService.saveResultJson(jsonData, 31);
                response.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
                response.put(SystemVarList.MESSAGE, "Success");
            } else {
                response.put(SystemVarList.STATUS, SystemVarList.ERROR);
                response.put(SystemVarList.MESSAGE, validationStatus);
            }

        } catch (Exception ex) {
            Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response.toJSONString();
    }

    @RequestMapping(value = "getJson.htm", method = RequestMethod.POST)
    @ResponseBody
    public String getJSON(HttpSession session,
            @RequestParam("json_file") MultipartFile resultFile,
            @RequestParam("form_data") String formData) {

        JSONObject response = new JSONObject();
        try {
            String jsonData = resultService.getDataFromFile(resultFile);
            response.put("json", jsonData);

        } catch (Exception ex) {
            Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response.toJSONString();
    }

    @RequestMapping(value = "/load_profile_list.htm", method = RequestMethod.GET)
    @ResponseBody
    public String loadDaysByProduct(HttpSession session, @RequestParam("productCode") String productCode) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_page.htm");

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
            Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
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
            List checkAuthorization = userService.checkAuthorization(session, "/result/show_page.htm");

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
                        = resultService.getNextDrawNoByProduct(
                                dlbWbProductProfile.getDlbWbProduct().
                                        getProductCode());
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
            Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

}
