/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.model.DlbWbWorkflow;
import com.epic.dlb.util.common.SystemVarList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kasun
 */
@Component("winingFileBuilder")
public class WiningFileBuilder {

    private static SimpleDateFormat dateFormat;
 
   

    //build json object from model object
    public DlbWbWiningFile buildWiningFile(String formData, HttpSession session, String productDescription) throws ParseException, java.text.ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        JSONParser jSONParser = new JSONParser(); 
        Date date = new Date();

        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        DlbWbEmployee employee = (DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE);

        DlbWbWiningFile dlbWbWiningFile = new DlbWbWiningFile();

        JSONObject jSONObject = (JSONObject) jSONParser.parse(formData);

        DlbWbProduct product = new DlbWbProduct();
        product.setProductCode((String) jSONObject.get("productCode"));
       
        dlbWbWiningFile.setDlbWbProduct(product);
        dlbWbWiningFile.setProductDescription(productDescription);
        dlbWbWiningFile.setDrawNo((String) jSONObject.get("drawNo"));
        String drawDateStr=(String) jSONObject.get("drawDate");
//        String origCheckSum = (String) jSONObject.get("origCheckSum");
        dlbWbWiningFile.setDate(dateFormat.parse(drawDateStr));
        dlbWbWiningFile.setDlbWbEmployeeByUploadBy(employee);
        dlbWbWiningFile.setDlbWbEmployeeByApprovedBy(employee);
        dlbWbWiningFile.setDlbWbWorkflow(new DlbWbWorkflow(SystemVarList.WINING_FILE_APPROVAL, null));
        dlbWbWiningFile.setDlbStatus(new DlbStatus(SystemVarList.SUBMITED, null, null));     
        dlbWbWiningFile.setLastUpdatedUser(systemUser.getUsername());
        dlbWbWiningFile.setLastUpdatedTime(date);
        dlbWbWiningFile.setCreatedTime(date);
        DlbWbTicket dlbWbTicket = new DlbWbTicket();
        dlbWbTicket.setId(Integer.parseInt((String) jSONObject.get("ticketID")));
        dlbWbWiningFile.setDlbWbTicket(dlbWbTicket);

        return dlbWbWiningFile;
    }

    public JSONArray buildWiningFileList(List<DlbWbWiningFile> list) {
        JSONArray array = new JSONArray();

        Iterator<DlbWbWiningFile> iterator = list.iterator();

        while (iterator.hasNext()) {//loop wining file list

            //pu into json object
            DlbWbWiningFile next = iterator.next();
            JSONObject jSONObject = new JSONObject();

            jSONObject.put("product", next.getProductDescription());
            jSONObject.put("date", next.getDate());
            jSONObject.put("draw_no", next.getDrawNo());
            jSONObject.put("status", next.getDlbStatus().getDescription());

            //add to josn array
            array.add(array);

        }
        return array;
    }

    public JSONObject convertToJSON(DlbWbWiningFile wbWiningFile,Double winingAmount, String jackpot) {
        JSONObject object = new JSONObject();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        object.put("id", wbWiningFile.getId());
        object.put("lottery", wbWiningFile.getProductDescription());
        object.put("draw_date", dateFormat.format(wbWiningFile.getDate())); 
        object.put("draw_no", wbWiningFile.getDrawNo());
        object.put("filename_check", wbWiningFile.getFilenameCheck());
        object.put("mac_check", wbWiningFile.getMacCheck());
        object.put("hash_check", wbWiningFile.getHashCheck());
        object.put("records", wbWiningFile.getRecordCount());
        object.put("status", wbWiningFile.getDlbStatus().getDescription());
        object.put("winning_amount",winingAmount +" +" +jackpot);

        return object;
    }

}
