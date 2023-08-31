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
public class BrandwisePaginatedPageData {
    
    private Integer totalRecords;
    private List<BrandwiseSearchReportDto>  paginatedData ;

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<BrandwiseSearchReportDto> getPaginatedData() {
        return paginatedData;
    }

    public void setPaginatedData(List<BrandwiseSearchReportDto> paginatedData) {
        this.paginatedData = paginatedData;
    }
    
    
}
