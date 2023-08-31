/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbWiningFileApprovalHistory;
import com.epic.dlb.model.DlbWbWiningFileErrorDetails;
import com.epic.dlb.model.DlbWbWorkflow;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nipuna_k
 */
public class DlbWbWiningFileDto { 

    private Date drawDate;
    private BigInteger recordCount;
    private Double epicAmount;
    private Double claimedAmount;
    private Double generatedAmount;
    private Double claimedDoneAmount;
    private Double dlbClaimedAmount;
    private Double dlbAmount;
    private Double amount;

    

    public BigInteger getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(BigInteger recordCount) {
        this.recordCount = recordCount;
    }

     

    public Double getEpicAmount() {
        return epicAmount;
    }

    public void setEpicAmount(Double epicAmount) {
        this.epicAmount = epicAmount;
    }

    public Double getDlbAmount() {
        return dlbAmount;
    }

    public void setDlbAmount(Double dlbAmount) {
        this.dlbAmount = dlbAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    
    public Double getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(Double claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Double getDlbClaimedAmount() {
        return dlbClaimedAmount;
    }

    public void setDlbClaimedAmount(Double dlbClaimedAmount) {
        this.dlbClaimedAmount = dlbClaimedAmount;
    }

    public Double getGeneratedAmount() {
        return generatedAmount;
    }

    public void setGeneratedAmount(Double generatedAmount) {
        this.generatedAmount = generatedAmount;
    }

    public Double getClaimedDoneAmount() {
        return claimedDoneAmount;
    }

    public void setClaimedDoneAmount(Double claimedDoneAmount) {
        this.claimedDoneAmount = claimedDoneAmount;
    }

    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }
    
    
    
    
}
