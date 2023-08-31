/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.report.model.PurchaseHistorySummery;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("purchaseReportService")
public class PurchaseReportService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionByDate(String fromDate, String toDate) {

        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchStatus = new WhereStatement("status", SystemVarList.READY_TO_CHECKOUT, SystemVarList.EQUAL);

        List listWithQuery = genericRepository.listWithQuery(DlbWbTicket.class, searchCriteriaFromDate, searchCriteriaToDate, searchStatus);

        Iterator<DlbWbTicket> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionByDate(String fromDate, String toDate, String lotteryCode) {

        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchLottery = new WhereStatement("productCode", lotteryCode, SystemVarList.EQUAL, SystemVarList.AND);
        WhereStatement searchStatus = new WhereStatement("status", SystemVarList.READY_TO_CHECKOUT, SystemVarList.EQUAL);

        List listWithQuery = genericRepository.listWithQuery(DlbWbTicket.class, searchCriteriaFromDate, searchCriteriaToDate, searchLottery, searchStatus);

        Iterator<DlbWbTicket> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            Hibernate.initialize(next.getDlbSwtStWallet());
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionSummeryByDate(String fromDate, String toDate) {

        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN,SystemVarList.AND);
        WhereStatement searchStatus = new WhereStatement("status", SystemVarList.READY_TO_CHECKOUT, SystemVarList.EQUAL);

        //set projection list
        List projectionList = new ArrayList();
        projectionList.add("productCode as productCode,");
        projectionList.add("cast(count(id) as int) as count");

        List listWithQuery = genericRepository.listWithQueryNGroup(DlbWbTicket.class, projectionList, "productCode", searchCriteriaFromDate, searchCriteriaToDate, searchStatus);

        List list = new ArrayList();
        Iterator iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {

            Object object[] = (Object[]) iterator.next();

            PurchaseHistorySummery purchaseHistory = new PurchaseHistorySummery();
            purchaseHistory.setProductCode((String) object[0]);
            purchaseHistory.setPurchaseCount((Integer) object[1]);
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(purchaseHistory.getProductCode(), DlbWbProduct.class);
            
            //get sales count
             WhereStatement status2 = new WhereStatement("status", SystemVarList.CHECKOUT, SystemVarList.EQUAL,SystemVarList.AND);
             WhereStatement productCodeWhere = new WhereStatement("productCode", product.getProductCode(), SystemVarList.EQUAL);
            List saleList = genericRepository.listWithQueryNGroup(DlbSwtStPurchaseHistory.class, projectionList, "productCode", searchCriteriaFromDate, searchCriteriaToDate, status2,productCodeWhere);
            if(saleList.size()>0){
               Object object1[]=(Object[]) saleList.get(0);
                purchaseHistory.setSalesCount((int) object1[1]);
            }
            
            purchaseHistory.setDescription(product.getDescription());

            list.add(purchaseHistory);
        }

        return list;
    }

}
