/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.builder.UserClaimPendingBuilder;
import com.epic.dlb.dto.UserClaimedRequestDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbUserClaimedRequest;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.repository.UserClaimPendingRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("userClaimPendingService")
public class UserClaimPendingService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private UserClaimPendingBuilder userClaimPendingBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private UserClaimPendingRepository userClaimPendingRepository;

    @Transactional(rollbackFor = Exception.class)
    public JSONArray search(String serialNo, String txnId, String purchaseId) throws Exception {
        if (!txnId.equals("-")) {
            return userClaimPendingBuilder.buildResultSetForTransactionId(getByTxnId(txnId), txnId);
        } else {
            return userClaimPendingBuilder.buildResultSet(get(serialNo, txnId, purchaseId));
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public String getTxnIdByPurchaseIdAndTxnId(Integer id, String txnId) {
        try {
            List<Object[]> objectList = userClaimPendingRepository.getRedeemHistoryDetailsByPurchaseIdAndTxnId(id, txnId);
            if (objectList.isEmpty()) {
                return "-";
            } else {
                Object[] object = objectList.get(0);
                return object[2] == null ? "-" : object[2].toString();
            }
        } catch (Exception e) {
            return "-";
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public String getTxnIdByPurchaseId(Integer id) {
        try {
            List<Object[]> objectList = userClaimPendingRepository.getRedeemHistoryDetailsByPurchaseId(id);
            if (objectList.isEmpty()) {
                return "-";
            } else {
                Object[] object = objectList.get(0);
                return object[2] == null ? "-" : object[2].toString();
            }
        } catch (Exception e) {
            return "-";
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public String getPurchaseIdByTxnId(String id) throws Exception {
        List<Object[]> objectList = userClaimPendingRepository.getRedeemHistoryDetailsByTxnId(id);
        if (objectList.isEmpty()) {
            return "-";
        } else {
            Object[] object = objectList.get(0);
            return object[2] == null ? "-" : object[2].toString();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRequest(UserClaimedRequestDto userClaimedRequestDto, DlbWbSystemUser dlbWbSystemUser) throws Exception {
        DlbWbUserClaimedRequest dlbWbUserClaimedRequest = new DlbWbUserClaimedRequest();
        dlbWbUserClaimedRequest.setPurchaseId(userClaimedRequestDto.getPurchaseId());
        dlbWbUserClaimedRequest.setStatus("REQUEST");
        dlbWbUserClaimedRequest.setRequestedDate(new Date());
        dlbWbUserClaimedRequest.setRequestedUser(dlbWbSystemUser.getUsername());
        dlbWbUserClaimedRequest.setRemark(userClaimedRequestDto.getRemark());
        dlbWbUserClaimedRequest.setStatementStatus(userClaimedRequestDto.getStatementStatus());
        dlbWbUserClaimedRequest.setTxnId(userClaimedRequestDto.getTxnId());
        dlbWbUserClaimedRequest.setFirstStatement(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(userClaimedRequestDto.getFirstStatement() + " " + "00:00:00"));
        if (userClaimedRequestDto.getSecondStatement() != null) {
            dlbWbUserClaimedRequest.setSecondStatement(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(userClaimedRequestDto.getSecondStatement() + " " + "00:00:00"));
        }
        genericRepository.save(dlbWbUserClaimedRequest);

        DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory
                = (DlbSwtStPurchaseHistory) genericRepository.get(
                        dlbWbUserClaimedRequest.getPurchaseId(),
                        DlbSwtStPurchaseHistory.class);
        saveRequestLog(dlbWbSystemUser, userClaimedRequestDto.getRemark(), userClaimedRequestDto.getTxnId(),
                dlbSwtStPurchaseHistory.getSerialNo(), userClaimedRequestDto.getPurchaseId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveApproveReject(UserClaimedRequestDto userClaimedRequestDto, DlbWbSystemUser dlbWbSystemUser) throws Exception {

        WhereStatement statement1 = new WhereStatement("status",
                "REQUEST", SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("id",
                userClaimedRequestDto.getId(), SystemVarList.EQUAL);

        DlbWbUserClaimedRequest dlbWbUserClaimedRequest
                = (DlbWbUserClaimedRequest) genericRepository.get(
                        DlbWbUserClaimedRequest.class, statement2, statement1);

        dlbWbUserClaimedRequest.setStatus(userClaimedRequestDto.getStatus());
        dlbWbUserClaimedRequest.setApprovedDate(new Date());
        dlbWbUserClaimedRequest.setApprovedUser(dlbWbSystemUser.getUsername());
        dlbWbUserClaimedRequest.setFeedback(userClaimedRequestDto.getFeedback());
        DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory
                = (DlbSwtStPurchaseHistory) genericRepository.get(
                        dlbWbUserClaimedRequest.getPurchaseId(),
                        DlbSwtStPurchaseHistory.class);
        if (userClaimedRequestDto.getStatus().equals("APPROVED")) {

            dlbSwtStPurchaseHistory.setDlbStatusByStatus(
                    (DlbStatus) genericRepository.get(
                            Integer.parseInt(dlbWbUserClaimedRequest.getStatementStatus()),
                            DlbStatus.class));

            genericRepository.save(dlbSwtStPurchaseHistory);
            saveApproveLog(dlbWbSystemUser, dlbSwtStPurchaseHistory.getTxnId(),
                    dlbSwtStPurchaseHistory.getSerialNo(), dlbSwtStPurchaseHistory.getId());
        } else {
            saveRejectLog(dlbWbSystemUser, userClaimedRequestDto.getFeedback(), dlbWbUserClaimedRequest.getTxnId(),
                    dlbSwtStPurchaseHistory.getSerialNo(), dlbSwtStPurchaseHistory.getId());
        }
        genericRepository.save(dlbWbUserClaimedRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject getUserClaimedRequestsByRequestId(UserClaimedRequestDto userClaimedRequestDto) {
        DlbWbUserClaimedRequest dlbWbUserClaimedRequest = (DlbWbUserClaimedRequest) genericRepository.get(
                userClaimedRequestDto.getId(), DlbWbUserClaimedRequest.class);
        DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory = (DlbSwtStPurchaseHistory) genericRepository.get(
                dlbWbUserClaimedRequest.getPurchaseId(),
                DlbSwtStPurchaseHistory.class);
        DlbStatus dlbStatus = (DlbStatus) genericRepository.get(
                Integer.parseInt(dlbWbUserClaimedRequest.getStatementStatus()),
                DlbStatus.class);
        DlbWbSystemUser dlbWbSystemUser = (DlbWbSystemUser) genericRepository.get(
                dlbWbUserClaimedRequest.getRequestedUser(),
                DlbWbSystemUser.class);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("current_status", dlbSwtStPurchaseHistory.getDlbStatusByStatus().getDescription());
        jSONObject.put("new_status", dlbStatus.getDescription());
        jSONObject.put("requested_by", dlbWbSystemUser.getDlbWbEmployee().getName());

        return jSONObject;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbSwtStPurchaseHistory> get(String serialNo, String txnId, String purchaseId) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        if (!serialNo.equals("-")) {
            WhereStatement statement1 = new WhereStatement("serialNo",
                    serialNo, SystemVarList.EQUAL);
            whereStatements.add(statement1);
        }
//        if (!txnId.equals("-")) {
//            WhereStatement statement2 = new WhereStatement("txnId",
//                    txnId, SystemVarList.EQUAL);
//            whereStatements.add(statement2);
//        }
        if (!purchaseId.equals("-")) {
            WhereStatement statement3 = new WhereStatement("id",
                    Integer.parseInt(purchaseId), SystemVarList.EQUAL);
            whereStatements.add(statement3);
        }

        WhereStatement statement4 = new WhereStatement("dlbStatusByStatus.statusCode",
                SystemVarList.USER_CLAIME_PENDING, SystemVarList.EQUAL);
        whereStatements.add(statement4);

        return genericRepository.search(DlbSwtStPurchaseHistory.class, whereStatements);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbSwtStPurchaseHistory> getByTxnId(String txnId) throws Exception {
        List<Object[]> list = userClaimPendingRepository.getRedeemHistoryDetailsByTxnId(txnId);
        List<DlbSwtStPurchaseHistory> dlbSwtStPurchaseHistorys = new ArrayList<>();
        list.forEach(object -> {
            DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory = (DlbSwtStPurchaseHistory) genericRepository.
                    get(Integer.parseInt(object[1].toString()), DlbSwtStPurchaseHistory.class);
            dlbSwtStPurchaseHistorys.add(dlbSwtStPurchaseHistory);
        });
        return dlbSwtStPurchaseHistorys;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getAllRequests() {
        WhereStatement statement = new WhereStatement("status",
                "REQUEST", SystemVarList.EQUAL);
        List<DlbWbUserClaimedRequest> dlbWbUserClaimedRequests
                = genericRepository.list(DlbWbUserClaimedRequest.class, statement);
        JSONArray jSONArray = new JSONArray();

        dlbWbUserClaimedRequests.stream().map((request) -> {
            DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory
                    = (DlbSwtStPurchaseHistory) genericRepository.get(
                            request.getPurchaseId(), DlbSwtStPurchaseHistory.class);
            String reqStatus = "";
            if (request.getStatementStatus().equals("20")) {
                reqStatus = "User Unclaimed";
            } else {
                DlbStatus requestedStatus = (DlbStatus) genericRepository.get(
                        Integer.parseInt(request.getStatementStatus()), DlbStatus.class);
                reqStatus = requestedStatus.getDescription();
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", request.getId());
            jSONObject.put("purchase_id", dlbSwtStPurchaseHistory.getId());
            jSONObject.put("serial_number", dlbSwtStPurchaseHistory.getSerialNo());
            jSONObject.put("nic", dlbSwtStPurchaseHistory.getDlbSwtStWallet().getNic());
            jSONObject.put("winning_amount", dlbSwtStPurchaseHistory.getWinningPrize());
            jSONObject.put("txn_id", request.getTxnId());
            jSONObject.put("current_status", dlbSwtStPurchaseHistory.getDlbStatusByStatus().getDescription());
            jSONObject.put("requested_status", reqStatus);
            jSONObject.put("requested_by", userService.get(request.getRequestedUser()).getDlbWbEmployee().getName());
            jSONObject.put("first_date", new SimpleDateFormat("yyyy-MM-dd").format(request.getFirstStatement()));
            jSONObject.put("second_date", request.getSecondStatement() == null ? "-" : new SimpleDateFormat("yyyy-MM-dd").format(request.getSecondStatement()));
            return jSONObject;
        }).forEachOrdered((jSONObject) -> {
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean isRequested(Integer purchaseId) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        WhereStatement statement1 = new WhereStatement("purchaseId",
                purchaseId, SystemVarList.EQUAL, SystemVarList.AND);
        whereStatements.add(statement1);
        WhereStatement statement2 = new WhereStatement("status",
                "REQUEST", SystemVarList.EQUAL);
        whereStatements.add(statement2);
        int count = genericRepository.getCount(DlbWbUserClaimedRequest.class, whereStatements);
        return count != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject getById(Integer id) {
        WhereStatement statement1 = new WhereStatement("status",
                "REQUEST", SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("id",
                id, SystemVarList.EQUAL);
        DlbWbUserClaimedRequest request = (DlbWbUserClaimedRequest) genericRepository.get(DlbWbUserClaimedRequest.class, statement2, statement1);

        DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory
                = (DlbSwtStPurchaseHistory) genericRepository.get(
                        request.getPurchaseId(), DlbSwtStPurchaseHistory.class);
        String reqStatus = "";
        if (request.getStatementStatus().equals("20")) {
            reqStatus = "User Unclaimed";
        } else {
            DlbStatus requestedStatus = (DlbStatus) genericRepository.get(
                    Integer.parseInt(request.getStatementStatus()), DlbStatus.class);
            reqStatus = "User Claimed";
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("id", request.getId());
        jSONObject.put("purchase_id", dlbSwtStPurchaseHistory.getId());
        jSONObject.put("serial_number", dlbSwtStPurchaseHistory.getSerialNo());
        jSONObject.put("nic", dlbSwtStPurchaseHistory.getDlbSwtStWallet().getNic());
        jSONObject.put("winning_amount", dlbSwtStPurchaseHistory.getWinningPrize());
        jSONObject.put("txn_id", request.getTxnId());
        jSONObject.put("current_status", dlbSwtStPurchaseHistory.getDlbStatusByStatus().getDescription());
        jSONObject.put("requested_status", reqStatus);
        jSONObject.put("requested_by", userService.get(request.getRequestedUser()).getDlbWbEmployee().getName());
        jSONObject.put("first_date", new SimpleDateFormat("yyyy-MM-dd").format(request.getFirstStatement()));
        jSONObject.put("second_date", request.getSecondStatement() == null ? "-" : new SimpleDateFormat("yyyy-MM-dd").format(request.getSecondStatement()));
        jSONObject.put("remark", request.getRemark());
        return jSONObject;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRequestLog(DlbWbSystemUser dlbWbSystemUser, String remark,
            String txnId, String serialNo, Integer purchaseId) throws Exception {
        DlbWbSystemUser user = userService.get(dlbWbSystemUser.getUsername());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Serial No", serialNo);
        jSONObject.put("Transaction Id", txnId);
        jSONObject.put("Purchase Id", purchaseId);
        jSONObject.put("Remark", remark);
        jSONObject.put("Requested By", user.getDlbWbEmployee().getName());
        activityLogService.save(activityLogService.buildActivityLog("REQUEST",
                jSONObject, "UCLR", user));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveApproveLog(DlbWbSystemUser dlbWbSystemUser, String txnId,
            String serialNo, Integer purchaseId) throws Exception {
        DlbWbSystemUser user = userService.get(dlbWbSystemUser.getUsername());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Serial No", serialNo);
        jSONObject.put("Transaction Id", txnId);
        jSONObject.put("Purchase Id", purchaseId);
        jSONObject.put("Approved By", user.getDlbWbEmployee().getName());
        activityLogService.save(activityLogService.buildActivityLog("APPROVED",
                jSONObject, "UCLA", user));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRejectLog(DlbWbSystemUser dlbWbSystemUser, String feedBack, String txnId,
            String serialNo, Integer purchaseId) throws Exception {
        DlbWbSystemUser user = userService.get(dlbWbSystemUser.getUsername());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Serial No", serialNo);
        jSONObject.put("Transaction Id", txnId);
        jSONObject.put("Purchase Id", purchaseId);
        jSONObject.put("Rejected By", user.getDlbWbEmployee().getName());
        activityLogService.save(activityLogService.buildActivityLog("REJECT",
                jSONObject, "UCLA", user));
    }
}
