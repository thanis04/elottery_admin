/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.DlbWbWiningFileDto;
import com.epic.dlb.dto.SalesTicket;
import com.epic.dlb.dto.WinningReport;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.report.model.PageData;
import com.epic.dlb.report.repository.WinningFileReportRepository;
import com.epic.dlb.repository.CustomRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.repository.JDBCConnection;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("winningFileReportService")
public class WinningFileReportService {

    @Autowired
    private WinningFileReportRepository winningFileReportRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private JDBCConnection jDBCConnection;

    @Autowired
    private CustomRepository customRepository;

    @Transactional(rollbackFor = Exception.class)
    public List searchTiketFile(List<WhereStatement> whereStatements, int start, int end) {
        List<DlbWbWiningFile> winingFileReport = winningFileReportRepository.getWiningFileReport(whereStatements, start, end);
        Iterator<DlbWbWiningFile> iterator = winingFileReport.iterator();

        while (iterator.hasNext()) {
            DlbWbWiningFile next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());

        }

        return winingFileReport;
    }

    @Transactional(rollbackFor = Exception.class)
    public Double getEpicWinningAmount(int id) {
        return winningFileReportRepository.getEpicWinningAmount(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void acceptPayment(int winningFileID, String refNo) {

        winningFileReportRepository.acceptPayment(winningFileID, refNo);
    }

    @Transactional
    public List listWalletByWiningFile(int id) {

        return winningFileReportRepository.listWinnerListByWiningFile(id);
    }

    @Transactional
    public List generatePrize() throws SQLException, FileNotFoundException, UnsupportedEncodingException, ParseException {
        System.out.println(new Date() + ": prize payment file generated");
        Connection connection = jDBCConnection.getConnection();
        connection.setAutoCommit(false);
        return winningFileReportRepository.generatePrize(connection);
    }

    @Transactional
    public List listTicketByTransaction(String id) {

        WhereStatement statement = new WhereStatement("txnId", id, SystemVarList.EQUAL);
        return genericRepository.list(DlbSwtStPurchaseHistory.class, statement);

    }

    @Transactional
    public List listTicketByNICPaginated(String nic, String str_start, String str_length) {
        List<WhereStatement> whereStatements = new ArrayList<WhereStatement>();
        WhereStatement statement = new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL);
        whereStatements.add(statement);
        Integer i_start = Integer.parseInt(str_start);
        Integer i_length = Integer.parseInt(str_length);
        return genericRepository.listWithQueryPaginated(DlbSwtStPurchaseHistory.class, whereStatements, i_start, i_length);
    }

    @Transactional
    public Long totalRecordsTicketByNIC(String nic) {
        List<WhereStatement> whereStatements = new ArrayList<WhereStatement>();
        WhereStatement statement = new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL);
        whereStatements.add(statement);
        return genericRepository.CountWithQuery(DlbSwtStPurchaseHistory.class, whereStatements);
    }

    @Transactional(rollbackFor = Exception.class)
    public PageData searchTiketFilePaginated(List<WhereStatement> whereStatements, int start, int end) {
        PageData pd = new PageData();
        List<DlbWbWiningFileDto> winingFileReport = winningFileReportRepository.getWiningFileReport(whereStatements, start, end);
        Iterator<DlbWbWiningFileDto> iterator = winingFileReport.iterator();

//        while (iterator.hasNext()) {
//            DlbWbWiningFileDto next = iterator.next();
//            Hibernate.initialize(next.getDlbStatus());
//            
//        }
        pd.setDataList(winingFileReport);
        pd.setNumberOfRecords(winingFileReport.size());

        return pd;
    }

    @Transactional(rollbackFor = Exception.class)
    public PageData getTiketDetails(Date date) {
        PageData pd = new PageData();
        List<DlbWbWiningFileDto> winingFileReport = winningFileReportRepository.getWiningFileDetail(date);

        pd.setDataList(winingFileReport);
        pd.setNumberOfRecords(winingFileReport.size());

        return pd;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long searchTiketFilePaginatedNumberOfRecords(List<WhereStatement> whereStatements) {
        Long totalRecords = winningFileReportRepository.getWiningFileReportCount(whereStatements);
        return totalRecords;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getPurchaseHistoryForUserClaimed(String fromDate, String toDate, String productcode, Integer statusCode) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT\n"
                + "winning_report.CREATED_DATE,\n"
                + "winning_report.DESCRIPTION,\n"
                + "winning_report.SERIAL_NO,\n"
                + "winning_report.DRAW_NO,\n"
                + "winning_report.DRAW_DATE,\n"
                + "winning_report.FIRST_NAME,\n"
                + "winning_report.LAST_NAME,\n"
                + "winning_report.NIC,\n"
                + "winning_report.WINNING_PRIZE,\n"
                + "winning_report.`STATUS`,\n"
                + "winning_report.STATUS_CODE,\n"
                + "winning_report.CREATED_TIME,\n"
                + "winning_report.PRODUCT_CODE,\n"
                + "winning_report.LAST_UPDATED,\n"
                + "winning_report.TICKET_ID, \n"
                + "winning_report.PURCHASE_HISTORY_ID, \n"
                + "winning_report.TXN_ID, \n"
                + "winning_report.REDEEM_MODE, \n"
                + "winning_report.REDEEM_TIME \n"
                + "FROM \n"
                + "winning_report \n"
                + "WHERE \n"
                + "winning_report.DRAW_DATE BETWEEN '" + fromDate + "' AND '" + toDate
                + "' AND winning_report.WINNING_PRIZE> 0 ";

        String optionalWhere = "";
        if (productcode != null && !"0".equals(productcode)) {
            optionalWhere = " AND winning_report.PRODUCT_CODE ='" + productcode + "'";
        }

        if (statusCode != null && statusCode != 0) {
            optionalWhere = optionalWhere + " AND  winning_report.STATUS_CODE = '" + statusCode + "'";
        }

        String fullQuery = query + optionalWhere;

        Iterator<Object[]> list = customRepository.queryExecuter(fullQuery).iterator();
        List<WinningReport> winningReports = new ArrayList<WinningReport>();

        while (list.hasNext()) {
            Object[] next = list.next();

            WinningReport winningReport = new WinningReport();

            Timestamp createdDate = (Timestamp) next[0];
            Timestamp drawDate = (Timestamp) next[4];
            Timestamp lastUpdated = (Timestamp) next[13];
            Timestamp redemptionTime = null;
            if (next[18] != null) {
                redemptionTime = (Timestamp) next[18];
            }
            winningReport.setCreatedDate(dateFormat.format(createdDate));
            winningReport.setProductDescription((String) next[1]);
            winningReport.setSerialNo((String) next[2]);
            winningReport.setDrawNo((String) next[3]);
            winningReport.setDrawDate(dateFormat2.format(drawDate));
            winningReport.setFirstName((String) next[5]);
            winningReport.setLastName((String) next[6]);
            winningReport.setNic((String) next[7]);
            winningReport.setWinningAmount((String) next[8]);
            winningReport.setStatus((String) next[9]);
            winningReport.setStatuCode((Integer) next[10]);
            winningReport.setProductCode((String) next[1]);
            winningReport.setLastUpdated(dateFormat.format(lastUpdated));
            winningReport.setTicketId((Integer) next[14]);
            winningReport.setPurchaseHisId((Integer) next[15]);
            winningReport.setTxnId((String) next[16]);
            if (next[17] != null) {
                winningReport.setRedemptionMode((String) next[17]);
            } else {
                winningReport.setRedemptionMode("-");
            }
            if (next[18] != null) {
                winningReport.setRedemptionTime((String) dateFormat2.format(redemptionTime));
            } else {
                winningReport.setRedemptionTime("-");
            }
            winningReport.setTicketPrice("20.00");

            winningReports.add(winningReport);

        }

        return winningReports;
    }

    public JSONArray buildJsonForTransaction_by_nic(List ticketTranceactions) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray table = new JSONArray();
        Iterator<DlbSwtStPurchaseHistory> iterator = ticketTranceactions.iterator();
        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory transaction = iterator.next();
            JSONObject row = new JSONObject();

            row.put("serialno", transaction.getSerialNo());
            row.put("lottery", transaction.getProductDescription());
            row.put("draw_no", transaction.getDrawNo());
            row.put("draw_date", dateFormat.format(transaction.getDrawDate()));
            DlbSwtStWallet wallte = transaction.getDlbSwtStWallet();
            row.put("customer_ref", wallte.getFirstName() + " " + wallte.getLastName() + " " + wallte.getNic());
            row.put("purchase_date", dateFormat2.format(transaction.getCreatedDate()));
            row.put("claimed_date", dateFormat2.format(transaction.getLastUpdated()));
            row.put("numbers", transaction.getLotteryNumbers());
            row.put("price", transaction.getWinningPrize());
            table.add(row);

        }

        return table;
    }

}
