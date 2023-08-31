/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author kasun_n
 */
@Entity
@Table(name = "DLB_WB_OPERATION_REQUEST")
public class DlbWbOperationRequest implements java.io.Serializable {


    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private Integer id;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "LAST_UPDATED_BY")
    private String updatedBy;

    @Column(name = "LAST_UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "REF_NO")
    private String refNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS")
    private DlbStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENT_STATUS")
    private DlbStatus currentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQ_STATUS")
    private DlbStatus requestStatus;

    @Column(name = "REF_EVIDENCE")
    private String refEvidence;

    @Column(name = "COMMENT")
    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public DlbStatus getStatus() {
        return status;
    }

    public void setStatus(DlbStatus status) {
        this.status = status;
    }

    public DlbStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DlbStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public DlbStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(DlbStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRefEvidence() {
        return refEvidence;
    }

    public void setRefEvidence(String refEvidence) {
        this.refEvidence = refEvidence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    

}
