/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.PasswordChangeDto;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.PasswordResetService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MD5Security;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author nipuna_k
 */
@Controller
@RequestMapping("{key}/password_reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/show_page.htm", method = RequestMethod.GET)
    public String showResetPage(@PathVariable("key") String key) {
        System.out.println(key);
        try {
            passwordResetService.executeExpiredKeys();
            Boolean isValid = passwordResetService.isExisting(key);
            if (!isValid) {
                return "common/password_reset";
            } else {
                return "common/password_reset_expired";
            }
        } catch (Exception ex) {
            Logger.getLogger(PasswordResetController.class.getName()).log(Level.SEVERE, null, ex);
            return "common/login";
        }
    }

    @RequestMapping(value = "/reset_password.htm", method = RequestMethod.POST)
    public String resetPasswordUpdate(@PathVariable("key") String key, PasswordChangeDto dto) {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        try {
            passwordResetService.executeExpiredKeys();
            Boolean isValid = passwordResetService.isExisting(key);
            if (!isValid) {
                String user = passwordResetService.getUsernameByKey(key);
                DlbWbSystemUser userdet = userService.get(user);
                if (userdet != null) {
                    userdet.setPassword(MD5Security.MD5(dto.getPassword()));
                    userService.update(userdet);
                    passwordResetService.updateCurrentKeyOfUsername(user);
                    //set to response              
                    msg = "Password updated successfully!";
                    status = SystemVarList.SUCCESS;

                } else {
                    msg = "User is not exist";
                    status = SystemVarList.ERROR;
                }

            } else {
                msg = "URL expired";
                status = SystemVarList.ERROR;
            }
            response.put(SystemVarList.STATUS, status);
            response.put(SystemVarList.MESSAGE, msg);
            return response.toJSONString();
        } catch (Exception ex) {
            Logger.getLogger(PasswordResetController.class.getName()).log(Level.SEVERE, null, ex);
            return "common/login";
        }
    }
    
    
}
