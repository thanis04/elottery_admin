/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("salesFileRepository")
public class SalesFileRepository {

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private JDBCConnection jDBCConnection;

    public List<String> getSerialNoList(String productCode, String drawNo) throws Exception {
        List<String> serialNo = new ArrayList<>();
        String queryStr = "SELECT "
                + "SERIAL_NO, "
                + "LOTTERY_NUMBERS, "
                + "COUNT(*) "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY WHERE "
                + "DRAW_NO = " + drawNo + " AND PRODUCT_CODE = '" + productCode + "' "
                + "GROUP BY SERIAL_NO, LOTTERY_NUMBERS HAVING COUNT(*) > 1";

        System.out.println(queryStr);
        
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        result.forEach(row -> {
            serialNo.add(row[0].toString());
        });
        return serialNo;
    }

    public void deleteItem(Integer id) throws Exception {
        Connection connection = jDBCConnection.getConnection();
        PreparedStatement prepareStatement = null;
        prepareStatement = connection.prepareStatement("DELETE FROM `DLB_SWT_ST_PURCHASE_HISTORY` WHERE `ID` = " + id + "");
        prepareStatement.execute();
//        customRepository.UpdateQueryExecuter(queryStr);
    }

    public void deleteItem1(Integer id) throws Exception {
        String queryStr = "DELETE FROM `DLB_SWT_ST_PURCHASE_HISTORY` WHERE `ID` = " + id + "";
        customRepository.UpdateQueryExecuter(queryStr);
    }
}
