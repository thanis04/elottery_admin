/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller_finance;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.builder.DailyTicketSalesReportBuilder;
import com.epic.dlb.report.controller.TicketReportController;
import com.epic.dlb.report.model.TicketViewModel;
import com.epic.dlb.report.service.SalesReportService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
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
@RequestMapping("daily_ticket_sales_report")
public class DailyTicketSalesReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private DailyTicketSalesReportBuilder dailyTicketSalesReportBuilder;

    @Autowired
    ServletContext context;

    @Autowired
    private ProductService productService;

    @Autowired
    private SalesReportService reportService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        List checkAuthorization = userService.checkAuthorization(session, "/daily_ticket_sales_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DailyTicketSalesReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/rpt_daily_ticket_sales";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_paginated_report.htm", method = RequestMethod.GET)
    @ResponseBody
    public String showPaginatedReportData(HttpSession httpSession,
            HttpServletRequest httpServletRequest) throws ParseException {

        //declare variable common varibles
        JSONObject obj = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(httpSession, "/daily_ticket_sales_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray data = null;
                String gameType = httpServletRequest.getParameter("gameType");
                String fromDate = httpServletRequest.getParameter("fromDate") + " 00:00:00";
                String toDate = httpServletRequest.getParameter("toDate") + " 23:59:59";
                String length = httpServletRequest.getParameter("length");
                String start = httpServletRequest.getParameter("start");
                String end = httpServletRequest.getParameter("end");

                ReportSearchCriteriaDto brandwiseSearchCriteriaDto = new ReportSearchCriteriaDto();
                brandwiseSearchCriteriaDto.setMode("REP");
                brandwiseSearchCriteriaDto.setLength(length);
                brandwiseSearchCriteriaDto.setStart(start);
                brandwiseSearchCriteriaDto.setEnd(end);

                data = dailyTicketSalesReportBuilder.dailyTicketSalesReportReportData(dateFormat.parse(fromDate),
                        dateFormat.parse(toDate), gameType, brandwiseSearchCriteriaDto);
                Integer count = reportService.getTicketSalesCount(dateFormat.parse(fromDate),
                        dateFormat.parse(toDate), gameType, brandwiseSearchCriteriaDto);
                Integer totalPrize = count * 20;
                obj.put("search_result", data);
                obj.put("data", data);
                obj.put("recordsTotal", count);
                obj.put("tktValue", totalPrize);
                obj.put("recordsFiltered", count);
                obj.put("msg", "Fetching Successfull");
                obj.put("status", SystemVarList.SUCCESS);
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

//    @ResponseBody
//    @RequestMapping(value = "/show_report.htm", method = RequestMethod.GET)
//    public String showReportData(HttpSession session,
//            @RequestParam("from_date") String fromDate,
//            @RequestParam("lottery") String lottery,
//            @RequestParam("to_date") String toDate) throws ParseException {
//
//        //declare variable common varibles
//        JSONObject obj = new JSONObject();
//        String msg = MessageVarList.COMMON_ERR;
//        boolean status = false;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
//        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");
//
//        //check Authorization
//        List checkAuthorization = userService.checkAuthorization(session, "/daily_ticket_sales_report/show_page.htm");
//        boolean authStatus = (boolean) checkAuthorization.get(1);
//        if (authStatus) {
//            try {
//                JSONArray reportResult = null;
//                List ticketsByDate = null;
//
//                //add starting time and end time of the day  - to get all records in day
//                fromDate = fromDate + " 00:00:00";
//                toDate = toDate + " 23:59:59";
//
//                reportResult = dailyTicketSalesReportBuilder.dailyTicketSalesReportReportData(dateFormat.parse(fromDate), dateFormat.parse(toDate), lottery);
//                obj.put("search_result", reportResult);
//                msg = SystemVarList.SUCCESS;
//                status = true;
//            } catch (Exception ex) {
//                java.util.logging.Logger.getLogger(DailyTicketSalesReportController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            //Authorization fail
//            //set response           
//            msg = SystemVarList.ERROR;
//        }
//        obj.put(SystemVarList.STATUS, status);
//        obj.put(SystemVarList.MESSAGE, msg);
//        return obj.toJSONString();
//    }
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

        ReportSearchCriteriaDto brandwiseSearchCriteriaDto = new ReportSearchCriteriaDto();
        brandwiseSearchCriteriaDto.setMode("EXP");
        File generateExcel = dailyTicketSalesReportBuilder.
                generateExcel(dateFormat.parse(fromDate), dateFormat.parse(toDate),
                        lottery, brandwiseSearchCriteriaDto);

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
