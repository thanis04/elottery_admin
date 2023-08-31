/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

/**
 *
 * @author kasun_n
 */
public class WinningReport {
    private String createdDate;
    private String serialNo;
    private String drawNo;
    private String drawDate;
    private String firstName;
    private String lastName;
    private String nic;
    private String status;
    private Integer statuCode;
    private String winningAmount;
    private String productDescription;
    private String productCode;
    private String ticketPrice;
    private String lastUpdated;
    private Integer ticketId;
    private Integer purchaseHisId;
    private String txnId;
    private String redemptionTime;
    private String redemptionMode;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(Integer statuCode) {
        this.statuCode = statuCode;
    }

   

    public String getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(String winningAmount) {
        this.winningAmount = winningAmount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getPurchaseHisId() {
        return purchaseHisId;
    }

    public void setPurchaseHisId(Integer purchaseHisId) {
        this.purchaseHisId = purchaseHisId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getRedemptionTime() {
        return redemptionTime;
    }

    public void setRedemptionTime(String redemptionTime) {
        this.redemptionTime = redemptionTime;
    }

    public String getRedemptionMode() {
        return redemptionMode;
    }

    public void setRedemptionMode(String redemptionMode) {
        this.redemptionMode = redemptionMode;
    }
    
    
    
}
