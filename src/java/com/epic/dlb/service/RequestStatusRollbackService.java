/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.StatusRollbackDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbStatusRollback;
import com.epic.dlb.model.DlbWbStatusRollbackAudit;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nipuna_k
 */
@Service("reverseClaimedTicketService")
public class RequestStatusRollbackService {

    @Autowired
    private GenericRepository genericRepository;

    private SimpleDateFormat dateFormat;

    private SimpleDateFormat dateFormat2;

    @Autowired
    private ActivityLogService activityLogService;

    @Transactional
    public JSONArray getData(int statusCode) {
        WhereStatement whereStatement1 = new WhereStatement(
                "dlbStatusByStatus.statusCode", 24, SystemVarList.EQUAL, " OR ");
        WhereStatement whereStatement2 = new WhereStatement(
                "dlbStatusByStatus.statusCode", 3, SystemVarList.EQUAL, " OR ");
        WhereStatement whereStatement3 = new WhereStatement(
                "dlbStatusByStatus.statusCode", 4, SystemVarList.EQUAL, " OR ");
        WhereStatement whereStatement4 = new WhereStatement(
                "dlbStatusByStatus.statusCode", 5, SystemVarList.EQUAL, " OR ");
        WhereStatement whereStatement5 = new WhereStatement(
                "dlbStatusByStatus.statusCode", 7, SystemVarList.EQUAL, " OR ");
        WhereStatement whereStatement6 = new WhereStatement(
                "dlbStatusByStatus.statusCode", 43, SystemVarList.EQUAL);

        List<WhereStatement> whereStatements = new ArrayList<>();
        whereStatements.add(whereStatement1);
        whereStatements.add(whereStatement2);
        whereStatements.add(whereStatement3);
        whereStatements.add(whereStatement4);
        whereStatements.add(whereStatement5);
        whereStatements.add(whereStatement6);

        JSONArray array = new JSONArray();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        List<DlbSwtStPurchaseHistory> historys = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, whereStatements);

        return buildData(historys);

    }

    @Transactional
    public JSONArray getRollbackData() {
        WhereStatement statusCodeWh = new WhereStatement("activeStatus", "Pending", SystemVarList.EQUAL);
        JSONArray array = new JSONArray();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        List<DlbWbStatusRollback> dlbWbStatusRollbacks = genericRepository.listWithQuery(DlbWbStatusRollback.class, statusCodeWh);

        return buildRollBackData(dlbWbStatusRollbacks);

    }

    @Transactional
    public DlbWbStatusRollback getByPurchaseId(Integer id) {
        DlbWbStatusRollback dlbWbStatusRollback = new DlbWbStatusRollback();

        WhereStatement whereStatementA = new WhereStatement("purchaseHistoryId", id, SystemVarList.EQUAL);
        dlbWbStatusRollback = (DlbWbStatusRollback) genericRepository.get(DlbWbStatusRollback.class, whereStatementA);
        return dlbWbStatusRollback;
    }

    @Transactional
    public void requestSaveData(StatusRollbackDto statusRollbackDto, DlbWbSystemUser dlbWbSystemUser) {
        try {
            DlbWbStatusRollback dlbWbStatusRollback = new DlbWbStatusRollback();

            WhereStatement whereStatementA = new WhereStatement("purchaseHistoryId", statusRollbackDto.getPurchaseHistoryId(), SystemVarList.EQUAL);
            dlbWbStatusRollback = (DlbWbStatusRollback) genericRepository.get(DlbWbStatusRollback.class, whereStatementA);

            DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory = (DlbSwtStPurchaseHistory) genericRepository.
                    get(statusRollbackDto.getPurchaseHistoryId(),
                            DlbSwtStPurchaseHistory.class);
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(dlbSwtStPurchaseHistory.getProductCode(), DlbWbProduct.class);

            if (dlbWbStatusRollback == null) {
                dlbWbStatusRollback = new DlbWbStatusRollback();
                dlbWbStatusRollback.setPurchaseHistoryId(statusRollbackDto.getPurchaseHistoryId());
                dlbWbStatusRollback.setActiveStatus("Pending");
                dlbWbStatusRollback.setStatus(Integer.toString(
                        dlbSwtStPurchaseHistory.getDlbStatusByStatus().
                                getStatusCode()));
                dlbWbStatusRollback.setCreatedTime(new Date());
                dlbWbStatusRollback.setLastUpdatedTime(new Date());
                genericRepository.save(dlbWbStatusRollback);
                DlbWbStatusRollback savedObject = getByPurchaseId(statusRollbackDto.getPurchaseHistoryId());
                saveAudit(savedObject.getId(), "REQUEST",
                        dlbWbSystemUser.getDlbWbEmployee().getName(),
                        dlbSwtStPurchaseHistory.getDlbStatusByStatus().
                                getStatusCode());

                saveActivityAudit("Request Status Rollback", dlbWbSystemUser,
                        dlbSwtStPurchaseHistory.getDlbStatusByStatus().getDescription(),
                        product.getDescription(),
                        dlbSwtStPurchaseHistory.getDrawNo(),
                        dlbSwtStPurchaseHistory.getSerialNo(), "ROBST", new Date(), "REQUEST");
            } else {
                dlbWbStatusRollback.setStatus(Integer.toString(
                        dlbSwtStPurchaseHistory.getDlbStatusByStatus().
                                getStatusCode()));
                dlbWbStatusRollback.setActiveStatus("Pending");
                dlbWbStatusRollback.setLastUpdatedTime(new Date());
                genericRepository.update(dlbWbStatusRollback);
                saveAudit(dlbWbStatusRollback.getId(), "REQUEST",
                        dlbWbSystemUser.getDlbWbEmployee().getName(),
                        dlbSwtStPurchaseHistory.getDlbStatusByStatus().
                                getStatusCode());

                saveActivityAudit("Request Status Rollback", dlbWbSystemUser,
                        dlbSwtStPurchaseHistory.getDlbStatusByStatus().getDescription(),
                        product.getDescription(),
                        dlbSwtStPurchaseHistory.getDrawNo(),
                        dlbSwtStPurchaseHistory.getSerialNo(), "ROBST", new Date(), "REQUEST");
            }

        } catch (Exception ex) {
            Logger.getLogger(RequestStatusRollbackService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void approveData(StatusRollbackDto statusRollbackDto, DlbWbSystemUser dlbWbSystemUser) {
        try {
            WhereStatement statement = new WhereStatement("id", statusRollbackDto.getPurchaseHistoryId(), SystemVarList.EQUAL);

            DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory
                    = (DlbSwtStPurchaseHistory) genericRepository.get(DlbSwtStPurchaseHistory.class, statement);
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(dlbSwtStPurchaseHistory.getProductCode(),
                    DlbWbProduct.class);

            DlbWbStatusRollback dlbWbStatusRollback = getByPurchaseId(dlbSwtStPurchaseHistory.getId());

            DlbStatus dlbStatus = (DlbStatus) genericRepository.get(21, DlbStatus.class);
            dlbSwtStPurchaseHistory.setDlbStatusByStatus(dlbStatus);
            genericRepository.update(dlbSwtStPurchaseHistory);
            updateRollbackData(statusRollbackDto.getId(), dlbWbSystemUser);

            saveActivityAudit("Approve Status Rollback", dlbWbSystemUser,
                    dlbSwtStPurchaseHistory.getDlbStatusByStatus().getDescription(),
                    product.getDescription(),
                    dlbSwtStPurchaseHistory.getDrawNo(),
                    dlbSwtStPurchaseHistory.getSerialNo(), "ROBAP", dlbWbStatusRollback.getCreatedTime(), "APPROVE");

        } catch (Exception ex) {
            Logger.getLogger(RequestStatusRollbackService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void updateRollbackData(Integer rollBackId, DlbWbSystemUser dlbWbSystemUser) throws Exception {
        WhereStatement statement = new WhereStatement("id", rollBackId, SystemVarList.EQUAL);
        DlbWbStatusRollback dlbWbStatusRollback
                = (DlbWbStatusRollback) genericRepository.get(DlbWbStatusRollback.class, statement);
        dlbWbStatusRollback.setActiveStatus("Approved");
        dlbWbStatusRollback.setStatus("21");
        genericRepository.update(dlbWbStatusRollback);
        saveAudit(dlbWbStatusRollback.getId(), "APPROVE", dlbWbSystemUser.getDlbWbEmployee().getName(), 21);
    }

    @Transactional
    private JSONArray buildData(List<DlbSwtStPurchaseHistory> historys) {
        JSONArray array = new JSONArray();
        historys.forEach(obj -> {

            JSONObject row = new JSONObject();

            DlbWbProduct product = (DlbWbProduct) genericRepository.get(obj.getProductCode(), DlbWbProduct.class);
            WhereStatement whereStatement = new WhereStatement("serialNumber", obj.getSerialNo(), SystemVarList.EQUAL);
            DlbWbTicket dlbWbTicket = (DlbWbTicket) genericRepository.get(DlbWbTicket.class, whereStatement);
            WhereStatement whereStatementA = new WhereStatement("purchaseHistoryId", obj.getId(), SystemVarList.EQUAL);
            DlbWbStatusRollback dlbWbStatusRollback = (DlbWbStatusRollback) genericRepository.get(DlbWbStatusRollback.class, whereStatementA);

            row.put("id", obj.getId());
            row.put("lotteryName", product.getDescription());
            row.put("lotteryId", obj.getSerialNo());
            row.put("drawNo", (obj.getDrawNo()));
            row.put("drawDate", dateFormat.format(obj.getDrawDate()));
            row.put("name", obj.getDlbSwtStWallet().getFirstName() + " " + obj.getDlbSwtStWallet().getLastName());
            row.put("nic", obj.getDlbSwtStWallet().getNic());
            row.put("winningPrize", obj.getWinningPrize());
            if (dlbWbStatusRollback != null) {
                if (dlbWbStatusRollback.getActiveStatus().equals("Approved")) {
                    row.put("status", "-");
                } else {
                    row.put("status", dlbWbStatusRollback.getActiveStatus());
                }
            } else {
                row.put("status", "-");
            }
            array.add(row);

        });

        return array;
    }

    @Transactional
    private JSONArray buildRollBackData(List<DlbWbStatusRollback> dlbWbStatusRollbacks) {
        JSONArray array = new JSONArray();
        dlbWbStatusRollbacks.forEach(obj -> {

            JSONObject row = new JSONObject();

            DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory = (DlbSwtStPurchaseHistory) genericRepository.get(obj.getPurchaseHistoryId(), DlbSwtStPurchaseHistory.class);
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(dlbSwtStPurchaseHistory.getProductCode(), DlbWbProduct.class);
            WhereStatement whereStatement = new WhereStatement("serialNumber", dlbSwtStPurchaseHistory.getSerialNo(), SystemVarList.EQUAL);
            DlbWbTicket dlbWbTicket = (DlbWbTicket) genericRepository.get(DlbWbTicket.class, whereStatement);

            row.put("purchaseHistoryId", dlbSwtStPurchaseHistory.getId());
            row.put("id", obj.getId());
            row.put("lotteryName", product.getDescription());
            row.put("lotteryId", dlbSwtStPurchaseHistory.getSerialNo());
            row.put("drawNo", (dlbSwtStPurchaseHistory.getDrawNo()));
            row.put("drawDate", dateFormat.format(dlbSwtStPurchaseHistory.getDrawDate()));
            row.put("name", dlbSwtStPurchaseHistory.getDlbSwtStWallet().getFirstName() + " " + dlbSwtStPurchaseHistory.getDlbSwtStWallet().getLastName());
            row.put("nic", dlbSwtStPurchaseHistory.getDlbSwtStWallet().getNic());
            row.put("winningPrize", dlbSwtStPurchaseHistory.getWinningPrize());
            row.put("status", obj.getActiveStatus());
            array.add(row);

        });

        return array;
    }

    @Transactional
    private void saveAudit(Integer statusRollbackId, String activity,
            String userName, Integer currentStatus) throws Exception {
        DlbWbStatusRollbackAudit dlbWbStatusRollbackAudit = new DlbWbStatusRollbackAudit();
        dlbWbStatusRollbackAudit.setStatusRollbackId(statusRollbackId);
        dlbWbStatusRollbackAudit.setActivity(activity);
        dlbWbStatusRollbackAudit.setUserName(userName);
        dlbWbStatusRollbackAudit.setCreatedDate(new Date());
        dlbWbStatusRollbackAudit.setCurrentStatus(currentStatus);
        genericRepository.save(dlbWbStatusRollbackAudit);
    }

    @Transactional
    public void saveActivityAudit(String activity, DlbWbSystemUser dlbWbSystemUser,
            String currentStatus, String lotteryName, String lotteryDraw,
            String lotteryId, String page, Date rollbackDate, String action) throws Exception {
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Activity", activity);
        jSONObject.put("User Name", dlbWbSystemUser.getDlbWbEmployee().getName());
        jSONObject.put("Created Date", dateFormat2.format(rollbackDate).replace(":", "."));
        jSONObject.put("Current Status", currentStatus);
        jSONObject.put("Lottery Name", lotteryName);
        jSONObject.put("Lottery Draw No", lotteryDraw);
        jSONObject.put("Lottery Id", lotteryId);
        activityLogService.save(activityLogService.buildActivityLog(
                action, jSONObject, page, dlbWbSystemUser));
    }
}
