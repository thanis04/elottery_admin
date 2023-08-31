/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.DrawWiseSalesInsightDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.service.PrizePaymentService;
import com.epic.dlb.util.common.Configuration;
import java.io.File;
import java.util.List;
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
@Component("prizePaymentBuilder")
public class PrizePaymentBuilder {

    @Autowired
    private PrizePaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getPrizePaymentData(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = paymentService.getPrizePaymentData(reportSearchCriteriaDto);
        list.forEach(object -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("txn_nic", object[0] == null ? "-" : object[0].toString());
            jSONObject.put("serial_no", object[1] == null ? "-" : object[1].toString());
            jSONObject.put("red_date", object[2] == null ? "-" : object[2].toString().replace(".0", ""));
            jSONObject.put("prize_val", object[3] == null ? "-" : object[3].toString());
            jSONObject.put("cus_nic", object[4] == null ? "-" : object[4].toString());
            jSONObject.put("prize_pay_date", object[5] == null ? "-" : object[5].toString().replace(".0", ""));
            jSONArray.add(jSONObject);
        });

        return jSONArray;
    }

    @Transactional
    public File getGeneratePrizePaymentExcel(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = paymentService.getPrizePaymentData(reportSearchCriteriaDto);

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Prize Payment Report From - " + reportSearchCriteriaDto.getFromDate().replace("00:00:00", "")
                + " To - " + reportSearchCriteriaDto.getToDate().replace("23:59:59", "")+ " "
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Prize Payment Report", 0);

        s.addCell(new Label(0, 0, "Transaction ID"));
        s.addCell(new Label(1, 0, "Serial Number"));
        s.addCell(new Label(2, 0, "Redemption Date"));
        s.addCell(new Label(3, 0, "Prize value (Rs.)"));
        s.addCell(new Label(4, 0, "Customer NIC"));
        s.addCell(new Label(5, 0, "Prize Payment File Generated Date"));
        
        int genCount = 1;
        for (Object[] obj : list) {
            s.addCell(new Label(0, genCount, obj[0] == null ? "-" : obj[0].toString()));
            s.addCell(new Label(1, genCount, obj[1] == null ? "-" : obj[1].toString()));
            s.addCell(new Label(2, genCount, obj[2] == null ? "-" : obj[2].toString().replace(".0", "")));
            s.addCell(new Label(3, genCount, obj[3] == null ? "-" : obj[3].toString()));
            s.addCell(new Label(4, genCount, obj[4] == null ? "-" : obj[4].toString()));
            s.addCell(new Label(5, genCount, obj[5] == null ? "-" : obj[5].toString().replace(".0", "")));
            genCount++;
        }
        w.write();
        w.close();
        return file;
    }
}
