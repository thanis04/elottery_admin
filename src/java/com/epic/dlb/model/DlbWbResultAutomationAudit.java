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
@Table(name="DLB_WB_RESULT_AUTOMATION_AUDIT")
public class DlbWbResultAutomationAudit implements Serializable {
    
    private Integer id;
    private Integer drawNo;
    private String status;
    private String product;
    private String day;
    private Date drawDate;
    private String description;
    private Date createdDate;

    public DlbWbResultAutomationAudit(Integer id, Integer drawNo, String status, String product, String day, Date drawDate, String gameDescription, Date createdDate) {
        this.id = id;
        this.drawNo = drawNo;
        this.status = status;
        this.product = product;
        this.day = day;
        this.drawDate = drawDate;
        this.description = gameDescription;
        this.createdDate = createdDate;
    }

    public DlbWbResultAutomationAudit() {
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

    @Column(name = "DRAW_NO", length = 255)
    public Integer getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(Integer drawNo) {
        this.drawNo = drawNo;
    }

    @Column(name = "STATUS", length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "PRODUCT", length = 255)
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Column(name = "DAY", length = 255)
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Column(name = "DRAW_DATE", length = 255)
    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    @Column(name = "DESCRIPTION", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CREATED_DATE", length = 255)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    
}
