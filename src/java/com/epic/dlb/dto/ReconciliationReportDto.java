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
public class ReconciliationReportDto {
    
    private String lotteryType;
    private String drawNo;
    private String drawDate;
    private String redemptionDate;
    private String redemptionMode;
    private String referenceID;
    private String winningPrize;
    private String ticketReference;
    private String ticketID;
    private String rrn;
    private String purchaseHis;

    public String getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType;
    }

    public String getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    public String getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(String redemptionDate) {
        this.redemptionDate = redemptionDate;
    }

    public String getRedemptionMode() {
        return redemptionMode;
    }

    public void setRedemptionMode(String redemptionMode) {
        this.redemptionMode = redemptionMode;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getWinningPrize() {
        return winningPrize;
    }

    public void setWinningPrize(String winningPrize) {
        this.winningPrize = winningPrize;
    }

    public String getTicketReference() {
        return ticketReference;
    }

    public void setTicketReference(String ticketReference) {
        this.ticketReference = ticketReference;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getPurchaseHis() {
        return purchaseHis;
    }

    public void setPurchaseHis(String purchaseHis) {
        this.purchaseHis = purchaseHis;
    }
    
}
