/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author nipuna_k
 */
@Entity
@Table(name = "DLB_WB_STATUS_ROLLBACK")
public class DlbWbStatusRollback {

    private Integer id;
    private Integer purchaseHistoryId;
    private String status;
    private String activeStatus;
    private Date createdTime;
    private Date lastUpdatedTime;

    public DlbWbStatusRollback() {
    }

    public DlbWbStatusRollback(Integer id, Integer purchaseHistoryId, String status, String activeStatus, Date createdTime, Date lastUpdatedTime) {
        this.id = id;
        this.purchaseHistoryId = purchaseHistoryId;
        this.status = status;
        this.activeStatus = activeStatus;
        this.createdTime = createdTime;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "PURCHASE_HISTORY_ID", length = 20)
    public Integer getPurchaseHistoryId() {
        return purchaseHistoryId;
    }

    public void setPurchaseHistoryId(Integer purchaseHistoryId) {
        this.purchaseHistoryId = purchaseHistoryId;
    }

    @Column(name = "STATUS", length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "ACTIVE_STATUS", length = 255)
    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    @Column(name = "CREATED_TIME", length = 255)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "LAST_UPDATED_TIME", length = 255)
    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

}
