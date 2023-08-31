/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbPage;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbUserPriviledge;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

/**
 *
 * @author kasun_n
 */
@Component("userPrivilegeBuilder")
public class UserPrivilegeBuilder {
    
    //build user privilages list from form data
    public List build(DlbWbUserPriviledge priviledge,String pageList,DlbWbSystemUser systemUser) throws ParseException{
        List<DlbWbUserPriviledge> userPriviledges=new ArrayList<>();
        JSONParser jSONParser=new JSONParser();
        Date date = new Date();
        //convert String page list to user Priviledges
        JSONArray pageListJSON = (JSONArray) jSONParser.parse(pageList);
        
        for (int i = 0; i < pageListJSON.size(); i++) {
            String pageID = (String) pageListJSON.get(i);
            DlbWbUserPriviledge setUserPriviledge=new DlbWbUserPriviledge();
            
            setUserPriviledge.setDlbWbUserRole(priviledge.getDlbWbUserRole());
            setUserPriviledge.setDlbWbPage(new DlbWbPage(pageID));
            setUserPriviledge.setDlbWbSection(priviledge.getDlbWbSection());
            setUserPriviledge.setDlbWbSubSection(priviledge.getDlbWbSubSection());
            setUserPriviledge.setLastupdateduser(systemUser.getUsername());
            setUserPriviledge.setCreatedtime(date);
            setUserPriviledge.setLastupdatedtime(date);
            
            //add to list
            userPriviledges.add(setUserPriviledge);
            
            
        }
        
        return userPriviledges;
    }
    
}
