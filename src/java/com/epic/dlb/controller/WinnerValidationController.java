/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import com.epic.dlb.model.DlbWbAudittrace;
import com.epic.dlb.service.UserService;
import com.epic.dlb.service.WinnerValidationService;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.epic.dlb.viewmodel.WinnerValidateSubViewVM;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.codec.binary.Base64;
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
 * @author yasitha_b
 */
@Controller
@RequestMapping("winnervalidation")
public class WinnerValidationController {

    @Autowired
    private UserService userService;

    @Autowired
    private WinnerValidationService winnerValidationService;

    @Autowired
    ServletContext context;

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            return "pages/winner_validation_main";
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
    }

    @RequestMapping(value = "/search.htm", method = RequestMethod.POST)
    @ResponseBody
    public String search(HttpSession session, @RequestParam("nic") String nic, @RequestParam("mobile") String mobile) {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        boolean status = false;
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        try {
            if (authStatus) {
                JSONArray searchResult = winnerValidationService.getWinnersData(nic, mobile);
                response.put("search_result", searchResult);
                status = true;
                response.put("status", status);
            } else {
                msg = (String) checkAuthorization.get(0);
            }
        } catch (Exception e) {
            status = false;
            Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response.toString();
    }

    @RequestMapping(value = "/view.htm", method = RequestMethod.POST)
    public String view(HttpSession session, Model model, @RequestParam("walletid") String walletid, @RequestParam("phid") String phid) throws Exception {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            try {
                WinnerValidateSubViewVM vm = winnerValidationService.getWalletIdRecord(walletid, phid);
                model.addAttribute("walletid", walletid);
                model.addAttribute("nic", vm.getNic());
                model.addAttribute("name", vm.getName());
                model.addAttribute("mobile", vm.getMobile());
                //model.addAttribute("userName",vm.getUserName());
                model.addAttribute("serialNumber", vm.getSerialNumber());
                model.addAttribute("winningPrize", vm.getWinnningPrize());
                model.addAttribute("lotteryNumbers", vm.getLotteryNumbers());
                model.addAttribute("nicFront", vm.getNicPage1());
                model.addAttribute("nicBack", vm.getNicPage2());
                model.addAttribute("phid", phid);//purchStatus
                model.addAttribute("purchStatus", vm.getPurchesHistoryStatus());//purchStatus
                model.addAttribute("lotteryName", vm.getLotteryName());//purchStatus
                String strHtmlTag = "";
                String strPaymentButtonTag = "";

                model.addAttribute("purchStatus", vm.getPurchesHistoryStatus());
                model.addAttribute("phstatus", strHtmlTag);
                model.addAttribute("strpaymentbuttontag", strPaymentButtonTag);
                model.addAttribute("status", vm.getPurchesHistoryStatus());

                model.addAttribute("profile", vm.getProfile());

                return "pages/winnervalidatesubview";
            } catch (Exception e) {
                Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
                return "common/error_page";
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;

        }
    }

    @RequestMapping(value = "/verify.htm", method = RequestMethod.POST)
    @ResponseBody
    public String verify(HttpSession session, Model model, @RequestParam("walletid") String walletid, @RequestParam("phid") String phid) throws IOException {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        boolean updatestatus = false;
        JSONObject response = new JSONObject();
        boolean status = false;
        String msg = MessageVarList.COMMON_ERR;
        try {
            if (authStatus) {
                updatestatus = winnerValidationService.getverified(walletid, phid);
                response.put("updatestatus", updatestatus);
                status = true;
            } else {
                msg = (String) checkAuthorization.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
            status = false;
        }
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.STATUS, status);
        return response.toString();
    }

    @RequestMapping(value = "/payment.htm", method = RequestMethod.POST)
    @ResponseBody
    public String updatePaymentState(HttpSession session, @RequestParam("phid") String phid) throws Exception {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        JSONObject response = new JSONObject();
        boolean status = false;
        String msg = MessageVarList.COMMON_ERR;
        try {
            if (authStatus) {
                winnerValidationService.updatePaymentState(phid);
                status = true;
            } else {
                msg = (String) checkAuthorization.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
            status = false;
        }
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.STATUS, status);
        return response.toString();
    }

    @RequestMapping(value = "/statuscheck.htm", method = RequestMethod.POST)
    @ResponseBody
    public String statuscheck(HttpSession session, Model model, @RequestParam("phid") String phid) {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        boolean status = false;
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        try {
            if (authStatus) {
                Integer phstatus = winnerValidationService.checkPurchesHistoryStatusById(phid);
                status = true;
                response.put("phstatus", phstatus.toString());
            } else {
                msg = (String) checkAuthorization.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
            status = false;
        }
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.STATUS, status);
        return response.toString();
    }

    @RequestMapping(value = "/print.htm", method = RequestMethod.GET)
    public String print(HttpSession session, Model model, HttpServletResponse response, @RequestParam("walletid") String walletid, @RequestParam("phid") String phid) {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);
        if (authStatus) {
            boolean status = false;
            String noImagePath = "resources/images/no_img_mid.jpg";
            try {
                String reportPath = "resources/reports/winnerValidation.jasper";
                HashMap params = new HashMap();
                //searchResult = cdcIssuanceReportService.buildSearchList(list);           
                params.put("logo", context.getRealPath("resources/common/images/dlb_logo.png"));

                WinnerValidateSubViewVM vm = winnerValidationService.getWalletIdRecord(walletid, phid);
                if (vm.getNicPage2() != null) {
                    params.put("nic", vm.getNic());
                } else {
                    params.put("nic", "-");
                }

                if (vm.getName() != null) {
                    params.put("name", vm.getName());
                } else {
                    params.put("name", "-");
                }

                if (vm.getLotteryName() != null) {
                    params.put("lottery", vm.getLotteryName());
                } else {
                    params.put("lottery", "-");
                }

                if (vm.getDay() != null) {
                    params.put("day", vm.getDay());
                } else {
                    params.put("day", "-");
                }

                if (vm.getLotteryTemplate() != null) {
                    URL url = new URL(vm.getLotteryTemplate());
                    BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
                    params.put("lottery_template", bis);

                } else {
                    File initialFile = new File(context.getRealPath(noImagePath));
                    InputStream targetStream = new FileInputStream(initialFile);
                    params.put("lottery_template", targetStream);
                }

                if (vm.getMobile() != null) {
                    params.put("mobile", vm.getMobile());
                } else {
                    params.put("mobile", "-");
                }

                //if(vm.getUserName() != null){params.put("username", vm.getUserName());  }
                //else{params.put("username","-"); }
                if (vm.getSerialNumber() != null) {
                    params.put("serialnumber", vm.getSerialNumber());
                } else {
                    params.put("serialnumber", "-");
                }

                if (vm.getLotteryNumbers() != null) {
                    params.put("lotterynumbers", vm.getLotteryNumbers());
                } else {
                    params.put("lotterynumbers", "-");
                }

                if (vm.getWinnningPrize() != null) {
                    params.put("winningprize", Double.parseDouble(vm.getWinnningPrize()));
                } else {
                    params.put("winningprize", "-");
                }

                if (vm.getDrawNo() != null) {
                    params.put("draw_no", vm.getDrawNo());
                } else {
                    params.put("draw_no", "-");
                }

                if (vm.getDrawNo() != null) {
                    params.put("draw_date", new SimpleDateFormat("yyyy-MM-dd").format(vm.getDrawDate()));
                } else {
                    params.put("draw_date", "-");
                }

                //get profile image
                URL url = null;
                if (vm.getProfile() != null && !vm.getProfile().isEmpty()) {
                    url = new URL(vm.getProfile());
                    BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
                    params.put("profile", bis);
                } else {
                    File initialFile = new File(context.getRealPath(noImagePath));
                    InputStream targetStream = new FileInputStream(initialFile);
                    params.put("profile", targetStream);

                }

                if (vm.getNicPage1() != null) {
                    String img = vm.getNicPage1();
                    InputStream stream = new ByteArrayInputStream(Base64.decodeBase64(img.getBytes()));
                    params.put("img", stream);
                } else {

                    File initialFile = new File(context.getRealPath(noImagePath));
                    InputStream targetStream = new FileInputStream(initialFile);
                    params.put("img", targetStream);
                }

                if (vm.getNicPage2() != null) {
                    String img2 = vm.getNicPage2();
                    InputStream stream2 = new ByteArrayInputStream(Base64.decodeBase64(img2.getBytes()));
                    params.put("imgback", stream2);
                } else {
                    File initialFile = new File(context.getRealPath(noImagePath));
                    InputStream targetStream = new FileInputStream(initialFile);
                    params.put("imgback", targetStream);
                }

                //generate QR
                String barcodeText = vm.getLotteryName() + ", " + vm.getSerialNumber() + ", " + vm.getLotteryNumbers() + ", " + vm.getWinnningPrize() + ", " + vm.getMobile() + ", " + vm.getNic();
                params.put("qr_code", barcodeText);

                InputStream jasperStream = new FileInputStream(context.getRealPath(reportPath));

                JRDataSource dataSource = new JREmptyDataSource();

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);
                //JasperExportManager.exportReportToPdfFile(jasperPrint,"D:\\reports\\report.pdf");

                JRPdfExporter exp = new JRPdfExporter();

                exp.setExporterInput(new SimpleExporterInput(jasperPrint));
                exp.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setMetadataTitle("winnerValidation.pdf");
                exp.setConfiguration(configuration);

                exp.exportReport();
            } catch (Exception e) {
                Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            //Authorization fail
            String msg = (String) checkAuthorization.get(0);
            model.addAttribute(SystemVarList.MESSAGE, msg);
            return SystemVarList.LOGIN_PAGE;
        }
        return null;
    }

    @RequestMapping(value = "/verify-otp.htm", method = RequestMethod.POST)
    @ResponseBody
    public String verifyOtp(HttpSession session, Model model, @RequestParam("walletid") Integer walletid,
            @RequestParam("otp") String otp,
            @RequestParam("pid") Integer pid) throws IOException {
        List checkAuthorization = userService.checkAuthorization(session, "/winnervalidation/show_page.htm");
        boolean authStatus = (boolean) checkAuthorization.get(1);

        boolean updatestatus = false;
        JSONObject response = new JSONObject();
        boolean status = false;
        String msg = MessageVarList.COMMON_ERR;
        try {
            if (authStatus) {
                updatestatus = winnerValidationService.checkOtp(walletid, otp, pid);
                response.put("updatestatus", updatestatus);
                status = true;
                msg = MessageVarList.ADD_SUC;
            } else {
                msg = (String) checkAuthorization.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(WinnerValidationController.class.getName()).log(Level.SEVERE, null, e);
            status = false;
        }
        response.put(SystemVarList.MESSAGE, msg);
        response.put(SystemVarList.STATUS, status);
        return response.toString();
    }
}
