/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import java.util.Date;

/**
 *
 * @author nipuna_k
 */
public class StatusRollbackDto {
    
    private Integer id;
    private Integer purchaseHistoryId;
    private String status;
    private String activeStatus;
    private Date createdTime;
    private Date lastUpdatedTime; 

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPurchaseHistoryId() {
        return purchaseHistoryId;
    }

    public void setPurchaseHistoryId(Integer purchaseHistoryId) {
        this.purchaseHistoryId = purchaseHistoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
    
}
