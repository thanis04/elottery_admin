/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.model.DlbWbProductList;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Kasun
 */
@Component("productItemBuilder")
public class ProductItemBuilder {
   
    
    //build json object from model object
    public JSONObject buildJSONObject(DlbWbProductItem productItem) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("itemCode", productItem.getItemCode());
        object.put("description", productItem.getDescription());
        object.put("status", productItem.getDlbStatus().getStatusCode());

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbProductItem> productItems) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < productItems.size(); i++) {
            DlbWbProductItem productItem = productItems.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("itemCode", productItem.getItemCode());
            row.put("description", productItem.getDescription());
            row.put("status", productItem.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }

}
