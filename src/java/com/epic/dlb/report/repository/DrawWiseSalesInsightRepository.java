/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.DrawWiseSalesInsightDto;
import com.epic.dlb.repository.CustomRepository;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository
public class DrawWiseSalesInsightRepository {

    @Autowired
    private CustomRepository customRepository;

    public List<Object[]> getInsight(DrawWiseSalesInsightDto dto) throws Exception {

        String condition = "IN";
        if (dto.getReportType().equals("2")) {
            condition = "NOT IN";
        }

        String queryStr = "SELECT DISTINCT "
                + "CONCAT ( W.FIRST_NAME, \" \", W.LAST_NAME ) AS CUS_NAME, "
                + "W.MOBILE_NO AS MOBILE_NO, "
                + "W.ID AS WALLET_ID "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY PH "
                + "RIGHT JOIN DLB_SWT_ST_WALLET W ON PH.WALLET_ID = W.ID "
                + "WHERE "
                + "W.ID " + condition + " "
                + "( SELECT PH.WALLET_ID FROM DLB_SWT_ST_PURCHASE_HISTORY PH WHERE "
                + "(PH.DRAW_NO = '" + dto.getDrawNumber() + "' "
                + "AND PH.PRODUCT_CODE = '" + dto.getGameType() + "') ) ";

        if (dto.getMode().equals("REP")) {

            int limit = 0;
            int length = 10;

            if (dto.getLength() != null) {
                length = Integer.parseInt(dto.getLength());
            }

            if (!dto.getStart().equals("0")) {
                limit = Integer.parseInt(dto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }

        List<Object[]> result = customRepository.queryExecuter(queryStr);

        return result;
    }

    public Integer getInsightCount(DrawWiseSalesInsightDto dto) throws Exception {
        
        String condition = "IN";
        if (dto.getReportType().equals("2")) {
            condition = "NOT IN";
        }

        String queryStr = "SELECT COUNT(DISTINCT W.ID) "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY PH "
                + "RIGHT JOIN DLB_SWT_ST_WALLET W ON PH.WALLET_ID = W.ID "
                + "WHERE "
                + "W.ID " + condition + " "
                + "( SELECT PH.WALLET_ID FROM DLB_SWT_ST_PURCHASE_HISTORY PH WHERE "
                + "(PH.DRAW_NO = '" + dto.getDrawNumber() + "' "
                + "AND PH.PRODUCT_CODE = '" + dto.getGameType() + "') ) ";
        
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        
        Integer count = Integer.parseInt(result.toString().replace("[", "").replace("]", ""));
        return count;
    }

}
