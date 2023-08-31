/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.builder.AgentFinanceBuilder;
import com.epic.dlb.report.service.AgentService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
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
@RequestMapping("agent_finance")
public class AgentFinanceController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgentFinanceBuilder agentFinanceBuilder;

    @Autowired
    private AgentService agentService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/agent_finance/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/rpt_agent_finance";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_data.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showPaginatedData(HttpSession session,
            HttpServletRequest request) {
        List checkAuthorization = userService.checkAuthorization(session, "/agent_finance/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {
                String start = request.getParameter("start");
                String end = request.getParameter("end");
                String length = request.getParameter("length");
                String fromDate = request.getParameter("fromDate") + " 00:00:00";
                String toDate = request.getParameter("toDate") + " 23:59:59";
                ReportSearchCriteriaDto reportSearchCriteriaDto = new ReportSearchCriteriaDto();
                reportSearchCriteriaDto.setStart(start);
                reportSearchCriteriaDto.setEnd(end);
                reportSearchCriteriaDto.setLength(length);
                reportSearchCriteriaDto.setFromDate(fromDate);
                reportSearchCriteriaDto.setToDate(toDate);
                reportSearchCriteriaDto.setMode("REP");

                JSONArray data = agentFinanceBuilder.financeReportData(reportSearchCriteriaDto);
                Integer count = agentService.getAgentFinanceDataCount(reportSearchCriteriaDto);

                response.put("search_result", data);
                response.put("data", data);
                response.put("recordsTotal", count);
                response.put("recordsFiltered", count);
                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);
            } catch (Exception e) {
                response.put("msg", "Something Went Wrong");
                response.put("status", SystemVarList.ERROR);
                Logger.getLogger(PrizePaymentController.class.getName()).
                        log(Level.SEVERE, null, e);
            }
        }
        return response.toJSONString();
    }

    @RequestMapping(value = "/download_excel.htm", method = RequestMethod.GET)
    public void downloadExcel(HttpSession session, HttpServletResponse resp,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate) throws ParseException, Exception {
        fromDate = fromDate + " 00:00:00";
        toDate = toDate + " 23:59:59";
        ReportSearchCriteriaDto reportSearchCriteriaDto = new ReportSearchCriteriaDto();
        reportSearchCriteriaDto.setFromDate(fromDate);
        reportSearchCriteriaDto.setToDate(toDate);
        reportSearchCriteriaDto.setMode("EXP");

        File generateExcel = agentFinanceBuilder.getGenerateFinanceReportExcel(reportSearchCriteriaDto);
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
