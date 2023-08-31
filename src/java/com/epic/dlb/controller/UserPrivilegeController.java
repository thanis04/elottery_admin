/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.UserPrivilegeBuilder;
import com.epic.dlb.model.DlbWbPage;
import com.epic.dlb.model.DlbWbSection;
import com.epic.dlb.model.DlbWbSystemPriviledge;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbUserPriviledge;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.PageService;
import com.epic.dlb.service.UserPrivilegeService;
import com.epic.dlb.service.UserRoleService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
@RequestMapping("user_privilege")
public class UserPrivilegeController {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PageService pageService;

    @Autowired
    private UserPrivilegeBuilder userPrivilegeBuilder;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/user_privilege/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                //add Attribute to JSP view
                WhereStatement whereStatement = new WhereStatement("dlbStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
                List userRoleList = userRoleService.listAll(whereStatement);

                //load sections
                List sections = pageService.listActivePageSections();
                model.addAttribute("user_role_select_box", userRoleList);
                model.addAttribute("sections", sections);

            } catch (Exception ex) {
                Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/user_privilege";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping("/load_pages.htm")
    @ResponseBody
    public String loadPageList(HttpSession session, Model model,
            @RequestParam("section") String sectionCode,
            DlbWbUserPriviledge userPriviledge) {
        //init varibles
        JSONArray pageListJSON = new JSONArray();
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/user_privilege/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                //Authorization ok
                DlbWbSection section = new DlbWbSection(sectionCode);

                //load all pages and user privilage pages
                List allPageList = pageService.listActivePagesBySection(section);

                for (int i = 0; i < allPageList.size(); i++) {
                    DlbWbSystemPriviledge systemPriviledge = (DlbWbSystemPriviledge) allPageList.get(i);
                    DlbWbPage page = systemPriviledge.getDlbWbPage();

                    //put to JOSN object and add to json array
                    String selectedAttr = "";
                    //check has privilage to selcted user role                 
                    if (userPrivilegeService.hasUserPrivilage(userPriviledge.getDlbWbUserRole(), page)) {
                        //privilage has 
                        selectedAttr = "selected";
                    }

                    JSONObject item = new JSONObject();
                    item.put("page_id", page.getPagecode());
                    item.put("page_name", page.getDescription());
                    item.put("selected_attr", selectedAttr);

                    pageListJSON.add(item);

                }

            } catch (Exception ex) {
                Logger.getLogger(WiningFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pageListJSON.toJSONString();
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session,
            DlbWbUserPriviledge userPriviledge,
            @RequestParam("page_list") String pageList) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/user_privilege/show_page.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                JSONParser jSONParser = new JSONParser();
                JSONArray pageListJSON = (JSONArray) jSONParser.parse(pageList);
                //build user userPrivileges list
                if (!"null".equals(pageList)) {
                    List userPrivileges = userPrivilegeBuilder.build(userPriviledge, pageList, systemUser);
                    userPrivilegeService.save(userPrivileges);

                    //Activity Log Save
                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Role", userRoleService.get(
                            userPriviledge.getDlbWbUserRole().getUserrolecode()).getDescription());
                    jSONObject.put("Section", pageService.getSection(
                            userPriviledge.getDlbWbSection().getSectioncode()));
                    jSONObject.put("Pages", pageService.getPageList(pageListJSON));

                    activityLogService.save(activityLogService.buildActivityLog(
                            "SAVE", jSONObject, "UPM", user));
                } else {
                    userPrivilegeService.deleteExsitingRecords(userPriviledge.getDlbWbUserRole().getUserrolecode(), userPriviledge.getDlbWbSection().getSectioncode());

                    //Activity Log Save
                    DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("User Role", userRoleService.get(
                            userPriviledge.getDlbWbUserRole().getUserrolecode()).getDescription());
                    jSONObject.put("Section", pageService.getSection(
                            userPriviledge.getDlbWbSection().getSectioncode()));
//                    jSONObject.put("page", userPriviledge.getDlbWbPage().getPagecode());
                    activityLogService.save(activityLogService.buildActivityLog(
                            "DELETE", jSONObject, "UPM", user));
                }

                msg = "User privileges " + MessageVarList.ADD_SUC;
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
