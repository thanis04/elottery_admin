/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.model.DlbSwtMtBank;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.report.model.TicketSearchVM;
import com.epic.dlb.report.model.TransactionSummery;
import com.epic.dlb.repository.CustomRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("transactionReportService")
public class TransactionReportService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private CustomRepository customRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionByDate(String fromDate, String toDate, String nic, String mobileNo, String transType, String paymentType, String bankCode) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("dateTime", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("dateTime", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);

        //add where 
        whereStatements.add(searchCriteriaFromDate);
        whereStatements.add(searchCriteriaToDate);

        if (transType != null && !transType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtTxnType.code", transType, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (nic != null && !nic.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (mobileNo != null && !mobileNo.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.mobileNo", mobileNo, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (paymentType != null && !paymentType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtPaymentMethod.code", paymentType, SystemVarList.EQUAL, SystemVarList.AND));
        }

        //remove next operator of last where statament 
        WhereStatement lastWhereStatement = whereStatements.get(whereStatements.size() - 1);
        whereStatements.remove(whereStatements.size() - 1);
        whereStatements.add(new WhereStatement(lastWhereStatement.getProperty(), lastWhereStatement.getValue(), lastWhereStatement.getOperator(), null));

        List listWithQuery = genericRepository.listWithQuery(DlbSwtStTransaction.class, whereStatements);
        Iterator<DlbSwtStTransaction> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbSwtStTransaction next = iterator.next();
            Hibernate.initialize(next.getDlbSwtMtTxnType());
            Hibernate.initialize(next.getDlbSwtStWallet());
            Hibernate.initialize(next.getDlbSwtMtPaymentMethod());

            if (next.getBank() != null) {
                DlbSwtMtBank dlbSwtMtBank = (DlbSwtMtBank) genericRepository.get(next.getBank(), DlbSwtMtBank.class);
                next.setBank(dlbSwtMtBank.getName());
            } else {
                next.setBank("-");
            }
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getBankTranferTransactionByDate(String fromDate, String toDate, String nic, String mobileNo, String transType, String fromBankAcc, String toBankAcc, String bankCode) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("dateTime", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("dateTime", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);

        //add where 
        whereStatements.add(searchCriteriaFromDate);
        whereStatements.add(searchCriteriaToDate);

        if (transType != null && !transType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtTxnType.code", transType, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (nic != null && !nic.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (mobileNo != null && !mobileNo.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.mobileNo", mobileNo, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (fromBankAcc != null && !fromBankAcc.isEmpty()) {
            whereStatements.add(new WhereStatement("fromAccount", fromBankAcc, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (toBankAcc != null && !toBankAcc.isEmpty()) {
            whereStatements.add(new WhereStatement("toAccount", toBankAcc, SystemVarList.EQUAL, SystemVarList.AND));
        }

        //remove next operator of last where statament 
        WhereStatement lastWhereStatement = whereStatements.get(whereStatements.size() - 1);
        whereStatements.remove(whereStatements.size() - 1);
        whereStatements.add(new WhereStatement(lastWhereStatement.getProperty(), lastWhereStatement.getValue(), lastWhereStatement.getOperator(), null));

        List listWithQuery = genericRepository.listWithQuery(DlbSwtStTransaction.class, whereStatements);

        Iterator<DlbSwtStTransaction> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbSwtStTransaction next = iterator.next();
            Hibernate.initialize(next.getDlbSwtMtTxnType());
            Hibernate.initialize(next.getDlbSwtStWallet());
            Hibernate.initialize(next.getDlbSwtMtPaymentMethod());
            // Hibernate.initialize(next.getDateTime());

            if (next.getBank() != null) {
                DlbSwtMtBank dlbSwtMtBank = (DlbSwtMtBank) genericRepository.get(next.getBank(), DlbSwtMtBank.class);
                next.setBank(dlbSwtMtBank.getName());
            } else {
                next.setBank("-");
            }
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionByDate(String fromDate, String toDate) {

        List<TransactionSummery> summerys = new ArrayList<>();
        List<WhereStatement> whereStatements = new ArrayList<>();
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("dateTime", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("dateTime", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchPaymentMethod = new WhereStatement("dlbSwtMtPaymentMethod.code", 0, SystemVarList.NOT_EQUAL, null);

        //add where 
        whereStatements.add(searchCriteriaFromDate);
        whereStatements.add(searchCriteriaToDate);
        whereStatements.add(searchPaymentMethod);

        //set projection list
        List projectionList = new ArrayList();
        projectionList.add("dlbSwtMtPaymentMethod.description as description,");
        projectionList.add("sum(amount) as amount ");

        List listWithQuery = genericRepository.listWithQueryNGroup(DlbSwtStTransaction.class, projectionList, "dlbSwtMtPaymentMethod",
                searchCriteriaFromDate, searchCriteriaToDate, searchPaymentMethod);

        Iterator iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            Object[] next = (Object[]) iterator.next();

            TransactionSummery summery = new TransactionSummery();
            summery.setDescription((String) next[0]);
            summery.setAmount(Double.parseDouble((String) next[1]));

            summerys.add(summery);

        }

        return summerys;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionByDatePaginated(String fromDate, String toDate, String nic, String mobileNo, String transType, String paymentType, String bankCode, String str_start, String str_length) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("dateTime", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("dateTime", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);

        //add where 
        whereStatements.add(searchCriteriaFromDate);
        whereStatements.add(searchCriteriaToDate);

        if (transType != null && !transType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtTxnType.code", transType, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (nic != null && !nic.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (mobileNo != null && !mobileNo.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.mobileNo", mobileNo, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (paymentType != null && !paymentType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtPaymentMethod.code", paymentType, SystemVarList.EQUAL, SystemVarList.AND));
        }

        //remove next operator of last where statament 
        WhereStatement lastWhereStatement = whereStatements.get(whereStatements.size() - 1);
        whereStatements.remove(whereStatements.size() - 1);
        whereStatements.add(new WhereStatement(lastWhereStatement.getProperty(), lastWhereStatement.getValue(), lastWhereStatement.getOperator(), null));

        Integer i_start = Integer.parseInt(str_start);
        Integer i_length = Integer.parseInt(str_length);
        List listWithQuery = genericRepository.listWithQueryPaginated(DlbSwtStTransaction.class, whereStatements, i_start, i_length);

        Iterator<DlbSwtStTransaction> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbSwtStTransaction next = iterator.next();
            Hibernate.initialize(next.getDlbSwtMtTxnType());
            Hibernate.initialize(next.getDlbSwtStWallet());
            Hibernate.initialize(next.getDlbSwtMtPaymentMethod());

            if (next.getBank() != null) {
                DlbSwtMtBank dlbSwtMtBank = (DlbSwtMtBank) genericRepository.get(next.getBank(), DlbSwtMtBank.class);
                next.setBank(dlbSwtMtBank.getName());
            } else {
                next.setBank("-");
            }
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long getTransactionByDateTotalReocrds(String fromDate, String toDate, String nic, String mobileNo, String transType, String paymentType, String bankCode) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("dateTime", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("dateTime", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);

        //add where 
        whereStatements.add(searchCriteriaFromDate);
        whereStatements.add(searchCriteriaToDate);

        if (transType != null && !transType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtTxnType.code", transType, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (nic != null && !nic.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (mobileNo != null && !mobileNo.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.mobileNo", mobileNo, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (paymentType != null && !paymentType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtPaymentMethod.code", paymentType, SystemVarList.EQUAL, SystemVarList.AND));
        }

        //remove next operator of last where statament 
        WhereStatement lastWhereStatement = whereStatements.get(whereStatements.size() - 1);
        whereStatements.remove(whereStatements.size() - 1);
        whereStatements.add(new WhereStatement(lastWhereStatement.getProperty(), lastWhereStatement.getValue(), lastWhereStatement.getOperator(), null));

        Long TotalRecords = genericRepository.CountWithQuery(DlbSwtStTransaction.class, whereStatements);

        return TotalRecords;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTransactionByDate(String fromDate, String toDate, String nic, String transType) {
        List<WhereStatement> whereStatements = new ArrayList<>();
        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("dateTime", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("dateTime", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);

        //add where 
        whereStatements.add(searchCriteriaFromDate);
        whereStatements.add(searchCriteriaToDate);

        if (transType != null && !transType.equals("0")) {
            whereStatements.add(new WhereStatement("dlbSwtMtTxnType.code", transType, SystemVarList.EQUAL, SystemVarList.AND));
        }
        if (nic != null && !nic.isEmpty()) {
            whereStatements.add(new WhereStatement("dlbSwtStWallet.nic", nic, SystemVarList.EQUAL, SystemVarList.AND));
        }

        //remove next operator of last where statament 
        WhereStatement lastWhereStatement = whereStatements.get(whereStatements.size() - 1);
        whereStatements.remove(whereStatements.size() - 1);
        whereStatements.add(new WhereStatement(lastWhereStatement.getProperty(), lastWhereStatement.getValue(), lastWhereStatement.getOperator(), null));

        List listWithQuery = genericRepository.listWithQuery(DlbSwtStTransaction.class, whereStatements);
        Iterator<DlbSwtStTransaction> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbSwtStTransaction next = iterator.next();
            Hibernate.initialize(next.getDlbSwtMtTxnType());
            Hibernate.initialize(next.getDlbSwtStWallet());
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> getTransactonSummeryByDate(String fromDate, String toDate) throws Exception {

        String query = "SELECT\n"
                + "DATE_FORMAT(DLB_SWT_ST_TRANSACTION.UPDATEDTIME,\"%Y- %M- %d\"),\n"
                + "DLB_SWT_MT_TXN_TYPE.DESCRIPTION,\n"
                + "Sum(DLB_SWT_ST_TRANSACTION.AMOUNT),\n"
                + "SUM(IF(DLB_SWT_ST_TRANSACTION.PAY_METHOD='"+SystemVarList.PAY_METHOD_CREDIT_CARD+"' "
                + "OR DLB_SWT_ST_TRANSACTION.PAY_METHOD='"+SystemVarList.PAY_METHOD_DEBIT_CARD+"',"+SystemVarList.CARD_COMMISION+"* DLB_SWT_ST_TRANSACTION.AMOUNT,0.00)) AS CARD,\n"
                + "SUM(IF(DLB_SWT_ST_TRANSACTION.PAY_METHOD='"+SystemVarList.PAY_METHOD_CASA+"',"+SystemVarList.CASA_COMMISION+"* DLB_SWT_ST_TRANSACTION.AMOUNT,0.00)) AS CASA "
                + "FROM\n"
                + "DLB_SWT_ST_TRANSACTION\n"
                + "INNER JOIN DLB_SWT_MT_TXN_TYPE ON DLB_SWT_ST_TRANSACTION.TXNTYPE = DLB_SWT_MT_TXN_TYPE.`CODE`\n"
                + "WHERE DLB_SWT_ST_TRANSACTION.UPDATEDTIME > '" + fromDate + "' AND DLB_SWT_ST_TRANSACTION.UPDATEDTIME < '" + toDate + "' "
                + "AND DLB_SWT_ST_TRANSACTION.TXNTYPE='" + SystemVarList.TICKERT_PURCHASE + "'\n"
                + "GROUP BY\n"
                + "DATE_FORMAT(DLB_SWT_ST_TRANSACTION.UPDATEDTIME,\"%Y- %M- %d\"), DLB_SWT_MT_TXN_TYPE.DESCRIPTION \n"
                + "ORDER BY  DLB_SWT_ST_TRANSACTION.UPDATEDTIME ASC";

        List<Object[]> result = customRepository.queryExecuter(query);

        return result;

    }

    
    
    public List getAuditTrail(String fromdate, String toDate) {
        List list =new ArrayList();
        
        return list;
    }
}
