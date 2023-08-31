/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.dto.BrandwisePaginatedPageData;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.builder.BrandWiseReportBuilder;
import com.epic.dlb.report.controller_agent.UserReportAgentController;
import com.epic.dlb.report.controller_finance.DailyTicketSalesReportController;
import com.epic.dlb.report.service.BrandWiseSearchReportService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
@RequestMapping("brand_w_ticket_exp")
public class BrandWiseTicketExpiredReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandWiseReportBuilder brandWiseReportBuilder;

    @Autowired
    private BrandWiseSearchReportService brandWiseSearchReportService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/brand_w_ticket_exp/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DailyTicketSalesReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/rpt_brand_wise_texpired";
        } else {
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/show_report_data.htm")
    @ResponseBody
    public String showReportData(HttpSession session,
            ReportSearchCriteriaDto brandwiseSearchCriteriaDto) {
        List checkAuthorization = userService.checkAuthorization(session, "/user_report_agent/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {
                brandwiseSearchCriteriaDto.setMode("REP");
                response.put("result", brandWiseReportBuilder.ticketExpiredReportData(
                        brandWiseSearchReportService.getTicketExpired(
                                brandwiseSearchCriteriaDto)));
                Integer allCount = brandWiseSearchReportService.
                        getAllRecordCount(brandwiseSearchCriteriaDto);
                response.put("recordsTotal", allCount);
                response.put("recordsFiltered", allCount);

                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);
            } catch (Exception ex) {
                response.put("msg", "Something Went Wrong");
                response.put("status", SystemVarList.ERROR);
                Logger.getLogger(UserReportAgentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return SystemVarList.LOGIN_PAGE;
        }
        return response.toJSONString();
    }

    @RequestMapping(value = "/show_paginated_data.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showPaginatedData(HttpSession session,
            HttpServletRequest request) {
        List checkAuthorization = userService.checkAuthorization(session, "/user_report_agent/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {
                String gameType = request.getParameter("gameType");
                String drawNumber = request.getParameter("drawNumber");
                String start = request.getParameter("start");
                String end = request.getParameter("end");
                String length = request.getParameter("length");

                ReportSearchCriteriaDto brandwiseSearchCriteriaDto = new ReportSearchCriteriaDto();
                brandwiseSearchCriteriaDto.setGameType(gameType);
                brandwiseSearchCriteriaDto.setDrawNumber(drawNumber);
                brandwiseSearchCriteriaDto.setStart(start);
                brandwiseSearchCriteriaDto.setEnd(end);
                brandwiseSearchCriteriaDto.setMode("REP");
                brandwiseSearchCriteriaDto.setLength(length);

                if (!(request.getParameter("fromDate").equals("")
                        || request.getParameter("toDate").equals(""))) {
                    List<String> list = brandWiseSearchReportService.
                            getFormattedExpiredDate(request.getParameter("fromDate"),
                                    request.getParameter("toDate"));
                    brandwiseSearchCriteriaDto.setFromDate(list.get(0));
                    brandwiseSearchCriteriaDto.setToDate(list.get(1));
                }

                BrandwisePaginatedPageData paginatedPageData
                        = brandWiseSearchReportService.getTicketExpiredPaginated(
                                brandwiseSearchCriteriaDto);
                JSONArray data = brandWiseReportBuilder.ticketExpiredReportData(
                        paginatedPageData.getPaginatedData());

                response.put("search_result", data);
                response.put("data", data);
                response.put("recordsTotal", paginatedPageData.getTotalRecords());
                response.put("recordsFiltered", paginatedPageData.getTotalRecords());
                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);
            } catch (Exception ex) {
                response.put("msg", "Something Went Wrong");
                response.put("status", SystemVarList.ERROR);
                Logger.getLogger(UserReportAgentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return SystemVarList.LOGIN_PAGE;
        }
        return response.toJSONString();
    }

    @RequestMapping(value = "/download_excel.htm", method = RequestMethod.GET)
    public void downloadExcel(HttpSession session, HttpServletResponse resp,
            @RequestParam("gameType") String gameType,
            @RequestParam("drawNumber") String drawNumber,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate)
            throws ParseException, Exception {

        ReportSearchCriteriaDto brandwiseSearchCriteriaDto = new ReportSearchCriteriaDto();
        brandwiseSearchCriteriaDto.setGameType(gameType);
        brandwiseSearchCriteriaDto.setDrawNumber(drawNumber);
        brandwiseSearchCriteriaDto.setMode("EXP");

        if (!(fromDate.equals("") || toDate.equals(""))) {
            List<String> list = brandWiseSearchReportService.
                    getFormattedExpiredDate(fromDate, toDate);
            brandwiseSearchCriteriaDto.setFromDate(list.get(0));
            brandwiseSearchCriteriaDto.setToDate(list.get(1));
        }

        File generateExcel = brandWiseReportBuilder.ticketExpiredGenerateExcel(brandWiseSearchReportService.getTicketExpired(brandwiseSearchCriteriaDto),
                brandwiseSearchCriteriaDto);

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
