/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.service.UserClaimPendingService;
import java.text.SimpleDateFormat;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Component("userClaimPendingBuilder")
public class UserClaimPendingBuilder {

    private SimpleDateFormat dateFormat;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private UserClaimPendingService userClaimPendingService;

    @Transactional(rollbackFor = Exception.class)
    public JSONArray buildResultSet(List<DlbSwtStPurchaseHistory> dlbSwtStPurchaseHistorys) throws Exception {
        JSONArray jSONArray = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");

        for(DlbSwtStPurchaseHistory object : dlbSwtStPurchaseHistorys){
            DlbWbProduct dlbWbProduct = (DlbWbProduct) genericRepository.get(
                    object.getProductCode(), DlbWbProduct.class);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("serial_number", object.getSerialNo());
//            jSONObject.put("txn_id", object.getTxnId());
            jSONObject.put("txn_id", userClaimPendingService.getTxnIdByPurchaseId(object.getId()));
            jSONObject.put("purchase_id", object.getId());
            jSONObject.put("draw_no", object.getDrawNo());
            jSONObject.put("draw_date", new SimpleDateFormat("yyyy-MM-dd").format(object.getDrawDate()));
            jSONObject.put("lottery_name", dlbWbProduct.getDescription());
            jSONObject.put("nic", object.getDlbSwtStWallet().getNic());
            jSONObject.put("pur_date", new SimpleDateFormat("yyyy-MM-dd h:mm:ss a").format(object.getCreatedDate()));
            jSONObject.put("winning_amount", object.getWinningPrize());
            jSONObject.put("status", object.getDlbStatusByStatus().getDescription());
            jSONObject.put("isRequested", userClaimPendingService.isRequested(object.getId()));
            jSONArray.add(jSONObject);
        }
        System.out.println(jSONArray.toJSONString());
        return jSONArray;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public JSONArray buildResultSetForTransactionId(List<DlbSwtStPurchaseHistory> dlbSwtStPurchaseHistorys, String txnId) throws Exception {
        JSONArray jSONArray = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");

        for(DlbSwtStPurchaseHistory object : dlbSwtStPurchaseHistorys){
            DlbWbProduct dlbWbProduct = (DlbWbProduct) genericRepository.get(
                    object.getProductCode(), DlbWbProduct.class);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("serial_number", object.getSerialNo());
//            jSONObject.put("txn_id", object.getTxnId());
            jSONObject.put("txn_id", userClaimPendingService.getTxnIdByPurchaseIdAndTxnId(object.getId(), txnId));
            jSONObject.put("purchase_id", object.getId());
            jSONObject.put("draw_no", object.getDrawNo());
            jSONObject.put("draw_date", new SimpleDateFormat("yyyy-MM-dd").format(object.getDrawDate()));
            jSONObject.put("lottery_name", dlbWbProduct.getDescription());
            jSONObject.put("nic", object.getDlbSwtStWallet().getNic());
            jSONObject.put("pur_date", new SimpleDateFormat("yyyy-MM-dd h:mm:ss a").format(object.getCreatedDate()));
            jSONObject.put("winning_amount", object.getWinningPrize());
            jSONObject.put("status", object.getDlbStatusByStatus().getDescription());
            jSONObject.put("isRequested", userClaimPendingService.isRequested(object.getId()));
            jSONArray.add(jSONObject);
        }
        System.out.println(jSONArray.toJSONString());
        return jSONArray;
    }
}
