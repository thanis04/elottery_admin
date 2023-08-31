/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.TransactionBuilder;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.report.builder.TicketReportBuilder;
import com.epic.dlb.report.model.PageData;
import com.epic.dlb.report.service.WinningFileReportService;
import com.epic.dlb.service.DeviceService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.TransactionService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.service.WiningFileService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author nipuna_k
 */
@Controller
@RequestMapping("ticket_winning_file")
public class TicketWinningFileController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TicketReportBuilder ticketReportBuilder;

    @Autowired
    ServletContext context;

    @Autowired
    private WinningFileReportService winningFileReportService;

    @Autowired
    private WiningFileService winingFileService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionBuilder transactionBuilder;

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/ticket_winning_file";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String showReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam(value = "draw_date", required = false) String drawDate,
            @RequestParam(value = "draw_no", required = false) String drawNo,
            @RequestParam(value = "lottery", required = false) String lottery) {


        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                JSONArray reportResult = null;
                List ticketsByDate = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List<WhereStatement> whereStatements = new ArrayList<>();
                whereStatements.add(new WhereStatement("createdTime", dateFormat.parse(fromDate), SystemVarList.GREATER_THAN));
                whereStatements.add(new WhereStatement("createdTime", dateFormat.parse(toDate), SystemVarList.LESS_THAN));

                if (drawDate != null && !drawDate.isEmpty()) {
                    whereStatements.add(new WhereStatement("date", toDate, SystemVarList.EQUAL));
                }

                if (drawNo != null && !drawNo.isEmpty()) {
                    whereStatements.add(new WhereStatement("drawNo", toDate, SystemVarList.EQUAL));
                }

                if (lottery != null && !"0".equals(lottery)) {
                    whereStatements.add(new WhereStatement("dlbWbProduct.productCode", lottery, SystemVarList.EQUAL));
                }

                ticketsByDate = winningFileReportService.searchTiketFile(whereStatements, start, end);
                reportResult = ticketReportBuilder.buildWinningTicketReportData(ticketsByDate);

                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } catch (ParseException ex) {
                java.util.logging.Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
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

    @RequestMapping(value = "/view_payment.htm", method = RequestMethod.GET)
    @ResponseBody
    public String showRecprd(HttpSession session,
            @RequestParam("id") int id) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            Double winingAmount = winningFileReportService.getEpicWinningAmount(id);
            response.put("record", winingAmount);
            msg = SystemVarList.SUCCESS;
            status = true;
        } else {
            //Authorization fail
            //set response           
            msg = SystemVarList.ERROR;

        }

        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "/accept_payment.htm", method = RequestMethod.GET)
    @ResponseBody
    public String acceptPayment(HttpSession session,
            @RequestParam("id") int id,
            @RequestParam("ref_no") String refNo) {

        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            winningFileReportService.acceptPayment(id, refNo);
            msg = "Payment accept succesfully!";
            status = true;
        } else {
            //Authorization fail
            //set response           
            msg = SystemVarList.ERROR;

        }

        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "/print_ticket_winning_report.htm", method = RequestMethod.GET)
    public String printTikcetWinningReportData(HttpSession session,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("draw_date") String drawDate,
            @RequestParam("draw_no") String drawNo,
            @RequestParam("lottery") String lottery,
            HttpServletResponse response,
            Model modal) throws ParseException, JRException {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //add params
                HashMap params = new HashMap();
                params.put("logo", "../../resources/common/images/dlb_logo.png");
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
                params.put("lottery", lottery);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
                DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

                List ticketsWinningReport = null;
                //add starting time and end time of the day  - to get all records in day
                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";

                List summery = null;
                String reportPath = null;

                List<WhereStatement> whereStatements = new ArrayList<>();
                whereStatements.add(new WhereStatement("createdTime", dateFormat.parse(fromDate), SystemVarList.GREATER_THAN));
                whereStatements.add(new WhereStatement("createdTime", dateFormat.parse(toDate), SystemVarList.LESS_THAN));

                if (drawDate != null && !drawDate.isEmpty()) {
                    whereStatements.add(new WhereStatement("date", toDate, SystemVarList.EQUAL));
                }

                if (drawNo != null && !drawNo.isEmpty()) {
                    whereStatements.add(new WhereStatement("drawNo", toDate, SystemVarList.EQUAL));
                }

                if (lottery != null && !"0".equals(lottery)) {
                    whereStatements.add(new WhereStatement("dlbWbProduct.productCode", lottery, SystemVarList.EQUAL));
                }

                ticketsWinningReport = winningFileReportService.searchTiketFile(whereStatements, start, end);
                reportPath = "/resources/reports/ticket_winning_report.jasper";

                params.put("total", ticketsWinningReport.size());

//                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));
                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(ticketsWinningReport);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("ticket_winning_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();

            } catch (JRException | IOException ex) {
                java.util.logging.Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            modal.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
        return null;
    }

    @RequestMapping(value = "/generate_prize.htm", method = RequestMethod.GET)
    @ResponseBody
    public String generatePrize(HttpSession session) throws FileNotFoundException {

        String filePath = "";
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            DlbWbSystemUser dlbWbSystemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            try {
                List list = winningFileReportService.generatePrize();
                response.put("filePath", list.get(0));
                response.put("filename", list.get(1));

            } catch (UnsupportedEncodingException | SQLException | ParseException e) {
            }

            status = true;
        } else {
            //Authorization fail
            //set response           
            msg = SystemVarList.ERROR;

        }

        response.put(SystemVarList.STATUS, status); 

        return response.toJSONString();
    }

    @RequestMapping(value = "/download_prize_file.htm", method = RequestMethod.POST)
    public void downloadPrizefile(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("filePath") String filePath) {
        //If user is not authorized - he should be thrown out from here itself

        //Authorized user will download the file
        Path file = Paths.get(filePath);
        if (Files.exists(file)) {
            response.setContentType("fileName");
            int index = filePath.lastIndexOf(File.separator);
            String fileName = filePath.substring(index, filePath.length()).replaceFirst("DLB", "DLB_");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/list_ticket.htm", method = RequestMethod.GET)
    public String listTicketByID(HttpSession session, Model model,
            @RequestParam("id") int id) {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            List listTicket = winningFileReportService.listWalletByWiningFile(id);
            DlbWbWiningFile dlbWbWiningFile = winingFileService.get(id);
            model.addAttribute("record", listTicket);
            model.addAttribute("winingFile", dlbWbWiningFile);

        } else {
            //Authorization fail
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }

        return "pages/winning_ticket_list";
    }

    @RequestMapping(value = "/list_ticket_print.htm", method = RequestMethod.GET)
    public String listTicketByIDPrint(HttpSession session, Model model, HttpServletResponse response,
            @RequestParam("id") int id) throws FileNotFoundException, JRException, IOException {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {

            HashMap params = new HashMap();
            params.put("logo", "../../resources/common/images/dlb_logo.png");

            List listTicket = winningFileReportService.listWalletByWiningFile(id);
            DlbWbWiningFile dlbWbWiningFile = winingFileService.get(id);
            String reportPath = null;

            reportPath = "/resources/reports/winning_tickets_report.jasper";

            System.out.println(listTicket.size());
            params.put("total", listTicket.size());
            params.put("fileNo", dlbWbWiningFile.getId());
            params.put("lotteryType", dlbWbWiningFile.getProductDescription());
            params.put("drawDate", dlbWbWiningFile.getDate());
            params.put("drawNo", dlbWbWiningFile.getDrawNo());

//                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));
            InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

            JRDataSource dataSource = new JRBeanCollectionDataSource(listTicket);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

            JRPdfExporter exp = new JRPdfExporter();

            exp.setExporterInput(new SimpleExporterInput(jasperPrint));
            exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

            configuration.setMetadataTitle("winning_tickets_report.pdf");
            exp.setConfiguration(configuration);

            exp.exportReport();

        } else {
            //Authorization fail
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }

        return "pages/winning_ticket_list";
    }

//    @RequestMapping(value = "/list_transaction.htm", method = RequestMethod.GET) 
//    @ResponseBody
//    public String listTicketByTransactionByID(HttpSession session,  
//            @RequestParam("id") String txNo) {
//        //declare variable common varibles
//        JSONObject response = new JSONObject();
//        String msg = MessageVarList.COMMON_ERR;
//        boolean status = false;
//        
//        //check Authorization
//        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm"); 
//        boolean authStatus = (boolean) checkAuthorization.get(1);
//
//        if (authStatus) {
//            try {
//                List listTicket = winningFileReportService.listTicketByTransaction(txNo);
//                DlbSwtStTransaction transaction = transactionService.get(txNo);
//                status=true;
//                msg=SystemVarList.SUCCESS; 
//                response.put(SystemVarList.RECORD, status);
//                response.put("record", transactionBuilder.buildJSONObject(transaction));
//                response.put("record_lst",ticketReportBuilder.buildTicketList(listTicket) );
//               
//              
//            } catch (Exception e) {
//                Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, e);
//            }
//
//        } else {
//            //Authorization fail
//            //Authorization fail
//            msg = (String) checkAuthorization.get(0);
//           
//
//        }
//
//        response.put(SystemVarList.STATUS, status);
//        response.put(SystemVarList.MESSAGE, msg);
//
//        return response.toJSONString();
//    }
    @RequestMapping(value = "/list_transaction.htm", method = RequestMethod.GET)
    public String listTicketByTransactionByID(HttpSession session, @RequestParam("id") String txNo, Model model) {
        //declare variable common varibles       

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        String msg = SystemVarList.ERROR;

        if (authStatus) {
            try {
                List listTicket = winningFileReportService.listTicketByTransaction(txNo);
                DlbSwtStTransaction transaction = transactionService.get(txNo);

                model.addAttribute("object", transaction);
                model.addAttribute("record_lst", listTicket);
                model.addAttribute(SystemVarList.RECORD, "transaction");
            } catch (Exception e) {
                Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, e);
            }

            return "pages/winning_ticket_trans";

        } else {
            //Authorization fail        
            msg = (String) checkAuthorization.get(0);

        }
        model.addAttribute(SystemVarList.MESSAGE, msg);
        return SystemVarList.LOGIN_PAGE;

    }

    @RequestMapping(value = "/list_transaction_by_nic.htm", method = RequestMethod.GET)
    public String listTicketByNIC(HttpSession session,
            @RequestParam("nic") String nic, Model model) {
        //declare variable common varibles       

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        String msg = SystemVarList.ERROR;

        if (authStatus) {
            try {
                WhereStatement whereStatement = new WhereStatement("nic", nic, SystemVarList.EQUAL);
//                List listTicket = winningFileReportService.listTicketByNIC(nic);
                DlbSwtStWallet wallet = deviceService.get(whereStatement);
                model.addAttribute("nic", nic);
                model.addAttribute("object", wallet);
//              model.addAttribute("record_lst", listTicket);
                model.addAttribute(SystemVarList.RECORD, "device");

            } catch (Exception e) {
                Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, e);
            }

            return "pages/winning_ticket_trans";

        } else {
            //Authorization fail        
            msg = (String) checkAuthorization.get(0);

        }
        model.addAttribute(SystemVarList.MESSAGE, msg);
        return SystemVarList.LOGIN_PAGE;

    }

    @RequestMapping(value = "/list_transaction_print.htm", method = RequestMethod.GET)
    public String listTicketByTransactionByIDPrint(HttpSession session, HttpServletResponse response,
            @RequestParam("id") String txNo, Model model) {
        //declare variable common varibles       

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        String msg = SystemVarList.ERROR;

        if (authStatus) {
            try {
                HashMap params = new HashMap();
                params.put("logo", "../../resources/common/images/dlb_logo.png");

                List listTicket = winningFileReportService.listTicketByTransaction(txNo);
                DlbSwtStTransaction transaction = transactionService.get(txNo);

//                ------check the is data available--------
                if (listTicket.size() != 0) {
                    System.out.println("working");

                    String reportPath = null;

                    reportPath = "/resources/reports/ticket_for_transaction.jasper";

                    params.put("transaction_id", transaction.getTxnid());
                    params.put("purchase_date", transaction.getDateTime());
                    params.put("name", transaction.getDlbSwtStWallet().getFirstName() + " " + transaction.getDlbSwtStWallet().getLastName());
                    params.put("nic", transaction.getDlbSwtStWallet().getNic());
                    params.put("total", listTicket.size());

                    InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                    JRDataSource dataSource = new JRBeanCollectionDataSource(listTicket);

                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                    JRPdfExporter exp = new JRPdfExporter();

                    exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                    configuration.setMetadataTitle("ticket_for_transaction.pdf");
                    exp.setConfiguration(configuration);

                    exp.exportReport();

                } else {
                    System.out.println("Nothing");
                }

            } catch (Exception e) {
                Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, e);
            }

        } else {
            //Authorization fail
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
        return "pages/winning_ticket_list";
    }

    @RequestMapping(value = "/searchpaginated.htm", method = RequestMethod.POST)
    @ResponseBody
    public String test(HttpSession session, HttpServletRequest request, HttpServletResponse httpResponse) {
        JSONObject jsonResponse = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                String fromDate = request.getParameter("from_date");
                String toDate = request.getParameter("to_date");
                String start1 = request.getParameter("start1");
                String end1 = request.getParameter("end1");

                String start = request.getParameter("start");
                String length = request.getParameter("length");
                String draw = request.getParameter("draw");

                JSONArray buildSearchList = new JSONArray();
                JSONArray reportResult = new JSONArray();

                Long recordsTotal = null;

                List ticketsByDate = null;

                fromDate = fromDate + " 00:00:00";
                toDate = toDate + " 23:59:59";
                Date fDate = new SimpleDateFormat("yyyy-MM-dd h:mm:ss").parse(fromDate.trim());
                Date tDate = new SimpleDateFormat("yyyy-MM-dd h:mm:ss").parse(toDate.trim());

                List<WhereStatement> whereStatements = new ArrayList<>();
                whereStatements.add(new WhereStatement("createdTime", fDate, SystemVarList.GREATER_THAN, SystemVarList.AND));
                whereStatements.add(new WhereStatement("createdTime", tDate, SystemVarList.LESS_THAN, SystemVarList.AND));

//                if (drawDate != null && !drawDate.isEmpty()) {
//                    whereStatements.add(new WhereStatement("date", drawDate, SystemVarList.EQUAL));
//                }
//                if (drawNo != null && !drawNo.isEmpty()) {
//                    whereStatements.add(new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL));
//                }
//                if (lottery != null && !"0".equals(lottery)) {
//                    whereStatements.add(new WhereStatement("dlbWbProduct.productCode", lottery, SystemVarList.EQUAL));
//                }
                int i_start = Integer.parseInt(start1);
                int i_end = Integer.parseInt(end1);
                PageData pd = winningFileReportService.searchTiketFilePaginated(whereStatements, i_start, i_end);
                reportResult = ticketReportBuilder.buildWinningTicketReportData(pd.getDataList());
                recordsTotal = winningFileReportService.searchTiketFilePaginatedNumberOfRecords(whereStatements);

                jsonResponse.put("search_result", reportResult);
                jsonResponse.put("data", reportResult);
                jsonResponse.put("recordsTotal", recordsTotal);
                jsonResponse.put("recordsFiltered", recordsTotal);
                jsonResponse.put("draw", draw);

                msg = SystemVarList.SUCCESS;
                status = true;
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                msg = (String) checkAuthorization.get(0);
                status = false;
            }
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
            msg = SystemVarList.ERROR;
            status = false;
        }
        jsonResponse.put(SystemVarList.STATUS, status);
        jsonResponse.put(SystemVarList.MESSAGE, msg);
        return jsonResponse.toString();
    }

    @RequestMapping(value = "/list_transaction_by_nic_paginated.htm", method = RequestMethod.GET)
    @ResponseBody
    public String listTicketByNICPaginated(HttpSession session, HttpServletResponse httpResponse,
            @RequestParam("nic") String nic, @RequestParam("start") String start, @RequestParam("length") String length, @RequestParam("draw") String draw, Model model) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            List checkAuthorization = userService.checkAuthorization(session, "/ticket_report/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray reportResult = null;
                List listTicket = winningFileReportService.listTicketByNICPaginated(nic, start, length);
                Long listTicketsize = winningFileReportService.totalRecordsTicketByNIC(nic);
                reportResult = winningFileReportService.buildJsonForTransaction_by_nic(listTicket);
                response.put("data", reportResult);
                response.put("recordsTotal", listTicketsize);
                response.put("recordsFiltered", listTicketsize);
                response.put("draw", draw);
                msg = SystemVarList.SUCCESS;
                status = true;
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                msg = (String) checkAuthorization.get(0);
                status = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, ex);
            msg = SystemVarList.ERROR;
            status = false;
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();

    }

    @RequestMapping(value = "/findByDateWinningFile.htm", method = RequestMethod.GET)
    @ResponseBody
    public String winningFileDetails(HttpSession session, HttpServletRequest request, HttpServletResponse httpResponse) {
        JSONObject jsonResponse = new JSONObject();
        JSONArray reportResult = new JSONArray();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        try {
            List checkAuthorization = userService.checkAuthorization(session, "/ticket_winning_file/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                String drawDate = request.getParameter("draw_date");
                drawDate = drawDate + " 00:00:00";

                Date date = new SimpleDateFormat("yyyyMMdd h:mm:ss").parse(drawDate.trim());
                PageData pd = winningFileReportService.getTiketDetails(date);
                reportResult = ticketReportBuilder.buildWinningTicketReportDataDetails(pd.getDataList());
                jsonResponse.put("data", reportResult);
                jsonResponse.put("draw_date", new SimpleDateFormat("yyyy-MM-dd").format(date));
                status = true;
                msg = SystemVarList.SUCCESS;
            }

        } catch (Exception e) {
            Logger.getLogger(TicketWinningFileController.class.getName()).log(Level.SEVERE, null, e);
            msg = SystemVarList.ERROR;
            status = false;
        }
        jsonResponse.put(SystemVarList.STATUS, status);
        jsonResponse.put(SystemVarList.MESSAGE, msg);
        return jsonResponse.toString();
    }
}
