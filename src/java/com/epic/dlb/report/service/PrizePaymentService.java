/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.repository.PrizePaymentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("prizePaymentService")
public class PrizePaymentService {

    @Autowired
    private PrizePaymentRepository paymentRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> getPrizePaymentData(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        return paymentRepository.getPrizePaymentData(reportSearchCriteriaDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getPrizePaymentDataCount(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        return paymentRepository.getPrizePaymentDataCount(reportSearchCriteriaDto);
    }
}
