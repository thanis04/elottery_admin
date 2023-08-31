/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.report.builder.TicketReportBuilder;
import com.epic.dlb.report.model.TicketViewModel;
import com.epic.dlb.report.service.TicketReportService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nipuna_k
 */
@Controller
@RequestMapping("ticket_report")
public class TicketReportController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private TicketReportService ticketReportService;

    @Autowired
    private TicketReportBuilder ticketReportBuilder;

    @Autowired
    ServletContext context;

    @Autowired
    private ProductService productService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(TicketReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/rpt_ticket";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_report_bydateAndCategory.htm", method = RequestMethod.GET)
    @ResponseBody
    public String showReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("category") String category) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                JSONArray reportResult = null;
                List ticketsByDate = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                if (category.equalsIgnoreCase("1")) {
                    ticketsByDate = ticketReportService.getTicketByDateUsingTicket(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
                    reportResult = ticketReportBuilder.buildReportData(ticketsByDate);

                } else if (category.equalsIgnoreCase("2")) {
                    ticketsByDate = ticketReportService.getTicketByDateUsingPurchaseHistory(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
                    reportResult = ticketReportBuilder.buildPurchaseHistoryReportData(ticketsByDate);

                } else if (category.equalsIgnoreCase("3")) {
                    ticketsByDate = ticketReportService.getTicketByDateUsingReturnTicket(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
                    reportResult = ticketReportBuilder.buildReportData(ticketsByDate);

                } else if (category.equalsIgnoreCase(null)) {
//                    ticketsByDate = ticketReportService.getTicketByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
//                    reportResult = ticketReportBuilder.buildReportData(ticketsByDate);
                    System.out.println(category);
                }

                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } catch (ParseException ex) {
                java.util.logging.Logger.getLogger(TicketReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            //set response           
            msg = SystemVarList.ERROR;

        }

        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "/print_report_bydateAndCategory.htm", method = RequestMethod.POST)
    public String printReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("category") String category,
            HttpServletResponse response,
            Model modal) throws ParseException {

        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(category);

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //add params
                HashMap params = new HashMap();
                params.put("logo", "../../resources/common/images/dlb_logo.png");
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
                params.put("category", category);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
                DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List<TicketViewModel> ticketsByDate = null;
                List summery = null;
                String reportPath = null;

                if (category.equalsIgnoreCase("1")) {
                    ticketsByDate = ticketReportService.getTicketByDateUsingTicket(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
                    reportPath = "/resources/reports/ticket_purchase_report.jasper";
                } else if (category.equalsIgnoreCase("2")) {
                    ticketsByDate = ticketReportService.getTicketByDateUsingPurchaseHistory(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
                    reportPath = "/resources/reports/ticket_sales_report.jasper";

                } else if (category.equalsIgnoreCase("3")) {
                    ticketsByDate = ticketReportService.getTicketByDateUsingReturnTicket(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
                    reportPath = "/resources/reports/ticket_return_report.jasper";
                } else if (category.equalsIgnoreCase(null)) {
//                    ticketsByDate = ticketReportService.getTicketByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
//                    summery = ticketReportService.getTicketByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));
//                    params.put("summery", summery);
                    System.out.println(category);
                }
                System.out.println(ticketsByDate.size());
                params.put("total", ticketsByDate.size());

//                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));
                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(ticketsByDate);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("ticket_purchase_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();

            } catch (JRException | IOException ex) {
                java.util.logging.Logger.getLogger(TicketReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            modal.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
        return null;
    }

    
}
