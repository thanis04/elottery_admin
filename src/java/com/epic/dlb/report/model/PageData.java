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
public class PageData 
{
     private List dataList;
     private Integer numberOfRecords;

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public Integer getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(Integer numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }
}
