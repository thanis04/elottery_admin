/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.email.service.EmailService;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.DashboardService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author kasun_n
 */
@Controller
public class HomeController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    //login page show
    @RequestMapping(value = "login.htm", method = RequestMethod.GET)
    public String showLoginPage() {
        return "common/login";
    }

    //login page show
    @RequestMapping(value = "index.htm", method = RequestMethod.GET)
    public String showWelcomePage(HttpSession session, Model model) {

        if (session != null) {
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (systemUser != null) {

                return "common/home";
            }
        }

        return "common/login";
    }

    //login proccess page
    @RequestMapping(value = "login.htm", method = RequestMethod.POST)
    public String processLogin(HttpSession session, Model model,
            HttpServletRequest servlet,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("public_key") String publicKey) {

        String msg = MessageVarList.COMMON_ERR;
        try {

            List checkUserLogin = userService.checkUserLogin(username, password, session, servlet, publicKey);
            boolean loginStatus = (boolean) checkUserLogin.get(0); //get status
            if (loginStatus) {
                //login success
                //redirect to login page            List checkUserLogin = userService.checkUserLogin(username, password, session, servlet,publicKey);
                userService.saveAuditLogInLogOut(servlet, username, "LOGIN", "SACC");
                return SystemVarList.HOME_PAGE;

            } else {
                //login error
                msg = (String) checkUserLogin.get(1);
                model.addAttribute(SystemVarList.MESSAGE, msg);

                //redirect to login page
                return SystemVarList.LOGIN_PAGE;
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            msg = MessageVarList.LOGIN_PUBLIC_KEY_COMMON_ERROR;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            msg = MessageVarList.LOGIN_PUBLIC_KEY_COMMON_ERROR;
        } catch (InvalidKeySpecException ex) {
            msg = MessageVarList.LOGIN_PUBLIC_KEY_COMMON_ERROR;
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            msg = MessageVarList.LOGIN_PUBLIC_KEY_ERROR;
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            msg = MessageVarList.LOGIN_PUBLIC_KEY_COMMON_ERROR;
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            msg = MessageVarList.LOGIN_PUBLIC_KEY_COMMON_ERROR;
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            msg = MessageVarList.LOGIN_PUBLIC_KEY_COMMON_ERROR;
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        model.addAttribute(SystemVarList.MESSAGE, msg);
        return "common/index";
    }

    //login proccess page
    @RequestMapping(value = "logout.htm", method = RequestMethod.GET)
    public String logOut(HttpSession session, HttpServletRequest servlet) {

        if (session != null) {
            DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (user != null) {
                userService.saveAuditLogInLogOut(servlet, user.getUsername(), "LOGOUT", "SACC");
            }
            session.invalidate();
        }

        return "common/login";
    }

    @RequestMapping(value = "forget_password.htm", method = RequestMethod.GET)
    public String showForgetPasswordPage() {
        return "common/forget_password";
    }
    
    @RequestMapping(value = "reset_password.htm", method = RequestMethod.GET)
    public String resetPasswordGETMethod(HttpSession session,
            HttpServletRequest servlet) {
        return "common/forget_password";
    }

    @RequestMapping(value = "reset_password.htm", method = RequestMethod.POST)
    public String resetPassword(HttpSession session,
            HttpServletRequest servlet,
            @RequestParam("username") String username, Model model) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        DlbWbSystemUser user = userService.get(username);
        if (user != null) {
            emailService.sendResetEmail(user);
            msg = "Please check your inbox on instruction to reset your password";
            status = "text-secondary";
        } else {
            if (username.equals("")) {
                msg = "Username cannot be empty.";
                status = "text-danger";
            } else {
                msg = "User does not exist";
                status = "text-danger";
            }

        }
        model.addAttribute(SystemVarList.MESSAGE, msg);
        model.addAttribute("type", status);
        //redirect to login page

        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return "common/forget_password";
    }

}
