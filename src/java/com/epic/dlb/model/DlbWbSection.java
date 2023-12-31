package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DlbWbSection generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_SECTION"
    
)
public class DlbWbSection  implements java.io.Serializable {


     private String sectioncode;
     private DlbStatus dlbStatus;
     private String description;
     private String url;
     private String lastupdateduser;
     private Date lastupdatedtime;
     private Date createdtime;
     private Set<DlbWbUserPriviledge> dlbWbUserPriviledges = new HashSet<DlbWbUserPriviledge>(0);
     private Set<DlbWbSectionPriviledge> dlbWbSectionPriviledges = new HashSet<DlbWbSectionPriviledge>(0);
     private Set<DlbWbSystemPriviledge> dlbWbSystemPriviledges = new HashSet<DlbWbSystemPriviledge>(0);

    public DlbWbSection() {
    }

	
    public DlbWbSection(String sectioncode) {
        this.sectioncode = sectioncode;
    }
    public DlbWbSection(String sectioncode, DlbStatus dlbStatus, String description, String url, String lastupdateduser, Date lastupdatedtime, Date createdtime, Set<DlbWbUserPriviledge> dlbWbUserPriviledges, Set<DlbWbSectionPriviledge> dlbWbSectionPriviledges, Set<DlbWbSystemPriviledge> dlbWbSystemPriviledges) {
       this.sectioncode = sectioncode;
       this.dlbStatus = dlbStatus;
       this.description = description;
       this.url = url;
       this.lastupdateduser = lastupdateduser;
       this.lastupdatedtime = lastupdatedtime;
       this.createdtime = createdtime;
       this.dlbWbUserPriviledges = dlbWbUserPriviledges;
       this.dlbWbSectionPriviledges = dlbWbSectionPriviledges;
       this.dlbWbSystemPriviledges = dlbWbSystemPriviledges;
    }
   
     @Id 

    
    @Column(name="SECTIONCODE", unique=true, nullable=false, length=16)
    public String getSectioncode() {
        return this.sectioncode;
    }
    
    public void setSectioncode(String sectioncode) {
        this.sectioncode = sectioncode;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STATUS")
    public DlbStatus getDlbStatus() {
        return this.dlbStatus;
    }
    
    public void setDlbStatus(DlbStatus dlbStatus) {
        this.dlbStatus = dlbStatus;
    }

    
    @Column(name="DESCRIPTION", length=64)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    
    @Column(name="URL", length=1024)
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    
    @Column(name="LASTUPDATEDUSER", length=128)
    public String getLastupdateduser() {
        return this.lastupdateduser;
    }
    
    public void setLastupdateduser(String lastupdateduser) {
        this.lastupdateduser = lastupdateduser;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LASTUPDATEDTIME", length=19)
    public Date getLastupdatedtime() {
        return this.lastupdatedtime;
    }
    
    public void setLastupdatedtime(Date lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATEDTIME", length=19)
    public Date getCreatedtime() {
        return this.createdtime;
    }
    
    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbSection")
    public Set<DlbWbUserPriviledge> getDlbWbUserPriviledges() {
        return this.dlbWbUserPriviledges;
    }
    
    public void setDlbWbUserPriviledges(Set<DlbWbUserPriviledge> dlbWbUserPriviledges) {
        this.dlbWbUserPriviledges = dlbWbUserPriviledges;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbSection")
    public Set<DlbWbSectionPriviledge> getDlbWbSectionPriviledges() {
        return this.dlbWbSectionPriviledges;
    }
    
    public void setDlbWbSectionPriviledges(Set<DlbWbSectionPriviledge> dlbWbSectionPriviledges) {
        this.dlbWbSectionPriviledges = dlbWbSectionPriviledges;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbSection")
    public Set<DlbWbSystemPriviledge> getDlbWbSystemPriviledges() {
        return this.dlbWbSystemPriviledges;
    }
    
    public void setDlbWbSystemPriviledges(Set<DlbWbSystemPriviledge> dlbWbSystemPriviledges) {
        this.dlbWbSystemPriviledges = dlbWbSystemPriviledges;
    }




}


