/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.dto.ActivityLogDto;
import com.epic.dlb.model.DlbWbPage;
import com.epic.dlb.model.DlbWbSection;
import com.epic.dlb.model.DlbWbSystemPriviledge;
import com.epic.dlb.service.ActivityLogService;
import com.epic.dlb.service.PageService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.SystemVarList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
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
 * @author nipuna_k
 */
@Controller
@RequestMapping("activity_audit")
public class ActivityAuditController {

    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session, "/activity_audit/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                List sections = pageService.listActivePageSections();
                model.addAttribute("sections", sections);
            } catch (Exception ex) {
                Logger.getLogger(ActivityAuditController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/activity_audit";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/load_page_list.htm", method = RequestMethod.GET)
    @ResponseBody
    public String loadPageList(HttpSession session, Model model, @RequestParam("sectioncode") String sectionCode) {

        JSONObject jSONObject = new JSONObject();
        JSONArray pageListJSON = new JSONArray();
        List checkAuthorization = userService.checkAuthorization(session, "/activity_audit/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                DlbWbSection section = new DlbWbSection(sectionCode);
                List allPageList = pageService.listActivePagesBySection(section);

                for (int i = 0; i < allPageList.size(); i++) {
                    DlbWbSystemPriviledge systemPriviledge = (DlbWbSystemPriviledge) allPageList.get(i);
                    DlbWbPage page = systemPriviledge.getDlbWbPage();

                    JSONObject item = new JSONObject();
                    item.put("page_id", page.getPagecode());
                    item.put("page_name", page.getDescription());

                    pageListJSON.add(item);

                }
                jSONObject.put("status", true);
                jSONObject.put("list", pageListJSON);
            } catch (Exception e) {
                jSONObject.put("status", false);
                Logger.getLogger(ActivityAuditController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return jSONObject.toJSONString();
    }

    @RequestMapping(value = "/show_data.htm", method = RequestMethod.GET)
    @ResponseBody
    public String showPaginatedData(HttpSession session, Model model,
            ActivityLogDto activityLogDto, HttpServletRequest request) {
        List checkAuthorization = userService.checkAuthorization(session, "/activity_audit/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        JSONObject response = new JSONObject();
        if (authStatus) {
            try {

                String start = request.getParameter("start");
                String end = request.getParameter("end");
                String length = request.getParameter("length");

                String section = request.getParameter("dlbWbSection");
                String page = request.getParameter("dlbWbPage");
                String createdTime = request.getParameter("date");
                String toDate = request.getParameter("toDate");

                ActivityLogDto activityLogDto1 = new ActivityLogDto();
                activityLogDto1.setStart(start);
                activityLogDto1.setEnd(end);
                activityLogDto1.setLength(length);

                activityLogDto1.setModule(section);
                activityLogDto1.setDlbWbPage(page);
                activityLogDto1.setCreatedTime(createdTime);
                activityLogDto1.setToDate(toDate);

                JSONArray data = activityLogService.getList(activityLogDto1);
                Integer count = activityLogService.getCount(activityLogDto1);

                response.put("search_result", data);
                response.put("data", data);
                response.put("recordsTotal", count);
                response.put("recordsFiltered", count);
                response.put("msg", "Fetching Successfull");
                response.put("status", SystemVarList.SUCCESS);

            } catch (Exception ex) {
                Logger.getLogger(ActivityAuditController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
        }

        return response.toJSONString();
    }

    @RequestMapping(value = "/getDetail.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getDetail(HttpSession session, Model model,
            HttpServletRequest request,
            @RequestParam("id") String id) {
        JSONObject response = new JSONObject();
        JSONObject jSONObject = activityLogService.splitJSON(Integer.parseInt(id));
        response.put("data", jSONObject);
        return response.toJSONString();
    }
}
