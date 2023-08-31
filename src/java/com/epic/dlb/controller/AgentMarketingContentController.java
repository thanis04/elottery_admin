/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.builder.AgentMarketingContentBuilder;
import com.epic.dlb.dto.AgentMarketingContentDto;
import com.epic.dlb.model.DlbWbMarketingContent;
import com.epic.dlb.model.DlbWbPromotion;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.service.AgentMarketingContentService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("agent_marketing_content")
public class AgentMarketingContentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgentMarketingContentService agentMarketingContentService;

    @Autowired
    private AgentMarketingContentBuilder agentMarketingContentBuilder;

    @RequestMapping("/show_page_upload.htm")
    public String showPageUpload(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session,
                "/agent_marketing_content/show_page_upload.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/agt_uploading_content";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    @RequestMapping("/show_page_download.htm")
    public String showPageDownload(HttpSession session, Model model) {
        //check Authorization
        List checkAuthorization = userService.checkAuthorization(session,
                "/agent_marketing_content/show_page_download.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            //Authorization ok             
            return "pages/agt_download_content";

        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }

    }

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveContent(AgentMarketingContentDto dto,
            HttpSession session) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(
                    session, "/agent_marketing_content/show_page_upload.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
                DlbWbSystemUser user = userService.get(systemUser.getUsername());

                agentMarketingContentService.save(dto, user.getDlbWbEmployee().getName());

                status = SystemVarList.SUCCESS;
                msg = "Content saved successfully.";
            }

        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "/search.htm", method = RequestMethod.GET)
    @ResponseBody
    public String search(HttpSession session, AgentMarketingContentDto dto) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(
                    session, "/agent_marketing_content/show_page_upload.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;
                if (dto.getName().equals("null")) {
                    search = agentMarketingContentService.getAll(dto);
                } else {
                    search = agentMarketingContentService.getAllByName(dto);
                }

                searchResult = agentMarketingContentBuilder.buildSearchResult(search);

                //set to response
                response.put("search_result", searchResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "/search_agent.htm", method = RequestMethod.GET)
    @ResponseBody
    public String searchForAgents(HttpSession session, AgentMarketingContentDto dto) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(
                    session, "/agent_marketing_content/show_page_download.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                JSONArray searchResult;
                List search;

                search = agentMarketingContentService.getAllActive(dto);

                searchResult = agentMarketingContentBuilder.buildSearchResult(search);

                //set to response
                response.put("search_result", searchResult);
                msg = SystemVarList.SUCCESS;
                status = true;

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();

    }

    @RequestMapping(value = "get.htm", method = RequestMethod.POST)
    @ResponseBody
    public String getRecord(HttpSession session,
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        JSONObject record = null;
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session, "/agent_marketing_content/show_page_upload.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                DlbWbMarketingContent dlbWbMarketingContent;

                dlbWbMarketingContent = agentMarketingContentService.getById(id);
                if (dlbWbMarketingContent != null) {
                    //record found
                    //create JSON object
                    record = agentMarketingContentBuilder.buildSingleResult(dlbWbMarketingContent);

                    //set to response              
                    msg = SystemVarList.SUCCESS;
                    status = SystemVarList.SUCCESS;
                } else {
                    //record not found
                    //set to response              
                    msg = "Marketing Content " + MessageVarList.NOT_FOUND;
                    status = SystemVarList.WARNING;
                }

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.RECORD, record);

        return response.toJSONString();
    }

    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updateContent(AgentMarketingContentDto dto,
            HttpSession session) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(
                    session, "/agent_marketing_content/show_page_upload.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {

                agentMarketingContentService.update(dto);

                status = SystemVarList.SUCCESS;
                msg = "Content updated successfully.";
            }

        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }

    @RequestMapping(value = "delete.htm", method = RequestMethod.POST)
    @ResponseBody
    public String delete(HttpSession session,
            @RequestParam("id") int id) {
        //declare variable common varibles
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;

        try {
            //check Authorization
            List checkAuthorization = userService.checkAuthorization(session,
                    "/agent_marketing_content/show_page_upload.htm");

            boolean authStatus = (boolean) checkAuthorization.get(1);
            if (authStatus) {
                agentMarketingContentService.deleteById(id);
                status = SystemVarList.SUCCESS;
                msg = "Content " + MessageVarList.DEL_SUC;

            } else {
                //Authorization fail
                msg = (String) checkAuthorization.get(0);

            }

        } catch (Exception ex) {
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);

            if (ex.getClass().equals(DataIntegrityViolationException.class)) {
                MySQLIntegrityConstraintViolationException ex1 = (MySQLIntegrityConstraintViolationException) ex.getCause().getCause();
                if (ex1.getErrorCode() == MysqlErrorNumbers.ER_ROW_IS_REFERENCED_2) {
                    msg = "Can't delete.Item" + MessageVarList.ALREADY_USED;
                }
            }
            Logger.getLogger(AgentMarketingContentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set response
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);

        return response.toJSONString();
    }
}
