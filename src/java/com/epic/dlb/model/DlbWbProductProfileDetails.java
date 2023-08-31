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
 * DlbWbProductProfileDetails generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_PRODUCT_PROFILE_DETAILS"
    
)
public class DlbWbProductProfileDetails  implements java.io.Serializable {

    
     private Integer id;
     private DlbWbProduct dlbWbProduct;
     private DlbWbProductItem dlbWbProductItem;
     private DlbWbProductProfile dlbWbProductProfile;
     private DlbWbWeekDay dlbWbWeekDay;
     private String lastUpdatedUser;
     private Date lastUpdatedTime;
     private Date createdTime;
     private Integer itemOrder;

    public DlbWbProductProfileDetails() {
    }

	
    public DlbWbProductProfileDetails(DlbWbProduct dlbWbProduct, DlbWbProductItem dlbWbProductItem, DlbWbProductProfile dlbWbProductProfile, DlbWbWeekDay dlbWbWeekDay) {
        this.dlbWbProduct = dlbWbProduct;
        this.dlbWbProductItem = dlbWbProductItem;
        this.dlbWbProductProfile = dlbWbProductProfile;
        this.dlbWbWeekDay = dlbWbWeekDay;
    }
    public DlbWbProductProfileDetails(DlbWbProduct dlbWbProduct, DlbWbProductItem dlbWbProductItem, DlbWbProductProfile dlbWbProductProfile, DlbWbWeekDay dlbWbWeekDay, String lastUpdatedUser, Date lastUpdatedTime, Date createdTime, Integer itemOrder) {
       this.dlbWbProduct = dlbWbProduct;
       this.dlbWbProductItem = dlbWbProductItem;
       this.dlbWbProductProfile = dlbWbProductProfile;
       this.dlbWbWeekDay = dlbWbWeekDay;
       this.lastUpdatedUser = lastUpdatedUser;
       this.lastUpdatedTime = lastUpdatedTime;
       this.createdTime = createdTime;
       this.itemOrder = itemOrder;
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
    @JoinColumn(name="PRODUCT", nullable=false)
    public DlbWbProduct getDlbWbProduct() {
        return this.dlbWbProduct;
    }
    
    public void setDlbWbProduct(DlbWbProduct dlbWbProduct) {
        this.dlbWbProduct = dlbWbProduct;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ITEM", nullable=false)
    public DlbWbProductItem getDlbWbProductItem() {
        return this.dlbWbProductItem;
    }
    
    public void setDlbWbProductItem(DlbWbProductItem dlbWbProductItem) {
        this.dlbWbProductItem = dlbWbProductItem;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT_PROFILE_ID", nullable=false)
    public DlbWbProductProfile getDlbWbProductProfile() {
        return this.dlbWbProductProfile;
    }
    
    public void setDlbWbProductProfile(DlbWbProductProfile dlbWbProductProfile) {
        this.dlbWbProductProfile = dlbWbProductProfile;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DAY", nullable=false)
    public DlbWbWeekDay getDlbWbWeekDay() {
        return this.dlbWbWeekDay;
    }
    
    public void setDlbWbWeekDay(DlbWbWeekDay dlbWbWeekDay) {
        this.dlbWbWeekDay = dlbWbWeekDay;
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

    
    @Column(name="ITEM_ORDER")
    public Integer getItemOrder() {
        return this.itemOrder;
    }
    
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }




}


