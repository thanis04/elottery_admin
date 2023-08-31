/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.ReconciliationPaginatedPageData;
import com.epic.dlb.dto.ReconciliationReportDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.repository.GenericRepository;
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
@Component("reconciliationReportBuilder")
public class ReconciliationReportBuilder {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public JSONArray reconcilationReportData(List<ReconciliationReportDto> reconciliationReportDto) {
        JSONArray table = new JSONArray();

        reconciliationReportDto.forEach(dto -> {
            JSONObject row = new JSONObject();
            row.put("lotteryType", dto.getLotteryType());
            row.put("drawNo", dto.getDrawNo());
            row.put("drawDate", dto.getDrawDate());
            row.put("redemptionDate", dto.getRedemptionDate());
            row.put("redemptionMode", dto.getRedemptionMode());
            row.put("referenceID", dto.getReferenceID());
            row.put("winningPrize", dto.getWinningPrize());
            row.put("ticketReference", dto.getTicketReference());
            row.put("ticketID", dto.getTicketID());
            row.put("rrn", dto.getRrn());
            row.put("pur_id", dto.getPurchaseHis());
            table.add(row);
        });

        return table;
    }

    @Transactional
    public File reconcilationGenerateExcel(List<ReconciliationReportDto> list,
            ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {

        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "Reconcilation Report " + reportSearchCriteriaDto.getFromDate().replaceAll("00:00:00", "")
                + "- " + reportSearchCriteriaDto.getToDate().replaceAll("23:59:59", "")
                + ".xls";

        File file = new File(tmpFilepath);

        WritableWorkbook w = Workbook.createWorkbook(file);
        WritableSheet s = w.createSheet("Reconcilation report", 0);

        s.addCell(new Label(0, 0, "Lottery Name"));
        s.addCell(new Label(1, 0, "Draw Number"));
        s.addCell(new Label(2, 0, "Draw Date"));
        s.addCell(new Label(3, 0, "Redemption Date"));
        s.addCell(new Label(4, 0, "Redemption Mode"));
        s.addCell(new Label(5, 0, "Reference ID"));
        s.addCell(new Label(6, 0, "Winning Prize"));
        s.addCell(new Label(7, 0, "Ticket Reference"));
        s.addCell(new Label(8, 0, "Purchase History ID"));
        s.addCell(new Label(9, 0, "RRN"));

        int genCount = 1;
        for (ReconciliationReportDto dto : list) {

            s.addCell(new Label(0, genCount, dto.getLotteryType()));
            s.addCell(new Label(1, genCount, dto.getDrawNo()));
            s.addCell(new Label(2, genCount, dto.getDrawDate()));
            s.addCell(new Label(3, genCount, dto.getRedemptionDate()));
            s.addCell(new Label(4, genCount, dto.getRedemptionMode()));
            s.addCell(new Label(5, genCount, dto.getReferenceID()));
            s.addCell(new Label(6, genCount, dto.getWinningPrize()));
            s.addCell(new Label(7, genCount, dto.getTicketReference()));
            s.addCell(new Label(8, genCount, dto.getPurchaseHis()));
            s.addCell(new Label(9, genCount, dto.getRrn()));
            genCount++;

        }

        w.write();
        w.close();

        return file;

    }
}
