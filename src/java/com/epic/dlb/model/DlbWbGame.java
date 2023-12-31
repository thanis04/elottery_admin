package com.epic.dlb.model;
// Generated Mar 27, 2020 3:49:15 PM by Hibernate Tools 4.3.1


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
import javax.persistence.Version;

/**
 * DlbWbGame generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_GAME"
   
)
public class DlbWbGame  implements java.io.Serializable {


     private Integer id;
     private Date timestamp;
     private DlbStatus dlbStatus;
     private DlbWbGameProfile dlbWbGameProfile;
     private String name;
     private String verificationDigits;
     private Set<DlbWbGamePrize> dlbWbGamePrizes = new HashSet<DlbWbGamePrize>(0);

    public DlbWbGame() {
    }

    public DlbWbGame(Integer id) {
        this.id = id;
    }
    
   

    public DlbWbGame(DlbStatus dlbStatus, DlbWbGameProfile dlbWbGameProfile, String name, String verificationDigits, Set<DlbWbGamePrize> dlbWbGamePrizes) {
       this.dlbStatus = dlbStatus;
       this.dlbWbGameProfile = dlbWbGameProfile;
       this.name = name;
       this.verificationDigits = verificationDigits;
       this.dlbWbGamePrizes = dlbWbGamePrizes;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    @Version@Temporal(TemporalType.TIMESTAMP)
    @Column(name="TIMESTAMP", length=19)
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STATUS")
    public DlbStatus getDlbStatus() {
        return this.dlbStatus;
    }
    
    public void setDlbStatus(DlbStatus dlbStatus) {
        this.dlbStatus = dlbStatus;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="GAME_PROFILE_ID")
    public DlbWbGameProfile getDlbWbGameProfile() {
        return this.dlbWbGameProfile;
    }
    
    public void setDlbWbGameProfile(DlbWbGameProfile dlbWbGameProfile) {
        this.dlbWbGameProfile = dlbWbGameProfile;
    }

    
    @Column(name="NAME")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="VERIFICATION_DIGITS", length=50)
    public String getVerificationDigits() {
        return this.verificationDigits;
    }
    
    public void setVerificationDigits(String verificationDigits) {
        this.verificationDigits = verificationDigits;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbGame")
    public Set<DlbWbGamePrize> getDlbWbGamePrizes() {
        return this.dlbWbGamePrizes;
    }
    
    public void setDlbWbGamePrizes(Set<DlbWbGamePrize> dlbWbGamePrizes) {
        this.dlbWbGamePrizes = dlbWbGamePrizes;
    }




}


