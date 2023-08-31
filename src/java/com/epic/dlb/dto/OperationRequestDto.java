/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import com.epic.dlb.model.DlbStatus;

/**
 *
 * @author kasun_n
 */
public class OperationRequestDto {

    private Integer id;
    private String type;
    private String refNo;
    private String comment;
    private String refEvidence;
    private Integer currentStatusCode;
    private String currentStatus;
    private Integer requestStatusCode;
    private String requestStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRefEvidence() {
        return refEvidence;
    }

    public void setRefEvidence(String refEvidence) {
        this.refEvidence = refEvidence;
    }

    public Integer getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(Integer currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getRequestStatusCode() {
        return requestStatusCode;
    }

    public void setRequestStatusCode(Integer requestStatusCode) {
        this.requestStatusCode = requestStatusCode;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    

}
