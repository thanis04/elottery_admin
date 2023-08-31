/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.controller;

import com.epic.dlb.report.controller_finance.DailyTicketSalesReportController;
import com.epic.dlb.service.ProductService;
import com.epic.dlb.service.UserService;
import com.epic.dlb.util.common.SystemVarList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nipuna_k
 */

@Controller
@RequestMapping("brand_w_prize")
public class BrandWisePrizeReportController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {
        List checkAuthorization = userService.checkAuthorization(session, "/brand_w_prize/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        if (authStatus) {
            try {
                List productSelectBox = productService.loadSelectBox(SystemVarList.ACTIVE);
                model.addAttribute("product_select_box", productSelectBox);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DailyTicketSalesReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "pages/rpt_brand_wise_prize";
        } else {
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }
}
