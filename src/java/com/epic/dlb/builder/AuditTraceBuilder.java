/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbAudittrace;
import java.text.SimpleDateFormat;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author salinda_r
 */
@Component("auditTraceBuilder")
public class AuditTraceBuilder {

    private static SimpleDateFormat dateFormat;

    public JSONArray buildSearchResult(List<DlbWbAudittrace> audittracesList) {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        //changing type to JSONobject
      
        
        for (int i = 0; i < audittracesList.size(); i++) {
            DlbWbAudittrace audittrace = audittracesList.get(i);

            //put dlbdevices to json objects
            JSONObject row = new JSONObject();
            row.put("activity", audittrace.getActivity());
            row.put("username", audittrace.getUsername());
            row.put("ip", audittrace.getIp());

            row.put("id", (audittrace.getId()).toString());
            row.put("lastupdateduser", audittrace.getLastupdateduser());
            row.put("createdtime", dateFormat.format(audittrace.getCreatedtime()));
            row.put("Lastupdatedtime",dateFormat.format(audittrace.getLastupdatedtime()));

            table.add(row);
        }
        return table;
    }

}
