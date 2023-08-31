/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbSection;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbUserPriviledge;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.service.UserPrivilegeService;
import com.epic.dlb.service.UserRoleService;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kasun
 */
@Component("systemUserBuilder")
public class SystemUserBuilder {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    //build json object from model object
    public JSONObject buildJSONObject(DlbWbSystemUser systemUser) {

        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("username", systemUser.getUsername());
        object.put("employee", systemUser.getDlbWbEmployee().getEmployeeid());
        object.put("userRole", systemUser.getDlbWbUserRole().getUserrolecode());
        object.put("status", systemUser.getDlbStatus().getStatusCode());

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbWbSystemUser> systemUsers) {
        JSONArray table = new JSONArray();

        //loop lists
        for (int i = 0; i < systemUsers.size(); i++) {
            DlbWbSystemUser systemUser = systemUsers.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();
            row.put("username", systemUser.getUsername());
            row.put("employee", systemUser.getDlbWbEmployee().getName());
            row.put("userRole", systemUser.getDlbWbUserRole().getDescription());
            row.put("status", systemUser.getDlbStatus().getDescription());

            table.add(row);

        }

        return table;

    }

    public JSONArray buildPrivilages(String userRoleCode) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<DlbWbSection> dlbWbSectionList = userPrivilegeService.
                getUserPrivilagedSections(userRoleService.get(userRoleCode));

        for (DlbWbSection dlbWbSection : dlbWbSectionList) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("section", dlbWbSection.getDescription());
            JSONArray jSONArray1 = new JSONArray();
            List<DlbWbUserPriviledge> dlbWbUserPriviledge = userPrivilegeService.
                    getUserPrivilagesBySectionsAndUserRole(dlbWbSection.getSectioncode(), userRoleCode);
            for (DlbWbUserPriviledge priviledge : dlbWbUserPriviledge) {
                JSONObject jSONObject1 = new JSONObject();
                jSONObject1.put("page_name", priviledge.getDlbWbPage().getDescription());
                jSONArray1.add(jSONObject1);
            }
            jSONObject.put("page", jSONArray1);
            jSONArray.add(jSONObject);
        };
        return jSONArray;
    }

}
