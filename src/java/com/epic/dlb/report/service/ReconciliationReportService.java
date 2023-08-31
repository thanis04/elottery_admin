/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.ReconciliationPaginatedPageData;
import com.epic.dlb.dto.ReconciliationReportDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.repository.ReconciliationReportRepository;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("reconciliationReportService")
public class ReconciliationReportService {

    @Autowired
    private ReconciliationReportRepository reconciliationReportRepository;

    private static SimpleDateFormat dateFormat;

    @Transactional(rollbackFor = Exception.class)
    public ReconciliationPaginatedPageData getReconciliationPaginated(
            ReportSearchCriteriaDto dto) throws Exception {
        List<ReconciliationReportDto> resultList = reconciliationReportRepository.
                findReconciliationData(dto);
        return getReconciliationPaginatedData(resultList, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ReconciliationReportDto> getReconciliationData(
            ReportSearchCriteriaDto dto) throws Exception {
        List<ReconciliationReportDto> resultList = reconciliationReportRepository.
                findReconciliationData(dto);
        return resultList;  
    }

    @Transactional(rollbackFor = Exception.class)
    public ReconciliationPaginatedPageData getReconciliationPaginatedData(
            List<ReconciliationReportDto> resultList,
            ReportSearchCriteriaDto dto) throws Exception {

        ReconciliationPaginatedPageData pageData = new ReconciliationPaginatedPageData();
        pageData.setReconciliationReportDto(resultList);
        pageData.setTotalRecords(reconciliationReportRepository.findReconciliationCount(dto));

        return pageData;
    }
}
