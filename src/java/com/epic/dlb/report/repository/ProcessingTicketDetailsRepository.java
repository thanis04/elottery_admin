/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtMtTxnType;
import com.epic.dlb.repository.CustomRepository;
import com.epic.dlb.repository.GenericRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository
public class ProcessingTicketDetailsRepository {

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private GenericRepository genericRepository;

    public JSONArray getData(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        String queryStr = "SELECT "
                + "RH.TICKET_ID AS Redeemed_Ticket, "
                + "RH.TXN_ID, "
                + "PH.REDEEM_MODE, "
                + "TX.TXNTYPE, "
                + "PH.CREATED_DATE AS Purchase_Date, "
                + "WA.NIC, "
                + "PH.WINNING_PRIZE, "
                + "TX.RRN, "
                + "RH.RESPONSE_CODE, "
                + "RH.REDEEM_STATUS, "
                + "RH.CREATED_DATE AS Redeemed_Date "
                + "FROM "
                + "DLB_SWT_ST_REDEEM_HISTORY AS RH, "
                + "DLB_SWT_ST_PURCHASE_HISTORY AS PH, "
                + "DLB_SWT_ST_WALLET AS WA, "
                + "DLB_SWT_ST_TRANSACTION AS TX "
                + "WHERE "
                + "RH.REDEEM_STATUS = '26' "
                + "AND RH.TXN_ID = TX.TXNID "
                + "AND RH.TICKET_ID = PH.ID "
                + "AND PH.WALLET_ID = WA.ID "
                + "AND RH.CREATED_DATE BETWEEN '" + reportSearchCriteriaDto.getFromDate() + "' "
                + "AND '" + reportSearchCriteriaDto.getToDate() + "' ";

        if (reportSearchCriteriaDto.getMode().equals("REP")) {

            int limit = 0;
            int length = 10;

            if (reportSearchCriteriaDto.getLength() != null) {
                length = Integer.parseInt(reportSearchCriteriaDto.getLength());
            }

            if (!reportSearchCriteriaDto.getStart().equals("0")) {
                limit = Integer.parseInt(reportSearchCriteriaDto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        result.forEach(row -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("red_ticket_id", row[0] == null ? "-" : row[0].toString());
            jSONObject.put("txn_id", row[1] == null ? "-" : row[1].toString());
            jSONObject.put("red_mode", row[2] == null ? "-" : getRedeemMode(row[2].toString()));
            jSONObject.put("txn_type", row[3] == null ? "-" : getTxnType(row[3].toString()));
            jSONObject.put("created_date", row[4] == null ? "-" : row[4].toString().replace(".0", ""));
            jSONObject.put("nic", row[5] == null ? "-" : row[5].toString());
            jSONObject.put("winning_prize", row[6] == null ? "-" : row[6].toString());
            jSONObject.put("rrn", row[7] == null ? "-" : row[7].toString());
            jSONObject.put("response_code", row[8] == null ? "-" : row[8].toString());
            jSONObject.put("red_status", row[9] == null ? "-" : getStatus(row[9].toString()));
            jSONObject.put("red_date", row[10] == null ? "-" : row[10].toString().replace(".0", ""));
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }
    
    public JSONArray getDataSet2(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        JSONArray jSONArray = new JSONArray();
        String queryStr = "SELECT DISTINCT "
                + "RH.TXN_ID, "
                + "PH.ID AS NEW_TICKET_ID, "
                + "RH.CREATED_DATE "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY AS PH, "
                + "DLB_SWT_ST_REDEEM_HISTORY AS RH "
                + "WHERE "
                + "RH.REDEEM_STATUS = '26' "
                + "AND RH.TXN_ID = PH.TXN_ID "
                + "AND RH.CREATED_DATE BETWEEN '" + reportSearchCriteriaDto.getFromDate() + "' "
                + "AND '" + reportSearchCriteriaDto.getToDate() + "' ";

        if (reportSearchCriteriaDto.getMode().equals("REP")) {

            int limit = 0;
            int length = 10;

            if (reportSearchCriteriaDto.getLength() != null) {
                length = Integer.parseInt(reportSearchCriteriaDto.getLength());
            }

            if (!reportSearchCriteriaDto.getStart().equals("0")) {
                limit = Integer.parseInt(reportSearchCriteriaDto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        result.forEach(row -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("txn_id", row[0] == null ? "-" : row[0].toString());
            jSONObject.put("new_tck_id", row[1] == null ? "-" : row[1].toString());
            jSONObject.put("created_date", row[2] == null ? "-" : row[2].toString().replace(".0", ""));
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }

    public Integer getCount(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        String queryStr = "SELECT COUNT(*) AS ticket_count "
                + "FROM "
                + "DLB_SWT_ST_REDEEM_HISTORY AS RH, "
                + "DLB_SWT_ST_PURCHASE_HISTORY AS PH, "
                + "DLB_SWT_ST_WALLET AS WA, "
                + "DLB_SWT_ST_TRANSACTION AS TX "
                + "WHERE "
                + "RH.REDEEM_STATUS = '26' "
                + "AND RH.TXN_ID = TX.TXNID "
                + "AND RH.TICKET_ID = PH.ID "
                + "AND PH.WALLET_ID = WA.ID "
                + "AND RH.CREATED_DATE BETWEEN '" + reportSearchCriteriaDto.getFromDate() + " 00:00:00' "
                + "AND '" + reportSearchCriteriaDto.getToDate() + " 23:59:59' ";

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        Integer total = Integer.parseInt(result.toString().replace("[", "").replace("]", "").trim());

        return total;
    }

    public String getRedeemMode(String id) {
        try {
            String queryStr = "SELECT DESCRIPTION FROM DLB_REDEEM_MODE WHERE REDEEM_MODE = " + id + " ";
            List<Object[]> result = customRepository.queryExecuter(queryStr);
            return result.toString().replace("[", "").replace("]", "").trim();
        } catch (Exception ex) {
            Logger.getLogger(ProcessingTicketDetailsRepository.class.getName()).log(Level.SEVERE, null, ex);
            return "-";
        }
    }

    public String getStatus(String id) {
        DlbStatus dlbStatus = (DlbStatus) genericRepository.get(Integer.parseInt(id), DlbStatus.class);
        return dlbStatus.getDescription();
    }

    public String getTxnType(String id) {
        DlbSwtMtTxnType dlbSwtMtTxnType = (DlbSwtMtTxnType) genericRepository.get(
                Integer.parseInt(id), DlbSwtMtTxnType.class);
        return dlbSwtMtTxnType.getDescription();
    }
}
