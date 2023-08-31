/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nipuna_k
 */

@Entity
@Table(name="DLB_WB_SUB_AGENT")
public class DlbWbSubAgent {

    private String subAgentCode;
    private String firstName;
    private String lastName;
    private String alternativeContactNo;
    private DlbWbProvince dlbWbProvince;
    private DlbWbDistrict dlbWbDistrict;
    private DlbWbEmployee dlbWbEmployee;
    private Date createdDate;
    private Date lastUpdatedDate;
    private String createdUser;
    private String lastUpdatedUser;
    private DlbWbFile nicFile;
    private DlbWbFile scanCopyPassbookFile;
    private DlbWbFile proofOfAddressFile;
    private DlbWbFile businessRegFile;

    public DlbWbSubAgent(String subAgentCode, String firstName, String lastName, String alternativeContactNo, DlbWbProvince dlbWbProvince, DlbWbDistrict dlbWbDistrict, DlbWbEmployee dlbWbEmployee, Date createdDate, Date lastUpdatedDate, String createdUser, String lastUpdatedUser, DlbWbFile nicFile, DlbWbFile scanCopyPassbookFile, DlbWbFile proofOfAddressFile, DlbWbFile businessRegFile) {
        this.subAgentCode = subAgentCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alternativeContactNo = alternativeContactNo;
        this.dlbWbProvince = dlbWbProvince;
        this.dlbWbDistrict = dlbWbDistrict;
        this.dlbWbEmployee = dlbWbEmployee;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.createdUser = createdUser;
        this.lastUpdatedUser = lastUpdatedUser;
        this.nicFile = nicFile;
        this.scanCopyPassbookFile = scanCopyPassbookFile;
        this.proofOfAddressFile = proofOfAddressFile;
        this.businessRegFile = businessRegFile;
    }

    public DlbWbSubAgent() {
    }

    @Id 
    @Column(name="CODE", unique=true, nullable=false)
    public String getSubAgentCode() {
        return subAgentCode;
    }

    public void setSubAgentCode(String subAgentCode) {
        this.subAgentCode = subAgentCode;
    }

    @Column(name="FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name="LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name="ALT_CONTACT_NO")
    public String getAlternativeContactNo() {
        return alternativeContactNo;
    }

    public void setAlternativeContactNo(String alternativeContactNo) {
        this.alternativeContactNo = alternativeContactNo;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROVINCE")
    public DlbWbProvince getDlbWbProvince() {
        return dlbWbProvince;
    }

    public void setDlbWbProvince(DlbWbProvince dlbWbProvince) {
        this.dlbWbProvince = dlbWbProvince;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DISTRICT")
    public DlbWbDistrict getDlbWbDistrict() {
        return dlbWbDistrict;
    }

    public void setDlbWbDistrict(DlbWbDistrict dlbWbDistrict) {
        this.dlbWbDistrict = dlbWbDistrict;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="EMP_NO")
    public DlbWbEmployee getDlbWbEmployee() {
        return dlbWbEmployee;
    }

    public void setDlbWbEmployee(DlbWbEmployee dlbWbEmployee) {
        this.dlbWbEmployee = dlbWbEmployee;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_UPDATED_DATE")
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Column(name="CREATED_USER")
    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    @Column(name="LAST_UPDATED_USER")
    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NIC_FILE")
    public DlbWbFile getNicFile() {
        return nicFile;
    }

    public void setNicFile(DlbWbFile nicFile) {
        this.nicFile = nicFile;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PASSBOOK_FILE")
    public DlbWbFile getScanCopyPassbookFile() {
        return scanCopyPassbookFile;
    }

    public void setScanCopyPassbookFile(DlbWbFile scanCopyPassbookFile) {
        this.scanCopyPassbookFile = scanCopyPassbookFile;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROOF_OF_ADDRESS_FILE")
    public DlbWbFile getProofOfAddressFile() {
        return proofOfAddressFile;
    }

    public void setProofOfAddressFile(DlbWbFile proofOfAddressFile) {
        this.proofOfAddressFile = proofOfAddressFile;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="BUSINESS_REG_FILE")
    public DlbWbFile getBusinessRegFile() {
        return businessRegFile;
    }

    public void setBusinessRegFile(DlbWbFile businessRegFile) {
        this.businessRegFile = businessRegFile;
    }

    
}
