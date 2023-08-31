/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author nipuna_k
 */
public class UserTransactionSearchDTO {

    private String nic;
    private String mobileNo;
    private String fromDate;
    private String toDate;
    private String start;
    private String end;
    private String length;
    private String mode;
    private List<Object[]> list;
    private Integer count;
    private JSONArray dataArray;

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Object[]> getList() {
        return list;
    }

    public void setList(List<Object[]> list) {
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public JSONArray getDataArray() {
        return dataArray;
    }

    public void setDataArray(JSONArray dataArray) {
        this.dataArray = dataArray;
    }
    
    
}
