/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.support;

import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.util.common.SystemVarList;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 *
 * @author Kasun Nadeeshana
 * This Filter help to bind additional parameters to all requests
 * EX: STD (lastUpdatedUser,createdTime,lastUpdatedUser)
 */
public class AppendParametersFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //declare varibles
         AddableHttpRequest enhancedHttpRequest=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Map<String, String[]> additionalParams = new HashMap<String, String[]>();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();

        
        
        //get session user
        if (session != null) {//check  session null
            DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
            if (systemUser != null) {//check user loggin
                
                additionalParams.put("lastUpdatedTime", new String[]{dateFormat.format(new Date())});
                additionalParams.put("createdTime", new String[]{dateFormat.format(new Date())});
                additionalParams.put("lastUpdatedUser", new String[]{systemUser.getUsername()});
                
                additionalParams.put("lastupdatedtime", new String[]{dateFormat.format(new Date())});
                additionalParams.put("createdtime", new String[]{dateFormat.format(new Date())});
                additionalParams.put("lastupdateduser", new String[]{systemUser.getUsername()});
            }

        }
        
         // pass the request along the filter chain
         enhancedHttpRequest = new AddableHttpRequest((HttpServletRequest) request, additionalParams);
         chain.doFilter(enhancedHttpRequest, response);

    }
    
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        
//        Map<String, String[]> additionalParams = new HashMap<String, String[]>();
//       HttpServletRequest httpServletRequest = (HttpServletRequest) request;
////        HttpSession session = httpServletRequest.getSession();
////        
////        String userName=(String) session.getAttribute("user_name");
////        additionalParams.put("date", new String[]{dateFormat.format(new Date())});     
////        additionalParams.put("userName", new String[]{userName});   
//        
//        AddableHttpRequest enhancedHttpRequest = new AddableHttpRequest((HttpServletRequest) request, additionalParams);
//
//        // pass the request along the filter chain
//        chain.doFilter(enhancedHttpRequest, response);
//    }

}
