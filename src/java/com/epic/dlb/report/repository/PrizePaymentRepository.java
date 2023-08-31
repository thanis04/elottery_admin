/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.repository.CustomRepository;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("prizePaymentRepository")
public class PrizePaymentRepository {

    @Autowired
    private CustomRepository customRepository;

    private SimpleDateFormat dateFormat;

    public List<Object[]> getPrizePaymentData(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        String queryStr = "SELECT "
                + "IF ( ( RH.TXN_ID = NULL ), '', SUBSTRING(CONCAT( RH.TXN_ID, \"_\", WL.NIC ),1,43) ) AS TRN_ID, "
                + "PH.SERIAL_NO AS SERIAL_NO, "
                + "IF ( ( PH.REDEEM_TIME = NULL ), '', ( PH.REDEEM_TIME ) ) AS RED_DATE, "
                + "PH.WINNING_PRIZE AS WINNING_PRIZE, "
                + "WL.NIC AS NIC, "
                + "PH.LAST_UPDATED AS GEN_DATE "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY AS PH "
                + "LEFT JOIN DLB_SWT_ST_REDEEM_HISTORY AS RH ON PH.ID = RH.TICKET_ID "
                + "INNER JOIN DLB_SWT_ST_WALLET AS WL ON PH.WALLET_ID = WL.ID "
                + "WHERE PH.`STATUS` = 60 "
                + "AND (RH.REDEEM_STATUS IS NULL OR RH.REDEEM_STATUS = 25) ";

        if (!(reportSearchCriteriaDto.getFromDate() == null || reportSearchCriteriaDto.getToDate() == null)) {
            queryStr = queryStr + "AND PH.LAST_UPDATED "
                    + "BETWEEN '" + reportSearchCriteriaDto.getFromDate() + "' AND '" + reportSearchCriteriaDto.getToDate() + "' ";
        }
        queryStr = queryStr + "ORDER BY PH.CREATED_DATE ASC ";
        
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
        return result;
    }

    public Integer getPrizePaymentDataCount(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        String queryStr = "SELECT COUNT(*) "
                + "FROM winning_report WR "
                + "LEFT JOIN DLB_SWT_ST_REDEEM_HISTORY RH ON WR.PURCHASE_HISTORY_ID = RH.TICKET_ID "
                + "WHERE WR.`STATUS_CODE` = 60 ";

        if (!(reportSearchCriteriaDto.getFromDate() == null || reportSearchCriteriaDto.getToDate() == null)) {
            queryStr = queryStr + "AND WR.LAST_UPDATED "
                    + "BETWEEN '" + reportSearchCriteriaDto.getFromDate() + "' AND '" + reportSearchCriteriaDto.getToDate() + "' ";
        }

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return Integer.parseInt(result.toString().replace("[", "").replace("]", ""));
    }
}
