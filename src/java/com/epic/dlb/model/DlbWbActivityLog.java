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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author nipuna_k
 */
@Entity
@Table(name = "DLB_WB_ACTIVITY_LOG")
public class DlbWbActivityLog implements Serializable {

    private Integer id;
    private DlbWbPage dlbWbPage;
    private DlbWbSection dlbWbSection;
    private DlbWbSystemUser dlbWbSystemUser;
    private String employeeid;
    private String action;
    private String description;
    private Date createdTime;

    public DlbWbActivityLog() {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAGE", nullable = false)
    public DlbWbPage getDlbWbPage() {
        return dlbWbPage;
    }

    public void setDlbWbPage(DlbWbPage dlbWbPage) {
        this.dlbWbPage = dlbWbPage;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", nullable = false)
    public DlbWbSystemUser getDlbWbSystemUser() {
        return dlbWbSystemUser;
    }

    public void setDlbWbSystemUser(DlbWbSystemUser dlbWbSystemUser) {
        this.dlbWbSystemUser = dlbWbSystemUser;
    }

    @Column(name = "ACTION", length = 255)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "DESCRIPTION", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CREATED_TIME", length = 255)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "EMPLOYEEID", length = 16)
    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION", nullable = false)
    public DlbWbSection getDlbWbSection() {
        return dlbWbSection;
    }

    public void setDlbWbSection(DlbWbSection dlbWbSection) {
        this.dlbWbSection = dlbWbSection;
    }

    
}
