/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author nipuna_k
 */
@Entity
@Table(name = "DLB_WB_SUB_AGENT_BANK")
public class DlbWbSubAgentBank {
    
    private Integer id;
    private String bankName;
    private String branchName;
    private String accountNo;
    private DlbWbSubAgent dlbWbSubAgent;

    public DlbWbSubAgentBank(Integer id, String bankName, String branchName, String accountNo, DlbWbSubAgent dlbWbSubAgent) {
        this.id = id;
        this.bankName = bankName;
        this.branchName = branchName;
        this.accountNo = accountNo;
        this.dlbWbSubAgent = dlbWbSubAgent;
    }

    public DlbWbSubAgentBank() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "BRANCH_NAME")
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Column(name = "ACCOUNT_NUMBER")
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SUB_AGENT_ID")
    public DlbWbSubAgent getDlbWbSubAgent() {
        return dlbWbSubAgent;
    }

    public void setDlbWbSubAgent(DlbWbSubAgent dlbWbSubAgent) {
        this.dlbWbSubAgent = dlbWbSubAgent;
    }
    
    
}
