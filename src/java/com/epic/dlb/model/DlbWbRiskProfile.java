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

/**
 * DlbWbRiskProfile generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_RISK_PROFILE"
    
)
public class DlbWbRiskProfile  implements java.io.Serializable {


     private Integer profileId;
     private DlbDeviceProfile dlbDeviceProfile;
     private DlbStatus dlbStatus;
     private String description;
     private String value;
     private String lastupdateduser;
     private Date lastupdatedtime;
     private Date createdtime;

    public DlbWbRiskProfile() {
    }

	
    public DlbWbRiskProfile(DlbDeviceProfile dlbDeviceProfile, String description, String value) {
        this.dlbDeviceProfile = dlbDeviceProfile;
        this.description = description;
        this.value = value;
    }
    public DlbWbRiskProfile(DlbDeviceProfile dlbDeviceProfile, DlbStatus dlbStatus, String description, String value, String lastupdateduser, Date lastupdatedtime, Date createdtime) {
       this.dlbDeviceProfile = dlbDeviceProfile;
       this.dlbStatus = dlbStatus;
       this.description = description;
       this.value = value;
       this.lastupdateduser = lastupdateduser;
       this.lastupdatedtime = lastupdatedtime;
       this.createdtime = createdtime;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="PROFILE_ID", unique=true, nullable=false)
    public Integer getProfileId() {
        return this.profileId;
    }
    
    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DEVICE_PROFILE", nullable=false)
    public DlbDeviceProfile getDlbDeviceProfile() {
        return this.dlbDeviceProfile;
    }
    
    public void setDlbDeviceProfile(DlbDeviceProfile dlbDeviceProfile) {
        this.dlbDeviceProfile = dlbDeviceProfile;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STATUS")
    public DlbStatus getDlbStatus() {
        return this.dlbStatus;
    }
    
    public void setDlbStatus(DlbStatus dlbStatus) {
        this.dlbStatus = dlbStatus;
    }

    
    @Column(name="DESCRIPTION", nullable=false, length=64)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    
    @Column(name="VALUE", nullable=false, length=512)
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
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




}


