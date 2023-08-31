/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbGame;
import com.epic.dlb.model.DlbWbGameProfile;
import com.epic.dlb.model.DlbWbGameResult;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbResult;
import com.epic.dlb.model.DlbWbResultDetails;
import com.epic.dlb.model.DlbWbResultSpDetails;
import com.epic.dlb.service.ProductProfileService;
import com.epic.dlb.util.common.SystemVarList;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kasun
 */
@Component("ResultBuilder")
public class ResultBuilder {

    private static SimpleDateFormat dateFormat1;
    private static SimpleDateFormat dateFormat2;

    @Autowired
    private ProductProfileService productProfileService;

    //build json object from model object
    public JSONObject buildJSONObject(DlbWbResult result, List<DlbWbResultDetails> rstDetailses) {
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("id", result.getId());
        object.put("product", result.getDlbWbProduct().getDescription());
        object.put("day_code", result.getDlbWbWeekDay().getDescription());
        object.put("draw_no", result.getDrawNo());
        object.put("draw_date", dateFormat1.format(result.getDate()));
        object.put("status_code", result.getDlbStatus().getStatusCode());
        object.put("status", result.getDlbStatus().getDescription());

        Iterator<DlbWbResultDetails> detailses = rstDetailses.iterator();

        //set result items
        JSONArray resultItems = new JSONArray();
        while (detailses.hasNext()) {
            JSONObject resultItem = new JSONObject();
            DlbWbResultDetails details = detailses.next();
            resultItem.put("item", details.getDlbWbProductItem().getDescription());
            if (details.getValue().length() > 2) {
                resultItem.put("value", details.getValue());
            } else {
                resultItem.put("value", details.getValue());
            }

            resultItems.add(resultItem);

        }
        object.put("items", resultItems);

        return object;

    }

    public DlbWbResult buildResultObject(DlbWbResult result, String items, String drawDate, DlbWbProductProfile dlbWbProductProfile) throws ParseException, java.text.ParseException, Exception {

        JSONParser jSONParser = new JSONParser();
        dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
//        MM/dd/yyyy
        JSONArray jsonItems = (JSONArray) jSONParser.parse(items);
        Date date = new Date();

        List<DlbWbResultDetails> dlbWbResultDetailses = new ArrayList<DlbWbResultDetails>(0);

        for (int i = 0; i < jsonItems.size(); i++) {

            JSONObject jSONObject = (JSONObject) jsonItems.get(i);

            //get item code and value from json
            String resultItemCode = (String) jSONObject.get("code");
            String val = (String) jSONObject.get("val");

            //set proxy objects 
            DlbWbProductItem productItem = new DlbWbProductItem(resultItemCode, null, null);
            DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(),
                    productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
            details.setLastUpdatedTime(date);
            details.setCreatedTime(date);
            dlbWbResultDetailses.add(details);

        }
        DlbStatus dlbStatus = new DlbStatus(SystemVarList.SUBMITED, null, null);
        result.setDlbStatus(dlbStatus);
        // set SET to main object
        result.setNextJackpot(result.getNextJackpot().replace(",", ""));
        result.setDlbWbResultDetailses(dlbWbResultDetailses);
        result.setDate(dateFormat2.parse(drawDate));
        Date nxtDateStr = dateFormat2.parse(result.getNextDate());
        result.setNextDate(dateFormat1.format(nxtDateStr));
        result.setLastUpdatedTime(date);
        result.setCreatedTime(date);

        return result;
    }

   
    public List<DlbWbResultDetails> buildResultItem(DlbWbResult result, String items) throws ParseException, java.text.ParseException {
        JSONParser jSONParser = new JSONParser();
        JSONArray jsonItems = (JSONArray) jSONParser.parse(items);

        List<DlbWbResultDetails> dlbWbResultDetailses = new ArrayList<DlbWbResultDetails>();

        for (int i = 0; i < jsonItems.size(); i++) {

            JSONObject jSONObject = (JSONObject) jsonItems.get(i);

            //get item code and value from json
            String resultItemCode = (String) jSONObject.get("code");
            String val = (String) jSONObject.get("val");

            //set proxy objects 
            DlbWbProductItem productItem = new DlbWbProductItem(resultItemCode, null, null);
            DlbWbResultDetails details = new DlbWbResultDetails(result.getDlbWbProduct(), productItem, result, result.getDlbWbWeekDay(), result.getDrawNo(), val);
            dlbWbResultDetailses.add(details);
        }
        return dlbWbResultDetailses;
    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbResult> productLists) {
        JSONArray table = new JSONArray();
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        //loop lists
        for (int i = 0; i < productLists.size(); i++) {
            DlbWbResult result = productLists.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("id", result.getId());
            row.put("product", result.getDlbWbProduct().getDescription());
            row.put("day", result.getDlbWbWeekDay().getDescription());
            row.put("draw_no", result.getDrawNo());
            row.put("date", dateFormat1.format(result.getDate()));
            row.put("status", result.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }

    //create days
    public JSONArray buildDayList(List<DlbWbProductList> list) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < list.size(); i++) {
            DlbWbProductList productList = list.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("day_code", productList.getDlbWbWeekDay().getDayCode());
            row.put("description", productList.getDlbWbWeekDay().getDescription());

            table.add(row);

        }

        return table;
    }
}
