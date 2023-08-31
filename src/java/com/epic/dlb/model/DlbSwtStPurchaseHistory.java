    package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.transaction.annotation.Transactional;

/**
 * DlbSwtStPurchaseHistory generated by hbm2java
 */
@Entity
@Table(name="DLB_SWT_ST_PURCHASE_HISTORY"
    
)
public class DlbSwtStPurchaseHistory  implements java.io.Serializable {


     private Integer id;
     private DlbStatus dlbStatusByStatus;
     private DlbStatus dlbStatusByNotifyStatus;
     private DlbSwtStWallet dlbSwtStWallet;
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
        private String productDescription;
        private String paymentMethod;        
        private String referenceNo;
    private String redeemMode;
    private String redeemTime;
        
     private Set<DlbMissMatchRows> dlbMissMatchRowses = new HashSet<DlbMissMatchRows>(0);

    public DlbSwtStPurchaseHistory() {
    }

	
    public DlbSwtStPurchaseHistory(DlbSwtStWallet dlbSwtStWallet) {
        this.dlbSwtStWallet = dlbSwtStWallet;
    }

    public DlbSwtStPurchaseHistory(Integer id, DlbStatus dlbStatusByStatus, DlbStatus dlbStatusByNotifyStatus, DlbSwtStWallet dlbSwtStWallet, String serialNo, String mid, String txnId, String tid, String drawNo, Date drawDate, String productCode, Date lastUpdated, Date createdDate, String notifiedMesssage, String lotteryNumbers, String winningPrize, String shaKey, String redeemMode, String redeemTime) {
        this.id = id;
        this.dlbStatusByStatus = dlbStatusByStatus;
        this.dlbStatusByNotifyStatus = dlbStatusByNotifyStatus;
        this.dlbSwtStWallet = dlbSwtStWallet;
        this.serialNo = serialNo;
        this.mid = mid;
        this.txnId = txnId;
        this.tid = tid;
        this.drawNo = drawNo;
        this.drawDate = drawDate;
        this.productCode = productCode;
        this.lastUpdated = lastUpdated;
        this.createdDate = createdDate;
        this.notifiedMesssage = notifiedMesssage;
        this.lotteryNumbers = lotteryNumbers;
        this.winningPrize = winningPrize;
        this.shaKey = shaKey;
        this.redeemMode = redeemMode;
        this.redeemTime = redeemTime;
    }
    
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STATUS")
    public DlbStatus getDlbStatusByStatus() {
        return this.dlbStatusByStatus;
    }
    
    public void setDlbStatusByStatus(DlbStatus dlbStatusByStatus) {
        this.dlbStatusByStatus = dlbStatusByStatus;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NOTIFY_STATUS")
    public DlbStatus getDlbStatusByNotifyStatus() {
        return this.dlbStatusByNotifyStatus;
    }
    
    public void setDlbStatusByNotifyStatus(DlbStatus dlbStatusByNotifyStatus) {
        this.dlbStatusByNotifyStatus = dlbStatusByNotifyStatus;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="WALLET_ID", nullable=false)
    public DlbSwtStWallet getDlbSwtStWallet() {
        return this.dlbSwtStWallet;
    }
    
    public void setDlbSwtStWallet(DlbSwtStWallet dlbSwtStWallet) {
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

    

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbSwtStPurchaseHistory")
    public Set<DlbMissMatchRows> getDlbMissMatchRowses() {
        return this.dlbMissMatchRowses;
    }
    
    public void setDlbMissMatchRowses(Set<DlbMissMatchRows> dlbMissMatchRowses) {
        this.dlbMissMatchRowses = dlbMissMatchRowses;
    }

    @Transient
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    @Transient
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Transient
    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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


