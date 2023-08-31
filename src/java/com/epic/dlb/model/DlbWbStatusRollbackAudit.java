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
@Table(name = "DLB_WB_STATUS_ROLLBACK_AUDIT")
public class DlbWbStatusRollbackAudit {

    private Integer id;
    private Integer statusRollbackId;
    private String activity;
    private String userName;
    private Date createdDate;
    private Integer currentStatus;

    public DlbWbStatusRollbackAudit(Integer id, Integer statusRollbackId, String activity, String userName, Date createdDate, Integer currentStatus) {
        this.id = id;
        this.statusRollbackId = statusRollbackId;
        this.activity = activity;
        this.userName = userName;
        this.createdDate = createdDate;
        this.currentStatus = currentStatus;
    }

    public DlbWbStatusRollbackAudit() {
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

    @Column(name = "STATUS_ROLLBACK_ID", length = 20)
    public Integer getStatusRollbackId() {
        return statusRollbackId;
    }

    public void setStatusRollbackId(Integer statusRollbackId) {
        this.statusRollbackId = statusRollbackId;
    }

    @Column(name = "ACTIVITY", length = 255)
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Column(name = "USER_NAME", length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "CREATED_DATE", length = 255)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "CURRENT_STATUS", length = 20)
    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

}
