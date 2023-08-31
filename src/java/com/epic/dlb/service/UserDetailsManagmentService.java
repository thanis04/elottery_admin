/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("userDetailsManagmentService")
public class UserDetailsManagmentService {

    @Autowired
    private GenericRepository genericRepository;

    private SimpleDateFormat dateFormat;

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getUserDetails(String mobileNo, String nicNo) {

        List<WhereStatement> searchCriterias = new ArrayList<>();
        if (!nicNo.isEmpty()) {
            if (!mobileNo.isEmpty()) {
                WhereStatement searchCriteria = new WhereStatement("nic", nicNo, SystemVarList.EQUAL, SystemVarList.AND);
                searchCriterias.add(searchCriteria);
            } else {
                WhereStatement searchCriteria = new WhereStatement("nic", nicNo, SystemVarList.EQUAL);
                searchCriterias.add(searchCriteria);
            }
        }
        if (!mobileNo.isEmpty()) {
            WhereStatement searchCriteria = new WhereStatement("mobileNo", mobileNo, SystemVarList.EQUAL);
            searchCriterias.add(searchCriteria);
        }
        List<DlbSwtStWallet> userDetails = genericRepository.listWithQuery(DlbSwtStWallet.class, searchCriterias);
        JSONArray jSONArray = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        userDetails.forEach(user -> {
            JSONObject row = new JSONObject();
            row.put("id", user.getId());
            row.put("model", user.getModel());
            row.put("brand", user.getBrand());
            row.put("fanme", user.getFirstName());
            row.put("lanme", user.getLastName());
            row.put("nic", user.getNic());
            row.put("mobile", user.getMobileNo());
            row.put("login_date", user.getLastLoginTime() == null ? "-"
                    : dateFormat.format(user.getLastLoginTime()));
            row.put("pin_request", user.getRequest());
            row.put("user_name", user.getUsername());
            row.put("email", user.getEmail());
            jSONArray.add(row);
        });

        return jSONArray;
    }

}
