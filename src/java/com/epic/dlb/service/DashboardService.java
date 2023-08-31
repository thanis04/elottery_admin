/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.SalesTicket;
import com.epic.dlb.repository.CustomRepository;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("dashboardService")
public class DashboardService {

    @Autowired
    private CustomRepository customRepository;

    @Transactional
    public Integer getSalesByDate(String fromdate, String toDate, String lottery) throws Exception {

        BigInteger sales = new BigInteger("0");

        String query = "SELECT "
                + "Count(DLB_SWT_ST_PURCHASE_HISTORY.ID) "
                + "FROM "
                + "DLB_SWT_ST_PURCHASE_HISTORY "
                + "WHERE "
                + "DLB_SWT_ST_PURCHASE_HISTORY.CREATED_DATE "
                + "BETWEEN '" + fromdate + "' AND '" + toDate + "'";

        String where = "";

        if (lottery != null) {
            lottery = " AND DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE='" + lottery + "'";
        }

        List list = customRepository.queryExecuter(query + where);

        sales = (BigInteger) list.get(0);

        return sales.intValue();

    }

    @Transactional
    public List getSalesByDate(String fromdate, String toDate) throws Exception {

        String query = "SELECT\n"
                + "DLB_WB_PRODUCT.DESCRIPTION,\n"
                + "Count(DLB_SWT_ST_PURCHASE_HISTORY.ID)\n"
                + "FROM\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY\n"
                + "INNER JOIN DLB_WB_PRODUCT ON DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE = DLB_WB_PRODUCT.PRODUCT_CODE\n"
                + "WHERE\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.CREATED_DATE "
                + "BETWEEN '" + fromdate + "' AND '" + toDate + "'"
                + "GROUP BY DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE";

        List<Object[]> list = customRepository.queryExecuter(query);

        List<SalesTicket> salesTickets = new ArrayList<>();

        for (Object[] objects : list) {
            SalesTicket salesTicket = new SalesTicket();
            salesTicket.setProductDescription((String) objects[0]);
            salesTicket.setCount((Integer) objects[1]);

            salesTickets.add(salesTicket);
        }

        return salesTickets;

    }

    @Transactional
    public Integer customerCount(String fromdate, String toDate) throws Exception {

        BigInteger count = new BigInteger("0");

        String query = "SELECT\n"
                + "Count(DLB_SWT_ST_WALLET.ID)\n"
                + "FROM\n"
                + "DLB_SWT_ST_WALLET\n"
                + "WHERE\n"
                + "DLB_SWT_ST_WALLET.CREATE_TIME "
                + "BETWEEN '" + fromdate + "' AND '" + toDate + "'";

        List list = customRepository.queryExecuter(query);

        count = (BigInteger) list.get(0);

        return count.intValue();
    }

    @Transactional
    public Integer allCustomerCount() throws Exception {

        BigInteger count = new BigInteger("0");

        String query = "SELECT\n"
                + "Count(DLB_SWT_ST_WALLET.ID)\n"
                + "FROM\n"
                + "DLB_SWT_ST_WALLET\n";  

        List list = customRepository.queryExecuter(query);
        count = (BigInteger) list.get(0);

        return count.intValue();
    }

}
