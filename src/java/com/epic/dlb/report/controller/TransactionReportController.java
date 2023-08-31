/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.model.DlbSwtMtBank;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.report.builder.TransactionReportBuilder;
import com.epic.dlb.report.service.TransactionReportService;
import com.epic.dlb.service.TransactionService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
@RequestMapping("transaction_report")
public class TransactionReportController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionReportService transactionReportService;

    @Autowired
    private TransactionReportBuilder transactionReportBuilder;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    ServletContext context;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                List transTypes = transactionService.listTypes();
                List<DlbSwtMtBank> listBank = transactionService.listBank();
                model.addAttribute("payTypes", transTypes);
                model.addAttribute("listBank", listBank);
            } catch (Exception e) {
                e.printStackTrace();;
            }
            return "pages/rpt_transaction";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/bank_tranfer_show_page.htm")
    public String banckTranferPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/bank_tranfer_show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
//                List transTypes = transactionService.listTypes(); for  Future Enhancements --- kasun_n
                //  model.addAttribute("payTypes", transTypes);
                List<DlbSwtMtBank> listBank = transactionService.listBank();
//              
                model.addAttribute("listBank", listBank);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "pages/rpt_tran_account";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_bank_tranfer_report_bydate.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showBankTranferReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("mobile_no") String mobileNo,
            @RequestParam("nic") String nic,
            @RequestParam("trans_type") String transType,
            @RequestParam("bank_code") String bankCode,
            @RequestParam("from_acc_no") String fromAccNo,
            @RequestParam("to_acc_no") String toAccNo) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List transactionByDate = transactionReportService.getBankTranferTransactionByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)), nic, mobileNo, transType, fromAccNo, toAccNo, bankCode);
                reportResult = transactionReportBuilder.buildBankTrnferReportData(transactionByDate);
                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } catch (ParseException ex) {
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
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

    @RequestMapping("/bank_tranfer_report_bydate_print.htm")
    public String showBankTranferReportPrint(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("mobile_no") String mobileNo,
            @RequestParam("nic") String nic,
            @RequestParam("trans_type") String transType,
            @RequestParam("bank_code") String bankCode,
            @RequestParam("from_acc_no") String fromAccNo,
            @RequestParam("to_acc_no") String toAccNo,
            HttpServletResponse response,
            Model modal) throws FileNotFoundException, JRException, IOException {

        //declare variable common varibles
//        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {

                HashMap params = new HashMap();
                params.put("logo", "../../resources/common/images/dlb_logo.png");
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
                params.put("mobile_no", mobileNo !=null? mobileNo : "-");
                params.put("nic", nic !=null? nic : "-");
                params.put("trans_type", transType !=null? transType : "-");
                params.put("bank_code", transactionService.getBank(bankCode) !=null? transactionService.getBank(bankCode) : "-");
                params.put("from_acc_no", fromAccNo !=null? fromAccNo : "-");
                params.put("to_acc_no", toAccNo !=null? toAccNo : "-");
                
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List transactionByDate = transactionReportService.getBankTranferTransactionByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)), nic, mobileNo, transType, fromAccNo, toAccNo, bankCode);

                params.put("total", transactionByDate.size());
                
                String reportPath = "/resources/reports/bankTransfer_report.jasper";

                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(transactionByDate);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("bank_tranfer_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();
                
            } catch (ParseException | JRException | IOException ex) {
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
            modal.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
        return null;
    }

    @RequestMapping("/pay_summary_show_page.htm")
    public String showPaySummaryPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/pay_summary_show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                List transTypes = transactionService.listTypes();
                model.addAttribute("payTypes", transTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "pages/rpt_pay_summary";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/show_report_bydate.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("mobile_no") String mobileNo,
            @RequestParam("nic") String nic,
            @RequestParam("trans_type") String transType,
            @RequestParam("pay_type") String paymentType,
            @RequestParam(value = "bank_code", required = false) String bankCode) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List transactionByDate = transactionReportService.getTransactionByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)), nic, mobileNo, transType, paymentType, bankCode);
                reportResult = transactionReportBuilder.buildReportData(transactionByDate);
                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } catch (ParseException ex) {
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
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

    @RequestMapping(value = "/show_report_trans_summary.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showSummaryReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate
    ) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List transactionByDate = transactionReportService.getTransactionByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));

                reportResult = transactionReportBuilder.buildReportSummaryData(transactionByDate);
                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } catch (ParseException ex) {
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
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

    @RequestMapping(value = "/print_report_bydate.htm", method = RequestMethod.POST)
    public String printReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("mobile_no") String mobileNo,
            @RequestParam("nic") String nic,
            @RequestParam("trans_type") String transType,
            @RequestParam("pay_type") String paymentType,
            @RequestParam("bank_code") String bankCode,
            HttpServletResponse response,
            Model modal) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

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

                List transactionByDate = transactionReportService.getTransactionByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)), nic, mobileNo, transType, paymentType, bankCode);

                String reportPath = "/resources/reports/transaction_report.jasper";

                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(transactionByDate);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("transaction_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();

            } catch (ParseException | JRException | IOException ex) {
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            modal.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
        return null;
    }

    @RequestMapping(value = "/print_report_trans_summary.htm", method = RequestMethod.POST)
    public String printReportSuumaryData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            HttpServletResponse response,
            Model modal) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

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

                List transactionByDate = transactionReportService.getTransactionByDate(dateFormat2.format(dateFormat.parse(fromDate)), dateFormat2.format(dateFormat.parse(toDate)));

                String reportPath = "resources/reports/payment_summary.jasper";

                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(transactionByDate);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("transaction_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();

            } catch (ParseException | JRException | IOException ex) {
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            modal.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
        return null;
    }
    //fromDate: 2018-11-01
//toDate: 2018-11-07
//mobile_no: 
//nic: 
//pay_type: 0
//  bank_code: 0
    @RequestMapping(value = "/show_report_bydate_paginated.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showReportDataPaginated(HttpSession session,
            HttpServletResponse httpResponse,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("mobile_no") String mobileNo,
            @RequestParam("nic") String nic,
            @RequestParam("trans_type") String transType,
            @RequestParam("pay_type") String paymentType,
            @RequestParam("start") String start,
            @RequestParam("length") String length,
            @RequestParam("draw") String draw,
            @RequestParam(value = "bank_code", required = false) String bankCode) 
    {   
        
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_report/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                JSONArray reportResult = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";
                String formattedFromDate= dateFormat2.format(dateFormat.parse(fromDate));
                String formattedToDate=  dateFormat2.format(dateFormat.parse(toDate));      
                List transactionByDate = transactionReportService.getTransactionByDatePaginated(formattedFromDate, formattedToDate, nic, mobileNo, transType, paymentType, bankCode,start,length);
                Long totalRecords = transactionReportService.getTransactionByDateTotalReocrds(formattedFromDate,formattedToDate, nic, mobileNo, transType, paymentType, bankCode); 
                reportResult = transactionReportBuilder.buildReportData(transactionByDate);
                //System.out.println("------------------------------------------- reportResult " + reportResult.toString());
                response.put("data", reportResult);
                response.put("recordsTotal", totalRecords);
                response.put("recordsFiltered", totalRecords);
                response.put("draw",draw );
                
                msg = SystemVarList.SUCCESS;
                status = true;
            } 
            catch (ParseException ex)
            {
                status = false;
                Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        else 
        { 
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            msg = (String) checkAuthorization.get(0);
            status = false;
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

}
