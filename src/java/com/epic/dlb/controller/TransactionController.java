/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.TransactionBuilder;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbWbCeftTxnHistory;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.CeftTransactionService;
import com.epic.dlb.service.EncryptionService;
import com.epic.dlb.service.TransactionService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * @author methsiri_h
 */
@Controller
@RequestMapping("transaction_explore")
public class TransactionController {

//    /*-----------------------------
//    Dependancy Injection
//    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionBuilder transactionBuilder;

    private static SimpleDateFormat dateFormat;

    @Autowired
    private CeftTransactionService ceftTransactionService;

    @Autowired
    private EncryptionService encryptionService;

//
    //load page
    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/transaction_explore/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                List listByStatus = ceftTransactionService.listByStatus(SystemVarList.OTHER_BNK_TXT_FILE_GENERATED);
                model.addAttribute("listByStatus", listByStatus);

            } catch (Exception ex) {
                Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/transaction_explorer";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }
//
    //search 

    @RequestMapping(value = "/generate_file.htm", method = RequestMethod.GET)

    public void search(HttpSession session, DlbSwtStTransaction swtStTransaction,
            @RequestParam("from_date") String fromDate,
            @RequestParam("to_date") String toDate,
            HttpServletResponse resp) {

        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/transaction_explore/show_page.htm");
            DlbWbEmployee dlbWbEmployee = (DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE);
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                List search;

                //search using criteria
                List<WhereStatement> searchCriterias = new ArrayList<>();

                if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                    //add starting time and end time of the day  - to get all records in day
                    fromDate = fromDate + " 00:00:00";
                    toDate = toDate + " 23:59:59";

                    WhereStatement searchCriteriaFromDate = new WhereStatement("updatedtime", dateFormat.parse(fromDate), SystemVarList.GREATER_THAN);
                    WhereStatement searchCriteriaToDate = new WhereStatement("updatedtime", dateFormat.parse(toDate), SystemVarList.LESS_THAN);
                    WhereStatement txtType = new WhereStatement("dlbSwtMtTxnType.code", SystemVarList.REDEEM, SystemVarList.EQUAL);
                    WhereStatement bankCode = new WhereStatement("bankcode", SystemVarList.HNB_BANK_CODE, SystemVarList.NOT_EQUAL);
                    WhereStatement statusCode = new WhereStatement("dlbStatus.statusCode", SystemVarList.OTHER_BNK_TXT_PENDING, SystemVarList.EQUAL);
                    searchCriterias.add(searchCriteriaFromDate);
                    searchCriterias.add(searchCriteriaToDate);
                    searchCriterias.add(txtType);
                    searchCriterias.add(bankCode);
                    searchCriterias.add(statusCode);
                }

                search = transactionService.search(searchCriterias);
                List resultList = transactionBuilder.generateFile(search, dateFormat.parse(fromDate), dateFormat.parse(toDate));

                double totalAmt = (double) resultList.get(0);
                int numOfTrans = (int) resultList.get(1);
                File file = (File) resultList.get(2);

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

                DlbWbCeftTxnHistory ceftTxnHistory = new DlbWbCeftTxnHistory();
                ceftTxnHistory.setDlbStatus(new DlbStatus(SystemVarList.OTHER_BNK_TXT_FILE_GENERATED, null, null));
                ceftTxnHistory.setNoOfRecords(numOfTrans);
                ceftTxnHistory.setTotalAmount(totalAmt + "");
                ceftTxnHistory.setDlbWbEmployee(dlbWbEmployee);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                String afterHashCode = encryptionService.genarateHashCode(bis);
                ceftTxnHistory.setFileHash(afterHashCode);
                ceftTxnHistory.setCreatedTime(new Date());
                ceftTxnHistory.setLastUpdatedTime(new Date());
                ceftTxnHistory.setLastUpdatedUser(systemUser.getUsername());

                ceftTransactionService.save(ceftTxnHistory);

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

            }

        } catch (Exception ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//
//    @RequestMapping(value = "get.htm", method = RequestMethod.POST)
//    @ResponseBody
//    public String getRecord(HttpSession session,
//            @RequestParam("id") int id) {
//        //declare variable common varibles
//        JSONObject response = new JSONObject();
//        JSONObject record = null;
//        String msg = MessageVarList.COMMON_ERR;
//        String status = SystemVarList.ERROR;;
//
//        try {
//            //check Authorization
//            List checkAuthorization = userService.checkAuthorization(session, "/request_explore/show_page.htm");
//
//            boolean authStatus = (boolean) checkAuthorization.get(1);
//            if (authStatus) {
//                //get record
//                
//                DlbSwtStTransaction transaction = transactionService.get(id);
//                //check is exsting (when record is  not found return null object)
//                if (transaction != null) {
//                    //record found
//                    //create JSON object
//                    record = transactionBuilder.buildJSONObject(transaction);
//
//                    //set to response              
//                    msg = SystemVarList.SUCCESS;
//                    status = SystemVarList.SUCCESS;
//                } else {
//                    //record not found
//                    //set to response              
//                    msg = "Transaction " + MessageVarList.NOT_FOUND;
//                    status = SystemVarList.WARNING;
//                }
//
//            } else {
//                //Authorization fail
//                msg = (String) checkAuthorization.get(0);
//
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //set response
//        response.put(SystemVarList.STATUS, status);
//        response.put(SystemVarList.MESSAGE, msg);
//        response.put(SystemVarList.RECORD, record);
//
//        return response.toJSONString();
//    }

    @RequestMapping(value = "update_sequence.htm", method = RequestMethod.POST) 
    @ResponseBody
    public String updateRecord(HttpSession session, @RequestParam("id") int id, @RequestParam("val") int val) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/transaction_explore/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                DlbWbCeftTxnHistory ceftTxnHistory = ceftTransactionService.get(id);
                ceftTxnHistory.setSequenceNo(val);
                ceftTransactionService.update(ceftTxnHistory);
                //set to response              
                msg = SystemVarList.SUCCESS;
                status = SystemVarList.SUCCESS;

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
