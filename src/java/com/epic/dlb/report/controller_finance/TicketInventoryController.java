/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller_finance;

import com.epic.dlb.report.builder.TicketInventoryBuilder;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
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
import static java.util.Date.from;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletContext;
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
 * @author nipuna_k
 */
@Controller
@RequestMapping("ticket_inventory_report")
public class TicketInventoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TicketInventoryBuilder ticketInventoryBuilder;

    @Autowired
    ServletContext context;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        List checkAuthorization = userService.checkAuthorization(session, "/ticket_inventory_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(TicketInventoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/rpt_ticket_inventory";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/show_report.htm", method = RequestMethod.GET)
    public String showReportData(HttpSession session,
            @RequestParam(value = "from_date", required = false) String fromDate,
            @RequestParam(value = "to_date", required = false) String toDate,
            @RequestParam("lottery") String product) {

        //declare variable common varibles
        JSONObject obj = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_inventory_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                List ticketsByDate = null;

                //add starting time and end time of the day  - to get all records in day
                reportResult = ticketInventoryBuilder.ticketInventoryReportData(fromDate, toDate, product);
                obj.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(TicketInventoryController.class.getName()).log(Level.SEVERE, null, ex);
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

    @RequestMapping(value = "/download_excel.htm", method = RequestMethod.GET)
    public void search(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("lottery") String lottery,
            HttpServletResponse resp) throws ParseException, Exception {

        //add starting time and end time of the day  - to get all records in day
        fromDate = fromDate + " 00:00:00";
        toDate = toDate + " 23:59:59";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        File generateExcel = ticketInventoryBuilder.generateExcel(fromDate, toDate, lottery);

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
}
