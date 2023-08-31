/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.OperationRequestDto;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.service.TicketRedeemService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author kasun_n
 */
@Controller
@RequestMapping("operation")
public class OperationReportController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private TicketRedeemService redeemService;

    @RequestMapping("/processing_redeem.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/operation/processing_redeem.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {

            return "pages/rpt_processing_redeem";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/list_processing_redeem.htm")
    @ResponseBody
    public String loadRedeem(HttpSession session,
            @RequestParam(value = "from_date", required = false) String fromDate,
            @RequestParam(value = "to_date", required = false) String toDate) throws ParseException {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/operation/processing_redeem.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        if (authStatus) {

            JSONArray searchResult = null;

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                searchResult = redeemService.getReportByStatus(SystemVarList.USER_CLAIME_PENDING);
            } else {
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";
                searchResult = redeemService
                        .getReportByStatus(SystemVarList.USER_CLAIME_PENDING, fromDate, toDate);
            }

            //set to response
            response.put("search_result", searchResult);
            msg = SystemVarList.SUCCESS;
            status = true;

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
            return SystemVarList.LOGIN_PAGE;
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "/download_excel.htm", method = RequestMethod.GET)
    public void search(HttpSession session,
            @RequestParam(value = "from_date", required = false) String fromDate,
            @RequestParam(value = "to_date", required = false) String toDate,
            HttpServletResponse resp) throws ParseException, Exception {

        File generateExcel = null;

        //add starting time and end time of the day  - to get all records in day
        if (fromDate.isEmpty() || toDate.isEmpty()) {
            generateExcel = redeemService.generateExcel(SystemVarList.USER_CLAIME_PENDING);
        } else {
            //add starting time and end time of the day  - to get all records in day
            fromDate = fromDate + " 00:00:00";
            toDate = toDate + " 23:59:59";
            generateExcel = redeemService.generateExcel(SystemVarList.USER_CLAIME_PENDING, fromDate, toDate);
        }

        String mimeType = URLConnection.guessContentTypeFromName(generateExcel.getName());
        if (mimeType == null) {
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/vnd.ms-excel";
        }

        resp.setContentType(mimeType);

        resp.setHeader("Content-Disposition", String.format("attachment; filename=\"" + generateExcel.getName() + "\""));

        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        resp.setContentLength((int) generateExcel.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(generateExcel));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, resp.getOutputStream());

    }

    @RequestMapping(value = "save_request.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session, OperationRequestDto operationRequestDto) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/operation/processing_redeem.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                redeemService.saveRequest(operationRequestDto, session);

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }
    
    @RequestMapping(value = "approve_reject_request.htm", method = RequestMethod.POST)
    @ResponseBody
    public String approveRejectRecord(HttpSession session,@RequestParam("id") Integer id,
            @RequestParam("status") Boolean isApproved) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/operation/processing_redeem.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                //check is exsiting
                redeemService.approveOrRejectRequest(id, isApproved, session);

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

}
