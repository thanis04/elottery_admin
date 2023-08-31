/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.ReconciliationReportDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.util.common.Configuration;
import java.io.File;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Component("processingTicketDetailsBuilder")
public class ProcessingTicketDetailsBuilder {

    @Transactional
    public File generateExcel(JSONArray jSONArray1, JSONArray jSONArray2,
            ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Process Ticket Details Report " + reportSearchCriteriaDto.getFromDate().replaceAll("00:00:00", "")
                + "- " + reportSearchCriteriaDto.getToDate().replaceAll("23:59:59", "")
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Process Ticket Details Report", 0);

        s.addCell(new Label(0, 0, "Redeem Ticket ID"));
        s.addCell(new Label(1, 0, "Transaction ID"));
        s.addCell(new Label(2, 0, "Redemption mode"));
        s.addCell(new Label(3, 0, "Transaction type"));
        s.addCell(new Label(4, 0, "Created date"));
        s.addCell(new Label(5, 0, "NIC"));
        s.addCell(new Label(6, 0, "Winning prize"));
        s.addCell(new Label(7, 0, "RRN"));
        s.addCell(new Label(8, 0, "Response Code"));
        s.addCell(new Label(9, 0, "Redeem Status"));
        s.addCell(new Label(10, 0, "Redeem Date"));

        int genCount = 1;
        for (int i = 0; i < jSONArray1.size(); i++) {
            JSONObject objects = (JSONObject) jSONArray1.get(i);
            s.addCell(new Label(0, genCount, (String) objects.get("red_ticket_id")));
            s.addCell(new Label(1, genCount, (String) objects.get("txn_id")));
            s.addCell(new Label(2, genCount, (String) objects.get("red_mode")));
            s.addCell(new Label(3, genCount, (String) objects.get("txn_type")));
            s.addCell(new Label(4, genCount, (String) objects.get("created_date")));
            s.addCell(new Label(5, genCount, (String) objects.get("nic")));
            s.addCell(new Label(6, genCount, (String) objects.get("winning_prize")));
            s.addCell(new Label(7, genCount, (String) objects.get("rrn")));
            s.addCell(new Label(8, genCount, (String) objects.get("response_code")));
            s.addCell(new Label(9, genCount, (String) objects.get("red_status")));
            s.addCell(new Label(10, genCount, (String) objects.get("red_date")));
            genCount++;

        }

        WritableSheet s1 = w.createSheet("Report-2", 1);

        s1.addCell(new Label(0, 0, "Transaction ID"));
        s1.addCell(new Label(1, 0, "New Ticket Id"));
        s1.addCell(new Label(2, 0, "Created Date"));

        int genCount1 = 1;
        for (int i = 0; i < jSONArray2.size(); i++) {
            JSONObject objects = (JSONObject) jSONArray2.get(i);
            s1.addCell(new Label(0, genCount1, (String) objects.get("txn_id")));
            s1.addCell(new Label(1, genCount1, (String) objects.get("new_tck_id")));
            s1.addCell(new Label(2, genCount1, (String) objects.get("created_date")));
            genCount1++;

        }

        w.write();
        w.close();

        return file;

    }

}
