/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.dto.DrawWiseSalesInsightDto;
import com.epic.dlb.dto.ReconciliationPaginatedPageData;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.builder.DrawWiseSalesInsightBuilder;
import com.epic.dlb.report.service.DrawWiseSalesInsightService;
import com.epic.dlb.service.ProductService;
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
@RequestMapping("drawWiseSalesInsight_rpt")
public class DrawWiseSalesInsightController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DrawWiseSalesInsightBuilder drawWiseSalesInsightBuilder;

    @Autowired
    private DrawWiseSalesInsightService drawWiseSalesInsightService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/drawWiseSalesInsight_rpt/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ReconciliationReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/rpt_draw_wise_sales";
        } else {
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_data.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showPaginatedData(HttpSession session,
            HttpServletRequest request) {
        List checkAuthorization = userService.checkAuthorization(session, "/drawWiseSalesInsight_rpt/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {
                String start = request.getParameter("start");
                String end = request.getParameter("end");
                String length = request.getParameter("length");
                String gameType = request.getParameter("lottery");
                String drawNo = request.getParameter("drawNo");
                String reportType = request.getParameter("reportType");

                DrawWiseSalesInsightDto dto = new DrawWiseSalesInsightDto();
                dto.setStart(start);
                dto.setEnd(end);
                dto.setMode("REP");
                dto.setLength(length);
                dto.setGameType(gameType);
                dto.setDrawNumber(drawNo);
                dto.setReportType(reportType);

                JSONArray data = drawWiseSalesInsightBuilder.getSalesInsight(dto);
                Integer count = drawWiseSalesInsightService.getInsightCount(dto);
                
                response.put("search_result", data);
                response.put("data", data);
                response.put("recordsTotal", count);
                response.put("recordsFiltered", count);
                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);

            } catch (Exception e) {
                response.put("msg", "Something Went Wrong");
                response.put("status", SystemVarList.ERROR);
                Logger.getLogger(ReconciliationReportController.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return response.toJSONString();
    }
    
    @RequestMapping(value = "/download_excel.htm", method = RequestMethod.GET)
    public void downloadExcel(HttpSession session, HttpServletResponse resp,
            @RequestParam("lottery") String lottery,
            @RequestParam("drawNo") String drawNo,
            @RequestParam("reportType") String reportType)
            throws ParseException, Exception {
        DrawWiseSalesInsightDto dto = new DrawWiseSalesInsightDto();
        dto.setGameType(lottery);
        dto.setDrawNumber(drawNo);
        dto.setReportType(reportType);
        dto.setMode("EXP");

        File generateExcel = drawWiseSalesInsightBuilder.getGenerateSalesInsightExcel(dto);

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
