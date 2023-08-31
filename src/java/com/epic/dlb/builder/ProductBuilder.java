/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbProduct;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component; 

/**
 *
 * @author Kasun
 */
@Component("productBuilder")
public class ProductBuilder {

    
 
    
    //build json object from model object
    public JSONObject buildJSONObject(DlbWbProduct product) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("code", product.getProductCode());
        object.put("description", product.getDescription());
        object.put("status", product.getDlbStatus().getStatusCode());       
        object.put("status_des", product.getDlbStatus().getDescription());       
        object.put("icon",product.getProductIcon() );

        return object;
        
        
//         InputStream inputStream = null;
//        OutputStream outputStream = null;
//        String destincationPath = Configuration.getConfiguration("TMP_STORE_DIR_PATH");
//        File directory = new File(destincationPath);
//        if (!directory.exists()) {
//            directory.mkdir(); 
//        }
//        Files.deleteIfExists(new File(destincationPath + File.separator + product.getProductCode() + ".jpg").toPath());    
//        
//        //put to JSON Object
//        JSONObject object = new JSONObject();
//        object.put("code", product.getProductCode());
//        object.put("description", product.getDescription());       
//        object.put("status_des", product.getDlbStatus().getDescription());
//        object.put("icon", product.getProductIcon());
//        //save image to server from firebase
//
//        URL url = new URL(product.getProductIcon());
//        inputStream = url.openStream();
//        outputStream = new FileOutputStream(destincationPath + File.separator + product.getProductCode() + ".jpg");
//
//        byte[] buffer = new byte[2048];
//        int length;
//
//        while ((length = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, length);
//        }
//
//        object.put("file", destincationPath + File.separator + product.getProductCode() + ".jpg");
//
//        return object;

    }
    
    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbProduct> products) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < products.size(); i++) {
            DlbWbProduct dlbWbProduct = products.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("code", dlbWbProduct.getProductCode());
            row.put("description", dlbWbProduct.getDescription());
            row.put("status", dlbWbProduct.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }

}
