/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.WiningFileBuilder;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbTicketFile;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.AuthDeviceService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.EncryptionService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.TicketService;
import com.epic.dlb.service.WiningFileService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SecurityService;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedInputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
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

/**
 *
 * @author kasun_n
 */
@Controller
@RequestMapping("wining_file")
public class WiningFileController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WiningFileBuilder winingFileBuilder;

    @Autowired
    private WiningFileService winingFileService;

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
    private TicketService ticketService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");
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
                Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/wining_file";
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
            @RequestParam("result_file") MultipartFile resultFile,
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

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
//                //check is exsiting
                DlbWbWiningFile buildWiningFile = winingFileBuilder.buildWiningFile(formData, session, productDescription);

                if (winingFileService.validateTicketFile(resultFile, buildWiningFile.getDrawNo())) {
                    //[1] validate File Format
                    fileNameValidation = winingFileService.checkFileName(resultFile.getOriginalFilename(), buildWiningFile);

                    //[2] validate mac address
                    macAddressValidation = authDeviceService.checkIsAuthDevice(deviceMacAddress);

                    //[3]  hashcode validation
                    BufferedInputStream bis = new BufferedInputStream(resultFile.getInputStream());
                    String afterHashCode = encryptionService.genarateHashCode(bis);
                    hashCodeValidation = beforeHashcode.equals(afterHashCode);

                    //[4] token validation
                    byte[] localPublicKeyArry = Base64.decodeBase64(hashCodeSignKey);
                    boolean verifyTextStatus = securityService.verifyText(session, beforeHashcode.getBytes(), localPublicKeyArry);

                    //set validation status
                    buildWiningFile.setFilenameCheck(fileNameValidation);
                    buildWiningFile.setMacCheck(macAddressValidation);
                    buildWiningFile.setHashCheck(hashCodeValidation);
                    buildWiningFile.setMacAddress(deviceMacAddress);
                    buildWiningFile.setTokenCheck(verifyTextStatus);

                    Integer save = winingFileService.save(buildWiningFile, resultFile, session);

                    //audit trace log save
                    String activity
                            = AuditTraceVarList.WINING_FILE + AuditTraceVarList.UPLOADED + AuditTraceVarList.REF_NO + save;
                    auditTraceService.save(activity, session);
                    if (save != 0) {
                        status = SystemVarList.SUCCESS;
                        msg = "Winning file uploaded " + SystemVarList.SUCCESS;

                        DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("Draw No", buildWiningFile.getDrawNo());
                        jSONObject.put("Product", buildWiningFile.getProductDescription());
                        jSONObject.put("Draw Date", buildWiningFile.getDate());
                        jSONObject.put("Ticket File ID", buildWiningFile.getDlbWbTicket().getId());
                        activityLogService.save(activityLogService.buildActivityLog(
                                "SAVE", jSONObject, "RM", user));

                    } else {
                        msg = "Winning file has already been uploaded for the selected lottery and the draw No";
                    }
                } else {
                    status = SystemVarList.ERROR;
                    msg = "Invalide winner file!. Please try again";
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "validate_wining_file.htm", method = RequestMethod.GET)
    @ResponseBody
    public String validateWiningFile(HttpSession session,
            @RequestParam("result_file") MultipartFile resultFile,
            @RequestParam("form_data") String formData) {

        JSONParser parser = new JSONParser();
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        boolean fileNameValidation = false;
        boolean macAddressValidation = false;
        boolean hashCodeValidation = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //get request parameters
                JSONObject formDataJSON = (JSONObject) parser.parse(formData);
                String beforeHashcode = (String) formDataJSON.get("hash_code");
                String deviceMacAddress = (String) formDataJSON.get("mac_address");

                //[1] validate File Format
//                  fileNameValidation = winingFileService.checkFileName(resultFile.getOriginalFilename());
                //[2] validate mac address
                macAddressValidation = authDeviceService.checkIsAuthDevice(deviceMacAddress);

                //[3] validate hashcode
                BufferedInputStream bis = new BufferedInputStream(resultFile.getInputStream());
                String afterHashCode = encryptionService.genarateHashCode(bis);

                hashCodeValidation = beforeHashcode.equals(afterHashCode);

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping("/approve_pending_list.htm")
    public String showApprovePage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/wining_file/approve_pending_list.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                List productSelectBox = winingFileService.listWiningFileByStage(SystemVarList.APPROVAL_PENDING, SystemVarList.SUBMITED, SystemVarList.PROCESSING);
                model.addAttribute("pending_list", productSelectBox);

            } catch (Exception ex) {
                Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/wining_file_approval";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

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
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/approve_pending_list.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                //check task status
                if (status.equals(SystemVarList.INIT)) {
                    //1st time
                    //get wining file by id
                    DlbWbWiningFile winingFile = winingFileService.get(id);

                    winingFileService.saveResultData(session, winingFile);

                    //audit trace log save
                    String activity
                            = AuditTraceVarList.WINING_FILE + AuditTraceVarList.APPROVED + AuditTraceVarList.REF_NO + id;
                    auditTraceService.save(activity, session);

                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Draw No", winingFile.getDrawNo());
                    jSONObject.put("Product", winingFile.getProductDescription());
                    jSONObject.put("Draw Date", winingFile.getDate());
                    jSONObject.put("Ticket File ID", winingFile.getDlbWbTicket().getId());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "APPROVE", jSONObject, "RA", user));
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response     
        response.put(SystemVarList.MESSAGE, msg);
        response.put("id", id);
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.TASK_PROGRESS, taskProgress);

        return response.toJSONString();
    }

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
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/approve_pending_list.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbWiningFile winingFile = winingFileService.get(id);
                List<Object> totalWinningByFile = winingFileService.getTotalWinningByFile(winingFile);
                Double amount = (Double) totalWinningByFile.get(0);
                String jackpot = (String) totalWinningByFile.get(1);
                winingFileJSON = winingFileBuilder.convertToJSON(winingFile, amount, jackpot);
                status = SystemVarList.SUCCESS;
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
        response.put(SystemVarList.RECORD, winingFileJSON);

        return response.toJSONString();
    }

    @RequestMapping(value = "show_wining_file_approve_progress.htm", method = RequestMethod.POST)
    @ResponseBody
    public String showProcessProgress(HttpSession session, @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();

        JSONParser jSONParser = new JSONParser();
        String msg = "";
        String taskStatus = "";
        JSONObject taskInfo = null;
        int taskProgress = 0;

        //update status
        DlbWbWiningFile winingFile = new DlbWbWiningFile();
        winingFile.setId(id);
        try {

            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");
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
                            Long taskLongVal = (long) taskInfo.get(SystemVarList.TASK_PROGRESS);
                            taskProgress = taskLongVal.intValue();
                        }
                        msg = "Winning file approved " + SystemVarList.SUCCESS;
                        session.setAttribute(SystemVarList.TASK_INFO, null);

                        winingFileService.updateStatus(winingFile, SystemVarList.APPROVED, session);

                    }

                    if (taskStatus.trim().equals(SystemVarList.ERROR)) {
                        msg = MessageVarList.COMMON_ERR;
                        taskStatus = SystemVarList.ERROR;

                        Long status = (Long) taskInfo.get(SystemVarList.STATUS);
                        if (status == 1062) {
                            msg = (String) taskInfo.get(SystemVarList.MESSAGE);
                            session.setAttribute(SystemVarList.TASK_INFO, null);
                        }

                        winingFileService.updateStatus(winingFile, SystemVarList.SUBMITED, session);
                        session.setAttribute(SystemVarList.TASK_INFO, null);
                    }
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response     
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.STATUS, taskStatus.trim());
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
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                int errCount = winingFileService.getErrorCountByFile(id);
                DlbWbWiningFile winingFile = winingFileService.get(id);
                response.put("error_count", errCount);

                List<Object> totalWinningByFile = winingFileService.getTotalWinningByFile(winingFile);
                Double amount = (Double) totalWinningByFile.get(0);
                String jackpot = (String) totalWinningByFile.get(1);
                JSONObject winingFileJSON = winingFileBuilder.convertToJSON(winingFile, amount, jackpot);

                response.put(SystemVarList.RECORD, winingFileJSON);
                status = SystemVarList.SUCCESS;
                msg = "Wining file approved " + SystemVarList.SUCCESS;
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

    //search 
    @RequestMapping(value = "/load_ticket_file.htm", method = RequestMethod.GET)
    @ResponseBody
    public String loadProfile(HttpSession session,
            @RequestParam("code") String productCode,
            @RequestParam("draw_no") String drawNo) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                DlbWbTicketFile ticketFile = ticketService.getTicketFile(productCode, drawNo);

                if (ticketFile != null) {
                    //set to response
                    response.put("ticketID", ticketFile.getId());
                    response.put("draw_date", simpleDateFormat.format(ticketFile.getDate()));
                    msg = SystemVarList.SUCCESS;
                    status = true;
                } else {
                    msg = "No Ticket file upload for selected draw no";
                    status = false;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

}
