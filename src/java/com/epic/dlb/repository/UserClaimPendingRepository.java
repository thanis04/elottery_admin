/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("userClaimPendingRepository")
public class UserClaimPendingRepository {

    @Autowired
    private CustomRepository customRepository;

    public List<Object[]> getRedeemHistoryDetailsByPurchaseId(Integer id) throws Exception {
        String queryStr = "SELECT "
                + "PH.SERIAL_NO, "
                + "PH.ID, "
                + "RH.TXN_ID "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY PH "
                + "INNER JOIN DLB_SWT_ST_REDEEM_HISTORY RH ON PH.ID = RH.TICKET_ID "
                + "WHERE "
                + "PH.`STATUS` = \"26\" "
                + "AND PH.ID = " + id + " "
                + "ORDER BY RH.CREATED_DATE DESC";
       
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result;
    }

    public List<Object[]> getRedeemHistoryDetailsByPurchaseIdAndTxnId(Integer id, String txnId) throws Exception {
        String queryStr = "SELECT "
                + "PH.SERIAL_NO, "
                + "PH.ID, "
                + "RH.TXN_ID "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY PH "
                + "INNER JOIN DLB_SWT_ST_REDEEM_HISTORY RH ON PH.ID = RH.TICKET_ID "
                + "WHERE "
                + "PH.`STATUS` = \"26\" "
                + "AND PH.ID = " + id + " "
                + "AND RH.TXN_ID = '" + txnId + "' ";

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result;
    }

    public List<Object[]> getRedeemHistoryDetailsByTxnId(String id) throws Exception {
        String queryStr = "SELECT "
                + "RH.TXN_ID, "
                + "RH.TICKET_ID, "
                + "RH.CREATED_DATE "
                + "FROM "
                + "`DLB_SWT_ST_REDEEM_HISTORY` RH "
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY PH ON RH.TICKET_ID = PH.ID "
                + "WHERE "
                + "RH.`TXN_ID` = '" + id + "' "
                + "AND PH.`STATUS` = '26' "
                + "ORDER BY PH.ID ";

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result;
    }
}
