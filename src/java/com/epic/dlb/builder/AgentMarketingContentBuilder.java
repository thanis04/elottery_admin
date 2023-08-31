/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbMarketingContent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author nipuna_k
 */
@Component("agentMarketingContentBuilder")
public class AgentMarketingContentBuilder {

    private static SimpleDateFormat dateFormat;

    public JSONArray buildSearchResult(List<DlbWbMarketingContent> dlbWbMarketingContent) {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dlbWbMarketingContent.forEach(obj -> {
            try {
                JSONObject row = new JSONObject();
                row.put("id", obj.getId());
                row.put("name", obj.getName());
                row.put("description", obj.getDescription());
                row.put("url", obj.getUrl());
                row.put("status", obj.getStatus());
                row.put("createdBy", obj.getCreatedBy());
                row.put("createdTime", dateFormat.format(obj.getCreatedTime()));
//            row.put("lastUpdatedTime", obj.getLastUpdatedTime());
                table.add(row);
            } catch (Exception ex) {
                Logger.getLogger(AgentMarketingContentBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return table;
    }

    public JSONObject buildSingleResult(DlbWbMarketingContent obj) {
        JSONObject row = new JSONObject();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {

            row.put("id", obj.getId());
            row.put("name", obj.getName());
            row.put("description", obj.getDescription());
            row.put("url", obj.getUrl());
            row.put("status", obj.getStatus());
            row.put("createdBy", obj.getCreatedBy());
            row.put("createdTime", dateFormat.format(obj.getCreatedTime()));
//            row.put("lastUpdatedTime", obj.getLastUpdatedTime());\
        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }
}
