/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DLBWbPrizePaymentHistory;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kasun_n
 */
@Service("prizePaymentHistoryService")
public class PrizePaymentHistoryService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public List listNew() {
        WhereStatement statement = new WhereStatement("isNew", null, SystemVarList.IS_NOT_NULL);
        return genericRepository.list(DLBWbPrizePaymentHistory.class,statement);
    }

    @Transactional
    public List lisByDate(String fromDate, String toDate) {
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN);
        return genericRepository.listWithQuery(DLBWbPrizePaymentHistory.class, searchCriteriaFromDate, searchCriteriaToDate);
    }

    @Transactional
    public DLBWbPrizePaymentHistory findById(Integer id) {
        return (DLBWbPrizePaymentHistory) genericRepository.get(id, DLBWbPrizePaymentHistory.class);
    }

    @Transactional
    public void update(DLBWbPrizePaymentHistory dLBWbPrizePaymentHistory) throws Exception {
        genericRepository.update(dLBWbPrizePaymentHistory);
    }

}
