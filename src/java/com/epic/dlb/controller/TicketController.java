/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.TicketBuilder;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbTicketFile;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.AuthDeviceService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.EncryptionService;
import com.epic.dlb.service.ProductProfileService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.TicketService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SecurityService;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author kasun_n
 */
@Controller
@RequestMapping("ticket")
public class TicketController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TicketBuilder ticketBuilder;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuthDeviceService authDeviceService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private AuditTraceService auditTraceService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ProductProfileService productProfileService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                List employeeList = employeeService.listAll();
                model.addAttribute("product_select_box", productSelectBox);
                model.addAttribute("employee_list", employeeList);

                String tmpWiningFilePath = Configuration.getConfiguration("TMP_WINING_FILES_PATH");
                model.addAttribute("tmp_wining_filepath", tmpWiningFilePath);

            } catch (Exception ex) {
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/ticket";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "upload_result_file.htm", method = RequestMethod.POST)
    @ResponseBody
    public String uploadResultFile(HttpSession session,
            @RequestParam("result_file") MultipartFile ticketFile,
            @RequestParam("form_data") String formData,
            @RequestParam("product_description") String productDescription,
            @RequestParam("hash") String beforeHashcode,
            @RequestParam("mac") String deviceMacAddress,
            @RequestParam("hash_code_sign_key") String hashCodeSignKey) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        boolean fileNameValidation = false;
        boolean macAddressValidation = false;
        boolean hashCodeValidation = false;

        MultipartFile checkMultipartFile = ticketFile;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
//                //check is exsiting
                DlbWbTicketFile buildWiningFile = ticketBuilder.buildWiningFile(formData, session, productDescription);

                String weekDay = productProfileService
                        .get(buildWiningFile.getDlbWbProductProfile().getId()).getDlbWbWeekDay().getDescription();

                if (ticketService.validateDrawDateAndDay(buildWiningFile.getDate(), weekDay)) {

                    List validationResult = ticketService.validateTicketFile(checkMultipartFile, buildWiningFile);

                    if (validationResult.isEmpty()) {

                        //[1] validate File Format
                        fileNameValidation = ticketService.checkFileName(ticketFile.getOriginalFilename(), buildWiningFile);

                        //[2] validate mac address
                        macAddressValidation = authDeviceService.checkIsAuthDevice(deviceMacAddress);

                        //[3] validate hashcode
                        BufferedInputStream bis = new BufferedInputStream(ticketFile.getInputStream());
                        String afterHashCode = encryptionService.genarateHashCode(bis);
//                    hashCodeValidation = beforeHashcode.equals(afterHashCode);
                        hashCodeValidation = true;

                        //[4] token validation
                        byte[] localPublicKeyArry = Base64.decodeBase64(hashCodeSignKey);
//                        boolean verifyTextStatus = securityService.verifyText(session, beforeHashcode.getBytes(), localPublicKeyArry);

                        //set validation status
                        buildWiningFile.setFilenameCheck(fileNameValidation);
                        buildWiningFile.setMacCheck(macAddressValidation);
                        buildWiningFile.setHashCheck(hashCodeValidation);
                        buildWiningFile.setMacAddress(deviceMacAddress);
                        buildWiningFile.setTokenCheck(true);
                        buildWiningFile.setHash(afterHashCode);
                        Integer save = ticketService.saveTicketFile(buildWiningFile, ticketFile, session);

                        //audit trace log save
                        String activity
                                = AuditTraceVarList.TICKET_FILE + AuditTraceVarList.UPLOADED + AuditTraceVarList.REF_NO + save;
                        auditTraceService.save(activity, session);
                        if (save != 0) {
                            status = SystemVarList.SUCCESS;
                            msg = "Ticket file uploaded " + SystemVarList.SUCCESS;
                            DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("Product", buildWiningFile.getProductDescription());
                            jSONObject.put("Draw No", buildWiningFile.getDrawNo());
                            jSONObject.put("Draw Date", buildWiningFile.getDate());
                            activityLogService.save(activityLogService.buildActivityLog(
                                    "SAVE", jSONObject, "TFU", user));
                        } else {
                            msg = "Ticket file has already been uploaded for the selected lottery and the draw No";
                        }

                    } else {
                        status = SystemVarList.WARNING;
                        String details = "";
                        Iterator<String> iterator = validationResult.iterator();
                        while (iterator.hasNext()) {
                            String error = iterator.next();

                            details = details + "<li>" + error + "</li>";

                        }
                        msg = "Invalid ticket file!. see the the error report";
                        response.put(SystemVarList.RECORD, details);
                    }

                } else {
                    status = SystemVarList.ERROR;
                    msg = "Draw date and day don't match";
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping("/approve_pending_list.htm")
    public String showApprovePage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket/approve_pending_list.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = ticketService.listPendingList(SystemVarList.APPROVED);
                model.addAttribute("pending_list", productSelectBox);

            } catch (Exception ex) {
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/ticket_approval";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/getPendingList.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getPendingList(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/ticket/approve_pending_list.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {

//                List<DlbWbTicketFile> productSelectBox = ticketService.listPendingList(SystemVarList.APPROVED);
//                ObjectMapper mapper = new ObjectMapper();
//                String json = mapper.writeValueAsString(productSelectBox);
                String status = SystemVarList.SUCCESS;
                response.put("dataList", ticketService.jsonPendingList(SystemVarList.APPROVED));
                response.put("status", status);
            } catch (Exception ex) {
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return response.toJSONString();
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/ticket_file_list.htm")
    public String showATicket(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/ticket/ticket_file_list.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = ticketService.listApproved(SystemVarList.SUBMITED);
                model.addAttribute("pending_list", productSelectBox);

            } catch (Exception ex) {
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/ticket_file_list";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }
//

    @RequestMapping(value = "approve_wining_file.htm", method = RequestMethod.POST)
    @ResponseBody
    public String approveWiningFile(HttpSession session,
            HttpServletRequest request, @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = "";
        //set task status
        String taskStatus = (String) session.getAttribute(SystemVarList.TASK_STATUS);
        String status = taskStatus == null ? "" + SystemVarList.INIT : "" + taskStatus;
        int taskProgress = 0;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/approve_pending_list.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check task status
                if (status.equals(SystemVarList.INIT)) {
                    //1st time
                    //get wining file by id
                    DlbWbTicketFile winingFile = ticketService.get(id);
                    ticketService.saveTicketData(session, winingFile);
                    status = SystemVarList.SUCCESS;
                    msg = "Ticket file approved successfully";

                    winingFile = ticketService.get(winingFile.getId());

                    //audit trace log save
                    String activity
                            = AuditTraceVarList.TICKET_FILE + AuditTraceVarList.APPROVED + AuditTraceVarList.REF_NO + winingFile.getId()
                            + ", Lottery: " + winingFile.getDlbWbProduct().getDescription() + ", Draw No: " + winingFile.getDrawNo();
                    auditTraceService.save(activity, session);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Draw No", winingFile.getDrawNo());
                    jSONObject.put("Product", winingFile.getProductDescription());
                    jSONObject.put("Draw Date", winingFile.getDate());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "APPROVE", jSONObject, "TFA", user));

                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response     
        response.put(SystemVarList.MESSAGE, msg);
        response.put("id", id);
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.TASK_PROGRESS, taskProgress);

        return response.toJSONString();
    }

//
    @RequestMapping(value = "get.htm", method = RequestMethod.POST)
    @ResponseBody
    public String get(HttpSession session,
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = SystemVarList.ERROR;
        JSONObject winingFileJSON = null;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/approve_pending_list.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbTicketFile winingFile = ticketService.get(id);
                winingFileJSON = ticketBuilder.convertToJSON(winingFile);
                status = SystemVarList.SUCCESS;
            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response     
        response.put("id", id);
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, winingFileJSON);

        return response.toJSONString();
    }
//

    @RequestMapping(value = "show_wining_file_approve_progress.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showProcessProgress(HttpSession session, @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();

        JSONParser jSONParser = new JSONParser();
        String msg = "";
        String taskStatus = "";
        JSONObject taskInfo = null;
        String status = SystemVarList.ERROR;
        long taskProgress = 0;

        //update status
        DlbWbTicketFile winingFile = new DlbWbTicketFile();
        winingFile.setId(id);
        try {

            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");
            boolean authStatus = (boolean) checkAuthorization.get(1);

            if (authStatus) {

                //set task status
                if (session.getAttribute(SystemVarList.TASK_INFO + "_" + id) != null) {
                    taskInfo = (JSONObject) jSONParser.parse((String) session.getAttribute(SystemVarList.TASK_INFO + "_" + id));
                    taskStatus = (String) taskInfo.get(SystemVarList.TASK_STATUS);

                    if (taskStatus.trim().equals(SystemVarList.PENDING)) {
                        //get task progress
                        Long taskPrg = (Long) taskInfo.get(SystemVarList.TASK_PROGRESS);
                        taskProgress = taskPrg.intValue();

                    }

                    if (taskStatus.trim().equals(SystemVarList.COMPLTED)) {
                        //get task progress
                        if (taskInfo.get(SystemVarList.TASK_PROGRESS) != null) {
                            taskProgress = (long) taskInfo.get(SystemVarList.TASK_PROGRESS);
                        }

                        if (taskInfo.get(SystemVarList.STATUS).equals(SystemVarList.SUCCESS)) {
                            msg = "Ticket file approved " + SystemVarList.SUCCESS;
                            status = (String) taskInfo.get(SystemVarList.STATUS);
                            session.setAttribute(SystemVarList.TASK_INFO, null);

                            //update status
                            ticketService.updateStatus(winingFile, SystemVarList.APPROVED);

                        } else {
                            msg = "Ticket file approval failed. Please view error report";
                            status = (String) taskInfo.get(SystemVarList.STATUS);
                            session.setAttribute(SystemVarList.TASK_INFO, null);

                        }

                    }

                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response     
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.TASK_STATUS, taskStatus);
        response.put(SystemVarList.TASK_PROGRESS, taskProgress);

        return response.toJSONString();
    }

    @RequestMapping(value = "get_upload_report.htm", method = RequestMethod.POST)
    @ResponseBody
    public String getReport(HttpSession session,
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = SystemVarList.ERROR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                int errCount = ticketService.getErrorCountByFile(id);
                DlbWbTicketFile ticketFile = ticketService.get(id);
                response.put("error_count", errCount);
                response.put(SystemVarList.RECORD, ticketBuilder.convertToJSON(ticketFile));
                status = SystemVarList.SUCCESS;
                msg = "Ticket file approved " + SystemVarList.SUCCESS;
            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response     
        response.put("id", id);
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "dowanload_sales_file.htm", method = RequestMethod.GET)
    public void downloadSales(HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") int id, HttpSession session) throws IOException {
        //declare variable common varibles
        String response = "";
        File file = null;

        try {
            //genarate text file
            file = ticketService.genarateSalesFile(id, session);

            if (!file.exists()) {
                String errorMessage = "Sorry. The file you are looking for does not exist";
                System.out.println(errorMessage);
                OutputStream outputStream = resp.getOutputStream();
                outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
                outputStream.close();
                return;
            }

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                System.out.println("mimetype is not detectable, will take default");
                mimeType = "application/octet-stream";
            }

            System.out.println("mimetype : " + mimeType);

            resp.setContentType(mimeType);

            /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
            resp.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

            /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
            //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            resp.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            //Copy bytes from source to destination(outputstream in this example), closes both streams.
            FileCopyUtils.copy(inputStream, resp.getOutputStream());
        } catch (Exception e) {
//            if(e.getMessage()==null){
//                String errorMessage = "Some tickets are in the cart. You can not generate sales file now. "
//                        + "If business hours are over, please wait 10Minute to release tickets.";
//                System.out.println(errorMessage);
//                OutputStream outputStream = resp.getOutputStream();
//                outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
//                outputStream.close();
//                return;
//            }
            Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, e);
        }

        //list ticket by ticket id
    }

    @RequestMapping(value = "generate_check_sum.htm", method = RequestMethod.POST)
    @ResponseBody
    public String generateCheckSum(HttpSession session,
            @RequestParam("result_file") MultipartFile ticketFile) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
//                //check is exsiting
                String generateCheckSumForFile = ticketService.generateCheckSumForFile(ticketFile);
                status = SystemVarList.SUCCESS;

                response.put(SystemVarList.RECORD, generateCheckSumForFile);

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "get_duplicate.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getDupliacates(HttpSession session,
            @RequestParam("id") int id) {

        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray jSONArray = ticketService.getDuplicates(id);
                response.put(SystemVarList.RECORD, jSONArray);
                response.put("count", jSONArray.size());
                status = SystemVarList.SUCCESS;
                msg = "Successfully Fetched";
            }
        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }
    
    @RequestMapping(value = "remove_duplicate.htm", method = RequestMethod.POST)
    @ResponseBody
    public String removeDuplicate(HttpSession session,
            @RequestParam("id") int id) {

        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/ticket/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                
                ticketService.doRemovingProcess(id);
                JSONArray jSONArray = ticketService.getDuplicates(id);
                response.put("count", jSONArray.size());
                status = SystemVarList.SUCCESS;
                msg = "Successfully removed the duplicates.";
            }
        } catch (Exception ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

}
