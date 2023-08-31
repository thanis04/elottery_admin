/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nipuna_k
 */
public class SubAgentDto {

    private String subAgentCode;
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String province;
    private String district;
    private String email;
    private String mobile;
    private String alternativeContactNo;
    private String nic;
    private String nob;
    private String regAddress;
    private String businessRegNo;
    private String businessEmail;
    private String businessPhoneNo;
    private String bankName;
    private String branchName;
    private String accoutNumber;
    private MultipartFile nicFile;
    private MultipartFile scanCopyPassbookFile;
    private MultipartFile proofOfAddressFile;
    private MultipartFile businessRegFile;

    public SubAgentDto() {
    }

    public SubAgentDto(String subAgentCode, String firstName, String lastName, String gender, String address, String province, String district, String email, String mobile, String alternativeContactNo, String nic, String nob, String regAddress, String businessRegNo, String businessEmail, String businessPhoneNo, String bankName, String branchName, String accoutNumber, MultipartFile nicFile, MultipartFile scanCopyPassbookFile, MultipartFile proofOfAddressFile, MultipartFile businessRegFile) {
        this.subAgentCode = subAgentCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.province = province;
        this.district = district;
        this.email = email;
        this.mobile = mobile;
        this.alternativeContactNo = alternativeContactNo;
        this.nic = nic;
        this.nob = nob;
        this.regAddress = regAddress;
        this.businessRegNo = businessRegNo;
        this.businessEmail = businessEmail;
        this.businessPhoneNo = businessPhoneNo;
        this.bankName = bankName;
        this.branchName = branchName;
        this.accoutNumber = accoutNumber;
        this.nicFile = nicFile;
        this.scanCopyPassbookFile = scanCopyPassbookFile;
        this.proofOfAddressFile = proofOfAddressFile;
        this.businessRegFile = businessRegFile;
    }

    public String getSubAgentCode() {
        return subAgentCode;
    }

    public void setSubAgentCode(String subAgentCode) {
        this.subAgentCode = subAgentCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternativeContactNo() {
        return alternativeContactNo;
    }

    public void setAlternativeContactNo(String alternativeContactNo) {
        this.alternativeContactNo = alternativeContactNo;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getNob() {
        return nob;
    }

    public void setNob(String nob) {
        this.nob = nob;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getBusinessRegNo() {
        return businessRegNo;
    }

    public void setBusinessRegNo(String businessRegNo) {
        this.businessRegNo = businessRegNo;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessPhoneNo() {
        return businessPhoneNo;
    }

    public void setBusinessPhoneNo(String businessPhoneNo) {
        this.businessPhoneNo = businessPhoneNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccoutNumber() {
        return accoutNumber;
    }

    public void setAccoutNumber(String accoutNumber) {
        this.accoutNumber = accoutNumber;
    }

    public MultipartFile getNicFile() {
        return nicFile;
    }

    public void setNicFile(MultipartFile nicFile) {
        this.nicFile = nicFile;
    }

    public MultipartFile getScanCopyPassbookFile() {
        return scanCopyPassbookFile;
    }

    public void setScanCopyPassbookFile(MultipartFile scanCopyPassbookFile) {
        this.scanCopyPassbookFile = scanCopyPassbookFile;
    }

    public MultipartFile getProofOfAddressFile() {
        return proofOfAddressFile;
    }

    public void setProofOfAddressFile(MultipartFile proofOfAddressFile) {
        this.proofOfAddressFile = proofOfAddressFile;
    }

    public MultipartFile getBusinessRegFile() {
        return businessRegFile;
    }

    public void setBusinessRegFile(MultipartFile businessRegFile) {
        this.businessRegFile = businessRegFile;
    }

    @Override
    public String toString() {
        return "SubAgentDto{" + "subAgentCode=" + subAgentCode + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", address=" + address + ", province=" + province + ", district=" + district + ", email=" + email + ", mobile=" + mobile + ", alternativeContactNo=" + alternativeContactNo + ", nic=" + nic + ", nob=" + nob + ", regAddress=" + regAddress + ", businessRegNo=" + businessRegNo + ", businessEmail=" + businessEmail + ", businessPhoneNo=" + businessPhoneNo + ", bankName=" + bankName + ", branchName=" + branchName + ", accoutNumber=" + accoutNumber + ", nicFile=" + nicFile + ", scanCopyPassbookFile=" + scanCopyPassbookFile + ", proofOfAddressFile=" + proofOfAddressFile + ", businessRegFile=" + businessRegFile + '}';
    }
    
    

}
