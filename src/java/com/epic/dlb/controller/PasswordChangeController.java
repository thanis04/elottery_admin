/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.PasswordChangeDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MD5Security;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nipuna_k
 */
@RequestMapping("/passwordChange")
@Controller
public class PasswordChangeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/show_page.htm", method = RequestMethod.GET)
    public String showPage(HttpSession session, Model model) {
        if (session != null) {
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (systemUser != null) {
                return "common/password_change";
            }
        }

        return "common/login";
    }
    
    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updatePassword(HttpSession session, PasswordChangeDto dto) {

        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            if (session != null) {
                DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                if (systemUser != null) {

                    DlbWbSystemUser user = userService.get(systemUser.getUsername());
                    if (user != null) {

                        if (MD5Security.MD5(dto.getOldPassword()).equals(user.getPassword())) {
                            //encript password
                            user.setPassword(MD5Security.MD5(dto.getPassword()));
                            userService.update(user);

                            //set to response              
                            msg = "Password has been changed successfully";
                            status = SystemVarList.SUCCESS;
                        } else {
                            //set to response              
                            msg = "Current Password is Wrong";
                            status = SystemVarList.ERROR;
                        }
                    } else {
                        msg = "Somthing Went Wrong";
                        status = SystemVarList.ERROR;
                    }
                    response.put(SystemVarList.STATUS, status);
                    response.put(SystemVarList.MESSAGE, msg);
                    return response.toJSONString();
                } else {
                    return "common/login";
                }
            }
        } catch (Exception ex) {
            msg = "Somthing Went Wrong";
            status = SystemVarList.ERROR;
            Logger.getLogger(PasswordChangeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

}
