/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DLBWbPrizePaymentHistory;
import com.epic.dlb.report.controller_finance.DailyTicketSalesReportController;
import com.epic.dlb.service.PrizePaymentHistoryService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("prize_payment_history")
public class PrizePaymentHistoryController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private PrizePaymentHistoryService prizePaymentHistoryService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/prize_payment_history/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List list = prizePaymentHistoryService.listNew();
                List build = build(list);
                model.addAttribute("list", build);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/prize_payment_history";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    public String showReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate) throws ParseException {

        //declare variable common varibles
        JSONObject obj = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/prize_payment_history/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                List ticketsByDate = null;

                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List list = prizePaymentHistoryService.lisByDate(fromDate, toDate);
                reportResult = build(list);
                obj.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DailyTicketSalesReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            //set response           
            msg = SystemVarList.ERROR;
        }
        obj.put(SystemVarList.STATUS, status);
        obj.put(SystemVarList.MESSAGE, msg);
        return obj.toJSONString();
    }

    @RequestMapping(value = "/download_prize_file.htm", method = RequestMethod.GET)
    public void downloadPrizefile(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("id") Integer id) {
        //If user is not authorized - he should be thrown out from here itself

        DLBWbPrizePaymentHistory history = prizePaymentHistoryService.findById(id);

        String filePath = history.getFile();

        //Authorized user will download the file
        Path file = Paths.get(filePath);
        if (Files.exists(file)) {
            response.setContentType("fileName");
            int index = filePath.lastIndexOf(File.separator);
            String fileName = filePath.substring(index, filePath.length()).replaceFirst("DLB", "DLB_");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName.replaceAll("", ""));
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
                history.setIsNew(false);
                prizePaymentHistoryService.update(history);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                Logger.getLogger(PrizePaymentHistoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public JSONArray build(List<DLBWbPrizePaymentHistory> historys)
            throws Exception {
        JSONArray table = new JSONArray();

        Iterator<DLBWbPrizePaymentHistory> iterator = historys.iterator();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while (iterator.hasNext()) {
            DLBWbPrizePaymentHistory next = iterator.next();

            JSONObject row = new JSONObject();

            row.put("id", next.getId());
            row.put("createdDate", dateFormat.format(next.getCreatedDate()));
            row.put("user", next.getUser());
            row.put("count", next.getCount());
            row.put("file", next.getFile());
            row.put("isNew", dateFormat.format(next.getCreatedDate()));

            table.add(row);

        }

        return table;
    }

}
