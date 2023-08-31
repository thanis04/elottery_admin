/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DlbWbRiskProfile;
import com.epic.dlb.service.RiskProfileService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
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
 * @author kasun_n
 */
@Controller
@RequestMapping("risk_profile")
public class RiskProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private RiskProfileService profileService;

    //load page
    @RequestMapping(value = "/show_page.htm", method = RequestMethod.GET)
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/risk_profile/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                List listRiskProfile = profileService.listAll();
                List listDeviceProfile = profileService.listAllProfile();
                model.addAttribute("listRiskProfile", listRiskProfile);
                model.addAttribute("listDeviceProfile", listDeviceProfile);

            } catch (Exception ex) {
                Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/risk_profile";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    @RequestMapping(value = "/save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpSession session, Model model, DlbWbRiskProfile riskProfile) {
        //check Authorization
        JSONObject response = new JSONObject();
        String msg = "";
        String status = SystemVarList.ERROR;
        List checkAuthorization = userService.checkAuthorization(session, "/risk_profile/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {

                DlbWbRiskProfile dlbWbRiskProfile = profileService.get(riskProfile.getDescription());
                if (dlbWbRiskProfile == null) {
                    profileService.save(riskProfile);
                    msg = "Risk Profile" + MessageVarList.ADD_SUC;
                    status = SystemVarList.SUCCESS;
                } else {
                    msg = "Risk Profile" + MessageVarList.ALREADY_EXSITS;
                }

            } catch (Exception ex) {
                Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);

        }
        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "/update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String update(HttpSession session, Model model, DlbWbRiskProfile riskProfile) {
        //check Authorization
        JSONObject response = new JSONObject();
        String msg = "";
        String status = SystemVarList.ERROR;
        List checkAuthorization = userService.checkAuthorization(session, "/risk_profile/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {

                DlbWbRiskProfile dlbWbRiskProfile = profileService.get(riskProfile.getDescription());

                if (dlbWbRiskProfile != null) {
                    
                    dlbWbRiskProfile.setDescription(riskProfile.getDescription());
                    dlbWbRiskProfile.setValue(riskProfile.getValue());
                    dlbWbRiskProfile.setDlbDeviceProfile(riskProfile.getDlbDeviceProfile());
                    dlbWbRiskProfile.setDlbStatus(riskProfile.getDlbStatus());
                    
                    profileService.update(dlbWbRiskProfile);
                    msg = "Risk Profile " + MessageVarList.UPDATED_SUC;
                    status = SystemVarList.SUCCESS;

                } else {
                    msg = "Risk Profile " + MessageVarList.ALREADY_EXSITS;
                }

            } catch (Exception ex) {
                Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);

        }
        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "/get.htm", method = RequestMethod.POST)
    @ResponseBody
    public String get(HttpSession session, Model model, @RequestParam("id") int id) {
        //check Authorization
        JSONObject response = new JSONObject();
        String msg = "";
        String status = SystemVarList.ERROR;
        List checkAuthorization = userService.checkAuthorization(session, "/risk_profile/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject jSONObject = new JSONObject();
        if (authStatus) {
            try {
                DlbWbRiskProfile riskProfile = profileService.get(id);

                jSONObject.put("deviceProfileId", riskProfile.getDlbDeviceProfile().getProfileId());
                jSONObject.put("profileId", riskProfile.getProfileId());
                jSONObject.put("deviceProfile", riskProfile.getDlbDeviceProfile().getProfileId());
                jSONObject.put("status", riskProfile.getDlbStatus().getStatusCode());
                jSONObject.put("description", riskProfile.getDescription());
                jSONObject.put("value", riskProfile.getValue());

                status = SystemVarList.SUCCESS;

            } catch (Exception ex) {
                Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
        }

        //set response       
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, jSONObject);

        return response.toJSONString();

    }

    @RequestMapping(value = "/list.htm", method = RequestMethod.POST)
    @ResponseBody
    public String list(HttpSession session, Model model) {
        //check Authorization
        JSONObject response = new JSONObject();
        String msg = "";
        String status = SystemVarList.ERROR;
        List checkAuthorization = userService.checkAuthorization(session, "/risk_profile/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONArray array = new JSONArray();
        if (authStatus) {
            try {

                Iterator<DlbWbRiskProfile> listRiskProfile = profileService.listAll().iterator();

                while (listRiskProfile.hasNext()) {
                    DlbWbRiskProfile riskProfile = listRiskProfile.next();
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("profileId", riskProfile.getProfileId() );
                    jSONObject.put("profileDes", riskProfile.getDescription() );
                    jSONObject.put("deviceProfile", riskProfile.getDlbDeviceProfile().getDescription());
                    jSONObject.put("value", riskProfile.getValue());
                    jSONObject.put("status", riskProfile.getDlbStatus().getDescription());

                    array.add(jSONObject);

                }

                status = SystemVarList.SUCCESS;

            } catch (Exception ex) {
                Logger.getLogger(MasterConfigController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Authorization fail
            msg = (String) checkAuthorization.get(0);
        }

        //set response       
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, array);

        return response.toJSONString();

    }

}
