/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.report.builder.PurchaseReportBuilder;
import com.epic.dlb.report.builder.TransactionReportBuilder;
import com.epic.dlb.report.service.PurchaseReportService;
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
import java.util.logging.Logger;
import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author kasun_n
 */ 
@Controller
@RequestMapping("sales_summery_report")
public class SalesSummeryReportController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseReportService purchaseReportService;

    @Autowired
    private PurchaseReportBuilder purchaseReportBuilder;

    @Autowired
    ServletContext context;

    @Autowired
    private ProductService productService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/sales_summery_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);
            } catch (Exception ex) {
                Logger.getLogger(SalesSummeryReportController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "pages/rpt_sales_purchase";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_report_bydate.htm",method = RequestMethod.POST) 
    @ResponseBody
    public String showReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate
           ) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/purchase_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                List transactionByDate = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

               transactionByDate = purchaseReportService.getTransactionSummeryByDate(fromDate, toDate);
                reportResult = purchaseReportBuilder.buildSummeryReportData(transactionByDate);
                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } catch (ParseException ex) {
                Logger.getLogger(SalesSummeryReportController.class.getName()).log(Level.SEVERE, null, ex);
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

  
    @RequestMapping(value = "/print_summery_report_bydate.htm", method = RequestMethod.POST)
    public String printSummery(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            HttpServletResponse response,           
            Model modal) {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //add params
                HashMap params = new HashMap();
                params.put("logo", "../../resources/common/images/dlb_logo.png");
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
              
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List summery = purchaseReportService.getTransactionSummeryByDate(fromDate, toDate);
                String reportPath = "resources/reports/purchase_report_summery_chart.jasper";

                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(summery);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("purchase_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();

            } catch (JRException | IOException ex) {
                Logger.getLogger(SalesSummeryReportController.class.getName()).log(Level.SEVERE, null, ex);
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
