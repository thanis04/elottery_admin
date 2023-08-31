/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.builder.SystemUserBuilder;
import com.epic.dlb.model.DlbSwtStWinningLogic;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbResult;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.MD5Security;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SecurityService;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("userService")
public class UserService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private SystemUserBuilder systemUserBuilder;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(DlbWbSystemUser systemUser) throws Exception {
        return genericRepository.save(systemUser);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbSystemUser systemUser) throws Exception {
        return (int) genericRepository.update(systemUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbSystemUser get(String id) {
        DlbWbSystemUser systemUser = (DlbWbSystemUser) genericRepository.get(id, DlbWbSystemUser.class);

        //init proxy objects
        if (systemUser != null) {
            Hibernate.initialize(systemUser.getDlbStatus());
            Hibernate.initialize(systemUser.getDlbWbEmployee());
            Hibernate.initialize(systemUser.getDlbWbUserRole());
        }

        return systemUser;
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbWbSystemUser.class);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbSystemUser systemUser = (DlbWbSystemUser) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(systemUser.getDlbStatus());
            Hibernate.initialize(systemUser.getDlbWbEmployee());
            Hibernate.initialize(systemUser.getDlbWbUserRole());
            list.add(systemUser);

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbWbSystemUser.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbSystemUser systemUser = (DlbWbSystemUser) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(systemUser.getDlbStatus());
            Hibernate.initialize(systemUser.getDlbWbEmployee());
            Hibernate.initialize(systemUser.getDlbWbUserRole());
            list.add(systemUser);

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbSystemUser.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbSystemUser systemUser = (DlbWbSystemUser) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(systemUser.getDlbStatus());
            Hibernate.initialize(systemUser.getDlbWbEmployee());
            Hibernate.initialize(systemUser.getDlbWbUserRole());
            list.add(systemUser);

        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public List checkUserLogin(String userName, String password,
            HttpSession session, HttpServletRequest servlet, String localPublicKeyStr)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException, IOException, ClassNotFoundException, InvalidKeyException, SignatureException {

        List responseList = new ArrayList();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        if (userName.isEmpty()) {
            msg = MessageVarList.LOGIN_USERNAME_EMPTY_ERR;
        } else if (password.isEmpty()) {
            msg = MessageVarList.LOGIN_PASSWORD_EMPTY_ERR;
        } else {
            DlbWbSystemUser systemUser = this.get(userName);
            if (systemUser != null) {
                Hibernate.initialize(systemUser.getDlbWbEmployee());
            }

            //check user is exsits
            if (systemUser != null) {
                //check username and password
                if (userName.equalsIgnoreCase(userName) && MD5Security.MD5(password).equals(systemUser.getPassword())) {

                    //check user is active
                    if (systemUser.getDlbStatus().getStatusCode() == (SystemVarList.ACTIVE)
                            && systemUser.getDlbWbEmployee().getDlbStatus().getStatusCode() == (SystemVarList.ACTIVE)
                            && systemUser.getDlbWbUserRole().getDlbStatus().getStatusCode() == (SystemVarList.ACTIVE)) {
                        //login details OK
                        //get privilages
                        List userPrivilages = userPrivilegeService
                                .getUserPrivilages(systemUser.getDlbWbUserRole());

                        //get privilaged sections
                        List userPrivilagedSections = userPrivilegeService.
                                getUserPrivilagedSections(systemUser.getDlbWbUserRole());
                        DlbWbEmployee employee = systemUser.getDlbWbEmployee();

                        //set User details to session
                        session.setAttribute(SystemVarList.USER, systemUser);
                        session.setAttribute(SystemVarList.USER_PRIVILEGES, userPrivilages);
                        session.setAttribute(SystemVarList.USER_PRIVILEGED_SECTIONS, userPrivilagedSections);
                        session.setAttribute(SystemVarList.EMPLOYEE, employee);
                        session.setAttribute(SystemVarList.IP, servlet.getRemoteAddr());

//                    //check login token
                        status = true;
//                        if (employee.getTokenPublicKey() != null && !employee.getTokenPublicKey().isEmpty()) {
//
//                            byte[] localPublicKeyArry = Base64.decodeBase64(localPublicKeyStr);
//
//                            status = securityService.verifyText(employee.getTokenPublicKey(), userName.getBytes(), localPublicKeyArry);
//
//                            //set response
//                            if (status) {
//                                msg = SystemVarList.SUCCESS;
//                                status = true;
//                            } else {
//                                msg = MessageVarList.LOGIN_PUBLIC_KEY_ERROR;
//                            }
//
//                        } else {
//                            msg = MessageVarList.LOGIN_PUBLIC_KEY_NOT_FOUND;
//                        }

                    } else {
                        //user not active
                        //set response
                        status = false;
                        msg = MessageVarList.LOGIN_USER_STATUS_ERR;
                    }

                } else {
                    //password not equal
                    msg = MessageVarList.LOGIN_ERR;
                    status = false;
                }

            } else {
                //user not found
                msg = "Invalid username or password ";
                status = false;
            }

        }

        //set response
        responseList.add(status);
        responseList.add(msg);

        return responseList;
    }

    //check user is loggin  
    @Transactional(rollbackFor = Exception.class)
    public List checkAuthorization(HttpSession session, String url) {
        List responseList = new ArrayList();
        String msg = MessageVarList.COMMON_ERR;
        boolean status = false;

        //check session is not  null
        if (session != null) {
            //check user is not null
            DlbWbSystemUser user = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);//get user from session
            if (user == null) {//check user is not null
                status = false;
                msg = MessageVarList.SESSION_ERR;
            } else {
                //check Authorization              
                boolean hasUserPrivilage = userPrivilegeService.hasUserPrivilage(user.getDlbWbUserRole(), url);
                if (hasUserPrivilage) {
                    //Authorization ok
                    msg = SystemVarList.SUCCESS;
                    status = true;
                } else {
                    //Authorization fail
                    msg = MessageVarList.USER_ACCESS_ERR;
                    status = false;
                }

            }

        } else {
            // session is nul
            msg = MessageVarList.SESSION_ERR;
            status = false;
        }

        //set response
        responseList.add(msg);
        responseList.add(status);

        return responseList;
    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbSystemUser systemUser) throws Exception {
        return genericRepository.delete(systemUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAuditLogInLogOut(HttpServletRequest servlet, String username, String status, String page) {
        try {
            System.out.println(servlet.getHeader("user-agent"));
//            System.out.println("Test======================================");
            DlbWbSystemUser systemUser = this.get(username);

            JSONObject jSONObject = new JSONObject();
            if (status.equals("LOGIN")) {
                jSONObject.put("Browser Used", getBrowserInfo(servlet.getHeader("user-agent")));
                jSONObject.put("IP Address", getClientAddress(servlet));
            } else if (status.equals("LOGOUT")) {
                jSONObject.put("Browser Used", getBrowserInfo(servlet.getHeader("user-agent")));
                jSONObject.put("IP Address", getClientAddress(servlet));
                jSONObject.put("Method", "User action");
            }

            activityLogService.saveWithoutSection(activityLogService.buildActivityLog(
                    status, jSONObject, page, systemUser));
            System.out.println("Test1======================================");
            
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getBrowserType(HttpServletRequest req) {
        String userAgent = req.getHeader("user-agent");
        String browserName = userAgent;
        return browserName;
    }

    public String getClientAddress(HttpServletRequest request) throws UnknownHostException {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        System.out.println(ipAddress);
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress localip = InetAddress.getLocalHost();
                ipAddress = localip.getHostAddress();
            }
        }
        return ipAddress;
    }

    public String getBrowserInfo(String Information) {
        String browsername = "";
        String browserversion = "";
        String browser = Information;
        if (browser.contains("MSIE")) {
            String subsString = browser.substring(browser.indexOf("MSIE"));
            String info[] = (subsString.split(";")[0]).split(" ");
            browsername = info[0];
            browserversion = info[1];
        } else if (browser.contains("Firefox")) {

            String subsString = browser.substring(browser.indexOf("Firefox"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if (browser.contains("Chrome")) {

            String subsString = browser.substring(browser.indexOf("Chrome"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if (browser.contains("Opera")) {

            String subsString = browser.substring(browser.indexOf("Opera"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if (browser.contains("Safari")) {

            String subsString = browser.substring(browser.indexOf("Safari"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        }
        return browsername + " (" + browserversion + ")";
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getSystemUser(String username, String userRoleCode) throws Exception {
        DlbWbSystemUser systemUser = get(username);
        JSONArray searchResult;
        List search;

        //check search criteria is null
        if (username.isEmpty() && userRoleCode.equals("0")) {
            //list all records
            search = this.listAll();
            searchResult = systemUserBuilder.buildSearchResult(search);

        } else {
            //search using criteria
            List<WhereStatement> searchCriterias = new ArrayList<>();
            if (!username.isEmpty()) {
                WhereStatement searchCriteria = new WhereStatement("username", username,
                        SystemVarList.LIKE);
                searchCriterias.add(searchCriteria);
            }
            if (!userRoleCode.equals("0")) {
                WhereStatement searchCriteria = new WhereStatement("dlbWbUserRole.userrolecode",
                        userRoleCode, SystemVarList.EQUAL);
                searchCriterias.add(searchCriteria);
            }

            search = this.search(searchCriterias);
            searchResult = systemUserBuilder.buildSearchResult(search);
        }

        return searchResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getTotalSystemUsers(String userRoleCode) {
        List<WhereStatement> whereStatementList = new ArrayList<>();

        if (!userRoleCode.equals("0")) {
            WhereStatement whereStatement2 = new WhereStatement("dlbWbUserRole.userrolecode",
                    userRoleCode, SystemVarList.EQUAL);
            whereStatementList.add(whereStatement2);
        }

        return genericRepository.getCount(DlbWbSystemUser.class, whereStatementList);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getTotalActiveInactiveSystemUsers(String username, String userRoleCode,
            int statusCode) throws Exception {
        DlbWbSystemUser systemUser = get(username);
        List<WhereStatement> whereStatementList = new ArrayList<>();
        WhereStatement whereStatement = new WhereStatement("dlbStatus.statusCode",
                statusCode, SystemVarList.EQUAL);
        whereStatementList.add(whereStatement);
        if (!userRoleCode.equals("0")) {
            whereStatementList.clear();

            WhereStatement whereStatement1 = new WhereStatement("dlbStatus.statusCode", statusCode, SystemVarList.EQUAL, SystemVarList.AND);
            whereStatementList.add(whereStatement1);

            WhereStatement whereStatement2 = new WhereStatement("dlbWbUserRole.userrolecode",
                    userRoleCode, SystemVarList.EQUAL);
            whereStatementList.add(whereStatement2);
        }
        return genericRepository.getCount(DlbWbSystemUser.class, whereStatementList);
    }
}
