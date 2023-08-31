/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.model;

import com.epic.dlb.model.DlbSwtStWallet;
import java.util.Date;
/**
 *
 * @author nipuna_k
 */
public class TicketViewModel {
    private Integer id;
    private DlbSwtStWallet dlbSwtStWallet;
    private String serialNumber;
    private String productCode;
    private String status;
    private String lotteryNumbers;
    private Date drawDate;
    private Date lastUpdated;
    private Date createdDate;
    private String mid;
    private String tid;
    private String drawNo;
    private String productDescription;
    private String shaKey;
    private String nic;
    private String mobileNo;
    private String winingPrize;

    public TicketViewModel() {
    }

    public TicketViewModel(Integer id, DlbSwtStWallet dlbSwtStWallet, String serialNumber, String productCode, String status, String lotteryNumbers, Date drawDate, Date lastUpdated, Date createdDate, String mid, String tid, String drawNo, String productDescription, String shaKey) {
        this.id = id;
        this.dlbSwtStWallet = dlbSwtStWallet;
        this.serialNumber = serialNumber;
        this.productCode = productCode;
        this.status = status;
        this.lotteryNumbers = lotteryNumbers;
        this.drawDate = drawDate;
        this.lastUpdated = lastUpdated;
        this.createdDate = createdDate;
        this.mid = mid;
        this.tid = tid;
        this.drawNo = drawNo;
        this.productDescription = productDescription;
        this.shaKey = shaKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DlbSwtStWallet getDlbSwtStWallet() {
        return dlbSwtStWallet;
    }

    public void setDlbSwtStWallet(DlbSwtStWallet dlbSwtStWallet) {
        this.dlbSwtStWallet = dlbSwtStWallet;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLotteryNumbers() {
        return lotteryNumbers;
    }

    public void setLotteryNumbers(String lotteryNumbers) {
        this.lotteryNumbers = lotteryNumbers;
    }

    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getShaKey() {
        return shaKey;
    }

    public void setShaKey(String shaKey) {
        this.shaKey = shaKey;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getWiningPrize() {
        return winingPrize;
    }

    public void setWiningPrize(String winingPrize) {
        this.winingPrize = winingPrize;
    }
    
    
    
}
