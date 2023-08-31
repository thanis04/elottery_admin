/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbGameProfile;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbProductProfileDetails;
import com.epic.dlb.model.DlbWbSymbol;
import com.epic.dlb.model.DlbWbWeekDay;
import com.epic.dlb.service.SymbolService;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Kasun
 */
@Component("productProfileBuilder")
public class ProductProfileBuilder {

    @Autowired
    private SymbolService symbolService;

    private static SimpleDateFormat dateFormat;

    //build object using request parameters   
    public DlbWbProductProfile buildObject(String formData, String url, String items, String lastUpdatedUser) throws ParseException, IOException {
        JSONParser jSONParser = new JSONParser();

        JSONObject jSONObject = (JSONObject) jSONParser.parse(formData);
        DlbWbProductProfile productProfile = new DlbWbProductProfile();
        productProfile.setId(Integer.parseInt((String) jSONObject.get("id")));
        DlbWbProduct product = new DlbWbProduct((String) jSONObject.get("productCode"), null, null);
        DlbWbWeekDay weekDay = new DlbWbWeekDay((String) jSONObject.get("dayCode"), null, null);
        DlbStatus dlbStatus = new DlbStatus(Integer.parseInt((String) jSONObject.get("statusCode")), null, null);

        String engLeterYes = jSONObject.get("engLetter") != null ? (String) jSONObject.get("engLetter") : "NO";
        String zodiYes = jSONObject.get("zodiac") != null ? (String) jSONObject.get("zodiac") : "NO";
        String bonusYes = jSONObject.get("bonusNo") != null ? (String) jSONObject.get("bonusNo") : "NO";
        String specialChk = jSONObject.get("specialStatus") != null ? (String) jSONObject.get("specialStatus") : "NO";

        DlbStatus specialStatus = specialChk.equals("YES") ? new DlbStatus(Integer.parseInt(SystemVarList.YES), null, null) : new DlbStatus(Integer.parseInt(SystemVarList.NO), null, null);

        String engLeterPos = jSONObject.get("engPos") != null ? (String) jSONObject.get("engPos") : "0";
        String zodiPos = jSONObject.get("zodiacPos") != null ? (String) jSONObject.get("zodiacPos") : "0";
        String bonusPOs = jSONObject.get("bonusPos") != null ? (String) jSONObject.get("bonusPos") : "0";
        String specialPOs = jSONObject.get("specialPos") != null ? (String) jSONObject.get("specialPos") : "0";

        if (specialStatus.getStatusCode() == Integer.parseInt(SystemVarList.YES)) {
            int gameProfileId = Integer.parseInt((String) jSONObject.get("gameProfile"));
            DlbWbGameProfile dlbWbGameProfile = new DlbWbGameProfile(gameProfileId);
            productProfile.setDlbWbGameProfile(dlbWbGameProfile);
        }

        //set image icon       
        productProfile.setDrawNo(0);

        productProfile.setDlbStatusBySpecialStatus(specialStatus);
//        productProfile.setTemplate(Files.readAllBytes(file.toPath()));
        productProfile.setDlbStatusByStatus(dlbStatus);
        productProfile.setDlbWbProduct(product);
        productProfile.setDlbWbWeekDay(weekDay);
        productProfile.setDlbWbWeekDay(weekDay);

        productProfile.setEngLetter(engLeterYes);
        productProfile.setZodiac(zodiYes);
        productProfile.setBonusNo(bonusYes);
        productProfile.setBonusNo(bonusYes);
        productProfile.setSpecialPos(Integer.parseInt(specialPOs));

        productProfile.setEngPos(Integer.parseInt(engLeterPos));
        productProfile.setZodiacPos(Integer.parseInt(zodiPos));
        productProfile.setBonusPos(Integer.parseInt(bonusPOs));

        //STD
        Date currentDate = new Date();
        productProfile.setLastUpdatedTime(currentDate);
        productProfile.setCreatedTime(currentDate);
        productProfile.setLastUpdatedUser(lastUpdatedUser);

        //set items
        List<DlbWbProductProfileDetails> dlbWbProductProfileDetailses = new ArrayList<DlbWbProductProfileDetails>(0);
        JSONArray jsonItems = (JSONArray) jSONParser.parse(items);

        for (int i = 0; i < jsonItems.size(); i++) {
            JSONObject jsonItem = (JSONObject) jsonItems.get(i);

            //get values
            String val = (String) jsonItem.get("val");
            int order = Integer.parseUnsignedInt((String) jsonItem.get("order"));

            DlbWbProductItem productItem = new DlbWbProductItem(val, null, null);
            DlbWbProductProfileDetails details = new DlbWbProductProfileDetails(product, productItem, productProfile, weekDay);
            details.setItemOrder(order);
            //add to list
            dlbWbProductProfileDetailses.add(details);
        }

        //set items to object
        productProfile.setDlbWbProductProfileDetailses(dlbWbProductProfileDetailses);

        return productProfile;

    }

    //build json object from model object
    public JSONObject buildJSONObject(DlbWbProduct product) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("code", product.getProductCode());
        object.put("description", product.getDescription());
        object.put("status", product.getDlbStatus().getStatusCode());
        object.put("icon", product.getProductIcon());

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbProductProfile> productProfiles) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < productProfiles.size(); i++) {
            DlbWbProductProfile productProfile = productProfiles.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
//            row.put("id", productProfile.getId());
            row.put("description", productProfile.getDlbWbProduct().getDescription() + "-" + productProfile.getDlbWbWeekDay().getDescription());
            row.put("status", productProfile.getDlbStatusByStatus().getDescription());
            row.put("id", productProfile.getId());

            table.add(row);

        }

        return table;

    }

    //create profile lists
    @Transactional
    public JSONArray buildDayList(List<DlbWbProductProfile> list) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < list.size(); i++) {
            DlbWbProductProfile productList = list.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("day_code", productList.getId());
            String specialText = "";

            if (productList.getDlbStatusBySpecialStatus().getStatusCode() == Integer.parseInt(SystemVarList.YES)) {
                specialText = " - Special Draw ";
            }
            row.put("description", productList.getDlbWbWeekDay().getDescription() + specialText);

            table.add(row);

        }

        return table;
    }

    //create profile lists
    public JSONArray buildItemList(List<DlbWbProductProfileDetails> list) throws Exception {
        JSONArray table = new JSONArray();
       
        //loop lists
        
        Iterator<DlbWbProductProfileDetails> tmpList = list.iterator();
  
        while (tmpList.hasNext()) {
            DlbWbProductProfileDetails profileDetails  = tmpList.next();


            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("code", profileDetails.getDlbWbProductItem().getItemCode());
            row.put("description", profileDetails.getDlbWbProductItem().getDescription());
            JSONArray symbolJSONList = new JSONArray();

            String valueType = "text";//default

            //load symbol lists if exsits(if not exsits show normal text input)
            List symbolList = symbolService.listByType(profileDetails.getDlbWbProductItem());
            if (symbolList != null && symbolList.size() > 0) {
                valueType = "select";

                for (int j = 0; j < symbolList.size(); j++) { 
                    DlbWbSymbol symbol = (DlbWbSymbol) symbolList.get(j);
                    JSONObject symbolJSON = new JSONObject();
                    symbolJSON.put("description", symbol.getDescription());

                    symbolJSONList.add(symbolJSON);
                }

            }

            row.put("value_type", valueType);
            row.put("symbols", symbolJSONList);

            table.add(row);

        }

        return table;
    }

    public JSONObject buildObject(DlbWbProductProfile product) {

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        //put to JSON Object
        JSONObject row = new JSONObject();
        row.put("lottery_name", product.getDlbWbProduct().getDescription());
        row.put("day", product.getDlbWbWeekDay().getDescription());
        row.put("status", product.getDlbStatusByStatus().getDescription());
        row.put("sp_draw", product.getDlbStatusBySpecialStatus().getDescription());

        JSONArray symbolJSONList = new JSONArray();

        List<DlbWbProductProfileDetails> sortedList = new ArrayList<DlbWbProductProfileDetails>();
        sortedList.addAll(product.getDlbWbProductProfileDetailses());
        Collections.sort(sortedList, (DlbWbProductProfileDetails it1, DlbWbProductProfileDetails it2) -> it1.getItemOrder().compareTo(it2.getItemOrder()));
        Iterator<DlbWbProductProfileDetails> iterator = sortedList.iterator();
        while (iterator.hasNext()) {
            DlbWbProductProfileDetails next = iterator.next();

            JSONObject item = new JSONObject();

            item.put("id", next.getId());
            item.put("product_item", next.getDlbWbProductItem().getDescription());

            symbolJSONList.add(item);

        }

        row.put("product_item", symbolJSONList);
        String base64 = "";
        if (product.getTemplate() != null) {
            base64 = new String(Base64.encodeBase64(product.getTemplate()));
        }

        row.put("template", base64);

        return row;

    }

    public JSONObject buildJSONObject(DlbWbProductProfile product) {

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        //put to JSON Object
        JSONObject row = new JSONObject();
        row.put("lottery", product.getDlbWbProduct().getProductCode());
        row.put("day_code", product.getDlbWbWeekDay().getDayCode());
        row.put("status", product.getDlbStatusByStatus().getStatusCode());
        row.put("sp_draw", product.getDlbStatusBySpecialStatus().getStatusCode());
        row.put("sp_draw_no", product.getDrawNo());

        row.put("eng_status", product.getEngLetter());
        row.put("eng_pos", product.getEngPos());

        row.put("bonus_status", product.getBonusNo());
        row.put("bonus_pos", product.getBonusPos());

        row.put("zd_status", product.getZodiac());
        row.put("zd_pos", product.getZodiacPos());

        JSONArray symbolJSONList = new JSONArray();

        List<DlbWbProductProfileDetails> sortedList = new ArrayList<DlbWbProductProfileDetails>();
        sortedList.addAll(product.getDlbWbProductProfileDetailses());
        Collections.sort(sortedList, (DlbWbProductProfileDetails it1, DlbWbProductProfileDetails it2) -> it1.getItemOrder().compareTo(it2.getItemOrder()));
        Iterator<DlbWbProductProfileDetails> iterator = sortedList.iterator();
        while (iterator.hasNext()) {
            DlbWbProductProfileDetails next = iterator.next();

            JSONObject item = new JSONObject();

            item.put("id", next.getDlbWbProductItem().getItemCode());
            item.put("product_item", next.getDlbWbProductItem().getDescription().substring(0, 3));

            symbolJSONList.add(item);

        }

        row.put("product_item", symbolJSONList);
        String base64 = "";
        if (product.getTemplate() != null) {
            base64 = new String(Base64.encodeBase64(product.getTemplate()));
        }

        row.put("template", base64);

        return row;

    }
}
