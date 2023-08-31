/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author kasun_n
 */
public class DlbWbWiningFileDetails {

    private int id;
    private String productDescription;
    private String drawNo;
    private Date date;
    private Date createdTime;
    private Integer recordCount;
    private Double epicAmount;
    private Double claimedAmount;
    private Double generatedAmount;
    private Double claimedDoneAmount;
    private Double dlbClaimedAmount;
    private Double dlbAmount;
    private Double amount;
    private String description;
    private Integer statusCode;
    


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


    

    public Double getEpicAmount() {
        return epicAmount;
    }

    public void setEpicAmount(Double epicAmount) {
        this.epicAmount = epicAmount;
    }

    public Double getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(Double claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Double getGeneratedAmount() {
        return generatedAmount;
    }

    public void setGeneratedAmount(Double generatedAmount) {
        this.generatedAmount = generatedAmount;
    }

    public Double getClaimedDoneAmount() {
        return claimedDoneAmount;
    }

    public void setClaimedDoneAmount(Double claimedDoneAmount) {
        this.claimedDoneAmount = claimedDoneAmount;
    }

    public Double getDlbClaimedAmount() {
        return dlbClaimedAmount;
    }

    public void setDlbClaimedAmount(Double dlbClaimedAmount) {
        this.dlbClaimedAmount = dlbClaimedAmount;
    }

    public Double getDlbAmount() {
        return dlbAmount;
    }

    public void setDlbAmount(Double dlbAmount) {
        this.dlbAmount = dlbAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

 
    
    

}
