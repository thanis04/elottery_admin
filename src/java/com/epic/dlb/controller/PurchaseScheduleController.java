/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.WalletBuilder;
import com.epic.dlb.dto.DlbSwtStWalletDto;
import com.epic.dlb.model.DlbSwtPurSchedule;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.service.PurchaseScheduleService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.service.WalletService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author kasun_n
 */
@Controller
@RequestMapping("purchase_schedule")
public class PurchaseScheduleController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseScheduleService purchaseScheduleService;
    
    @Autowired
    private WalletService walletService; 
    
    @Autowired
    private WalletBuilder walletBuilder;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/purchase_schedule/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/purchase_schedule";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    //search 
    @RequestMapping(value = "/search.htm", method = RequestMethod.POST)
    @ResponseBody
    public String search(HttpSession session, HttpServletResponse servletResponse, @RequestParam("walletId") Integer WalletId) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/purchase_schedule/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                //search using criteria   
                search = purchaseScheduleService.listAllByWalletId(WalletId);

                searchResult = buildSearch(search);

                //set to response
                response.put("search_result", searchResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } else {

                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(PurchaseScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    private JSONArray buildSearch(List<DlbSwtPurSchedule> list) {

        JSONArray array = new JSONArray();

        Iterator<DlbSwtPurSchedule> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbSwtPurSchedule next = iterator.next();
            JSONObject jSONObject = new JSONObject();
            
            jSONObject.put("id", next.getSchId());
            jSONObject.put("show_date", next.getDate());
            jSONObject.put("lottery", next.getDlbWbProduct().getDescription());
            jSONObject.put("qt", next.getTicketCount());
            jSONObject.put("payment", next.getDlbSwtMtPaymentMethod().getDescription());
            jSONObject.put("status", next.getDlbStatusByStatus().getDescription());
            jSONObject.put("created_date", next.getCreatedDate());
        }

        return array;

    }

    @RequestMapping(value = "/search_by_key.htm", method = RequestMethod.POST)
    @ResponseBody
    public String searchByMobileAndNIC(HttpSession session, 
            HttpServletResponse servletResponse, @RequestParam("key") String walletId) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            List checkAuthorization = userService.checkAuthorization(session, "/purchase_schedule/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            
            DlbSwtStWalletDto dlbSwtStWalletDto = walletService.findByNICOrMobileNo(walletId);
            response = walletBuilder.buildWalletDetails(dlbSwtStWalletDto);
            
        } catch (Exception e) {
           Logger.getLogger(PurchaseScheduleController.class.getName()).log(Level.SEVERE, null, e); 
        }
        
        return response.toJSONString();
    }

}
