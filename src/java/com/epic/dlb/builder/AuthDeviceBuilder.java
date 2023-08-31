/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbAuthorizedDevice;
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
@Component("authDeviceBuilder")
public class AuthDeviceBuilder {

    //build json object from model object
    public JSONObject buildJSONObject(DlbWbAuthorizedDevice authorizedDevice) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("id", authorizedDevice.getId());
        object.put("description", authorizedDevice.getDescription());
        object.put("macAddress", authorizedDevice.getMacAddress());
        object.put("status", authorizedDevice.getDlbStatus().getStatusCode());

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbAuthorizedDevice> authorizedDevices) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < authorizedDevices.size(); i++) {
            DlbWbAuthorizedDevice device = authorizedDevices.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("id", device.getId());
            row.put("description", device.getDescription());
            row.put("macAddress", device.getMacAddress());
            row.put("status", device.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }

}
