/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kasun
 */
@Component("productListBuilder")
public class ProductListBuilder {

    //build json object from model object
    public JSONObject buildJSONObject(DlbWbProductList productList) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("productCode", productList.getId().getProductCode());      
        object.put("day_code", productList.getId().getDayCode());
        object.put("status", productList.getDlbStatus().getStatusCode());
      

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbProductList> productLists) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < productLists.size(); i++) {
            DlbWbProductList productList = productLists.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("productCode", productList.getDlbWbProduct().getProductCode());
            row.put("description", productList.getDlbWbProduct().getDescription()+"-"+productList.getDlbWbWeekDay().getDescription());
            row.put("day_code", productList.getDlbWbWeekDay().getDayCode());
            row.put("status", productList.getDlbStatus().getDescription());
           

            table.add(row);

        }

        return table;

    }

    
    //create days
    public JSONArray buildDayList(List<DlbWbProductList> list){
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
