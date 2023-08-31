/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.SubAgentDto;
import com.epic.dlb.email.service.EmailService;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbDistrict;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbFile;
import com.epic.dlb.model.DlbWbProvince;
import com.epic.dlb.model.DlbWbSubAgent;
import com.epic.dlb.model.DlbWbSubAgentBank;
import com.epic.dlb.model.DlbWbSubAgentBusiness;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.MessageVarList;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nipuna_k
 */
@Service("subAgentManagementService")
public class SubAgentManagementService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ActivityLogService activityLogService;

    @Transactional(rollbackFor = Exception.class)
    public JSONObject startRegistration(String formData, MultipartFile nicFile,
            MultipartFile scanCopyPassbookFile, MultipartFile proofOfAddressFile,
            MultipartFile businessRegFile, DlbWbSystemUser systemUser) throws Exception {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        SubAgentDto subAgentDto = bulidObject(
                formData, nicFile, scanCopyPassbookFile, proofOfAddressFile, businessRegFile);
        if (employeeService.get(subAgentDto.getSubAgentCode()) == null) {
            save(subAgentDto, systemUser);
            saveAudit(formData, subAgentDto, systemUser);
            msg = "Sub-Agent " + MessageVarList.ADD_SUC;
            status = SystemVarList.SUCCESS;
        } else {
            msg = "Sub-Agent Code is already exist in the system";
            status = SystemVarList.WARNING;
        }
        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject startUpdate(String formData, MultipartFile nicFile,
            MultipartFile scanCopyPassbookFile, MultipartFile proofOfAddressFile,
            MultipartFile businessRegFile, DlbWbSystemUser systemUser) throws Exception {
        JSONObject response = new JSONObject();
        String msg = MessageVarList.COMMON_ERR;
        String status = SystemVarList.ERROR;
        SubAgentDto subAgentDto = bulidObject(
                formData, nicFile, scanCopyPassbookFile, proofOfAddressFile, businessRegFile);

        DlbWbSystemUser previousSystemUser = (DlbWbSystemUser) genericRepository.get(subAgentDto.getSubAgentCode(), DlbWbSystemUser.class);
        DlbWbSubAgent previousDlbWbSubAgent = (DlbWbSubAgent) genericRepository.get(subAgentDto.getSubAgentCode(), DlbWbSubAgent.class);

        WhereStatement businessStatement = new WhereStatement("dlbWbSubAgent.subAgentCode",
                subAgentDto.getSubAgentCode(), SystemVarList.EQUAL);
        DlbWbSubAgentBusiness previousDlbWbSubAgentBusiness = (DlbWbSubAgentBusiness) genericRepository.get(
                DlbWbSubAgentBusiness.class, businessStatement);

        WhereStatement bankStatement = new WhereStatement("dlbWbSubAgent.subAgentCode",
                subAgentDto.getSubAgentCode(), SystemVarList.EQUAL);
        DlbWbSubAgentBank previousDlbWbSubAgentBank = (DlbWbSubAgentBank) genericRepository.get(
                DlbWbSubAgentBank.class, bankStatement);
        saveUpdationAudit(formData, subAgentDto, previousSystemUser, previousDlbWbSubAgent, previousDlbWbSubAgentBusiness, previousDlbWbSubAgentBank, systemUser);
        update(subAgentDto, systemUser);
        msg = "Sub-Agent " + MessageVarList.UPDATED_SUC;
        status = SystemVarList.SUCCESS;

        response.put(SystemVarList.STATUS, status);
        response.put(SystemVarList.MESSAGE, msg);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SubAgentDto subAgentDto, DlbWbSystemUser operationUser) throws Exception {
        saveEmployee(subAgentDto, operationUser);
        saveUser(subAgentDto, (DlbWbEmployee) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbEmployee.class), operationUser);
        saveSubAgent(subAgentDto, (DlbWbEmployee) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbEmployee.class), operationUser);
        saveSubAgentBusiness(subAgentDto, (DlbWbSubAgent) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbSubAgent.class), operationUser);
        saveSubAgentBank(subAgentDto, (DlbWbSubAgent) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbSubAgent.class), operationUser);

        emailService.sendCreatePasswordEmail((DlbWbSystemUser) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbSystemUser.class));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SubAgentDto subAgentDto, DlbWbSystemUser operationUser) throws Exception {
        updateEmployee(subAgentDto, operationUser);
        updateSubAgent(subAgentDto, (DlbWbEmployee) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbEmployee.class), operationUser);
        updateSubAgentBusiness(subAgentDto, (DlbWbSubAgent) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbSubAgent.class), operationUser);
        updateSubAgentBank(subAgentDto, (DlbWbSubAgent) genericRepository.
                get(subAgentDto.getSubAgentCode(), DlbWbSubAgent.class), operationUser);

    }

    @Transactional(rollbackFor = Exception.class)
    public SubAgentDto bulidObject(String formData, MultipartFile nicFile, MultipartFile scanCopyPassbookFile,
            MultipartFile proofOfAddressFile, MultipartFile businessRegFile) throws Exception {
        JSONParser jSONParser = new JSONParser();
        JSONObject jSONObject = (JSONObject) jSONParser.parse(formData);
        SubAgentDto subAgentDto = new SubAgentDto();
        subAgentDto.setSubAgentCode((String) jSONObject.get("subAgentCode"));
        subAgentDto.setFirstName((String) jSONObject.get("firstName"));
        subAgentDto.setLastName((String) jSONObject.get("lastName"));
        subAgentDto.setAddress((String) jSONObject.get("address"));
        subAgentDto.setProvince((String) jSONObject.get("province"));
        subAgentDto.setDistrict((String) jSONObject.get("district"));
        subAgentDto.setEmail((String) jSONObject.get("email"));
        subAgentDto.setMobile((String) jSONObject.get("mobile"));
        subAgentDto.setAlternativeContactNo((String) jSONObject.get("alternativeContactNo"));
        subAgentDto.setNic((String) jSONObject.get("nic"));

        subAgentDto.setNob((String) jSONObject.get("nob"));
        subAgentDto.setRegAddress((String) jSONObject.get("regAddress"));
        subAgentDto.setBusinessRegNo((String) jSONObject.get("businessRegNo"));
        subAgentDto.setBusinessEmail((String) jSONObject.get("businessEmail"));
        subAgentDto.setBusinessPhoneNo((String) jSONObject.get("businessPhoneNo"));

        subAgentDto.setBankName((String) jSONObject.get("bankName"));
        subAgentDto.setBranchName((String) jSONObject.get("branchName"));
        subAgentDto.setAccoutNumber((String) jSONObject.get("accoutNumber"));

        subAgentDto.setNicFile(nicFile);
        subAgentDto.setScanCopyPassbookFile(scanCopyPassbookFile);
        subAgentDto.setProofOfAddressFile(proofOfAddressFile);
        subAgentDto.setBusinessRegFile(businessRegFile);

        return subAgentDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveEmployee(SubAgentDto subAgentDto,
            DlbWbSystemUser dlbWbSystemUser) throws Exception {
        DlbWbEmployee dlbWbEmployee = new DlbWbEmployee();
        dlbWbEmployee.setEmployeeid(subAgentDto.getSubAgentCode());
        dlbWbEmployee.setName(subAgentDto.getFirstName() + " " + subAgentDto.getLastName());
        dlbWbEmployee.setNic(subAgentDto.getNic());
        dlbWbEmployee.setAddress(subAgentDto.getAddress());
        dlbWbEmployee.setEmail(subAgentDto.getEmail());
        dlbWbEmployee.setContactno(subAgentDto.getMobile());
        dlbWbEmployee.setDlbStatus((DlbStatus) genericRepository.
                get(SystemVarList.ACTIVE, DlbStatus.class));
        dlbWbEmployee.setCreatedtime(new Date());
        dlbWbEmployee.setLastupdatedtime(new Date());
        dlbWbEmployee.setLastupdateduser(dlbWbSystemUser.getUsername());
        genericRepository.save(dlbWbEmployee);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SubAgentDto subAgentDto,
            DlbWbEmployee dlbWbEmployee,
            DlbWbSystemUser user) throws Exception {

        DlbWbSystemUser dlbWbSystemUser = new DlbWbSystemUser();
        dlbWbSystemUser.setUsername(subAgentDto.getSubAgentCode());
        dlbWbSystemUser.setPassword(generatePassword());

        dlbWbSystemUser.setDlbWbUserRole((DlbWbUserRole) genericRepository.
                get("Sub Agents", DlbWbUserRole.class));
        dlbWbSystemUser.setCreatedtime(new Date());
        dlbWbSystemUser.setDlbStatus((DlbStatus) genericRepository.
                get(SystemVarList.ACTIVE, DlbStatus.class));
        dlbWbSystemUser.setLastupdatedtime(new Date());
        dlbWbSystemUser.setLastupdateduser(user.getUsername());
        dlbWbSystemUser.setDlbWbEmployee(dlbWbEmployee);
        userService.save(dlbWbSystemUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSubAgent(SubAgentDto subAgentDto,
            DlbWbEmployee dlbWbEmployee, DlbWbSystemUser user) throws Exception {
        DlbWbSubAgent dlbWbSubAgent = new DlbWbSubAgent();
        dlbWbSubAgent.setSubAgentCode(subAgentDto.getSubAgentCode());
        dlbWbSubAgent.setFirstName(subAgentDto.getFirstName());
        dlbWbSubAgent.setLastName(subAgentDto.getLastName());
        dlbWbSubAgent.setDlbWbProvince((DlbWbProvince) genericRepository.get(
                Integer.parseInt(subAgentDto.getProvince()), DlbWbProvince.class));
        dlbWbSubAgent.setDlbWbDistrict((DlbWbDistrict) genericRepository.get(
                Integer.parseInt(subAgentDto.getDistrict()), DlbWbDistrict.class));
        dlbWbSubAgent.setDlbWbEmployee(dlbWbEmployee);
        dlbWbSubAgent.setCreatedUser(user.getUsername());
        dlbWbSubAgent.setLastUpdatedUser(user.getUsername());
        dlbWbSubAgent.setCreatedDate(new Date());
        dlbWbSubAgent.setLastUpdatedDate(new Date());
        dlbWbSubAgent.setNicFile(saveNicFile(subAgentDto));
        dlbWbSubAgent.setScanCopyPassbookFile(savePassbookFile(subAgentDto));

        if (subAgentDto.getProofOfAddressFile() != null) {
            dlbWbSubAgent.setProofOfAddressFile(saveAddressFile(subAgentDto));
        }
        if (subAgentDto.getBusinessRegFile() != null) {
            dlbWbSubAgent.setBusinessRegFile(saveRegFile(subAgentDto));
        }
        dlbWbSubAgent.setAlternativeContactNo(subAgentDto.getAlternativeContactNo());
        genericRepository.save(dlbWbSubAgent);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSubAgentBusiness(SubAgentDto subAgentDto,
            DlbWbSubAgent dlbWbSubAgent, DlbWbSystemUser user) throws Exception {
        if (!(subAgentDto.getNob().equals("")
                && subAgentDto.getRegAddress().equals("")
                && subAgentDto.getBusinessRegNo().equals("")
                && subAgentDto.getBusinessEmail().equals("")
                && subAgentDto.getBusinessPhoneNo().equals(""))) {
            DlbWbSubAgentBusiness dlbWbSubAgentBusiness = new DlbWbSubAgentBusiness();
            dlbWbSubAgentBusiness.setName(subAgentDto.getNob());
            dlbWbSubAgentBusiness.setRegistrationAddress(subAgentDto.getRegAddress());
            dlbWbSubAgentBusiness.setRegistrationNumber(subAgentDto.getBusinessRegNo());
            dlbWbSubAgentBusiness.setEmail(subAgentDto.getBusinessEmail());
            dlbWbSubAgentBusiness.setContactNo(subAgentDto.getBusinessPhoneNo());
            dlbWbSubAgentBusiness.setDlbWbSubAgent(dlbWbSubAgent);
            genericRepository.save(dlbWbSubAgentBusiness);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSubAgentBank(SubAgentDto subAgentDto,
            DlbWbSubAgent dlbWbSubAgent, DlbWbSystemUser user) throws Exception {
        DlbWbSubAgentBank dlbWbSubAgentBank = new DlbWbSubAgentBank();
        dlbWbSubAgentBank.setBankName(subAgentDto.getBankName());
        dlbWbSubAgentBank.setBranchName(subAgentDto.getBranchName());
        dlbWbSubAgentBank.setAccountNo(subAgentDto.getAccoutNumber());
        dlbWbSubAgentBank.setDlbWbSubAgent(dlbWbSubAgent);
        genericRepository.save(dlbWbSubAgentBank);
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbFile saveNicFile(SubAgentDto subAgentDto) throws Exception {
        DlbWbFile dlbWbFile = new DlbWbFile();
        dlbWbFile.setFile(getBlob(subAgentDto.getNicFile()));
        dlbWbFile.setType(subAgentDto.getNicFile().getContentType());
        Serializable serializable = genericRepository.save(dlbWbFile);
        return (DlbWbFile) genericRepository.get(Integer.parseInt(serializable.toString()), DlbWbFile.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbFile savePassbookFile(SubAgentDto subAgentDto) throws Exception {
        DlbWbFile dlbWbFile = new DlbWbFile();
        dlbWbFile.setFile(getBlob(subAgentDto.getScanCopyPassbookFile()));
        dlbWbFile.setType(subAgentDto.getScanCopyPassbookFile().getContentType());
        Serializable serializable = genericRepository.save(dlbWbFile);
        return (DlbWbFile) genericRepository.get(Integer.parseInt(serializable.toString()), DlbWbFile.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbFile saveAddressFile(SubAgentDto subAgentDto) throws Exception {
        DlbWbFile dlbWbFile = new DlbWbFile();
        dlbWbFile.setFile(getBlob(subAgentDto.getProofOfAddressFile()));
        dlbWbFile.setType(subAgentDto.getProofOfAddressFile().getContentType());
        Serializable serializable = genericRepository.save(dlbWbFile);
        return (DlbWbFile) genericRepository.get(Integer.parseInt(serializable.toString()), DlbWbFile.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbFile saveRegFile(SubAgentDto subAgentDto) throws Exception {
        DlbWbFile dlbWbFile = new DlbWbFile();
        dlbWbFile.setFile(getBlob(subAgentDto.getBusinessRegFile()));
        dlbWbFile.setType(subAgentDto.getBusinessRegFile().getContentType());
        Serializable serializable = genericRepository.save(dlbWbFile);
        return (DlbWbFile) genericRepository.get(Integer.parseInt(serializable.toString()), DlbWbFile.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public Blob getBlob(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        Blob blob = new SerialBlob(bytes);
        return blob;
    }

    @Transactional(rollbackFor = Exception.class)
    public String generatePassword() {
        String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * key.length());
            salt.append(key.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAudit(String formData, SubAgentDto subAgentDto, DlbWbSystemUser operationUser) throws Exception {
        DlbWbSystemUser dlbWbSystemUser = userService.get(subAgentDto.getSubAgentCode());
        JSONObject employee = new JSONObject();
        employee.put("Employee Id", dlbWbSystemUser.getDlbWbEmployee().getEmployeeid());
        employee.put("Employee Name", dlbWbSystemUser.getDlbWbEmployee().getName());
        employee.put("NIC", dlbWbSystemUser.getDlbWbEmployee().getNic());
        employee.put("Address", dlbWbSystemUser.getDlbWbEmployee().getAddress());
        employee.put("Email", dlbWbSystemUser.getDlbWbEmployee().getEmail());
        employee.put("Contact No", dlbWbSystemUser.getDlbWbEmployee().getContactno());
        employee.put("Status", dlbWbSystemUser.getDlbWbEmployee().getDlbStatus().getStatusCode());
        activityLogService.save(activityLogService.buildActivityLog("SAVE", employee, "EM", operationUser));

        JSONObject systemUser = new JSONObject();
        systemUser.put("User Name", dlbWbSystemUser.getUsername());
        systemUser.put("Employee Id", dlbWbSystemUser.getDlbWbEmployee().getEmployeeid());
        systemUser.put("Status", dlbWbSystemUser.getDlbStatus().getStatusCode());
        systemUser.put("User Role", dlbWbSystemUser.getDlbWbUserRole().getUserrolecode());
        activityLogService.save(activityLogService.buildActivityLog(
                "SAVE", systemUser, "UM", operationUser));

        JSONParser jSONParser = new JSONParser();
        JSONObject jSONObject = (JSONObject) jSONParser.parse(formData);
        JSONObject subAgentUser = new JSONObject();
        subAgentUser.put("Sub Agent Code", (String) jSONObject.get("subAgentCode"));
        subAgentUser.put("Sub Agent Name", (String) jSONObject.get("firstName") + " " + (String) jSONObject.get("lastName"));
        subAgentUser.put("Gender", (String) jSONObject.get("gender"));
        subAgentUser.put("Address", (String) jSONObject.get("address"));

        DlbWbDistrict dlbWbDistrict = (DlbWbDistrict) genericRepository.get(
                Integer.parseInt(subAgentDto.getDistrict()), DlbWbDistrict.class);

        subAgentUser.put("District", dlbWbDistrict.getName());
        subAgentUser.put("Email", (String) jSONObject.get("email"));
        subAgentUser.put("Mobile", (String) jSONObject.get("mobile"));
        subAgentUser.put("Alternative Contact No", (String) jSONObject.get("alternativeContactNo"));
        subAgentUser.put("NIC", (String) jSONObject.get("nic"));
        subAgentUser.put("Name of Business", (String) jSONObject.get("nob"));
        subAgentUser.put("Business Registration Address", (String) jSONObject.get("regAddress"));
        subAgentUser.put("Business Registration No", (String) jSONObject.get("businessRegNo"));
        subAgentUser.put("Business Email", (String) jSONObject.get("businessEmail"));
        subAgentUser.put("Business Phone No", (String) jSONObject.get("businessPhoneNo"));
        subAgentUser.put("Bank Name", (String) jSONObject.get("bankName"));
        subAgentUser.put("Branch Name", (String) jSONObject.get("branchName"));
        subAgentUser.put("Accout Number", (String) jSONObject.get("accoutNumber"));
        activityLogService.save(activityLogService.buildActivityLog(
                "SAVE", subAgentUser, "SBARE", operationUser));
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject getSubAgent(String agentCode) throws SQLException, UnsupportedEncodingException {
        JSONObject subAgent = new JSONObject();
        DlbWbSubAgent dlbWbSubAgent = (DlbWbSubAgent) genericRepository.get(agentCode, DlbWbSubAgent.class);
        if (dlbWbSubAgent != null) {

            subAgent.put("subAgentCode", dlbWbSubAgent.getSubAgentCode());
            subAgent.put("firstName", dlbWbSubAgent.getFirstName());
            subAgent.put("lastName", dlbWbSubAgent.getLastName());
            subAgent.put("address", dlbWbSubAgent.getDlbWbEmployee().getAddress());
            subAgent.put("province", dlbWbSubAgent.getDlbWbProvince().getId());
            subAgent.put("district", dlbWbSubAgent.getDlbWbDistrict().getId());
            subAgent.put("districtName", dlbWbSubAgent.getDlbWbDistrict().getName());
            subAgent.put("email", dlbWbSubAgent.getDlbWbEmployee().getEmail());
            subAgent.put("mobile", dlbWbSubAgent.getDlbWbEmployee().getContactno());
            subAgent.put("alternativeContactNo", dlbWbSubAgent.getAlternativeContactNo());
            subAgent.put("nic", dlbWbSubAgent.getDlbWbEmployee().getNic());

            WhereStatement businessStatement = new WhereStatement("dlbWbSubAgent.subAgentCode",
                    agentCode, SystemVarList.EQUAL);
            DlbWbSubAgentBusiness dlbWbSubAgentBusiness = (DlbWbSubAgentBusiness) genericRepository.get(
                    DlbWbSubAgentBusiness.class, businessStatement);
            JSONObject business = new JSONObject();
            if (dlbWbSubAgentBusiness != null) {
                business.put("nob", dlbWbSubAgentBusiness.getName());
                business.put("regAddress", dlbWbSubAgentBusiness.getRegistrationAddress());
                business.put("businessRegNo", dlbWbSubAgentBusiness.getRegistrationNumber());
                business.put("businessEmail", dlbWbSubAgentBusiness.getEmail());
                business.put("businessPhoneNo", dlbWbSubAgentBusiness.getContactNo());
            }
            WhereStatement bankStatement = new WhereStatement("dlbWbSubAgent.subAgentCode",
                    agentCode, SystemVarList.EQUAL);
            DlbWbSubAgentBank dlbWbSubAgentBank = (DlbWbSubAgentBank) genericRepository.get(
                    DlbWbSubAgentBank.class, bankStatement);
            JSONObject bank = new JSONObject();
            bank.put("bankName", dlbWbSubAgentBank.getBankName());
            bank.put("branchName", dlbWbSubAgentBank.getBranchName());
            bank.put("accoutNumber", dlbWbSubAgentBank.getAccountNo());

            JSONObject nic = new JSONObject();
            nic.put("file", dlbWbSubAgent.getNicFile().getId());
            subAgent.put("nic_file", nic);

            JSONObject passBook = new JSONObject();
            passBook.put("file", dlbWbSubAgent.getScanCopyPassbookFile().getId());
            subAgent.put("passBook", passBook);

            if (dlbWbSubAgent.getProofOfAddressFile() != null) {
                JSONObject proofAddress = new JSONObject();
                proofAddress.put("file", dlbWbSubAgent.getProofOfAddressFile().getId());
                subAgent.put("proofAddress_File", proofAddress);
            } else {
                subAgent.put("proofAddress_File", "-");
            }

            if (dlbWbSubAgent.getBusinessRegFile() != null) {
                JSONObject businessRegFile = new JSONObject();
                businessRegFile.put("file", dlbWbSubAgent.getBusinessRegFile().getId());
                subAgent.put("businessReg_File", businessRegFile);
            } else {
                subAgent.put("businessReg_File", "-");
            }
            subAgent.put("bank", bank);
            subAgent.put("business", business);
        } else {
            DlbWbEmployee dlbWbEmployee = (DlbWbEmployee) genericRepository.get(agentCode, DlbWbEmployee.class);
            subAgent.put("subAgentCode", dlbWbEmployee.getEmployeeid());
            subAgent.put("firstName", dlbWbEmployee.getName());
            subAgent.put("lastName", dlbWbEmployee.getName());
            subAgent.put("address", dlbWbEmployee.getAddress());
            subAgent.put("province", "-");
            subAgent.put("district", "-");
            subAgent.put("districtName", "-");
            subAgent.put("email", dlbWbEmployee.getEmail());
            subAgent.put("mobile", dlbWbEmployee.getContactno());
            subAgent.put("alternativeContactNo", "-");
            subAgent.put("nic", dlbWbEmployee.getNic());
            JSONObject business = new JSONObject();
            business.put("nob", "-");
            business.put("regAddress", "-");
            business.put("businessRegNo", "-");
            business.put("businessEmail", "-");
            business.put("businessPhoneNo", "-");

            JSONObject bank = new JSONObject();
            bank.put("bankName", "-");
            bank.put("branchName", "-");
            bank.put("accoutNumber", "-");

            subAgent.put("nic_file", "-");
            subAgent.put("passBook", "-");
            subAgent.put("proofAddress_File", "-");
            subAgent.put("businessReg_File", "-");

            subAgent.put("bank", bank);
            subAgent.put("business", business);
        }
        return subAgent;
    }

    @Transactional(rollbackFor = Exception.class)
    public byte[] getByte(Blob blob) throws SQLException {
        int blobLength = (int) blob.length();
        byte[] blobAsBytes = blob.getBytes(1, blobLength);
        return blobAsBytes;
    }

    @Transactional(rollbackFor = Exception.class)
    public void downloadFile(Integer id, String fileName, HttpServletResponse resp) throws Exception {
        DlbWbFile dlbWbFile = (DlbWbFile) genericRepository.get(id, DlbWbFile.class);
        File file = generateFile(dlbWbFile, fileName);

        Blob blob = dlbWbFile.getFile();
        BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());
        FileOutputStream fos = new FileOutputStream(file);
        // you can set the size of the buffer
        byte[] buffer = new byte[2048];
        int r = 0;
        while ((r = is.read(buffer)) != -1) {
            fos.write(buffer, 0, r);
        }

        resp.setContentType(dlbWbFile.getType());
        resp.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
        resp.setContentLength((int) file.length());
        InputStream inputStream = dlbWbFile.getFile().getBinaryStream();
        FileCopyUtils.copy(inputStream, resp.getOutputStream());
        fos.flush();
        fos.close();
        is.close();
        blob.free();
    }

    @Transactional(rollbackFor = Exception.class)
    public File generateFile(DlbWbFile dlbWbFile, String fileName) throws SQLException, IOException {

        File file = null;
        if (dlbWbFile.getType().equals("image/jpeg")) {
            file = File.createTempFile(Configuration.getConfiguration("TMP_STORE_DIR_PATH")
                    + File.separator + fileName, ".jpeg");
            return file;
        } else if (dlbWbFile.getType().equals("image/png")) {
            file = File.createTempFile(Configuration.getConfiguration("TMP_STORE_DIR_PATH")
                    + File.separator + fileName, ".png");
            return file;
        } else if (dlbWbFile.getType().equals("application/pdf")) {
            file = File.createTempFile(Configuration.getConfiguration("TMP_STORE_DIR_PATH")
                    + File.separator + fileName, ".pdf");
            return file;
        }
        return file;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean isValid(String user) {
        DlbWbSubAgent dlbWbSubAgent = (DlbWbSubAgent) genericRepository.get(
                user, DlbWbSubAgent.class);
        if (dlbWbSubAgent == null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void updateEmployee(SubAgentDto subAgentDto, DlbWbSystemUser dlbWbSystemUser) throws Exception {
        DlbWbEmployee dlbWbEmployee = (DlbWbEmployee) genericRepository.get(subAgentDto.getSubAgentCode(), DlbWbEmployee.class);
        dlbWbEmployee.setName(subAgentDto.getFirstName() + " " + subAgentDto.getLastName());
        dlbWbEmployee.setNic(subAgentDto.getNic());
        dlbWbEmployee.setAddress(subAgentDto.getAddress());
        dlbWbEmployee.setEmail(subAgentDto.getEmail());
        dlbWbEmployee.setContactno(subAgentDto.getMobile());
        dlbWbEmployee.setLastupdatedtime(new Date());
        dlbWbEmployee.setLastupdateduser(dlbWbSystemUser.getUsername());
        genericRepository.update(dlbWbEmployee);
    }

    @Transactional(rollbackFor = Exception.class)
    private void updateSubAgent(SubAgentDto subAgentDto, DlbWbEmployee dlbWbEmployee, DlbWbSystemUser operationUser) throws Exception {
        DlbWbSubAgent dlbWbSubAgent = (DlbWbSubAgent) genericRepository.get(subAgentDto.getSubAgentCode(), DlbWbSubAgent.class);
        dlbWbSubAgent.setFirstName(subAgentDto.getFirstName());
        dlbWbSubAgent.setLastName(subAgentDto.getLastName());
        dlbWbSubAgent.setDlbWbProvince((DlbWbProvince) genericRepository.get(
                Integer.parseInt(subAgentDto.getProvince()), DlbWbProvince.class));
        dlbWbSubAgent.setDlbWbDistrict((DlbWbDistrict) genericRepository.get(
                Integer.parseInt(subAgentDto.getDistrict()), DlbWbDistrict.class));
        dlbWbSubAgent.setLastUpdatedUser(operationUser.getUsername());
        dlbWbSubAgent.setLastUpdatedDate(new Date());

        if (subAgentDto.getNicFile() != null) {
            deleteFile(dlbWbSubAgent.getNicFile().getId());
            dlbWbSubAgent.setNicFile(saveNicFile(subAgentDto));
        }
        if (subAgentDto.getScanCopyPassbookFile() != null) {
            deleteFile(dlbWbSubAgent.getScanCopyPassbookFile().getId());
            dlbWbSubAgent.setScanCopyPassbookFile(savePassbookFile(subAgentDto));
        }
        if (subAgentDto.getProofOfAddressFile() != null) {
            if (dlbWbSubAgent.getProofOfAddressFile() != null) {
                deleteFile(dlbWbSubAgent.getProofOfAddressFile().getId());
            }
            dlbWbSubAgent.setProofOfAddressFile(saveAddressFile(subAgentDto));
        }
        if (subAgentDto.getBusinessRegFile() != null) {
            if (dlbWbSubAgent.getBusinessRegFile() != null) {
                deleteFile(dlbWbSubAgent.getBusinessRegFile().getId());
            }
            dlbWbSubAgent.setBusinessRegFile(saveRegFile(subAgentDto));
        }
        dlbWbSubAgent.setAlternativeContactNo(subAgentDto.getAlternativeContactNo());
        genericRepository.update(dlbWbSubAgent);
    }

    @Transactional(rollbackFor = Exception.class)
    private void updateSubAgentBusiness(SubAgentDto subAgentDto, DlbWbSubAgent dlbWbSubAgent, DlbWbSystemUser operationUser) throws Exception {
        if (!(subAgentDto.getNob().equals("")
                && subAgentDto.getRegAddress().equals("")
                && subAgentDto.getBusinessRegNo().equals("")
                && subAgentDto.getBusinessEmail().equals("")
                && subAgentDto.getBusinessPhoneNo().equals(""))) {
            WhereStatement businessStatement = new WhereStatement("dlbWbSubAgent.subAgentCode",
                    subAgentDto.getSubAgentCode(), SystemVarList.EQUAL);
            DlbWbSubAgentBusiness dlbWbSubAgentBusiness = (DlbWbSubAgentBusiness) genericRepository.get(
                    DlbWbSubAgentBusiness.class, businessStatement);
            if (dlbWbSubAgentBusiness != null) {
                dlbWbSubAgentBusiness.setName(subAgentDto.getNob());
                dlbWbSubAgentBusiness.setRegistrationAddress(subAgentDto.getRegAddress());
                dlbWbSubAgentBusiness.setRegistrationNumber(subAgentDto.getBusinessRegNo());
                dlbWbSubAgentBusiness.setEmail(subAgentDto.getBusinessEmail());
                dlbWbSubAgentBusiness.setContactNo(subAgentDto.getBusinessPhoneNo());
                genericRepository.update(dlbWbSubAgentBusiness);
            } else {
                DlbWbSubAgentBusiness dlbWbSubAgentBusiness1 = new DlbWbSubAgentBusiness();
                dlbWbSubAgentBusiness1.setDlbWbSubAgent(dlbWbSubAgent);
                dlbWbSubAgentBusiness1.setName(subAgentDto.getNob());
                dlbWbSubAgentBusiness1.setRegistrationAddress(subAgentDto.getRegAddress());
                dlbWbSubAgentBusiness1.setRegistrationNumber(subAgentDto.getBusinessRegNo());
                dlbWbSubAgentBusiness1.setEmail(subAgentDto.getBusinessEmail());
                dlbWbSubAgentBusiness1.setContactNo(subAgentDto.getBusinessPhoneNo());
                genericRepository.save(dlbWbSubAgentBusiness1);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void updateSubAgentBank(SubAgentDto subAgentDto, DlbWbSubAgent dlbWbSubAgent, DlbWbSystemUser operationUser) throws Exception {
        WhereStatement bankStatement = new WhereStatement("dlbWbSubAgent.subAgentCode",
                subAgentDto.getSubAgentCode(), SystemVarList.EQUAL);
        DlbWbSubAgentBank dlbWbSubAgentBank = (DlbWbSubAgentBank) genericRepository.get(
                DlbWbSubAgentBank.class, bankStatement);
        dlbWbSubAgentBank.setBankName(subAgentDto.getBankName());
        dlbWbSubAgentBank.setBranchName(subAgentDto.getBranchName());
        dlbWbSubAgentBank.setAccountNo(subAgentDto.getAccoutNumber());
        genericRepository.update(dlbWbSubAgentBank);
    }

    @Transactional(rollbackFor = Exception.class)
    private void deleteFile(Integer fileId) throws Exception {
        DlbWbFile dlbWbFile = (DlbWbFile) genericRepository.get(fileId, DlbWbFile.class);
        genericRepository.delete(dlbWbFile);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUpdationAudit(
            String formData,
            SubAgentDto subAgentDto,
            DlbWbSystemUser previousSystemUser,
            DlbWbSubAgent previousDlbWbSubAgent,
            DlbWbSubAgentBusiness previousDlbWbSubAgentBusiness,
            DlbWbSubAgentBank previousDlbWbSubAgentBank,
            DlbWbSystemUser operationUser) throws Exception {

        DlbWbSystemUser dlbWbSystemUser = userService.get(subAgentDto.getSubAgentCode());
        JSONObject employee = new JSONObject();
        JSONParser jSONParser = new JSONParser();
        JSONObject jSONObject = (JSONObject) jSONParser.parse(formData);
        JSONObject subAgentUser = new JSONObject();
        employee.put("Employee Id", (String) jSONObject.get("subAgentCode"));
        employee.put("Employee Name", (String) jSONObject.get("firstName") + " " + (String) jSONObject.get("lastName"));
        employee.put("NIC", (String) jSONObject.get("nic"));
        employee.put("Address", (String) jSONObject.get("address"));
        employee.put("Email", (String) jSONObject.get("email"));
        employee.put("Contact No", (String) jSONObject.get("mobile"));
        employee.put("Status", dlbWbSystemUser.getDlbWbEmployee().getDlbStatus().getDescription());

        employee.put("Previous Employee Id", previousSystemUser.getDlbWbEmployee().getEmployeeid());
        employee.put("Previous Employee Name", previousSystemUser.getDlbWbEmployee().getName());
        employee.put("Previous NIC", previousSystemUser.getDlbWbEmployee().getNic());
        employee.put("Previous Address", previousSystemUser.getDlbWbEmployee().getAddress());
        employee.put("Previous Email", previousSystemUser.getDlbWbEmployee().getEmail());
        employee.put("Previous Contact No", previousSystemUser.getDlbWbEmployee().getContactno());
        employee.put("Previous Status", previousSystemUser.getDlbWbEmployee().getDlbStatus().getStatusCode());
        activityLogService.save(activityLogService.buildActivityLog("UPDATE", employee, "EM", operationUser));

        JSONObject systemUser = new JSONObject();
        systemUser.put("User Name", (String) jSONObject.get("subAgentCode"));
        systemUser.put("Employee Id", (String) jSONObject.get("subAgentCode"));
        systemUser.put("Status", dlbWbSystemUser.getDlbStatus().getStatusCode());
        systemUser.put("User Role", dlbWbSystemUser.getDlbWbUserRole().getUserrolecode());

        systemUser.put("Previous User Name", previousSystemUser.getUsername());
        systemUser.put("Previous Employee Id", previousSystemUser.getDlbWbEmployee().getEmployeeid());
        systemUser.put("Previous Status", previousSystemUser.getDlbStatus().getStatusCode());
        systemUser.put("Previous User Role", previousSystemUser.getDlbWbUserRole().getUserrolecode());
        activityLogService.save(activityLogService.buildActivityLog(
                "UPDATE", systemUser, "UM", operationUser));

        subAgentUser.put("Sub Agent Code", (String) jSONObject.get("subAgentCode"));
        subAgentUser.put("Sub Agent Name", (String) jSONObject.get("firstName") + " " + (String) jSONObject.get("lastName"));
        subAgentUser.put("Address", (String) jSONObject.get("address"));

        DlbWbDistrict dlbWbDistrict = (DlbWbDistrict) genericRepository.get(
                Integer.parseInt(subAgentDto.getDistrict()), DlbWbDistrict.class);

        subAgentUser.put("District", dlbWbDistrict.getName());
        subAgentUser.put("Email", (String) jSONObject.get("email"));
        subAgentUser.put("Mobile", (String) jSONObject.get("mobile"));
        subAgentUser.put("Alternative Contact No", (String) jSONObject.get("alternativeContactNo"));
        subAgentUser.put("NIC", (String) jSONObject.get("nic"));
        subAgentUser.put("Name of Business", (String) jSONObject.get("nob"));
        subAgentUser.put("Business Registration Address", (String) jSONObject.get("regAddress"));
        subAgentUser.put("Business Registration No", (String) jSONObject.get("businessRegNo"));
        subAgentUser.put("Business Email", (String) jSONObject.get("businessEmail"));
        subAgentUser.put("Business Phone No", (String) jSONObject.get("businessPhoneNo"));
        subAgentUser.put("Bank Name", (String) jSONObject.get("bankName"));
        subAgentUser.put("Branch Name", (String) jSONObject.get("branchName"));
        subAgentUser.put("Accout Number", (String) jSONObject.get("accoutNumber"));

        subAgentUser.put("Previous Sub Agent Code", previousDlbWbSubAgent.getSubAgentCode());
        subAgentUser.put("Previous Sub Agent Name", previousDlbWbSubAgent.getFirstName() + " " + previousDlbWbSubAgent.getLastName());
        subAgentUser.put("Previous Address", previousDlbWbSubAgent.getDlbWbEmployee().getAddress());
        subAgentUser.put("Previous District", previousDlbWbSubAgent.getDlbWbDistrict().getName());
        subAgentUser.put("Previous Email", previousDlbWbSubAgent.getDlbWbEmployee().getEmail());
        subAgentUser.put("Previous Mobile", previousDlbWbSubAgent.getDlbWbEmployee().getContactno());
        subAgentUser.put("Previous Alternative Contact No", previousDlbWbSubAgent.getAlternativeContactNo());
        subAgentUser.put("Previous NIC", previousDlbWbSubAgent.getDlbWbEmployee().getNic());

        if (previousDlbWbSubAgentBusiness != null) {
            subAgentUser.put("Previous Name of Business",
                    previousDlbWbSubAgentBusiness.getName() == null
                    || previousDlbWbSubAgentBusiness.getName().equals("")
                    ? "" : previousDlbWbSubAgentBusiness.getName());

            subAgentUser.put("Previous Business Registration Address",
                    previousDlbWbSubAgentBusiness.getRegistrationAddress() == null
                    || previousDlbWbSubAgentBusiness.getRegistrationAddress().equals("")
                    ? "" : previousDlbWbSubAgentBusiness.getRegistrationAddress());

            subAgentUser.put("Previous Business Registration No",
                    previousDlbWbSubAgentBusiness.getRegistrationNumber() == null
                    || previousDlbWbSubAgentBusiness.getRegistrationNumber().equals("")
                    ? "" : previousDlbWbSubAgentBusiness.getRegistrationNumber());

            subAgentUser.put("Previous Business Email",
                    previousDlbWbSubAgentBusiness.getEmail() == null
                    || previousDlbWbSubAgentBusiness.getEmail().equals("")
                    ? "" : previousDlbWbSubAgentBusiness.getEmail());

            subAgentUser.put("Previous Business Phone No",
                    previousDlbWbSubAgentBusiness.getContactNo() == null
                    || previousDlbWbSubAgentBusiness.getContactNo().equals("")
                    ? "" : previousDlbWbSubAgentBusiness.getContactNo());
        } else {
            subAgentUser.put("Previous Name of Business", "-");
            subAgentUser.put("Previous Business Registration Address", "-");
            subAgentUser.put("Previous Business Registration No", "-");
            subAgentUser.put("Previous Business Email", "-");
            subAgentUser.put("Previous Business Phone No", "-");
        }

        subAgentUser.put("Previous Bank Name", previousDlbWbSubAgentBank.getBankName());
        subAgentUser.put("Previous Branch Name", previousDlbWbSubAgentBank.getBranchName());
        subAgentUser.put("Previous Accout Number", previousDlbWbSubAgentBank.getAccountNo());

        activityLogService.save(activityLogService.buildActivityLog(
                "UPDATE", subAgentUser, "SBARE", operationUser));
    }

}
