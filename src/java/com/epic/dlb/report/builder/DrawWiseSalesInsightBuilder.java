/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.DrawWiseSalesInsightDto;
import com.epic.dlb.report.service.DrawWiseSalesInsightService;
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
@Component("drawWiseSalesInsightBuilder")
public class DrawWiseSalesInsightBuilder {

    @Autowired
    private DrawWiseSalesInsightService drawWiseSalesInsightService;

    @Transactional
    public JSONArray getSalesInsight(DrawWiseSalesInsightDto drawWiseSalesInsightDto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = drawWiseSalesInsightService.getInsight(drawWiseSalesInsightDto);
        list.forEach(object -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", object[0] == null ? "-" : object[0].toString());
            jSONObject.put("mobile_no", object[1] == null ? "-" : object[1].toString());
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }

    @Transactional
    public File getGenerateSalesInsightExcel(DrawWiseSalesInsightDto drawWiseSalesInsightDto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        List<Object[]> list = drawWiseSalesInsightService.getInsight(drawWiseSalesInsightDto);

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Draw Wise Sales Insight Report Draw No - " + drawWiseSalesInsightDto.getDrawNumber()
                + " Lottery Type - " + drawWiseSalesInsightDto.getGameType()
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Draw Wise Sales Insight Report", 0);

        s.addCell(new Label(0, 0, "Customer Name"));
        s.addCell(new Label(1, 0, "Contact Number"));
        int genCount = 1;
        for (Object[] obj : list) {
            s.addCell(new Label(0, genCount, obj[0] == null ? "-" : obj[0].toString()));
            s.addCell(new Label(1, genCount, obj[1] == null ? "-" : obj[1].toString()));
            genCount++;
        }
        w.write();
        w.close();
        return file;
    }
}
