/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbPromotion;
import com.epic.dlb.util.common.SystemVarList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

/**
 *
 * @author kasun_n
 */
@Component("promotionBuilder")
public class PromotionBuilder {

    private static SimpleDateFormat dateFormat;

    public DlbWbPromotion buildPromotion(String jsonData,String largeImgUrl,String smallImgUrl) throws ParseException, java.text.ParseException {
        DlbWbPromotion promotion = new DlbWbPromotion();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONParser parser = new JSONParser();

        JSONObject jSONObject = (JSONObject) parser.parse(jsonData);
        promotion.setId(jSONObject.containsKey("id") &&  !"".equals((String) jSONObject.get("id"))?Integer.parseInt((String) jSONObject.get("id")):null); 
        promotion.setValidFrom(dateFormat.parse((String) jSONObject.get("validFrom")));
        promotion.setDescription((String) jSONObject.get("description"));
        promotion.setSmallImgUrl(smallImgUrl);
        promotion.setLargeImgUrl(largeImgUrl);
        promotion.setValidTo(dateFormat.parse((String) jSONObject.get("validTo")));
        promotion.setCreatedDate(new Date());
        promotion.setDlbStatus(new DlbStatus(SystemVarList.ACTIVE, null, null));

        return promotion;

    }
    
     //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbPromotion> promotions) {
        JSONArray table = new JSONArray();
          dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //loop lists
        for (int i = 0; i < promotions.size(); i++) {
            DlbWbPromotion promotion = promotions.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("id", promotion.getId());
            row.put("description", promotion.getDescription());
            row.put("valid_from",dateFormat.format( promotion.getValidFrom()));
            row.put("valid_to",dateFormat.format( promotion.getValidTo()));         
            row.put("status", promotion.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }
    public JSONObject buildObject(DlbWbPromotion promotion) {
     
          dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
      
            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("id", promotion.getId());
            row.put("description", promotion.getDescription());
            row.put("valid_from",dateFormat.format( promotion.getValidFrom()));
            row.put("valid_to",dateFormat.format( promotion.getValidTo()));         
            row.put("smallImgUrl", promotion.getSmallImgUrl());         
            row.put("largeImgUrl",promotion.getLargeImgUrl());         
            row.put("status", promotion.getDlbStatus().getStatusCode());


        return row;

    }
}
