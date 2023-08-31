/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.report.repository.TicketReportRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("ticketReportService")
public class TicketReportService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private TicketReportRepository ticketReportRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getTicketByDate(String fromDate, String toDate) {

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
    public List getTicketByDateUsingTicket(String fromDate, String toDate) {

        //search using criteria
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchStatus = new WhereStatement("status", SystemVarList.READY_TO_CHECKOUT, SystemVarList.EQUAL);

        List listWithQuery = genericRepository.listWithQuery(DlbWbTicket.class, searchCriteriaFromDate, searchCriteriaToDate, searchStatus);
//        DlbWbTicket obj= (DlbWbTicket)listWithQuery.get(0);
        Iterator<DlbWbTicket> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
        }
        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTicketByDateUsingPurchaseHistory(String fromDate, String toDate) {

        //search using criteria
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchStatus = new WhereStatement("status", SystemVarList.CHECKOUT, SystemVarList.EQUAL);

        List listWithQuery = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, searchCriteriaFromDate, searchCriteriaToDate, searchStatus);

        Iterator<DlbSwtStPurchaseHistory> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory next = iterator.next();
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
        }
        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTicketByDateUsingReturnTicket(String fromDate, String toDate) {

        //search using criteria
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchStatus = new WhereStatement("status", SystemVarList.RETURNED, SystemVarList.EQUAL);

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
    public List searchTiketFile(List<WhereStatement> whereStatements, int start, int end) {
        return ticketReportRepository.getTicketFile(whereStatements, start, end);
    }

}
