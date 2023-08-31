/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.model.AudittrailView;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.report.service.CoomonService;
import com.epic.dlb.report.service.WinningFileReportService;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
@Component("audittrailBuilder")
public class AudittrailBuilder {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public JSONArray getAudittrail(String fromDate, String toDate) throws Exception {
        JSONArray table = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        WhereStatement whereStatement1 = new WhereStatement("date", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement whereStatement2 = new WhereStatement("date", toDate, SystemVarList.LESS_THAN);

        List<WhereStatement> whereStatements = new ArrayList<>();
        whereStatements.add(whereStatement1);
        whereStatements.add(whereStatement2);

        Iterator<AudittrailView> list = genericRepository.listWithQuery(AudittrailView.class, whereStatements).iterator();

        while (list.hasNext()) {
            AudittrailView next = list.next();

            JSONObject row = new JSONObject();

            row.put("date", next.getDate());
            row.put("user", next.getFirstname() + " " + next.getLastname() + ", NIC:" + next.getNic() + ", Mobile:" + next.getMobile());
            row.put("tid", next.getTid());
            row.put("type", next.getType());
            row.put("status", next.getStatus());

            table.add(row);

        }

        return table;
    }

    @Transactional
    public File generateExcel(String fromDate, String toDate) throws Exception {

        File file = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-ddhhmmss");

            WhereStatement whereStatement1 = new WhereStatement("date", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
            WhereStatement whereStatement2 = new WhereStatement("date", toDate, SystemVarList.LESS_THAN);

            List<WhereStatement> whereStatements = new ArrayList<>();
            whereStatements.add(whereStatement1);
            whereStatements.add(whereStatement2);

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "auditt_rail_report" + fromDate.replaceAll(":", "") + "_" + toDate.replaceAll(":", "") + dateFormat2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Auditt report", 0);

            s.addCell(new Label(0, 0, "Date"));
            s.addCell(new Label(1, 0, "Transaction ID"));
            s.addCell(new Label(2, 0, "User"));
            s.addCell(new Label(3, 0, "Transaction Type"));
            s.addCell(new Label(4, 0, "Status"));

            Iterator<AudittrailView> iterator = genericRepository.listWithQuery(AudittrailView.class, whereStatements).iterator();

            int genCount = 1;
            while (iterator.hasNext()) {

                AudittrailView next = iterator.next();

                s.addCell(new Label(0, genCount, next.getDate()));
                s.addCell(new Label(1, genCount, next.getTid()));
                s.addCell(new Label(2, genCount, next.getFirstname() + " " + next.getLastname() + ", NIC:" + next.getNic() + ", Mobile:" + next.getMobile()));
                s.addCell(new Label(3, genCount, next.getType()));
                s.addCell(new Label(4, genCount, next.getStatus()));

                genCount++;

            }

            w.write();
            w.close();

            return file;

        } catch (Exception e) {
            return null;
        } finally {
            CoomonService.deleteTmpFile(file.getName());
        }

    }
}
