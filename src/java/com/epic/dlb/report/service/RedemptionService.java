/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.report.repository.RedemptionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("redemptionService")
public class RedemptionService {

    @Autowired
    private RedemptionRepository redemptionRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getPurchaseHistoryForUserClaimed(String fromDate, String toDate,
            String productcode, Integer statusCode) throws Exception {
        return redemptionRepository.getPurchaseHistoryForUserClaimed(fromDate, toDate, productcode, statusCode);
    }
}
