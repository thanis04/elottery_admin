/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.report.builder.TicketReportBuilder;
import com.epic.dlb.report.model.PaginatedPageData;
import com.epic.dlb.report.model.TicketSearchVM;
import com.epic.dlb.report.service.SalesReportService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRDataSource;
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
@RequestMapping("ticket_search")
public class TicketSearchController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // @Autowired
    // private TicketReportService ticketReportService;
    @Autowired
    ServletContext context;

    @Autowired
    private TicketReportBuilder ticketReportBuilder;

    @Autowired
    private SalesReportService salesReportService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) 
    {
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) 
        {
            try 
            {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);
            } 
            catch (Exception ex)
            {
                java.util.logging.Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/ticket_search";
   
        }
        else 
        {
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
            @RequestParam("draw_no") String drawNo,
            @RequestParam("lottery") String lottery) 
    {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        List checkAuthorization = userService.checkAuthorization(session, "/ticket_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        
        try 
        {
            if (authStatus) 
            {
                JSONArray reportResult = null;
                List<TicketSearchVM> ticketsByDate = new ArrayList<TicketSearchVM>();

                ticketsByDate = salesReportService.getTicketByDateUsingTicket(fromDate, toDate, drawNo, lottery);

                reportResult = salesReportService.buildTicketSearchReportData(ticketsByDate);

                response.put("search_result", reportResult);
                msg = SystemVarList.SUCCESS;
                status = true;
            } 
            else 
            {         
                msg = (String) checkAuthorization.get(0);                
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
            msg = SystemVarList.ERROR;
            
        }      
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }
    
    @RequestMapping(value = "/viewsales.htm", method = RequestMethod.POST)
    public String viewSales(HttpSession session,@RequestParam("fileid") String fileid, Model model)
    {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        try 
        {
            if (authStatus) 
            {
                List<Object[]> result = salesReportService.getFileDetailsById(fileid);
                Object[] row = result.get(0);
                model.addAttribute("fileid", fileid);

                if(row[0] != null)
                {
                    model.addAttribute("product", row[0].toString());
                }
                if(row[1] != null)
                {
                    model.addAttribute("drowno", row[1].toString());
                }
                if(row[2] != null)
                {
                    model.addAttribute("drowdate", row[2].toString());
                }
                if(row[3] != null)
                {
                    model.addAttribute("uploadedby", row[3].toString());
                }
                if(row[4] != null)
                {
                    model.addAttribute("approvedby", row[4].toString());
                }
                return "pages/salesticketsdetailsbyfile";
            }
            else 
            {
                String msg = (String) checkAuthorization.get(0);
                model.addAttribute(SystemVarList.MESSAGE, msg);
                return SystemVarList.LOGIN_PAGE;
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
            return "common/error_page";
        }
        
    }
    
    @RequestMapping(value = "/viewreturns.htm", method = RequestMethod.POST)
    public String viewReturns(HttpSession session,@RequestParam("fileid") String fileid, Model model)
    {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
    
        try 
        {
            if(authStatus)
            {    
                List<Object[]> result = salesReportService.getFileDetailsById(fileid);
                Object[] row = result.get(0);
                model.addAttribute("fileid", fileid);

                if(row[0] != null)
                {
                    model.addAttribute("product", row[0].toString());
                }
                if(row[1] != null)
                {
                    model.addAttribute("drowno", row[1].toString());
                }
                if(row[2] != null)
                {
                    model.addAttribute("drowdate", row[2].toString());
                }
                if(row[3] != null)
                {
                    model.addAttribute("uploadedby", row[3].toString());
                }
                if(row[4] != null)
                {
                    model.addAttribute("approvedby", row[4].toString());
                }
                        
                return "pages/returnedticketsdetailsbyfile";
            }
            else 
            {
                String msg = (String) checkAuthorization.get(0);
                model.addAttribute(SystemVarList.MESSAGE, msg);
                return SystemVarList.LOGIN_PAGE;
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
            return "common/error_page";
        }
    }
    
    @RequestMapping(value = "/viewsalesdetails.htm", method = RequestMethod.POST)
    @ResponseBody
    public String viewSalesdetails(HttpSession session,@RequestParam("fileid") String fileid)
    {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        
        JSONObject response = new JSONObject();
        JSONArray reportResult = null; 
        boolean status = false;
        String msg = MessageVarList.COMMON_ERR;
        try 
        {       
            if(authStatus)
            {
                List<Object[]> result = salesReportService.getTicketDetailsByFileId(fileid,"19");
                //List<Object[]> result = salesReportService.getTicketDetailsByFileId("36","17");
                reportResult = salesReportService.buildTicketDetailsByFileId(result);
                response.put("search_result", reportResult);
                status = true;
                msg = SystemVarList.SUCCESS;
            }
            else
            {
                msg = (String) checkAuthorization.get(0);            
            }
        }
        catch(Exception ex)
        {
            status = false;
            Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toString();
    }
    
    @RequestMapping(value = "/viewreturneddetails.htm", method = RequestMethod.POST)
    @ResponseBody
    public String viewReturndetails(HttpSession session,@RequestParam("fileid") String fileid)
    {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        
        JSONObject response = new JSONObject();
        JSONArray reportResult = null; 
        boolean status = false;
        String msg = MessageVarList.COMMON_ERR;
        try 
        {       
            if(authStatus)
            {
                List<Object[]> result = salesReportService.getTicketDetailsByFileId(fileid,"36");
                //List<Object[]> result = salesReportService.getTicketDetailsByFileId("36","17");
                reportResult = salesReportService.buildTicketDetailsByFileId(result);
                response.put("search_result", reportResult);
                status = true;
                msg = SystemVarList.SUCCESS;
            }
            else 
            {
                msg = (String) checkAuthorization.get(0);
            }
        }
        catch(Exception ex)
        {
            status = false;
            Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toString();
    }

    @RequestMapping(value = "/search_print.htm", method = RequestMethod.POST)
    public void showReportDataPrint(HttpSession session, Model model, HttpServletResponse response,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            @RequestParam("draw_no") String drawNo,
            @RequestParam("lottery") String lottery) {

        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket_search/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {

                HashMap params = new HashMap();
                params.put("logo", "../../resources/common/images/dlb_logo.png");

                List<TicketSearchVM> ticketsByDate = new ArrayList<TicketSearchVM>();
                //add starting time and end time of the day  - to get all records in day

                ticketsByDate = salesReportService.getTicketByDateUsingTicket(fromDate, toDate, drawNo, lottery);

                String reportPath = null;

                reportPath = "/resources/reports/ticket_search.jasper";

                params.put("total", ticketsByDate.size());
                params.put("from_date", fromDate);
                params.put("to_date", toDate);
//                params.put("lottery", productService.get(lottery).getDescription());

                if(drawNo.equals(null) ||drawNo.isEmpty() ){ 
                    params.put("draw_no", "-");
                }else{
                    params.put("draw_no", drawNo);
                }
                

                if (lottery.equalsIgnoreCase("0")) {
                    params.put("lottery", " ");
                } else {

                    params.put("lottery", productService.get(lottery).getDescription());
                }

                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JRBeanCollectionDataSource(ticketsByDate);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("tickets_search_report.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();

//                response.put("search_result", reportResult);
//                msg = SystemVarList.SUCCESS;
//                status = true;
            } catch (Exception ex) {
                Logger.getLogger(TicketSearchController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            //set response           
            msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
           

        }
       

    }
    
    @RequestMapping(value = "/viewSalesdetailspaginated.htm", method = RequestMethod.POST)
    @ResponseBody
    public String viewSalesdetailsPaginated(HttpServletRequest request,HttpSession session)
    {
        try
        {
            String start = request.getParameter("start");
            String length = request.getParameter("length");
            String draw = request.getParameter("draw");
            String fileid = request.getParameter("fileid");

            JSONArray buildSearchList = new JSONArray();
            JSONObject response = new JSONObject();
            JSONArray reportResult = null; 

            PaginatedPageData pd = salesReportService.getTicketDetailsByFileIdPagination(start,length,fileid,"19");
            reportResult = salesReportService.buildTicketDetailsByFileId(pd.getPaginatedData());

            response.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
            response.put("data", reportResult);
            response.put("recordsTotal", pd.getTotal_records());
            response.put("recordsFiltered", pd.getTotal_records());
            response.put("draw",draw );
            
            
            //System.out.println("-------------------------------------------------start " + start);
            //System.out.println("-------------------------------------------------length " + length);
            //System.out.println("-------------------------------------------------pd.getTotal_records() " + pd.getTotal_records());
            //System.out.println("-------------------------------------------------reportResult.size() " + reportResult.size());

            return response.toString();
        }
        catch(Exception e)
        {
        
        }
        return "error";
    }
    
    @RequestMapping(value = "/viewReturndetailspaginated.htm", method = RequestMethod.POST)
    @ResponseBody
    public String viewReturndetailsPaginated(HttpServletRequest request,HttpSession session)
    {
        try
        {
            String start = request.getParameter("start");
            String length = request.getParameter("length");
            String draw = request.getParameter("draw");
            String fileid = request.getParameter("fileid");

            JSONArray buildSearchList = new JSONArray();
            JSONObject response = new JSONObject();
            JSONArray reportResult = null; 

            PaginatedPageData pd = salesReportService.getTicketDetailsByFileIdPagination(start,length,fileid,"36");
            reportResult = salesReportService.buildTicketDetailsByFileId(pd.getPaginatedData());

            response.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
            response.put("data", reportResult);
            response.put("recordsTotal", pd.getTotal_records());
            response.put("recordsFiltered", pd.getTotal_records());
            response.put("draw",draw );
            
            
            //System.out.println("-------------------------------------------------start " + start);
            //System.out.println("-------------------------------------------------length " + length);
            //System.out.println("-------------------------------------------------pd.getTotal_records() " + pd.getTotal_records());
            //System.out.println("-------------------------------------------------reportResult.size() " + reportResult.size());

            return response.toString();
        }
        catch(Exception e)
        {
        
        }
        return "error";
    }
    
}
