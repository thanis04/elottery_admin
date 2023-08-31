/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

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
 * @author Kasun
 */
@Component("employeeBuilder")
public class EmployeeBuilder {

    //build json object from model object
    public JSONObject buildJSONObject(DlbWbEmployee employee) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("employeeid", employee.getEmployeeid());
        object.put("name", employee.getName());
        object.put("nic", employee.getNic());
        object.put("address", employee.getAddress());
        object.put("email", employee.getEmail());
        object.put("contactno", employee.getContactno());
        object.put("status", employee.getDlbStatus().getStatusCode());

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbEmployee> productLists) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < productLists.size(); i++) {
            DlbWbEmployee employee = productLists.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("employeeid", employee.getEmployeeid());
            row.put("name", employee.getName());
            row.put("nic", employee.getNic());
            row.put("address", employee.getAddress());
            row.put("contactno", employee.getContactno());
            row.put("email", employee.getEmail());
            row.put("status", employee.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }

}
