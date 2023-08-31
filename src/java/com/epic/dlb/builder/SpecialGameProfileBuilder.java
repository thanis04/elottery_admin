/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.controller.GameProfileController;
import com.epic.dlb.model.DlbWbGame;
import com.epic.dlb.model.DlbWbGameProfile;
import com.epic.dlb.model.DlbWbGameResult;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
@Component("specialGameProfileBuilder")
public class SpecialGameProfileBuilder {
    
    public JSONArray buildGameList(List<DlbWbGame> dlbWbGames){
    
        JSONArray array=new JSONArray();
        
        Iterator<DlbWbGame> iterator = dlbWbGames.iterator();
    
        while (iterator.hasNext()) {
            DlbWbGame next = iterator.next();
            
            JSONObject jSONObject=new JSONObject();
            
            jSONObject.put("id", next.getId());
            jSONObject.put("name", next.getName());
            
            array.add(jSONObject);
            
        }
        
        return array;
    
    }
    
      public List<DlbWbGameResult> buildSpResultObject(DlbWbResult result, String items) throws ParseException {
        List<DlbWbGameResult> results = new ArrayList<>();

        JSONParser jSONParser=new JSONParser();
        
        JSONArray jsonItems = (JSONArray) jSONParser.parse(items);
        Date date = new Date();

        for (int i = 0; i < jsonItems.size(); i++) {

            JSONObject jSONObject = (JSONObject) jsonItems.get(i);

            //get item code and value from json         
            Long gameId = (Long) jSONObject.get("gameId");
            String val = (String) jSONObject.get("val");

            DlbWbGameResult dlbWbGameResult = new DlbWbGameResult();
            
            

            dlbWbGameResult.setDlbWbGame(new DlbWbGame(gameId.intValue()));
            dlbWbGameResult.setDlbWbResult(result);
            dlbWbGameResult.setWinSpNumber(val);

            results.add(dlbWbGameResult);

        }

        return results;

    }
    
    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbGameProfile> dlbWbGameProfiles) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < dlbWbGameProfiles.size(); i++) {
            DlbWbGameProfile dlbWbProduct = dlbWbGameProfiles.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("id", dlbWbProduct.getId());          
            row.put("name", dlbWbProduct.getName());          
            row.put("status", dlbWbProduct.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }
    
    
    public JSONArray buildResult( List<DlbWbGameResult> resultByResultId ){
         //set result items
        JSONArray resultItems = new JSONArray();
        Iterator<DlbWbGameResult> iterator = resultByResultId.iterator();
        while (iterator.hasNext()) {
            JSONObject resultItem = new JSONObject();
            DlbWbGameResult details = iterator.next();
            resultItem.put("item", details.getDlbWbGame().getName());           
             resultItem.put("value", details.getWinSpNumber());

            resultItems.add(resultItem);

        }
        return resultItems;     
    }
    
}
