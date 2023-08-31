/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.support;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author Kasun
 */
public class AddableHttpRequest extends HttpServletRequestWrapper {
    
     private final Map<String, String[]> modifiableParameters;
    private Map<String, String[]> allParameters = null;

    public AddableHttpRequest(final HttpServletRequest request, 
                                                    final Map<String, String[]> additionalParams) {
        super(request);
        modifiableParameters = new TreeMap<String, String[]>();
        modifiableParameters.putAll(additionalParams);
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        final String userName = request.getParameter("user.name");
        ServletRequest requestModified
                = new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getRemoteUser() {
                return userName;
            }
        };
        filterChain.doFilter(requestModified, response);
    }

    @Override
    public String getParameter(final String name) {
        String[] strings = getParameterMap().get(name);
        if (strings != null) {
            return strings[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (allParameters == null) {
            allParameters = new TreeMap<String, String[]>();
            allParameters.putAll(super.getParameterMap());
            allParameters.putAll(modifiableParameters);
        }
        //Return an unmodifiable collection because we need to uphold the interface contract.
        return Collections.unmodifiableMap(allParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(final String name) {
         String[] get = getParameterMap().get(name);
        return get;
    }

}
