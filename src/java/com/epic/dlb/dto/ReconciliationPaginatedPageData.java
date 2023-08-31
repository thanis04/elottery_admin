/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import java.util.List;

/**
 *
 * @author nipuna_k
 */
public class ReconciliationPaginatedPageData {
    
    private Integer totalRecords;
    private List<ReconciliationReportDto> reconciliationReportDto;

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<ReconciliationReportDto> getReconciliationReportDto() {
        return reconciliationReportDto;
    }

    public void setReconciliationReportDto(List<ReconciliationReportDto> reconciliationReportDto) {
        this.reconciliationReportDto = reconciliationReportDto;
    }
    
    
}
