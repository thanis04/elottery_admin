/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DlbSwtMtConfiguration;
import com.epic.dlb.service.MasterConfigService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author methsiri_h
 */

@RequestMapping("/masterconfig")
@Controller
public class MasterConfigController {

    @Autowired
    private UserService userService;

    @Autowired
    private MasterConfigService masterConfigService;

    //load page
    @RequestMapping(value = "/show_page.htm", method = RequestMethod.GET)
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/masterconfig/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                List masterConfigdata = masterConfigService.get(); 
                if (masterConfigdata.size() > 0) {
                    //Assining fist record to model from list
                    model.addAttribute("masterConfigerdata", masterConfigdata.get(0));
                }

            } catch (Exception ex) {
                Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/masterconfig";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updateRecord(HttpSession session, DlbSwtMtConfiguration masterConfiguration) {

        //declaring common variable
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //checking Autherization
            List checkAutherization = userService.checkAuthorization(session, "/masterconfig/show_page.htm");
            boolean authStats = (boolean) checkAutherization.get(1);
            if (authStats) {
                //check the execisting data
                if (masterConfigService.get(masterConfiguration.getId()) != null) {
                    masterConfigService.update(masterConfiguration);

                    //set to response              
                    msg = "Configuration " + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //setting the response              
                    msg = "Configuration " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }
            } else {
                //Authorization fail
                 msg = (String) checkAutherization.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
        }

         //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toJSONString();
    }

}
