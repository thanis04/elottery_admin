/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kasun_n
 */
@Controller
public class ErrorController {

    @RequestMapping(value = "errors.htm", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("common/error_page");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Error! We track these errors automatically, but if the problem persists feel free to contact us. In the meantime, try refreshing ";
                break;
            }
            case 401: {
                errorMsg = "Access denied! Full authentication is required to access this resource";
                break;
            }
            case 404: {
                errorMsg = "Sorry! We couldn't find this page";
                break;
            }
            case 500: {
                errorMsg = "Internal Server Error ! We track these errors automatically, but if the problem persists feel free to contact us. In the meantime, try refreshing";

                break;
            }
        }
        errorPage.addObject("error_msg", errorMsg);
        errorPage.addObject("error_code", httpErrorCode);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}
