/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

/**
 *
 * @author nipuna_k
 */
public class ActivityLogDto {
    
    private Integer id;
    private String module;
    private String dlbWbPage;
    private String dlbWbSystemUser;
    private String action;
    private String description;
    private String createdTime;
    private String toDate;
    private String start;
    private String end;
    private String length;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDlbWbPage() {
        return dlbWbPage;
    }

    public void setDlbWbPage(String dlbWbPage) {
        this.dlbWbPage = dlbWbPage;
    }

    public String getDlbWbSystemUser() {
        return dlbWbSystemUser;
    }

    public void setDlbWbSystemUser(String dlbWbSystemUser) {
        this.dlbWbSystemUser = dlbWbSystemUser;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    
}
