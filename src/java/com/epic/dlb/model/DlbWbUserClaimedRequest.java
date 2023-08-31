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
@Table(name = "DLB_WB_USER_CLAIM_REQUEST")
public class DlbWbUserClaimedRequest implements Serializable {

    private Integer id;
    private Integer purchaseId;
    private String txnId;
    private String status;
    private String requestedUser;
    private String approvedUser;
    private Date requestedDate;
    private Date approvedDate;
    private String statementStatus;
    private Date firstStatement;
    private Date secondStatement;
    private String remark;
    private String feedback;

    public DlbWbUserClaimedRequest(Integer id, Integer purchaseId, String txnId, String status, String requestedUser, String approvedUser, Date requestedDate, Date approvedDate, String statementStatus, Date firstStatement, Date secondStatement, String remark, String feedback) {
        this.id = id;
        this.purchaseId = purchaseId;
        this.txnId = txnId;
        this.status = status;
        this.requestedUser = requestedUser;
        this.approvedUser = approvedUser;
        this.requestedDate = requestedDate;
        this.approvedDate = approvedDate;
        this.statementStatus = statementStatus;
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
        this.remark = remark;
        this.feedback = feedback;
    }

    public DlbWbUserClaimedRequest() {
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

    @Column(name = "PURCHASE_ID", length = 255)
    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Column(name = "TXN_ID")
    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
    
    @Column(name = "STATUS", length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "REQUESTED_USER", length = 255)
    public String getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(String requestedUser) {
        this.requestedUser = requestedUser;
    }

    @Column(name = "APPROVED_USER", length = 255)
    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    @Column(name = "REQUESTED_DATE")
    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    @Column(name = "APPROVED_DATE")
    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    @Column(name = "STATEMENT_STATUS", length = 255)
    public String getStatementStatus() {
        return statementStatus;
    }

    public void setStatementStatus(String statementStatus) {
        this.statementStatus = statementStatus;
    }

    @Column(name = "FIRST_STATEMENT")
    public Date getFirstStatement() {
        return firstStatement;
    }

    public void setFirstStatement(Date firstStatement) {
        this.firstStatement = firstStatement;
    }

    @Column(name = "SECOND_STATEMENT")
    public Date getSecondStatement() {
        return secondStatement;
    }

    public void setSecondStatement(Date secondStatement) {
        this.secondStatement = secondStatement;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "FEEDBACK")
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    
}
