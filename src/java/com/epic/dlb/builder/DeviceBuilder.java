/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbSwtStWalletHistory;
import com.epic.dlb.util.common.SystemVarList;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author methsiri_h
 */
@Component("deviceBuilder")
public class DeviceBuilder {

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat2;

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbSwtStWallet> deviceList) {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        //changing type to JSONobject
        for (int i = 0; i < deviceList.size(); i++) {
            DlbSwtStWallet dlbStDevice = deviceList.get(i);

            //put dlbdevices to json objects
            JSONObject row = new JSONObject();
            row.put("id", dlbStDevice.getId());
            row.put("model", dlbStDevice.getModel());
            row.put("brand", dlbStDevice.getBrand());
            row.put("fanme", dlbStDevice.getFirstName());
            row.put("lanme", dlbStDevice.getLastName());
            row.put("nic", dlbStDevice.getNic());
            row.put("mobile", dlbStDevice.getMobileNo());
            row.put("login_date", dlbStDevice.getLastLoginTime() == null ? "-" : dateFormat.format(dlbStDevice.getLastLoginTime()));
            row.put("pin_request", dlbStDevice.getRequest());

            table.add(row);
        }
        return table;
    }

    public JSONArray buildHistorylist(String type, List list) throws ParseException {
        JSONArray array = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#,##0.00");

        if (type.trim().equals(SystemVarList.ACTIVITY_HISTORY)) {

            Iterator<DlbSwtStWalletHistory> iterator = list.iterator();
            while (iterator.hasNext()) {
                DlbSwtStWalletHistory next = iterator.next();

                JSONObject jSONObject = new JSONObject();
                jSONObject.put("description", next.getDescription());
                jSONObject.put("date", dateFormat.format(next.getCreatedTime()));

                array.add(jSONObject);

            }

        }
        if (type.trim().equals(SystemVarList.PURCHASE_HISTORY)) {
            Iterator<DlbSwtStPurchaseHistory> iterator = list.iterator();
            while (iterator.hasNext()) {
                DlbSwtStPurchaseHistory next = iterator.next();

                JSONObject jSONObject = new JSONObject();
                jSONObject.put("lottery", next.getProductDescription());
                jSONObject.put("date", dateFormat.format(next.getCreatedDate()));
                jSONObject.put("draw_no", next.getDrawNo());
                jSONObject.put("draw_date", dateFormat2.format(next.getDrawDate()));
                jSONObject.put("serial_no", next.getSerialNo());

                if (next.getPaymentMethod() == null) {
                    jSONObject.put("payment_method", "");
                } else {
                    jSONObject.put("payment_method", next.getPaymentMethod());
                }

                if (next.getReferenceNo() == null) {
                    jSONObject.put("reference_no", "");
                } else {
                    jSONObject.put("reference_no", next.getReferenceNo());
                }

//                jSONObject.put("reference_no", next.getReferenceNo());
                array.add(jSONObject);

            }

        }
        if (type.trim().equals(SystemVarList.TRANSACTION_HISTORY)) {

            Iterator<DlbSwtStTransaction> iterator = list.iterator();
            while (iterator.hasNext()) {
                DlbSwtStTransaction next = iterator.next();

                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", next.getDlbSwtMtTxnType().getDescription());
                jSONObject.put("date", dateFormat.format(next.getUpdatedtime()));
                jSONObject.put("status", next.getDlbStatus().getDescription());
                jSONObject.put("amount", next.getAmount() == null ? "-" : df.format(Double.parseDouble(next.getAmount())));

                array.add(jSONObject);

            }

        }

        return array;

    }
}
