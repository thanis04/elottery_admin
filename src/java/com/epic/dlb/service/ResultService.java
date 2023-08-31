/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.builder.ResultBuilder;
import com.epic.dlb.builder.SpecialGameProfileBuilder;
import com.epic.dlb.dto.resultPublish.ResultPublishDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStWinningLogic;
import com.epic.dlb.model.DlbWbGame;
import com.epic.dlb.model.DlbWbGameProfile;
import com.epic.dlb.model.DlbWbGameResult;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbProductProfileDetails;
import com.epic.dlb.model.DlbWbResult;
import com.epic.dlb.model.DlbWbResultAutomationAudit;
import com.epic.dlb.model.DlbWbResultDetails;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbWeekDay;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpSession;
import jdk.nashorn.api.scripting.JSObject;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author kasun_n
 */
@Service("resultService")
public class ResultService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private ProductProfileService productProfileService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ResultBuilder resultBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private SpecialGameProfileBuilder specialGameProfileBuilder;

    @Autowired
    private GameProfileService gameProfileService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private WiningLogicService winingLogicService;

    private static SimpleDateFormat dateFormat1;
    private static SimpleDateFormat dateFormat2;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(DlbWbResult result, List<DlbWbResultDetails> dlbWbResultDetails, HttpSession session) throws Exception {

        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        result.setCreatedTime(new Date());
        result.setLastUpdatedTime(new Date());
        result.setLastUpdatedUser(systemUser.getUsername());

        int id = (int) genericRepository.save(result);

        Iterator<DlbWbResultDetails> items = dlbWbResultDetails.iterator();

        while (items.hasNext()) {
            DlbWbResultDetails details = items.next();
            details.setCreatedTime(new Date());
            details.setLastUpdatedTime(new Date());
            details.setLastUpdatedUser(systemUser.getUsername());
            details.setDate(result.getDate());

            genericRepository.save(details);

//            throw new Exception();
        }

        result.setId(id);
        return result;
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbResult result) throws Exception {
        return (int) genericRepository.update(result);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public List get(int id) throws Exception {
        List resultList = new ArrayList();
        List resultLines = new ArrayList();
        DlbWbResult result = (DlbWbResult) genericRepository.get(id, DlbWbResult.class);

        //init proxy objects
        Hibernate.initialize(result.getDlbWbProduct());
        Hibernate.initialize(result.getDlbWbWeekDay());
        Hibernate.initialize(result.getDlbStatus());

        WhereStatement statement = new WhereStatement("dlbWbResult.id", result.getId(), SystemVarList.EQUAL);
        Iterator<DlbWbResultDetails> tmpDetails = genericRepository.list(DlbWbResultDetails.class, "id", SystemVarList.ASC, statement).iterator();

        List<DlbWbResultDetails> dlbWbResultDetailses = new ArrayList<>();
        while (tmpDetails.hasNext()) {
            DlbWbResultDetails details = tmpDetails.next();
            Hibernate.initialize(details.getDlbWbProductItem().getDescription());
            //add init object to list
            resultLines.add(details);
        }

        //set details to list       
        resultList.add(result);
        resultList.add(resultLines);
        result.setDlbWbResultDetailses(dlbWbResultDetailses);
        return resultList;
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public boolean isAlreadySave(DlbWbProduct product, int drawNo, String drawDateStr) throws Exception {

        //set where Statement for draw no and lottery type
        Date drawDate = new SimpleDateFormat("yyyy-MM-dd").parse(drawDateStr);

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct", product, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL);
        WhereStatement whereStatement3 = new WhereStatement("date", drawDate, SystemVarList.EQUAL);

        DlbWbResult result = (DlbWbResult) genericRepository.get(DlbWbResult.class, whereStatement1, whereStatement2);

        DlbWbResult result2 = (DlbWbResult) genericRepository.get(DlbWbResult.class, whereStatement1, whereStatement3);

        if (result != null || result2 != null) {
            return true;
        }

        return false;
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbWbProductProfile.class);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductProfile productProfile = (DlbWbProductProfile) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
            Hibernate.initialize(productProfile.getDlbStatusByStatus());

            list.add(productProfile);

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        List list = new ArrayList();
        return genericRepository.list(DlbWbProductProfile.class, whereStatements);
    }

    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbResult.class, whereStatements, "id", SystemVarList.DESC);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbResult productProfile = (DlbWbResult) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
            Hibernate.initialize(productProfile.getDlbStatus());

            list.add(productProfile);

        }
        return list;

    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbProductProfile productProfile) throws Exception {
        return genericRepository.delete(productProfile);
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listByProduct(DlbWbProduct product) throws Exception {
        List list = new ArrayList();
        DlbStatus dlbStatus = new DlbStatus(SystemVarList.ACTIVE, null, null);
        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct", product, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbStatus", dlbStatus, SystemVarList.EQUAL);
        List tmpList = genericRepository.list(DlbWbProductProfile.class, whereStatement1, whereStatement2);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductProfile productProfile = (DlbWbProductProfile) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
            Hibernate.initialize(productProfile.getDlbStatusByStatus());

            list.add(productProfile);

        }
        return list;
    }
    //list all method

    @Transactional(rollbackFor = Exception.class)
    public List listByProductProfile(int profileID) throws Exception {
        List list = new ArrayList();
        DlbStatus dlbStatus = new DlbStatus(SystemVarList.ACTIVE, null, null);
        DlbWbProductProfile productProfile = new DlbWbProductProfile();
        productProfile.setId(profileID);

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProductProfile", productProfile, SystemVarList.EQUAL);
        // WhereStatement whereStatement2=new WhereStatement("dlbWbProductProfile.dlbStatus",dlbStatus,SystemVarList.EQUAL);
        List tmpList = genericRepository.list(DlbWbProductProfileDetails.class, whereStatement1);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductProfileDetails productProfileDetail = (DlbWbProductProfileDetails) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfileDetail.getDlbWbProductItem());

            list.add(productProfileDetail);

        }
        return list;
    }

    public void saveSpGameResult(List<DlbWbGameResult> results) throws Exception {

        for (DlbWbGameResult result : results) {

            genericRepository.save(result);
        }

    }

    @Transactional
    public Integer getNextDrawNoByProduct(String productCode) {
        int drawNo = 1;
        WhereStatement statement = new WhereStatement("dlbWbProduct.productCode", productCode, SystemVarList.EQUAL);
        List list = genericRepository.list(DlbWbResult.class, "id", SystemVarList.DESC, 1, statement);

        if (!list.isEmpty()) {
            DlbWbResult dlbWbResult = (DlbWbResult) list.get(0);
            drawNo = dlbWbResult.getDrawNo() + 1;

        }

        return drawNo;

    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean saveResultJson(String inputJson, Integer statusId) throws Exception {
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        JSONParser jSONParser = new JSONParser();
        JSONArray jSONArray = (JSONArray) jSONParser.parse(inputJson);
        JSONArray winningNos = new JSONArray();
        List<ResultPublishDto> list = new ArrayList<>();
        String items = "";

        for (Object o : jSONArray) {
            DlbWbResult dlbWbResult = new DlbWbResult();
            if (o instanceof JSONObject) {
                JSONObject object = (JSONObject) o;
                DlbWbProduct dlbWbProduct = productService.get((String) object.get("gameSeries"));
                dlbWbResult.setDlbWbProduct(dlbWbProduct);
                String drawDate = (String) object.get("drawDate");
                dlbWbResult.setDate(dateFormat2.parse(drawDate));
                DlbWbWeekDay dlbWbWeekDay = new DlbWbWeekDay();

                SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                String stringDate = sdf.format(dlbWbResult.getDate());
                dlbWbWeekDay.setDayCode(stringDate.toUpperCase());
                dlbWbResult.setDlbWbWeekDay(dlbWbWeekDay);

                Long drawNo = (Long) object.get("drawNo");
                dlbWbResult.setDrawNo(drawNo.intValue());

                String nextDrawDate = (String) object.get("nextDrawDate");
                dlbWbResult.setNextDate(nextDrawDate);

                DlbSwtStWinningLogic dlbSwtStWinningLogic = new DlbSwtStWinningLogic();
                dlbSwtStWinningLogic.setLogicId(0);
                dlbWbResult.setDlbSwtStWinningLogic(dlbSwtStWinningLogic);

                JSONObject drawResult = (JSONObject) object.get("drawResult");
                String resultStatus = (String) object.get("resultStatus");
                System.out.println(drawNo + " --> " + resultStatus);
                if (dlbWbProduct != null) {
                    if (resultStatus.equals("Available")) {
                        if (drawResult != null) {
                            winningNos = (JSONArray) drawResult.get("winningNos");
                            JSONArray prizesForNextDraw = (JSONArray) drawResult.get("prizesForNextDraw");
                            JSONObject prizesForNextDrawObject = new JSONObject();
                            if (prizesForNextDraw != null) {
                                prizesForNextDrawObject = (JSONObject) prizesForNextDraw.get(0);
                                DecimalFormat df = new DecimalFormat("#.##");
                                System.out.println(df.format(prizesForNextDrawObject.get("prizeValue")));
                                dlbWbResult.setNextJackpot(df.format(prizesForNextDrawObject.get("prizeValue")));
                            }
                        }

                        DlbWbResult result = buildResultObject(dlbWbResult, winningNos, drawDate);
                        saveAutomation(result, drawDate, winningNos, statusId);
                        System.out.println(drawNo + " --> " + "Done");
                    } else {
                        DlbWbResultAutomationAudit audit = new DlbWbResultAutomationAudit();
                        audit.setProduct((String) object.get("gameSeries"));
                        audit.setDrawNo(drawNo.intValue());
                        audit.setStatus("PENDING");
                        audit.setDay(stringDate.toUpperCase());
                        audit.setDrawDate(dateFormat2.parse(drawDate));
                        audit.setDescription((String) object.get("gameDescription"));
                        audit.setCreatedDate(new Date());
                        genericRepository.save(audit);
                    }
                } else {
                    DlbWbResultAutomationAudit audit = new DlbWbResultAutomationAudit();
                    audit.setProduct((String) object.get("gameSeries"));
                    audit.setDrawNo(drawNo.intValue());
                    audit.setStatus("NOT_SAVED");
                    audit.setDay(stringDate.toUpperCase());
                    audit.setDrawDate(dateFormat2.parse(drawDate));
                    audit.setDescription((String) object.get("gameSeries") + " -> Invalid Product Code");
                    audit.setCreatedDate(new Date());
                    genericRepository.save(audit);
                }
            }
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAutomation(DlbWbResult result, String drawDate,
            JSONArray winningNos, Integer statusId) throws Exception {
        boolean alreadySave = isAlreadySave(result.getDlbWbProduct(), result.getDrawNo(), drawDate);
        if (!alreadySave) {

            DlbWbProductProfile dlbWbProductProfile = new DlbWbProductProfile();

            if (winningNos.toJSONString().contains("S-")) {
                dlbWbProductProfile = productProfileService.
                        getValidByProductCodeAndDayCodeAndSpacialCode(
                                result.getDlbWbProduct().getProductCode(),
                                result.getDlbWbWeekDay().getDayCode(),
                                Integer.parseInt(SystemVarList.YES));
            } else {
                dlbWbProductProfile = productProfileService.
                        getValidByProductCodeAndDayCode(
                                result.getDlbWbProduct().getProductCode(),
                                result.getDlbWbWeekDay().getDayCode());
            }

            if (dlbWbProductProfile != null) {
                result.setDlbWbProductProfile(dlbWbProductProfile);
                String weekDay = dlbWbProductProfile.getDlbWbWeekDay().getDescription();
                if (ticketService.validateDrawDateAndDay(result.getDate(), weekDay)) {
                    List<DlbWbResultDetails> detailses = buildResultItem(result, winningNos);
                    DlbWbResult savedResult = (DlbWbResult) saveAutomateJSON(result, detailses, statusId);

                    if (winningNos.toJSONString().contains("S-")) {
                        //special game result
                        List<DlbWbGameResult> buildGameResult = buildSpResultObject(savedResult, winningNos);
//
                        gameProfileService.saveGameResult(buildGameResult);
                    }

                    DlbWbResultAutomationAudit audit = new DlbWbResultAutomationAudit();
                    audit.setProduct(result.getDlbWbProduct().getProductCode());
                    audit.setDrawNo(result.getDrawNo());
                    audit.setStatus("SAVED");
                    audit.setDay(result.getDlbWbWeekDay().getDayCode());
                    audit.setDrawDate(dateFormat2.parse(drawDate));
                    audit.setDescription(result.getDlbWbProduct().getDescription());
                    audit.setCreatedDate(new Date());
                    genericRepository.save(audit);
                }
            } else {
                DlbWbResultAutomationAudit audit = new DlbWbResultAutomationAudit();
                audit.setProduct(result.getDlbWbProduct().getProductCode());
                audit.setDrawNo(result.getDrawNo());
                audit.setStatus("NOT_SAVED");
                audit.setDay(result.getDlbWbWeekDay().getDayCode());
                audit.setDrawDate(dateFormat2.parse(drawDate));
                audit.setDescription("Product Profile Not Founded");
                audit.setCreatedDate(new Date());
                genericRepository.save(audit);
            }
        } else {
            DlbWbResultAutomationAudit audit = new DlbWbResultAutomationAudit();
            audit.setProduct(result.getDlbWbProduct().getProductCode());
            audit.setDrawNo(result.getDrawNo());
            audit.setStatus("ALREADY_SAVED");
            audit.setDay(result.getDlbWbWeekDay().getDayCode());
            audit.setDrawDate(dateFormat2.parse(drawDate));
            audit.setDescription(result.getDlbWbProduct().getDescription());
            audit.setCreatedDate(new Date());
            genericRepository.save(audit);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbResult buildResultObject(DlbWbResult result, JSONArray winningNos, String drawDate) throws ParseException, java.text.ParseException, Exception {

        JSONParser jSONParser = new JSONParser();
        dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
//        MM/dd/yyyy

        Date date = new Date();

        List<DlbWbResultDetails> dlbWbResultDetailses = new ArrayList<DlbWbResultDetails>(0);

        for (int i = 0; i < winningNos.size(); i++) {

            JSONObject jSONObject = (JSONObject) winningNos.get(i);

            //get item code and value from json
            String resultItemCode = (String) jSONObject.get("id");
            String val = (String) jSONObject.get("value");

            if (!resultItemCode.contains("S-")) {
                //set proxy objects 
                if (resultItemCode.contains("Lagna")) {
                    DlbWbProductItem productItem = new DlbWbProductItem(resultItemCode.replace("Lagna", "ZS"), null, null);
                    productItem = (DlbWbProductItem) genericRepository.get(resultItemCode.replace("Lagna", "ZS"), DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(),
                            productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    details.setLastUpdatedTime(date);
                    details.setCreatedTime(date);
                    dlbWbResultDetailses.add(details);
                } else if (resultItemCode.contains("EnglishLetter")) {
                    DlbWbProductItem productItem = new DlbWbProductItem("EL", null, null);
                    productItem = (DlbWbProductItem) genericRepository.get("EL", DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(),
                            productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    details.setLastUpdatedTime(date);
                    details.setCreatedTime(date);
                    dlbWbResultDetailses.add(details);
                } else if (resultItemCode.contains("Bonus No")) {
                    DlbWbProductItem productItem = new DlbWbProductItem("LM", null, null);
                    productItem = (DlbWbProductItem) genericRepository.get("LM", DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(),
                            productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    details.setLastUpdatedTime(date);
                    details.setCreatedTime(date);
                    dlbWbResultDetailses.add(details);
                } else {
                    DlbWbProductItem productItem = new DlbWbProductItem(resultItemCode.replace("No ", ""), null, null);
                    productItem = (DlbWbProductItem) genericRepository.get(resultItemCode.replace("No ", ""), DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(),
                            productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    details.setLastUpdatedTime(date);
                    details.setCreatedTime(date);
                    dlbWbResultDetailses.add(details);
                }

            }

        }
        DlbStatus dlbStatus = new DlbStatus(SystemVarList.SUBMITED, null, null);
        result.setDlbStatus(dlbStatus);
        // set SET to main object
        if (result.getNextJackpot() != null) {
            result.setNextJackpot(result.getNextJackpot().replace(",", ""));
        }
        result.setDlbWbResultDetailses(dlbWbResultDetailses);
        result.setDate(dateFormat2.parse(drawDate));
        Date nxtDateStr = dateFormat2.parse(result.getNextDate());
        result.setNextDate(dateFormat1.format(nxtDateStr));
        result.setLastUpdatedTime(date);
        result.setCreatedTime(date);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbResultDetails> buildResultItem(DlbWbResult result, JSONArray items) throws ParseException, java.text.ParseException {
        JSONParser jSONParser = new JSONParser();

        List<DlbWbResultDetails> dlbWbResultDetailses = new ArrayList<DlbWbResultDetails>();

        for (int i = 0; i < items.size(); i++) {

            JSONObject jSONObject = (JSONObject) items.get(i);

            //get item code and value from json
            String resultItemCode = (String) jSONObject.get("id");
            String val = (String) jSONObject.get("value");

            if (!resultItemCode.contains("S-")) {
                //set proxy objects 
                if (resultItemCode.contains("Lagna")) {
                    DlbWbProductItem productItem = new DlbWbProductItem(resultItemCode.replace("Lagna", "ZS"), null, null);
                    productItem = (DlbWbProductItem) genericRepository.get(resultItemCode.replace("Lagna", "ZS"), DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(), productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    dlbWbResultDetailses.add(details);
                } else if (resultItemCode.contains("EnglishLetter")) {
                    DlbWbProductItem productItem = new DlbWbProductItem("EL", null, null);
                    productItem = (DlbWbProductItem) genericRepository.get("EL", DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(), productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    dlbWbResultDetailses.add(details);
                } else if (resultItemCode.contains("Bonus No")) {
                    DlbWbProductItem productItem = new DlbWbProductItem("LM", null, null);
                    productItem = (DlbWbProductItem) genericRepository.get("LM", DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(), productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    dlbWbResultDetailses.add(details);
                }  else {
                    DlbWbProductItem productItem = new DlbWbProductItem(resultItemCode.replace("No ", ""), null, null);
                    productItem = (DlbWbProductItem) genericRepository.get(resultItemCode.replace("No ", ""), DlbWbProductItem.class);
                    DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(), productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
                    dlbWbResultDetailses.add(details);
                }

            }
        }
        return dlbWbResultDetailses;
    }

    public List<DlbWbGameResult> buildSpResultObject(DlbWbResult result, JSONArray spGame) throws ParseException {
        List<DlbWbGameResult> results = new ArrayList<>();
        DlbWbGameProfile dlbWbGameProfile = result.getDlbWbProductProfile().getDlbWbGameProfile();
        Date date = new Date();

        for (int i = 0; i < spGame.size(); i++) {
            JSONObject jSONObject = (JSONObject) spGame.get(i);

            //get item code and value from json         
            String gameId = (String) jSONObject.get("id");
            String val = (String) jSONObject.get("value");

            WhereStatement statement1 = new WhereStatement("dlbWbGameProfile.id", dlbWbGameProfile.getId(), SystemVarList.EQUAL);
            WhereStatement statement2 = new WhereStatement("verificationDigits", String.valueOf(val.length()), SystemVarList.EQUAL);
            DlbWbGame dlbWbGame = (DlbWbGame) genericRepository.
                    get(DlbWbGame.class, statement1, statement2);

            if (gameId.contains("S-")) {
                DlbWbGameResult dlbWbGameResult = new DlbWbGameResult();

                dlbWbGameResult.setDlbWbGame(dlbWbGame);
                dlbWbGameResult.setDlbWbResult(result);
                dlbWbGameResult.setWinSpNumber(val);

                results.add(dlbWbGameResult);
            }

        }

        return results;

    }

    @Transactional(rollbackFor = Exception.class)
    public Object saveAutomateJSON(DlbWbResult result, List<DlbWbResultDetails> dlbWbResultDetails, Integer statusId) throws Exception {

        DlbWbSystemUser systemUser = userService.get("admin");
        result.setCreatedTime(new Date());
        result.setLastUpdatedTime(new Date());
        result.setLastUpdatedUser(systemUser.getUsername());
        result.setDlbStatus((DlbStatus) genericRepository.get(statusId, DlbStatus.class));
        int id = (int) genericRepository.save(result);

        Iterator<DlbWbResultDetails> items = dlbWbResultDetails.iterator();

        while (items.hasNext()) {
            DlbWbResultDetails details = items.next();
            details.setCreatedTime(new Date());
            details.setLastUpdatedTime(new Date());
            details.setLastUpdatedUser(systemUser.getUsername());
            details.setDate(result.getDate());
            System.out.println(details.getDlbWbProductItem().getItemCode());
            genericRepository.save(details);
        }

        result.setId(id);
        if (statusId == 27) {
            saveAuditLog(result, systemUser, "AUTOMATED", "RSA");
        } else if (statusId == 31) {
            saveAuditLog(result, systemUser, "JSON-SUBMIT", "RUP");
        }

        return result;
    }

    public String getDataFromFile(MultipartFile resultFile) throws IOException {
        BufferedReader br;
        String result = new String();
        String line;
        InputStream is = resultFile.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        result = result + "[";
        while ((line = br.readLine()) != null) {
            result = result + line;
        }
        result = result + "]";
        return result;
    }

    public String getValidationStatus(String jsonResult) throws ParseException {
        if (jsonResult.contains("drawDate") && jsonResult.contains("gameSeries")
                && jsonResult.contains("gameDescription") && jsonResult.contains("drawNo")
                && jsonResult.contains("resultStatus") && jsonResult.contains("nextDrawDate")
                && jsonResult.contains("winningNos") && jsonResult.contains("prizesForNextDraw")
                && jsonResult.contains("prizeValue")) {
            if (getValidWinningNo(jsonResult)) {
                return "Valid";
            } else {
                return "Invalid Request, Please check JSON File";
            }
        } else {
            return "Invalid Request, Please check JSON File";
        }
    }

    public Boolean getValidWinningNo(String jsonResult) throws ParseException {
        JSONParser jSONParser = new JSONParser();
        JSONArray jSONArray = (JSONArray) jSONParser.parse(jsonResult);
        JSONArray winningNos = new JSONArray();
        JSONArray prizesForNextDraw = new JSONArray();
        for (Object o : jSONArray) {
            if (o instanceof JSONObject) {
                JSONObject object = (JSONObject) o;
                JSONObject drawResult = (JSONObject) object.get("drawResult");
                winningNos = (JSONArray) drawResult.get("winningNos");
                prizesForNextDraw = (JSONArray) drawResult.get("prizesForNextDraw");
            }
        }
        if (winningNos.toJSONString().contains("id")
                && winningNos.toJSONString().contains("value")
                && prizesForNextDraw.toJSONString().contains("prizeValue")) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void automationStartEndLog(String automationStatus, String jsonDataFromDlb) throws Exception {
        DlbWbResultAutomationAudit audit = new DlbWbResultAutomationAudit();
        audit.setStatus(automationStatus);
        audit.setDrawDate(new Date());
        audit.setDescription(jsonDataFromDlb);
        audit.setCreatedDate(new Date());
        genericRepository.save(audit);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAuditLog(DlbWbResult result, DlbWbSystemUser user, String status, String page) throws Exception {
        DlbWbProduct dlbWbProduct = productService.get(result.getDlbWbProduct().getProductCode());
        DlbSwtStWinningLogic logic = winingLogicService.getById(result.getDlbSwtStWinningLogic().getLogicId());

        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Draw No", result.getDrawNo());
        jSONObject.put("Product", dlbWbProduct.getDescription());
        jSONObject.put("Day", result.getDlbWbWeekDay().getDescription());
        jSONObject.put("Draw Date", result.getDate());
        jSONObject.put("Next Draw Date", result.getNextDate());
        jSONObject.put("Next Jackpot", result.getNextJackpot());
        jSONObject.put("Winning Logic", result.getDlbSwtStWinningLogic().
                getLogicId() + " - " + logic.getLogicDescription());

        System.out.println(jSONObject.toJSONString());
        System.out.println("----Going To Update as a Approved----");
        activityLogService.save(activityLogService.buildActivityLog(
                status, jSONObject, page, user));
    }
}
