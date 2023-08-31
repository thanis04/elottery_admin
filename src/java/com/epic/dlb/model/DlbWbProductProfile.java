package com.epic.dlb.model;
// Generated Oct 12, 2018 12:17:21 PM by Hibernate Tools 4.3.1


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

/**
 * DlbWbProductProfile generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_PRODUCT_PROFILE"
  
)
public class DlbWbProductProfile  implements java.io.Serializable {


     private Integer id;
     private DlbStatus dlbStatusByStatus;
     private DlbStatus dlbStatusBySpecialStatus;
     private DlbWbProduct dlbWbProduct;
     private DlbWbWeekDay dlbWbWeekDay;
     private byte[] template;
     private int drawNo;
     private String lastUpdatedUser;
     private Date lastUpdatedTime;
     private Date createdTime;
     private String engLetter;
     private Integer engPos;
     private String bonusNo;
     private Integer bonusPos;
     private Integer specialPos;
     private String zodiac;
     private Integer zodiacPos;
     private DlbWbGameProfile dlbWbGameProfile;
     
     
     private List<DlbWbProductProfileDetails> dlbWbProductProfileDetailses = new ArrayList<DlbWbProductProfileDetails>();
     private Set<DlbWbResult> dlbWbResults = new HashSet<DlbWbResult>(0);
     private Set<DlbWbTicketFile> dlbWbTicketFiles = new HashSet<DlbWbTicketFile>(0);

    public DlbWbProductProfile() {
    }

	
    public DlbWbProductProfile(DlbStatus dlbStatusBySpecialStatus, DlbWbProduct dlbWbProduct, DlbWbWeekDay dlbWbWeekDay, int drawNo) {
        this.dlbStatusBySpecialStatus = dlbStatusBySpecialStatus;
        this.dlbWbProduct = dlbWbProduct;
        this.dlbWbWeekDay = dlbWbWeekDay;
        this.drawNo = drawNo;
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
    @JoinColumn(name="SPECIAL_STATUS", nullable=false)
    public DlbStatus getDlbStatusBySpecialStatus() {
        return this.dlbStatusBySpecialStatus;
    }
    
    public void setDlbStatusBySpecialStatus(DlbStatus dlbStatusBySpecialStatus) {
        this.dlbStatusBySpecialStatus = dlbStatusBySpecialStatus;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT", nullable=false)
    public DlbWbProduct getDlbWbProduct() {
        return this.dlbWbProduct;
    }
    
    public void setDlbWbProduct(DlbWbProduct dlbWbProduct) {
        this.dlbWbProduct = dlbWbProduct;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DAY", nullable=false)
    public DlbWbWeekDay getDlbWbWeekDay() {
        return this.dlbWbWeekDay;
    }
    
    public void setDlbWbWeekDay(DlbWbWeekDay dlbWbWeekDay) {
        this.dlbWbWeekDay = dlbWbWeekDay;
    }

    
    @Column(name="TEMPLATE")
    public byte[] getTemplate() {
        return this.template;
    }
    
    public void setTemplate(byte[] template) {
        this.template = template;
    }

    
    @Column(name="DRAW_NO", nullable=false)
    public int getDrawNo() {
        return this.drawNo;
    }
    
    public void setDrawNo(int drawNo) {
        this.drawNo = drawNo;
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

    
    @Column(name="ENG_LETTER", length=3)
    public String getEngLetter() {
        return this.engLetter;
    }
    
    public void setEngLetter(String engLetter) {
        this.engLetter = engLetter;
    }

    
    @Column(name="ENG_POS")
    public Integer getEngPos() {
        return this.engPos;
    }
    
    public void setEngPos(Integer engPos) {
        this.engPos = engPos;
    }

     @Column(name="SPECIAL_POS")
    public Integer getSpecialPos() {
        return specialPos;
    }

    public void setSpecialPos(Integer specialPos) {
        this.specialPos = specialPos;
    }
    


    
    @Column(name="BONUS_NO", length=3)
    public String getBonusNo() {
        return this.bonusNo;
    }
    
    public void setBonusNo(String bonusNo) {
        this.bonusNo = bonusNo;
    }

    
    @Column(name="BONUS_POS")
    public Integer getBonusPos() {
        return this.bonusPos;
    }
    
    public void setBonusPos(Integer bonusPos) {
        this.bonusPos = bonusPos;
    }

    
    @Column(name="ZODIAC", length=3)
    public String getZodiac() {
        return this.zodiac;
    }
    
    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    
    @Column(name="ZODIAC_POS")
    public Integer getZodiacPos() {
        return this.zodiacPos;
    }
    
    public void setZodiacPos(Integer zodiacPos) {
        this.zodiacPos = zodiacPos;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbProductProfile")
    public List<DlbWbProductProfileDetails> getDlbWbProductProfileDetailses() {
        return this.dlbWbProductProfileDetailses;
    }
    
    public void setDlbWbProductProfileDetailses(List<DlbWbProductProfileDetails> dlbWbProductProfileDetailses) {
        this.dlbWbProductProfileDetailses = dlbWbProductProfileDetailses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbProductProfile")
    public Set<DlbWbResult> getDlbWbResults() {
        return this.dlbWbResults;
    }
    
    public void setDlbWbResults(Set<DlbWbResult> dlbWbResults) {
        this.dlbWbResults = dlbWbResults;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbWbProductProfile")
    public Set<DlbWbTicketFile> getDlbWbTicketFiles() {
        return this.dlbWbTicketFiles;
    }
    
    public void setDlbWbTicketFiles(Set<DlbWbTicketFile> dlbWbTicketFiles) {
        this.dlbWbTicketFiles = dlbWbTicketFiles;
    }

    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="GAME_PROFILE", nullable=false)
    public DlbWbGameProfile getDlbWbGameProfile() {
        return dlbWbGameProfile;
    }

    public void setDlbWbGameProfile(DlbWbGameProfile dlbWbGameProfile) {
        this.dlbWbGameProfile = dlbWbGameProfile;
    }

    



}


