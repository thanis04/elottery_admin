/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.builder.WiningFileBuilder;
import com.epic.dlb.dto.WinningReport;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.report.service.CoomonService;
import com.epic.dlb.report.service.RedemptionService;
import com.epic.dlb.report.service.WinningFileReportService;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
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
@Component("redemptionAndPendingRedemptionBuilder")
public class RedemptionAndPendingRedemptionBuilder {

    @Autowired
    private WinningFileReportService winningFileReportService;
    
    @Autowired
    private RedemptionService redemptionService;

    @Autowired
    private GenericRepository genericRepository;

    public JSONArray redemptionAndPendingRedemptionReportData(String fromDate, String toDate, String lottery) throws Exception {
        JSONArray table = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        List<Integer> statusList = new ArrayList<>();
        statusList.add(SystemVarList.USER_CLAIMED);
        statusList.add(SystemVarList.TICKERT_PURCHASE);
        statusList.add(SystemVarList.PRIZE_LESS_THAN_100000);
        statusList.add(SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000);
        statusList.add(SystemVarList.PRIZE_PAY_FILE_GENERATED);

        Iterator<WinningReport> list = redemptionService
                .getPurchaseHistoryForUserClaimed(fromDate, toDate, lottery, 0).iterator();

        while (list.hasNext()) {
            WinningReport next = list.next();

            JSONObject row = new JSONObject();

            row.put("lottery_name", next.getProductDescription());
            row.put("draw_no", next.getDrawNo());
            row.put("draw_date", next.getDrawDate());
            row.put("tkt_reff", next.getSerialNo());
            row.put("cus_reff", next.getFirstName() + " " + next.getLastName() + ", NIC: " + next.getNic());
            row.put("tkt_value", "20");
            row.put("currency", "LKR");
            row.put("ticket_pur_date", next.getCreatedDate());
            row.put("winning_amt", next.getWinningAmount());
            row.put("mode_redemption", next.getRedemptionMode());
            row.put("status", next.getStatuCode() == SystemVarList.USER_CLAIMED ? "Redemption Pending " : "Redemption Success");
            row.put("date_redemption", next.getRedemptionTime());
            row.put("tkt_id", next.getTicketId());
            row.put("last_updated", next.getLastUpdated());
            row.put("pur_his_id", next.getPurchaseHisId());
            row.put("txn_id", next.getTxnId());
            row.put("red_mode", next.getRedemptionMode());
            row.put("red_time", next.getRedemptionTime());
            table.add(row);

        }

        return table;
    }

    @Transactional
    public File generateExcel(String fromDate, String toDate, String lottery) throws Exception {
        File file = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "redemption_pending_report" + fromDate.replaceAll(":", "") + "_" + toDate.replaceAll(":", "") + sdf2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Redemption pending report", 0);

            s.addCell(new Label(0, 0, "Lottery"));
            s.addCell(new Label(1, 0, "Draw No"));
            s.addCell(new Label(2, 0, "Draw Date"));
            s.addCell(new Label(3, 0, "Ticket Reference No"));
            s.addCell(new Label(4, 0, "Ticket ID"));
            s.addCell(new Label(5, 0, "Customer Reference"));
            s.addCell(new Label(6, 0, "Ticket Value"));
            s.addCell(new Label(7, 0, "Currency"));
            s.addCell(new Label(8, 0, "Purchase Date"));
            s.addCell(new Label(9, 0, "Winning prize"));
            s.addCell(new Label(10, 0, "Mode of Redemption"));
            s.addCell(new Label(11, 0, "Redemption Status"));
            s.addCell(new Label(12, 0, "Redemption Date"));
            s.addCell(new Label(13, 0, "Last Updated Date"));
            s.addCell(new Label(14, 0, "Purchase History ID"));
            s.addCell(new Label(15, 0, "Transaction ID"));

            Iterator<WinningReport> iterator = redemptionService
                    .getPurchaseHistoryForUserClaimed(fromDate, toDate, lottery, 0).iterator();
            int genCount = 1;
            while (iterator.hasNext()) {

                WinningReport next = iterator.next();

                s.addCell(new Label(0, genCount, next.getProductDescription()));
                s.addCell(new Label(1, genCount, next.getDrawNo()));
                s.addCell(new Label(2, genCount, next.getDrawDate()));
                s.addCell(new Label(3, genCount, next.getSerialNo()));
                s.addCell(new Label(4, genCount, next.getTicketId().toString()));
                s.addCell(new Label(5, genCount, next.getFirstName() + " " + next.getLastName() + ", NIC: " + next.getNic()));
                s.addCell(new Label(6, genCount, "20"));
                s.addCell(new Label(7, genCount, "LKR"));
                s.addCell(new Label(8, genCount, next.getCreatedDate()));
                s.addCell(new Label(9, genCount, next.getWinningAmount()));
                s.addCell(new Label(10, genCount, next.getRedemptionMode()));
                s.addCell(new Label(11, genCount, next.getStatuCode() == SystemVarList.USER_CLAIMED ? "Redemption Pending " : "Redemption Success"));
                s.addCell(new Label(12, genCount, next.getRedemptionTime()));
                s.addCell(new Label(13, genCount, next.getLastUpdated()));
                s.addCell(new Label(14, genCount, next.getPurchaseHisId().toString()));
                s.addCell(new Label(15, genCount, next.getTxnId()));
                genCount++;

            }

            w.write();
            w.close();

            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CoomonService.deleteTmpFile(file.getName());
        }

    }
}
