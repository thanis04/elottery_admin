/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nipuna_k
 */
@Entity
@Table(name="DLB_WB_SALES_FILE_BACKUP")
public class DlbWbSalesFileBackup {

    private Integer id;
    private Integer dlbStatusByStatus;
    private Integer dlbStatusByNotifyStatus;
    private Integer dlbSwtStWallet;
    private String serialNo;
    private String mid;
    private String txnId;
    private String tid;
    private String drawNo;
    private Date drawDate;
    private String productCode;
    private Date lastUpdated;
    private Date createdDate;
    private String notifiedMesssage;
    private String lotteryNumbers;
    private String winningPrize;
    private String shaKey;
    private String redeemMode;
    private String redeemTime;

    @Id
    @Column(name="ID", length=45)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="STATUS", length=45)
    public Integer getDlbStatusByStatus() {
        return dlbStatusByStatus;
    }

    public void setDlbStatusByStatus(Integer dlbStatusByStatus) {
        this.dlbStatusByStatus = dlbStatusByStatus;
    }

    @Column(name="NOTIFY_STATUS", length=45)
    public Integer getDlbStatusByNotifyStatus() {
        return dlbStatusByNotifyStatus;
    }

    public void setDlbStatusByNotifyStatus(Integer dlbStatusByNotifyStatus) {
        this.dlbStatusByNotifyStatus = dlbStatusByNotifyStatus;
    }

    @Column(name="WALLET_ID", length=45)
    public Integer getDlbSwtStWallet() {
        return dlbSwtStWallet;
    }

    public void setDlbSwtStWallet(Integer dlbSwtStWallet) {
        this.dlbSwtStWallet = dlbSwtStWallet;
    }
    
    @Column(name="SERIAL_NO", length=45)
    public String getSerialNo() {
        return this.serialNo;
    }
    
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    
    @Column(name="MID", length=32)
    public String getMid() {
        return this.mid;
    }
    
    public void setMid(String mid) {
        this.mid = mid;
    }

    
    @Column(name="TXN_ID", length=32)
    public String getTxnId() {
        return this.txnId;
    }
    
    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    
    @Column(name="TID", length=32)
    public String getTid() {
        return this.tid;
    }
    
    public void setTid(String tid) {
        this.tid = tid;
    }
    
    @Column(name="DRAW_NO", length=16)
    public String getDrawNo() {
        return this.drawNo;
    }
    
    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DRAW_DATE", length=19)
    public Date getDrawDate() {
        return this.drawDate;
    }
    
    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }
    
    @Column(name="PRODUCT_CODE", length=45)
    public String getProductCode() {
        return this.productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_UPDATED", length=19)
    public Date getLastUpdated() {
        return this.lastUpdated;
    }
    
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_DATE", length=19)
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    
    @Column(name="NOTIFIED_MESSSAGE", length=1000)
    public String getNotifiedMesssage() {
        return this.notifiedMesssage;
    }
    
    public void setNotifiedMesssage(String notifiedMesssage) {
        this.notifiedMesssage = notifiedMesssage;
    }
    
    @Column(name="LOTTERY_NUMBERS", length=100)
    public String getLotteryNumbers() {
        return this.lotteryNumbers;
    }
    
    public void setLotteryNumbers(String lotteryNumbers) {
        this.lotteryNumbers = lotteryNumbers;
    }
    
    @Column(name="WINNING_PRIZE", length=64)
    public String getWinningPrize() {
        return this.winningPrize;
    }
    
    public void setWinningPrize(String winningPrize) {
        this.winningPrize = winningPrize;
    }
    
    @Column(name="SHA_KEY")
    public String getShaKey() {
        return shaKey;
    }

    public void setShaKey(String shaKey) {
        this.shaKey = shaKey;
    }
    
    @Column(name="REDEEM_MODE")
    public String getRedeemMode() {
        return redeemMode;
    }

    public void setRedeemMode(String redeemMode) {
        this.redeemMode = redeemMode;
    }

    @Column(name="REDEEM_TIME")
    public String getRedeemTime() {
        return redeemTime;
    }

    public void setRedeemTime(String redeemTime) {
        this.redeemTime = redeemTime;
    }
    
}
