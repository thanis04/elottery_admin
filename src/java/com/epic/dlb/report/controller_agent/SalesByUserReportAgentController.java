/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller_agent;

import com.epic.dlb.dto.AgentSearchCriteriaDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.report.builder.SalesReportAgentBuilder;
import com.epic.dlb.report.service.AgentService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
@RequestMapping("salesbyuser_report_agent")
public class SalesByUserReportAgentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private SalesReportAgentBuilder salesReportAgentBuilder;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/salesbyuser_report_agent/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);

        if (authStatus) {
            systemUser = userService.get(systemUser.getUsername());
            model.addAttribute("role", systemUser.getDlbWbUserRole().getUserrolecode());
            if (systemUser.getDlbWbUserRole().getUserrolecode().equals("Sub Agents")) {
                model.addAttribute("subAgentCode", systemUser.getDlbWbEmployee().getEmployeeid());
            }
            return "pages/rpt_agt_salesbyuser";
        } else {
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/show_report_data.htm")
    @ResponseBody
    public String showReportData(HttpSession session, AgentSearchCriteriaDto agentSearchCriteriaDto) {
        List checkAuthorization = userService.checkAuthorization(session, "/user_report_agent/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        if (agentService.isValid(agentSearchCriteriaDto.getAgentCode(),
                systemUser.getDlbWbUserRole().getUserrolecode())) {
            if (authStatus) {
                try {

                    if (systemUser.getDlbWbUserRole().getUserrolecode().equals("Sub Agents")) {
                        agentSearchCriteriaDto.setAgentCode(systemUser.getDlbWbEmployee().getEmployeeid());
                    }

                    String fromDate = agentSearchCriteriaDto.getFromDate() + " 00:00:00";
                    String toDate = agentSearchCriteriaDto.getToDate() + " 23:59:59";
                    agentSearchCriteriaDto.setFromDate(fromDate);
                    agentSearchCriteriaDto.setToDate(toDate);

                    response.put("result", salesReportAgentBuilder.salesReportDataByUser(
                            agentService.getSalesByUser(agentSearchCriteriaDto)));
                    response.put("total", agentService.getSalesByUserTotal(agentSearchCriteriaDto));
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
        } else {
            response.put("msg", "Invalid Agent Code");
            response.put("status", SystemVarList.ERROR);
        }

        return response.toJSONString();
    }

    @RequestMapping(value = "/download_excel.htm", method = RequestMethod.GET)
    public void downloadExcel(HttpSession session, HttpServletResponse resp,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("agent_code") String agentCode)
            throws ParseException, Exception {

        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        if (agentService.isValid(agentCode, systemUser.getDlbWbUserRole().
                getUserrolecode())) {
            AgentSearchCriteriaDto agentSearchCriteriaDto = new AgentSearchCriteriaDto();
            if (systemUser.getDlbWbUserRole().getUserrolecode().equals("Sub Agents")) {
                agentSearchCriteriaDto.setAgentCode(systemUser.getDlbWbEmployee().getEmployeeid());
            }
            fromDate = fromDate + " 00:00:00";
            toDate = toDate + " 23:59:59";
            agentSearchCriteriaDto.setFromDate(fromDate);
            agentSearchCriteriaDto.setToDate(toDate);
            agentSearchCriteriaDto.setAgentCode(agentCode);

            File generateExcel = salesReportAgentBuilder.generateExcelByUser(
                    agentService.getSalesByUser(agentSearchCriteriaDto),
                    agentSearchCriteriaDto);

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
}
