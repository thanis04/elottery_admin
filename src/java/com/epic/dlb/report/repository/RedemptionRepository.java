/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.WinningReport;
import com.epic.dlb.repository.CustomRepository;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Repository("redemptionRepository")
public class RedemptionRepository {

    @Autowired
    private CustomRepository customRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getPurchaseHistoryForUserClaimed(String fromDate, String toDate, String productcode, Integer statusCode) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT\n"
                + "winning_report.CREATED_DATE,\n"
                + "winning_report.DESCRIPTION,\n"
                + "winning_report.SERIAL_NO,\n"
                + "winning_report.DRAW_NO,\n"
                + "winning_report.DRAW_DATE,\n"
                + "winning_report.FIRST_NAME,\n"
                + "winning_report.LAST_NAME,\n"
                + "winning_report.NIC,\n"
                + "winning_report.WINNING_PRIZE,\n"
                + "winning_report.`STATUS`,\n"
                + "winning_report.STATUS_CODE,\n"
                + "winning_report.CREATED_TIME,\n"
                + "winning_report.PRODUCT_CODE,\n"
                + "winning_report.LAST_UPDATED,\n"
                + "winning_report.TICKET_ID, \n"
                + "winning_report.PURCHASE_HISTORY_ID, \n"
                + "winning_report.TXN_ID, \n"
                + "winning_report.REDEEM_MODE, \n"
                + "winning_report.REDEEM_TIME \n"
                + "FROM \n"
                + "winning_report \n"
                + "WHERE \n"
                + "winning_report.REDEEM_TIME BETWEEN '" + fromDate + "' AND '" + toDate
                + "' AND winning_report.WINNING_PRIZE> 0 ";

        String optionalWhere = "";
        if (productcode != null && !"0".equals(productcode)) {
            optionalWhere = " AND winning_report.PRODUCT_CODE ='" + productcode + "'";
        }

        if (statusCode != null && statusCode != 0) {
            optionalWhere = optionalWhere + " AND  winning_report.STATUS_CODE = '" + statusCode + "'";
        }

        String fullQuery = query + optionalWhere;

        Iterator<Object[]> list = customRepository.queryExecuter(fullQuery).iterator();
        List<WinningReport> winningReports = new ArrayList<WinningReport>();

        while (list.hasNext()) {
            Object[] next = list.next();

            WinningReport winningReport = new WinningReport();

            Timestamp createdDate = (Timestamp) next[0];
            Timestamp drawDate = (Timestamp) next[4];
            Timestamp lastUpdated = (Timestamp) next[13];
            Timestamp redemptionTime = null;
            if (next[18] != null) {
                redemptionTime = (Timestamp) next[18];
            }
            winningReport.setCreatedDate(dateFormat.format(createdDate));
            winningReport.setProductDescription((String) next[1]);
            winningReport.setSerialNo((String) next[2]);
            winningReport.setDrawNo((String) next[3]);
            winningReport.setDrawDate(dateFormat2.format(drawDate));
            winningReport.setFirstName((String) next[5]);
            winningReport.setLastName((String) next[6]);
            winningReport.setNic((String) next[7]);
            winningReport.setWinningAmount((String) next[8]);
            winningReport.setStatus((String) next[9]);
            winningReport.setStatuCode((Integer) next[10]);
            winningReport.setProductCode((String) next[1]);
            winningReport.setLastUpdated(dateFormat.format(lastUpdated));
            winningReport.setTicketId((Integer) next[14]);
            winningReport.setPurchaseHisId((Integer) next[15]);
            winningReport.setTxnId((String) next[16]);
            if (next[17] != null) {
                winningReport.setRedemptionMode((String) next[17]);
            } else {
                winningReport.setRedemptionMode("-");
            }
            if (next[18] != null) {
                winningReport.setRedemptionTime((String) dateFormat2.format(redemptionTime));
            } else {
                winningReport.setRedemptionTime("-");
            }
            winningReport.setTicketPrice("20.00");

            winningReports.add(winningReport);

        }

        return winningReports;
    }
}
