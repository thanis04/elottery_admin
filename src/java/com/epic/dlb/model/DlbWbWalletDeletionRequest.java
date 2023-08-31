/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import java.io.Serializable;
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
@Table(name = "DLB_WB_WALLET_DELETION_REQUEST")
public class DlbWbWalletDeletionRequest implements Serializable {

    private Integer id;
    private Integer walletId;
    private String requestedBy;
    private String approvedBy;
    private String status;
    private Date createdDateTime;
    private Date lastUpdatedTime;
    private String remark;

    public DlbWbWalletDeletionRequest() {
    }

    public DlbWbWalletDeletionRequest(Integer id, Integer walletId, String requestedBy, String approvedBy, String status, Date createdDateTime, Date lastUpdatedTime, String remark) {
        this.id = id;
        this.walletId = walletId;
        this.requestedBy = requestedBy;
        this.approvedBy = approvedBy;
        this.status = status;
        this.createdDateTime = createdDateTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.remark = remark;
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

    @Column(name = "WALLET_ID", length = 255)
    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    @Column(name = "REQUESTED_BY", length = 255)
    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Column(name = "STATUS", length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "LAST_UPDATED_TIME", length = 255)
    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Column(name = "APPROVED_BY", length = 255)
    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Column(name = "CREATED_TIME", length = 255)
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Column(name = "REMARK", length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
