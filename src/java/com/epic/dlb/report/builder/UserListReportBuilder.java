/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.UserListReportDto;
import com.epic.dlb.report.service.UserListReportService;
import com.epic.dlb.util.common.Configuration;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Component("userListReportBuilder")
public class UserListReportBuilder {
    
    @Autowired
    private UserListReportService userListReportService;
    
    public JSONArray userReportData(String fromDate, String toDate) throws Exception {
        JSONArray table = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//        List<Integer> statusList = new ArrayList<>();
//        statusList.add(SystemVarList.USER_CLAIMED);
//        statusList.add(SystemVarList.TICKERT_PURCHASE);
//        statusList.add(SystemVarList.PRIZE_LESS_THAN_100000);
//        statusList.add(SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000);
//        statusList.add(SystemVarList.PRIZE_PAY_FILE_GENERATED);

        Iterator<UserListReportDto> list = userListReportService.getUserListByFromDateToDate(fromDate, toDate).iterator();

        while (list.hasNext()) {
            UserListReportDto next = list.next();

            JSONObject row = new JSONObject();

            row.put("name", next.getFirstName() + " " + next.getLastName());
            row.put("nic", next.getNic());
            row.put("brand", next.getBrand());
            row.put("os_version", next.getOsVersion());
            row.put("os_type", next.getOsType());
            row.put("mobile_no", next.getMobileNumber());
            row.put("email", next.getEmail());
            row.put("created_time", next.getCreateTime());
            row.put("last_login_date", next.getLastLoginTime());
            row.put("last_transaction_date", next.getLastTransactionTime());
            row.put("agent_code", next.getAgentCode());

            table.add(row);

        }
        return table;
    }

    @Transactional
    public File generateExcel(String fromDate, String toDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("#00.00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "user_report" + fromDate.replaceAll(":", "") + "_" + toDate.replaceAll(":", "") + sdf2.format(new Date()) + ".xls";

        System.out.print("tmpFilepath" + tmpFilepath);
        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("User report", 0);
        s.addCell(new Label(0, 0, "Name"));
        s.addCell(new Label(1, 0, "User_Name"));
        s.addCell(new Label(2, 0, "NIC"));
        s.addCell(new Label(3, 0, "Brand"));
        s.addCell(new Label(4, 0, "OS_Version"));
        s.addCell(new Label(5, 0, "OS_Type"));
        s.addCell(new Label(6, 0, "Mobile_Number"));
        s.addCell(new Label(7, 0, "Email"));
        s.addCell(new Label(8, 0, "Created_Time"));
        s.addCell(new Label(9, 0, "Last_Login_Date"));
        s.addCell(new Label(10, 0, "Last_Transaction_Date"));
        s.addCell(new Label(11, 0, "Agent Code"));

        Iterator<UserListReportDto> iterator = userListReportService
                .getUserListByFromDateToDate(fromDate, toDate).iterator();

        int genCount = 1;
        while (iterator.hasNext()) {

            UserListReportDto next = iterator.next();

            s.addCell(new Label(0, genCount, next.getFirstName() + " " + next.getLastName()));
            s.addCell(new Label(1, genCount, next.getUserName()));
            s.addCell(new Label(2, genCount, next.getNic()));
            s.addCell(new Label(3, genCount, next.getBrand()));
            s.addCell(new Label(4, genCount, next.getOsVersion()));
            s.addCell(new Label(5, genCount, next.getOsType()));
            s.addCell(new Label(6, genCount, next.getMobileNumber()));
            s.addCell(new Label(7, genCount, next.getEmail()));
            s.addCell(new Label(8, genCount, next.getCreateTime()));
            s.addCell(new Label(9, genCount, next.getLastLoginTime()));
            s.addCell(new Label(10, genCount, next.getLastTransactionTime()));
            s.addCell(new Label(11, genCount, next.getAgentCode()));

            genCount++;

        }

        w.write();
        w.close();

        return file;

    }
    
}
