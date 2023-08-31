/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import com.epic.dlb.dto.UserTransactionSearchDTO;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("userTransactionRepository")
public class UserTransactionRepository {

    @Autowired
    private CustomRepository customRepository;

    public UserTransactionSearchDTO getData(UserTransactionSearchDTO userTransactionSearchDTO) throws Exception {
        userTransactionSearchDTO.setList(getList(userTransactionSearchDTO));
        userTransactionSearchDTO.setCount(getCount(userTransactionSearchDTO));
        return userTransactionSearchDTO;
    }

    public List<Object[]> getList(UserTransactionSearchDTO userTransactionSearchDTO) throws Exception {
        String queryStr = "SELECT "
                + "UTV.WALLET_ID, "
                + "UTV.CUS_NAME, "
                + "UTV.PAY_METHOD, "
                + "UTV.TXN_TYPE, "
                + "UTV.AMOUNT "
                + "FROM USER_TRANSACTION_VIEW UTV "
                + "WHERE ";

        if (!userTransactionSearchDTO.getNic().equals("-")) {
            queryStr = queryStr + "UTV.NIC = '" + userTransactionSearchDTO.getNic() + "' ";
        }

        if (userTransactionSearchDTO.getNic().equals("-")) {
            if (!userTransactionSearchDTO.getNic().equals("-")) {
                queryStr = queryStr + "AND ";
            }
            queryStr = queryStr + "UTV.MOBILE_NO = '" + userTransactionSearchDTO.getMobileNo() + "' ";
        }

        if (!(userTransactionSearchDTO.getFromDate().equals("-") && userTransactionSearchDTO.getToDate().equals("-"))) {
            queryStr = queryStr + "AND UTV.DATE_TIME "
                    + "BETWEEN '" + userTransactionSearchDTO.getFromDate() + " 00:00:00" + "' AND '" + userTransactionSearchDTO.getToDate() + " 23:59:59" + "' ";
        }

        if (userTransactionSearchDTO.getMode().equals("REP")) {
            int limit = 0;
            int length = 10;

            if (userTransactionSearchDTO.getLength() != null) {
                length = Integer.parseInt(userTransactionSearchDTO.getLength());
            }

            if (!userTransactionSearchDTO.getStart().equals("0")) {
                limit = Integer.parseInt(userTransactionSearchDTO.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }
        List<Object[]> resultList = customRepository.queryExecuter(queryStr);
        return resultList;
    }

    public Integer getCount(UserTransactionSearchDTO userTransactionSearchDTO) throws Exception {
        String queryStr = "SELECT COUNT(*) "
                + "FROM USER_TRANSACTION_VIEW UTV "
                + "WHERE ";

        if (!userTransactionSearchDTO.getNic().equals("-")) {
            queryStr = queryStr + "UTV.NIC = '" + userTransactionSearchDTO.getNic() + "' ";
        }

        if (userTransactionSearchDTO.getNic().equals("-")) {
            if (!userTransactionSearchDTO.getNic().equals("-")) {
                queryStr = queryStr + "AND ";
            }
            queryStr = queryStr + "UTV.MOBILE_NO = '" + userTransactionSearchDTO.getMobileNo() + "' ";
        }

        if (!(userTransactionSearchDTO.getFromDate().equals("-") && userTransactionSearchDTO.getToDate().equals("-"))) {
            queryStr = queryStr + "AND UTV.DATE_TIME "
                    + "BETWEEN '" + userTransactionSearchDTO.getFromDate() + " 00:00:00" + "' AND '" + userTransactionSearchDTO.getToDate() + " 23:59:59" + "' ";
        }

        List<Object[]> resultList = customRepository.queryExecuter(queryStr);
        return Integer.parseInt(resultList.toString().replace("[", "").replace("]", "").trim());
    }
}
