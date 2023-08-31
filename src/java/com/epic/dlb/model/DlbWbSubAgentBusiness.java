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
@Table(name = "DLB_WB_SUB_AGENT_BUSINESS")
public class DlbWbSubAgentBusiness {

    private Integer id;
    private String name;
    private String registrationAddress;
    private String registrationNumber;
    private String email;
    private String contactNo;
    private DlbWbSubAgent dlbWbSubAgent;

    public DlbWbSubAgentBusiness(Integer id, String name, String registrationAddress, String registrationNumber, String email, String contactNo, DlbWbSubAgent dlbWbSubAgent) {
        this.id = id;
        this.name = name;
        this.registrationAddress = registrationAddress;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.contactNo = contactNo;
        this.dlbWbSubAgent = dlbWbSubAgent;
    }

    public DlbWbSubAgentBusiness() {
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

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "REGISTERD_ADDRESS")
    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    @Column(name = "REGISTRATION_NUMBER")
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "CONTACT_NO")
    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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
