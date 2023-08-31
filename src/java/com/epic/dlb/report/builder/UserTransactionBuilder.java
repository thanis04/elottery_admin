/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.ReconciliationPaginatedPageData;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.UserTransactionSearchDTO;
import com.epic.dlb.repository.UserTransactionRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
 * @author nipuna_k
 */
@Component("userTransactionBuilder")
public class UserTransactionBuilder {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Transactional(rollbackFor = Exception.class)
    public JSONObject getResponseData(HttpServletRequest request) throws Exception {
        JSONObject response = new JSONObject();
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String length = request.getParameter("length");
        String nic = request.getParameter("nic");
        String mobileNo = request.getParameter("mobileNo");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        UserTransactionSearchDTO dto = new UserTransactionSearchDTO();
        dto.setStart(start);
        dto.setEnd(end);
        dto.setLength(length);
        dto.setMode("REP");
        dto.setNic(nic);
        dto.setMobileNo(mobileNo);
        dto.setFromDate(fromDate);
        dto.setToDate(toDate);
        UserTransactionSearchDTO userTransactionSearchDTO = userTransactionRepository.getData(dto);
        JSONArray data = getBuildedData(userTransactionSearchDTO);

        response.put("search_result", data);
        response.put("data", data);
        response.put("recordsTotal", userTransactionSearchDTO.getCount());
        response.put("recordsFiltered", userTransactionSearchDTO.getCount());
        response.put("msg", "Fetching Successfull");
        response.put("status", SystemVarList.SUCCESS);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getBuildedData(UserTransactionSearchDTO userTransactionSearchDTO) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = userTransactionSearchDTO.getList();
        list.forEach(object -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("cus_id", object[0] == null ? "-" : object[0].toString());
            jSONObject.put("cus_name", object[1] == null ? "-" : object[1].toString());
            jSONObject.put("tra_type", object[2] == null ? "-" : object[2].toString());
            jSONObject.put("tra_det", object[3] == null ? "-" : object[3].toString());
            jSONObject.put("tra_amount", object[4] == null ? "-" : object[4].toString().replace(".0", ".00"));
            jSONArray.add(jSONObject);
        });

        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public File getGenerateUserTransactionReportExcel(UserTransactionSearchDTO userTransactionSearchDTO) throws Exception {
        JSONArray jSONArray = new JSONArray();
        userTransactionSearchDTO = userTransactionRepository.getData(userTransactionSearchDTO);
        List<Object[]> list = userTransactionSearchDTO.getList();

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "User Transaction Report From - " + userTransactionSearchDTO.getFromDate().replace("00:00:00", "")
                + " To - " + userTransactionSearchDTO.getToDate().replace("23:59:59", "") + " "
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Agent Finance Report", 0);

        s.addCell(new Label(0, 0, "Customer ID"));
        s.addCell(new Label(1, 0, "Customer Name"));
        s.addCell(new Label(2, 0, "Transaction Type (CASA/CC,DB/WON TICKET)"));
        s.addCell(new Label(3, 0, "Transaction Details (Purchase/Redeem)"));
        s.addCell(new Label(4, 0, "Transaction Amount"));

        int genCount = 1;
        for (Object[] obj : list) {
            s.addCell(new Label(0, genCount, obj[0] == null ? "-" : obj[0].toString()));
            s.addCell(new Label(1, genCount, obj[1] == null ? "-" : obj[1].toString()));
            s.addCell(new Label(2, genCount, obj[2] == null ? "-" : obj[2].toString()));
            s.addCell(new Label(3, genCount, obj[3] == null ? "-" : obj[3].toString()));
            s.addCell(new Label(4, genCount, obj[4] == null ? "-" : obj[4].toString().replace(".0", ".00")));
            genCount++;
        }
        w.write();
        w.close();
        return file;
    }
}
