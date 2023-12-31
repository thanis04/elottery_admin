package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
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

/**
 * DlbWbWiningFile generated by hbm2java
 */
@Entity
@Table(name = "DLB_WB_WINING_FILE"
)
public class DlbWbWiningFile implements java.io.Serializable {

    private Integer id;
    private DlbStatus dlbStatus;
    private DlbWbEmployee dlbWbEmployeeByApprovedBy;
    private DlbWbEmployee dlbWbEmployeeByUploadBy;
    private DlbWbProduct dlbWbProduct;
    private DlbWbWorkflow dlbWbWorkflow;
    private String drawNo;
    private Date date;
    private String hash;
    private String macAddress;
    private String productDescription;
    private Boolean filenameCheck;
    private Boolean macCheck;
    private Boolean hashCheck;
    private String filePath;
    private Boolean tokenCheck;
    private Integer recordCount;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;
    private Date createdTime;
    private String resultTableName;
    private DlbWbTicket dlbWbTicket;
    private Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistories = new HashSet<DlbWbWiningFileApprovalHistory>(0);
    private Set<DlbWbWiningFileErrorDetails> dlbWbWiningFileErrorDetailses = new HashSet<DlbWbWiningFileErrorDetails>(0);

    private Double amount;
    private Double rsvAmount;
    private Double epicAmount;
    private Double dlbAmount;
    private int noOfTicket;

    public DlbWbWiningFile() {
    }

    public DlbWbWiningFile(DlbWbEmployee dlbWbEmployeeByUploadBy, DlbWbProduct dlbWbProduct, DlbWbWorkflow dlbWbWorkflow, String drawNo) {
        this.dlbWbEmployeeByUploadBy = dlbWbEmployeeByUploadBy;
        this.dlbWbProduct = dlbWbProduct;
        this.dlbWbWorkflow = dlbWbWorkflow;
        this.drawNo = drawNo;
    }

    public DlbWbWiningFile(DlbStatus dlbStatus, DlbWbEmployee dlbWbEmployeeByApprovedBy, DlbWbEmployee dlbWbEmployeeByUploadBy, DlbWbProduct dlbWbProduct, DlbWbWorkflow dlbWbWorkflow, String drawNo, Date date, String hash, String macAddress, String productDescription, Boolean filenameCheck, Boolean macCheck, Boolean hashCheck, String filePath, Integer recordCount, String lastUpdatedUser, Date lastUpdatedTime, Date createdTime, String resultTableName, Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistories, Set<DlbWbWiningFileErrorDetails> dlbWbWiningFileErrorDetailses) {
        this.dlbStatus = dlbStatus;
        this.dlbWbEmployeeByApprovedBy = dlbWbEmployeeByApprovedBy;
        this.dlbWbEmployeeByUploadBy = dlbWbEmployeeByUploadBy;
        this.dlbWbProduct = dlbWbProduct;
        this.dlbWbWorkflow = dlbWbWorkflow;
        this.drawNo = drawNo;
        this.date = date;
        this.hash = hash;
        this.macAddress = macAddress;
        this.productDescription = productDescription;
        this.filenameCheck = filenameCheck;
        this.macCheck = macCheck;
        this.hashCheck = hashCheck;
        this.filePath = filePath;
        this.recordCount = recordCount;
        this.lastUpdatedUser = lastUpdatedUser;
        this.lastUpdatedTime = lastUpdatedTime;
        this.createdTime = createdTime;
        this.resultTableName = resultTableName;
        this.dlbWbWiningFileApprovalHistories = dlbWbWiningFileApprovalHistories;
        this.dlbWbWiningFileErrorDetailses = dlbWbWiningFileErrorDetailses;
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
    @JoinColumn(name = "PENDING_APPROVAL")
    public DlbStatus getDlbStatus() {
        return this.dlbStatus;
    }

    public void setDlbStatus(DlbStatus dlbStatus) {
        this.dlbStatus = dlbStatus;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPROVED_BY")
    public DlbWbEmployee getDlbWbEmployeeByApprovedBy() {
        return this.dlbWbEmployeeByApprovedBy;
    }

    public void setDlbWbEmployeeByApprovedBy(DlbWbEmployee dlbWbEmployeeByApprovedBy) {
        this.dlbWbEmployeeByApprovedBy = dlbWbEmployeeByApprovedBy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_BY", nullable = false)
    public DlbWbEmployee getDlbWbEmployeeByUploadBy() {
        return this.dlbWbEmployeeByUploadBy;
    }

    public void setDlbWbEmployeeByUploadBy(DlbWbEmployee dlbWbEmployeeByUploadBy) {
        this.dlbWbEmployeeByUploadBy = dlbWbEmployeeByUploadBy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_CODE", nullable = false)
    public DlbWbProduct getDlbWbProduct() {
        return this.dlbWbProduct;
    }

    public void setDlbWbProduct(DlbWbProduct dlbWbProduct) {
        this.dlbWbProduct = dlbWbProduct;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_FILE_ID", nullable = false)
    public DlbWbTicket getDlbWbTicket() {
        return dlbWbTicket;
    }

    public void setDlbWbTicket(DlbWbTicket dlbWbTicket) {
        this.dlbWbTicket = dlbWbTicket;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENT_STAGE", nullable = false)
    public DlbWbWorkflow getDlbWbWorkflow() {
        return this.dlbWbWorkflow;
    }

    public void setDlbWbWorkflow(DlbWbWorkflow dlbWbWorkflow) {
        this.dlbWbWorkflow = dlbWbWorkflow;
    }

    @Column(name = "DRAW_NO", nullable = false, length = 16)
    public String getDrawNo() {
        return this.drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE", length = 10)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "TOKEN_CHECK")
    public Boolean getTokenCheck() {
        return tokenCheck;
    }

    public void setTokenCheck(Boolean tokenCheck) {
        this.tokenCheck = tokenCheck;
    }

    @Column(name = "HASH", length = 512)
    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "MAC_ADDRESS", length = 64)
    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Column(name = "PRODUCT_DESCRIPTION", length = 128)
    public String getProductDescription() {
        return this.productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Column(name = "FILENAME_CHECK")
    public Boolean getFilenameCheck() {
        return this.filenameCheck;
    }

    public void setFilenameCheck(Boolean filenameCheck) {
        this.filenameCheck = filenameCheck;
    }

    @Column(name = "MAC_CHECK")
    public Boolean getMacCheck() {
        return this.macCheck;
    }

    public void setMacCheck(Boolean macCheck) {
        this.macCheck = macCheck;
    }

    @Column(name = "HASH_CHECK")
    public Boolean getHashCheck() {
        return this.hashCheck;
    }

    public void setHashCheck(Boolean hashCheck) {
        this.hashCheck = hashCheck;
    }

    @Column(name = "FILE_PATH", length = 256)
    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "RECORD_COUNT")
    public Integer getRecordCount() {
        return this.recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    @Column(name = "LAST_UPDATED_USER", length = 128)
    public String getLastUpdatedUser() {
        return this.lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED_TIME", length = 19)
    public Date getLastUpdatedTime() {
        return this.lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", length = 19)
    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "RESULT_TABLE_NAME", length = 64)
    public String getResultTableName() {
        return this.resultTableName;
    }

    public void setResultTableName(String resultTableName) {
        this.resultTableName = resultTableName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dlbWbWiningFile")
    public Set<DlbWbWiningFileApprovalHistory> getDlbWbWiningFileApprovalHistories() {
        return this.dlbWbWiningFileApprovalHistories;
    }

    public void setDlbWbWiningFileApprovalHistories(Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistories) {
        this.dlbWbWiningFileApprovalHistories = dlbWbWiningFileApprovalHistories;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dlbWbWiningFile")
    public Set<DlbWbWiningFileErrorDetails> getDlbWbWiningFileErrorDetailses() {
        return this.dlbWbWiningFileErrorDetailses;
    }

    public void setDlbWbWiningFileErrorDetailses(Set<DlbWbWiningFileErrorDetails> dlbWbWiningFileErrorDetailses) {
        this.dlbWbWiningFileErrorDetailses = dlbWbWiningFileErrorDetailses;
    }

    @Transient
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Transient
    public int getNoOfTicket() {
        return noOfTicket;
    }

    public void setNoOfTicket(int noOfTicket) {
        this.noOfTicket = noOfTicket;
    }

    @Transient
    public Double getRsvAmount() {
        return rsvAmount;
    }

    public void setRsvAmount(Double rsvAmount) {
        this.rsvAmount = rsvAmount;
    }

    @Transient
    public Double getEpicAmount() {
        return epicAmount;
    }

    public void setEpicAmount(Double epicAmount) {
        this.epicAmount = epicAmount;
    }

    @Transient
    public Double getDlbAmount() {
        return dlbAmount;
    }

    public void setDlbAmount(Double dlbAmount) {
        this.dlbAmount = dlbAmount;
    }

}
