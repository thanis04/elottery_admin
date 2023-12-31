package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * DlbWbTicket generated by hbm2java
 */
@Entity
@Table(name = "DLB_WB_TICKET"
)
public class DlbWbTicket implements java.io.Serializable {

    private Integer id;
    private DlbSwtStWallet dlbSwtStWallet;
    private DlbWbTicketFile dlbWbTicketFile;
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



    public DlbWbTicket() {
    }

    public DlbWbTicket(DlbSwtStWallet dlbSwtStWallet, String serialNumber, String productCode, String status, String lotteryNumbers, Date drawDate, Date lastUpdated, Date createdDate, String mid, String tid, String drawNo) {
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
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALLET_ID")
    public DlbSwtStWallet getDlbSwtStWallet() {
        return this.dlbSwtStWallet;
    }

    public void setDlbSwtStWallet(DlbSwtStWallet dlbSwtStWallet) {
        this.dlbSwtStWallet = dlbSwtStWallet;
    }

    @Column(name = "SERIAL_NUMBER", length = 45)
    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "PRODUCT_CODE", length = 45)
    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Column(name = "STATUS", length = 45)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "LOTTERY_NUMBERS", length = 100)
    public String getLotteryNumbers() {
        return this.lotteryNumbers;
    }

    public void setLotteryNumbers(String lotteryNumbers) {
        this.lotteryNumbers = lotteryNumbers;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DRAW_DATE", length = 10)
    public Date getDrawDate() {
        return this.drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED", length = 19)
    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", length = 19)
    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "MID", length = 45)
    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Column(name = "TID", length = 45)
    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Column(name = "DRAW_NO", length = 45)
    public String getDrawNo() {
        return this.drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    @Transient
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Column(name = "SHA_KEY", length = 255)
    public String getShaKey() {
        return shaKey;
    }

    public void setShaKey(String shaKey) {
        this.shaKey = shaKey;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_FILE_ID")
    public DlbWbTicketFile getDlbWbTicketFile() {
        return this.dlbWbTicketFile;
    }

    public void setDlbWbTicketFile(DlbWbTicketFile dlbWbTicketFile) {
        this.dlbWbTicketFile = dlbWbTicketFile;
    }




}
