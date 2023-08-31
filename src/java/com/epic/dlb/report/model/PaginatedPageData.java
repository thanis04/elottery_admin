/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.model;

import java.util.List;

/**
 *
 * @author yasitha_b
 */

public class PaginatedPageData 
{
    private Integer total_records;
    private List<Object[]>  paginatedData ;
    
    public Integer getTotal_records() {
        return total_records;
    }

    public void setTotal_records(Integer total_records) {
        this.total_records = total_records;
    }

    public List<Object[]> getPaginatedData() {
        return paginatedData;
    }

    public void setPaginatedData(List<Object[]> paginatedData) {
        this.paginatedData = paginatedData;
    }
  
}
