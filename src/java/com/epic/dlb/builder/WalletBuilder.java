/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.dto.DlbSwtStWalletDto;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbWalletDeletionRequest;
import com.epic.dlb.service.WalletService;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nipuna_k
 */
@Component("walletBuilder")
public class WalletBuilder {

    @Autowired
    private WalletService walletService;
    
    public JSONObject buildWalletDetails(DlbSwtStWalletDto dlbSwtStWalletDto) {
//        JSONArray array = new JSONArray();
        JSONObject jSONObject = new JSONObject();

        jSONObject.put("id", dlbSwtStWalletDto.getId());
        jSONObject.put("name", dlbSwtStWalletDto.getFirstName() + " " + dlbSwtStWalletDto.getLastName());
        jSONObject.put("mobileNo", dlbSwtStWalletDto.getMobileNo());
        jSONObject.put("nic", dlbSwtStWalletDto.getNic());

        return jSONObject;
    }

    public JSONArray buildCustomerDetails(List<Object[]> listObject) {
        JSONArray jSONArray = new JSONArray();
        listObject.forEach(object -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", object[0] == null ? "-" : object[0].toString());
            Integer id = Integer.parseInt(object[0].toString());
            jSONObject.put("first_name", object[1] == null ? "-" : object[1].toString());
            jSONObject.put("last_name", object[2] == null ? "-" : object[2].toString());
            jSONObject.put("mobile_no", object[3] == null ? "-" : object[3].toString());
            jSONObject.put("nic", object[4] == null ? "-" : object[4].toString());
            jSONObject.put("last_login", object[5] == null ? "-" : object[5].toString().replace(".0", ""));
            jSONObject.put("username", object[6] == null ? "-" : object[6].toString());
            jSONObject.put("isRequested", walletService.checkIsRequested(id, "REQUESTED"));
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }
    
    public JSONArray buildCustomerDetailsForDeleton(List<Object[]> listObject) {
        JSONArray jSONArray = new JSONArray();
        listObject.forEach(object -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", object[0] == null ? "-" : object[0].toString());
            Integer id = Integer.parseInt(object[0].toString());
            jSONObject.put("first_name", object[1] == null ? "-" : object[1].toString());
            jSONObject.put("last_name", object[2] == null ? "-" : object[2].toString());
            jSONObject.put("mobile_no", object[3] == null ? "-" : object[3].toString());
            jSONObject.put("nic", object[4] == null ? "-" : object[4].toString());
            jSONObject.put("last_login", object[5] == null ? "-" : object[5].toString().replace(".0", ""));
            jSONObject.put("username", object[6] == null ? "-" : object[6].toString());
            jSONObject.put("isRequested", walletService.checkIsRequestedForDeletion(id, "REQUESTED"));
            jSONObject.put("create_time", object[7] == null ? "-" : object[7].toString().replace(".0", ""));
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }
    
    public JSONObject buildWalletDetailsForDeletion(DlbSwtStWalletDto dlbSwtStWalletDto, 
            DlbWbWalletDeletionRequest dlbWbWalletDeletionRequest) {
//        JSONArray array = new JSONArray();
        JSONObject jSONObject = new JSONObject();

        jSONObject.put("id", dlbSwtStWalletDto.getId());
        jSONObject.put("name", dlbSwtStWalletDto.getFirstName() + " " + dlbSwtStWalletDto.getLastName());
        jSONObject.put("mobileNo", dlbSwtStWalletDto.getMobileNo());
        jSONObject.put("nic", dlbSwtStWalletDto.getNic());
        jSONObject.put("remark", dlbWbWalletDeletionRequest.getRemark());
        return jSONObject;
    }
}
