/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbGameProfile;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbTicketFile;
import com.epic.dlb.service.ProductProfileService;
import com.epic.dlb.service.RiskProfileService;
import com.epic.dlb.service.TicketService;
import com.epic.dlb.util.common.SystemVarList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Component("ticketBuilder")

public class TicketBuilder {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RiskProfileService riskProfileService;   

    private static SimpleDateFormat dateFormat;
    
    @Autowired
    private ProductProfileService productProfileService;

    //build json object from model object
    @Transactional
    public DlbWbTicketFile buildWiningFile(String formData, HttpSession session, String productDescription) throws ParseException, java.text.ParseException, org.json.simple.parser.ParseException, Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        JSONParser jSONParser = new JSONParser();
        Date date = new Date();

        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        DlbWbEmployee employee = (DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE);

        DlbWbTicketFile ticketFile = new DlbWbTicketFile();

        JSONObject jSONObject = (JSONObject) jSONParser.parse(formData);

        DlbWbProduct product = new DlbWbProduct();
        product.setProductCode((String) jSONObject.get("productCode"));
        ticketFile.setDlbWbProduct(product);
        ticketFile.setProductDescription(productDescription);
        ticketFile.setDrawNo((String) jSONObject.get("drawNo"));
        String drawDateStr = (String) jSONObject.get("drawDate");
        String profileID = (String) jSONObject.get("dlbWbProductProfile.id");
        String jackpot = (String) jSONObject.get("jackpot");
        String origCheckSum = (String) jSONObject.get("origCheckSum");
        ticketFile.setDate(dateFormat.parse(drawDateStr));
        ticketFile.setDlbWbEmployeeByUploadBy(employee);
        ticketFile.setDlbStatus(new DlbStatus(SystemVarList.SUBMITED, null, null));
        ticketFile.setLastUpdatedUser(systemUser.getUsername());
        ticketFile.setLastUpdatedTime(date);
        ticketFile.setCreatedTime(date);
        ticketFile.setJackpot(jackpot);
        ticketFile.setOriginHash(origCheckSum);
        DlbWbProductProfile productProfile = new DlbWbProductProfile();
        productProfile.setId(Integer.parseInt(profileID));
        
        
        DlbWbProductProfile get = productProfileService.get(productProfile.getId());
        Hibernate.initialize(get.getDlbWbGameProfile());
        
        if(get.getDlbStatusBySpecialStatus().getStatusCode()==Integer.parseInt(SystemVarList.YES)){
              ticketFile.setDlbWbGameProfile(get.getDlbWbGameProfile());
              ticketFile.setDlbStatusByIsSpecial(new DlbStatus(Integer.parseInt(SystemVarList.YES), null, null));
        }
        
         else{
            ticketFile.setDlbStatusByIsSpecial(new DlbStatus(Integer.parseInt(SystemVarList.NO), null, null));  
        }
        
     
      
        ticketFile.setDlbWbProductProfile(get);
        return ticketFile;
    }

    public JSONObject convertToJSON(DlbWbTicketFile ticket) {
        JSONObject object = new JSONObject();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        object.put("id", ticket.getId());
        object.put("lottery", ticket.getProductDescription());
        object.put("draw_date", dateFormat.format(ticket.getDate()));
        object.put("draw_no", ticket.getDrawNo());
        object.put("no_of_tk", ticket.getNoOfTicket());
        object.put("filename_check", ticket.getFilenameCheck());
        object.put("mac_check", ticket.getMacCheck());
        object.put("hash_check", ticket.getHashCheck());
        object.put("status", ticket.getDlbStatus().getDescription());

        return object;
    }

}
