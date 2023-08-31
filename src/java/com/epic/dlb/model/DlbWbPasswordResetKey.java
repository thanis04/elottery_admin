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
@Table(name = "DLB_WB_RESET_KEY")
public class DlbWbPasswordResetKey implements Serializable {

    private Integer id;
    private String username;
    private String key;
    private String statusOfKey;
    private Date createdTime;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "USERNAME", length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "KEY", length = 255)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "STATUS_OF_KEY", length = 255)
    public String getStatusOfKey() {
        return statusOfKey;
    }

    public void setStatusOfKey(String statusOfKey) {
        this.statusOfKey = statusOfKey;
    }

    @Column(name = "CREATED_TIME", length = 255)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

}
