package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DlbWbWorkflow generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_WORKFLOW"
    
)
public class DlbWbWorkflow  implements java.io.Serializable {


     private String processCode;
     private String description;
     private String lastUpdatedUser;
     private Date lastUpdatedTime;
     private Date createdTime;
     private Set<DlbWbWiningFileHistory> dlbWbWiningFileHistories = new HashSet<DlbWbWiningFileHistory>(0);
     private Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistoriesForDestinationStage = new HashSet<DlbWbWiningFileApprovalHistory>(0);
     private Set<DlbWbWiningFile> dlbWbWiningFiles = new HashSet<DlbWbWiningFile>(0);
     private Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistoriesForInitialStage = new HashSet<DlbWbWiningFileApprovalHistory>(0);

    public DlbWbWorkflow() {
    }

	
    public DlbWbWorkflow(String processCode, String description) {
        this.processCode = processCode;
        this.description = description;
    }
    public DlbWbWorkflow(String processCode, String description, String lastUpdatedUser, Date lastUpdatedTime, Date createdTime, Set<DlbWbWiningFileHistory> dlbWbWiningFileHistories, Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistoriesForDestinationStage, Set<DlbWbWiningFile> dlbWbWiningFiles, Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistoriesForInitialStage) {
       this.processCode = processCode;
       this.description = description;
       this.lastUpdatedUser = lastUpdatedUser;
       this.lastUpdatedTime = lastUpdatedTime;
       this.createdTime = createdTime;
       this.dlbWbWiningFileHistories = dlbWbWiningFileHistories;
       this.dlbWbWiningFileApprovalHistoriesForDestinationStage = dlbWbWiningFileApprovalHistoriesForDestinationStage;
       this.dlbWbWiningFiles = dlbWbWiningFiles;
       this.dlbWbWiningFileApprovalHistoriesForInitialStage = dlbWbWiningFileApprovalHistoriesForInitialStage;
    }
   
     @Id 

    
    @Column(name="PROCESS_CODE", unique=true, nullable=false, length=16)
    public String getProcessCode() {
        return this.processCode;
    }
    
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    
    @Column(name="DESCRIPTION", nullable=false, length=64)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    
    @Column(name="LAST_UPDATED_USER", length=128)
    public String getLastUpdatedUser() {
        return this.lastUpdatedUser;
    }
    
    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_UPDATED_TIME", length=19)
    public Date getLastUpdatedTime() {
        return this.lastUpdatedTime;
    }
    
    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_TIME", length=19)
    public Date getCreatedTime() {
        return this.createdTime;
    }
    
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbWorkflow")
    public Set<DlbWbWiningFileHistory> getDlbWbWiningFileHistories() {
        return this.dlbWbWiningFileHistories;
    }
    
    public void setDlbWbWiningFileHistories(Set<DlbWbWiningFileHistory> dlbWbWiningFileHistories) {
        this.dlbWbWiningFileHistories = dlbWbWiningFileHistories;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbWorkflowByDestinationStage")
    public Set<DlbWbWiningFileApprovalHistory> getDlbWbWiningFileApprovalHistoriesForDestinationStage() {
        return this.dlbWbWiningFileApprovalHistoriesForDestinationStage;
    }
    
    public void setDlbWbWiningFileApprovalHistoriesForDestinationStage(Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistoriesForDestinationStage) {
        this.dlbWbWiningFileApprovalHistoriesForDestinationStage = dlbWbWiningFileApprovalHistoriesForDestinationStage;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbWorkflow")
    public Set<DlbWbWiningFile> getDlbWbWiningFiles() {
        return this.dlbWbWiningFiles;
    }
    
    public void setDlbWbWiningFiles(Set<DlbWbWiningFile> dlbWbWiningFiles) {
        this.dlbWbWiningFiles = dlbWbWiningFiles;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbWorkflowByInitialStage")
    public Set<DlbWbWiningFileApprovalHistory> getDlbWbWiningFileApprovalHistoriesForInitialStage() {
        return this.dlbWbWiningFileApprovalHistoriesForInitialStage;
    }
    
    public void setDlbWbWiningFileApprovalHistoriesForInitialStage(Set<DlbWbWiningFileApprovalHistory> dlbWbWiningFileApprovalHistoriesForInitialStage) {
        this.dlbWbWiningFileApprovalHistoriesForInitialStage = dlbWbWiningFileApprovalHistoriesForInitialStage;
    }




}

