/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author ridmi_g
 */
@Component("userRoleBuilder")
public class UserRoleBuilder {
    
     //build json object from model object
    public JSONObject buildJSONObject(DlbWbUserRole userRole) {

        //put to JSON Object
        JSONObject object=new JSONObject();
        object.put("userrolecode",userRole.getUserrolecode());
        object.put("description", userRole.getDescription());
        object.put("status", userRole.getDlbStatus().getStatusCode());

        return object;

    }
    
    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbUserRole> userRoles) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < userRoles.size(); i++) {
            DlbWbUserRole userRole = userRoles.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("userrolecode", userRole.getUserrolecode());
            row.put("description", userRole.getDescription());
            row.put("status", userRole.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }
    
}
