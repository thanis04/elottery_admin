/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.WiningFileBuilder;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.service.AuditTraceService;
import com.epic.dlb.service.AuthDeviceService;
import com.epic.dlb.service.EmployeeService;
import com.epic.dlb.service.EncryptionService;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.WiningFileService;
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
@RequestMapping("ticket_sales")
public class TicketSalesFileController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

  
    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/wining_file/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
         
//             
            return "pages/ticket_return";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

  
}
