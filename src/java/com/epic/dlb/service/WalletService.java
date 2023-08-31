/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.DlbSwtStWalletDto;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStToken;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbWalletDeletionRequest;
import com.epic.dlb.model.DlbWbResetPinRequest;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.repository.JDBCConnection;
import com.epic.dlb.repository.WalletRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("walletService")
public class WalletService {

    @Autowired
    private JDBCConnection jDBCConnection;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityLogService activityLogService;

    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWalletDto findByNICOrMobileNo(String key) throws SQLException {

        DlbSwtStWalletDto dlbSwtStWalletDto = new DlbSwtStWalletDto();
        List list = walletRepository.findWalletDataByNicOrMobileNo(key);

        if (list.size() != 0) {
            dlbSwtStWalletDto = (DlbSwtStWalletDto) list.get(0);
        }

        return dlbSwtStWalletDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> findByNicOrMobileNo(String nic, String mobileNo) throws SQLException, Exception {
        List<Object[]> list = walletRepository.findByNicOrMobileNo(nic, mobileNo);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWalletDto findById(Integer id) {
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(id, DlbSwtStWallet.class);
        DlbSwtStWalletDto dlbSwtStWalletDto = new DlbSwtStWalletDto();
        dlbSwtStWalletDto.setId(dlbSwtStWallet.getId());
        dlbSwtStWalletDto.setFirstName(dlbSwtStWallet.getFirstName());
        dlbSwtStWalletDto.setLastName(dlbSwtStWallet.getLastName());
        dlbSwtStWalletDto.setMobileNo(dlbSwtStWallet.getMobileNo());
        dlbSwtStWalletDto.setNic(dlbSwtStWallet.getNic());
        return dlbSwtStWalletDto;
    }
    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWallet findByWalletId(Integer id) {
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(id, DlbSwtStWallet.class);
        return dlbSwtStWallet;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRquest(Integer id, String remark, String username) throws Exception {
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(id, DlbSwtStWallet.class);
        DlbWbResetPinRequest dlbWbResetPinRequest = new DlbWbResetPinRequest();
        dlbWbResetPinRequest.setWalletId(dlbSwtStWallet.getId());
        dlbWbResetPinRequest.setRequestedBy(username);
        dlbWbResetPinRequest.setStatus("REQUESTED");
        dlbWbResetPinRequest.setCreatedDateTime(new Date());
        dlbWbResetPinRequest.setLastUpdatedTime(new Date());
        dlbWbResetPinRequest.setRemark(remark);
        genericRepository.save(dlbWbResetPinRequest);
        saveRequestAuditLog(dlbSwtStWallet.getFirstName() + " " + dlbSwtStWallet.getLastName(),
                remark, username, "REQUEST", "PINRQRS");
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDeletionRquest(Integer id, String remark, String username) throws Exception {
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(id, DlbSwtStWallet.class);
        DlbWbWalletDeletionRequest dlbWbDeletionRequest = new DlbWbWalletDeletionRequest();
        dlbWbDeletionRequest.setWalletId(dlbSwtStWallet.getId());
        dlbWbDeletionRequest.setRequestedBy(username);
        dlbWbDeletionRequest.setStatus("REQUESTED");
        dlbWbDeletionRequest.setCreatedDateTime(new Date());
        dlbWbDeletionRequest.setLastUpdatedTime(new Date());
        dlbWbDeletionRequest.setRemark(remark);
        genericRepository.save(dlbWbDeletionRequest);
        saveRequestAuditLog(dlbSwtStWallet.getFirstName() + " " + dlbSwtStWallet.getLastName(),
                remark, username, "REQUEST", "USARR");
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveApproved(Integer id, String username) throws Exception {
        DlbWbResetPinRequest dlbWbResetPinRequest = (DlbWbResetPinRequest) genericRepository.get(id, DlbWbResetPinRequest.class);
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(dlbWbResetPinRequest.getWalletId(), DlbSwtStWallet.class);
        dlbSwtStWallet.setMpin("011c945f30ce2cbafc452f39840f025693339c42");
        dlbWbResetPinRequest.setApprovedBy(username);
        dlbWbResetPinRequest.setStatus("APPROVED");
        dlbWbResetPinRequest.setLastUpdatedTime(new Date());
        genericRepository.update(dlbWbResetPinRequest);
        genericRepository.update(dlbSwtStWallet);
        saveApproveAuditLog(dlbSwtStWallet.getFirstName() + " " + dlbSwtStWallet.getLastName(),
                dlbWbResetPinRequest.getRemark(), username, "APPROVE", "PINAPPRS");
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDeletionApproved(Integer id, String username) throws Exception {
        DlbWbWalletDeletionRequest dlbWbWalletDeletionRequest = (DlbWbWalletDeletionRequest) genericRepository.get(id, DlbWbWalletDeletionRequest.class);
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(dlbWbWalletDeletionRequest.getWalletId(), DlbSwtStWallet.class);

        dlbWbWalletDeletionRequest.setApprovedBy(username);
        dlbWbWalletDeletionRequest.setStatus("APPROVED");
        dlbWbWalletDeletionRequest.setLastUpdatedTime(new Date());
        genericRepository.update(dlbWbWalletDeletionRequest);
        genericRepository.delete(dlbSwtStWallet);
        saveApproveAuditLog(dlbSwtStWallet.getFirstName() + " " + dlbSwtStWallet.getLastName(),
                dlbWbWalletDeletionRequest.getRemark(), username, "APPROVE", "USARA");
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray fetchPINResetRequests() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        JSONArray jSONArray = new JSONArray();
        WhereStatement whereStatement1 = new WhereStatement("status", "REQUESTED", SystemVarList.EQUAL);
        List<DlbWbResetPinRequest> list = genericRepository.list(DlbWbResetPinRequest.class, whereStatement1);

        for (DlbWbResetPinRequest dlbWbResetPinRequest : list) {
            DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(
                    dlbWbResetPinRequest.getWalletId(), DlbSwtStWallet.class);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("req_id", dlbWbResetPinRequest.getId());
            jSONObject.put("fullName", dlbSwtStWallet.getFirstName() + " " + dlbSwtStWallet.getLastName());
            jSONObject.put("username", dlbSwtStWallet.getUsername());
            jSONObject.put("nic", dlbSwtStWallet.getNic());
            jSONObject.put("mobileNo", dlbSwtStWallet.getMobileNo());
            jSONObject.put("lastLoginTime", dateFormat.format(dlbSwtStWallet.getLastLoginTime()));
            jSONObject.put("createdDate", dateFormat.format(dlbSwtStWallet.getCreateTime()));
            jSONObject.put("requestedBy", dlbWbResetPinRequest.getRequestedBy());
            jSONObject.put("remark", dlbWbResetPinRequest.getRemark());
            jSONArray.add(jSONObject);
        }

        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray fetchDeletionRequests() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        JSONArray jSONArray = new JSONArray();
        WhereStatement whereStatement1 = new WhereStatement("status", "REQUESTED", SystemVarList.EQUAL);
        List<DlbWbWalletDeletionRequest> list = genericRepository.list(DlbWbWalletDeletionRequest.class, whereStatement1);

        for (DlbWbWalletDeletionRequest dlbWbWalletDeletionRequest : list) {
            DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(
                    dlbWbWalletDeletionRequest.getWalletId(), DlbSwtStWallet.class);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("req_id", dlbWbWalletDeletionRequest.getId());
            jSONObject.put("fullName", dlbSwtStWallet.getFirstName() + " " + dlbSwtStWallet.getLastName());
            jSONObject.put("username", dlbSwtStWallet.getUsername());
            jSONObject.put("nic", dlbSwtStWallet.getNic());
            jSONObject.put("mobileNo", dlbSwtStWallet.getMobileNo());
            jSONObject.put("createdDate", dateFormat.format(dlbSwtStWallet.getCreateTime()));
            jSONObject.put("requestCreatedDate", dateFormat.format(dlbWbWalletDeletionRequest.getCreatedDateTime()));
            jSONObject.put("requestedBy", dlbWbWalletDeletionRequest.getRequestedBy());
            jSONObject.put("remark", dlbWbWalletDeletionRequest.getRemark());
            jSONArray.add(jSONObject);
        }

        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbResetPinRequest getRequestById(Integer id) {
        DlbWbResetPinRequest dlbWbResetPinRequest = (DlbWbResetPinRequest) genericRepository.get(id, DlbWbResetPinRequest.class);
        return dlbWbResetPinRequest;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbWalletDeletionRequest getDeletionRequestById(Integer id) {
        DlbWbWalletDeletionRequest dlbWbWalletDeletionRequest = (DlbWbWalletDeletionRequest) genericRepository.get(id, DlbWbWalletDeletionRequest.class);
        return dlbWbWalletDeletionRequest;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean checkIsRequested(Integer walletId, String status) {
        WhereStatement whereStatement1 = new WhereStatement("walletId", walletId, SystemVarList.EQUAL, SystemVarList.AND);
        WhereStatement whereStatement2 = new WhereStatement("status", status, SystemVarList.EQUAL);
        List list = genericRepository.list(DlbWbResetPinRequest.class, whereStatement1, whereStatement2);
        return !list.isEmpty();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean checkIsRequestedForDeletion(Integer walletId, String status) {
        WhereStatement whereStatement1 = new WhereStatement("walletId", walletId, SystemVarList.EQUAL, SystemVarList.AND);
        WhereStatement whereStatement2 = new WhereStatement("status", status, SystemVarList.EQUAL);
        List list = genericRepository.list(DlbWbWalletDeletionRequest.class, whereStatement1, whereStatement2);
        return !list.isEmpty();
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveRequestAuditLog(String cusName, String remark, String requestBy,
            String activity, String page) throws Exception {
        DlbWbSystemUser dlbWbSystemUser = userService.get(requestBy);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Customer Name", cusName);
        jSONObject.put("Remark", remark);
        jSONObject.put("Requested By", dlbWbSystemUser.getDlbWbEmployee().getName());
        activityLogService.save(activityLogService.buildActivityLog(activity,
                jSONObject, page, dlbWbSystemUser));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveApproveAuditLog(String cusName, String remark,
            String approvedBy, String activity, String page) throws Exception {
        DlbWbSystemUser dlbWbSystemUser = userService.get(approvedBy);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Customer Name", cusName);
        jSONObject.put("Remark", remark);
        jSONObject.put("Approved By", dlbWbSystemUser.getDlbWbEmployee().getName());
        activityLogService.save(activityLogService.buildActivityLog(activity,
                jSONObject, page, dlbWbSystemUser));
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWallet getByDeletionRequest(Integer id) {
        DlbWbWalletDeletionRequest dlbWbWalletDeletionRequest = (DlbWbWalletDeletionRequest) genericRepository.get(id, DlbWbWalletDeletionRequest.class);
        DlbSwtStWallet dlbSwtStWallet = (DlbSwtStWallet) genericRepository.get(dlbWbWalletDeletionRequest.getWalletId(), DlbSwtStWallet.class);
        return dlbSwtStWallet;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject getIsValidToDelete(DlbSwtStWallet dlbSwtStWallet) {
        JSONObject jSONObject = new JSONObject();
        WhereStatement whereStatement1 = new WhereStatement("dlbSwtStWallet.id", dlbSwtStWallet.getId(), SystemVarList.EQUAL);
        List<WhereStatement> list1 = new ArrayList<>();
        list1.add(whereStatement1);
        Integer purchaseList = genericRepository.getCount(DlbSwtStPurchaseHistory.class, list1);

        WhereStatement whereStatement2 = new WhereStatement("dlbSwtStWallet.id", dlbSwtStWallet.getId(), SystemVarList.EQUAL, SystemVarList.AND);
        WhereStatement whereStatement2_1 = new WhereStatement("dlbSwtMtTxnType.code", 1, SystemVarList.NOT_EQUAL);
        List<WhereStatement> list2 = new ArrayList<>();
        list2.add(whereStatement2);
        list2.add(whereStatement2_1);
        Integer transactionList = genericRepository.getCount(DlbSwtStTransaction.class, list2);

        WhereStatement whereStatement3 = new WhereStatement("dlbSwtStWallet.id", dlbSwtStWallet.getId(), SystemVarList.EQUAL);
        List<WhereStatement> list3 = new ArrayList<>();
        list3.add(whereStatement3);
        Integer tokenList = genericRepository.getCount(DlbSwtStToken.class, list3);
        
        if (purchaseList != 0) {
            jSONObject.put("msg", "This account has been used to purchase "
                    + "lotteries and cannot be deleted. Please contact "
                    + "administration for more information");
            jSONObject.put("status", "false");
        } else if (transactionList != 0) {
            jSONObject.put("msg", "This account has been used to purchase "
                    + "lotteries and cannot be deleted. "
                    + "Please contact administration for more information");
            jSONObject.put("status", "false");
        } else if (tokenList != 0) {
            jSONObject.put("msg", "Cannot be deleted. "
                    + "Please contact administration for more information");
            jSONObject.put("status", "false");
        } else {
            jSONObject.put("status", "true");
        }
        return jSONObject;
    }
}
