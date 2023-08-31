/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.DlbWbWiningFileDetails;
import com.epic.dlb.dto.DlbWbWiningFileDto;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbTicketFile;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.report.model.TicketViewModel;
import com.epic.dlb.repository.TicketRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kasun_n
 */
@Repository("winningFileReportRepository")
public class WinningFileReportRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TicketRepository ticketRepository;

    private SimpleDateFormat dateFormat;

    public List getWiningFileReport(List<WhereStatement> whereStatements, int start, int end) {

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List rstList = new ArrayList();
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria

        String queryStr = "SELECT "
                + "COUNT(WF.ID) AS recordCount,\n"
                + "WF.DATE  AS drawDate,\n"
                + "SUM(IF(PH.`STATUS`<:winning_price, PH.WINNING_PRIZE, 0)) AS epicAmount, \n"
                + "SUM(IF(PH.`STATUS`>:winning_price, PH.WINNING_PRIZE, 0)) AS dlbAmount,\n"
                + "SUM(IF(PH.`STATUS`=:user_claimed_status, PH.WINNING_PRIZE, 0)) AS claimedAmount,\n"
                + "SUM(IF(PH.`STATUS`=:file_gen_status, PH.WINNING_PRIZE, 0)) AS generatedAmount,\n"
                + "SUM(IF(PH.`STATUS`=:claimedDone, PH.WINNING_PRIZE, 0)) AS claimedDoneAmount,\n"
                + "SUM(IF(PH.`STATUS`=:dlb_claimed_status, PH.WINNING_PRIZE, 0)) AS dlbClaimedAmount,\n"
                + "SUM(PH.WINNING_PRIZE) AS amount "
                + "FROM DLB_WB_WINING_FILE AS WF "
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY AS PH ON WF.DRAW_NO = PH.DRAW_NO AND WF.PRODUCT_CODE = PH.PRODUCT_CODE\n"
                + "INNER JOIN DLB_STATUS AS DST ON WF.PENDING_APPROVAL = DST.STATUS_CODE\n"
                + "INNER JOIN DLB_WB_PRODUCT AS DP ON WF.PRODUCT_CODE = DP.PRODUCT_CODE \n"
                + "WHERE  WF.DATE \n"
                + "BETWEEN :from_date \n"
                + "AND  :to_date AND WF.PENDING_APPROVAL = :status \n"
                + "GROUP BY WF.DATE;";

        Query query = session.createSQLQuery(queryStr);

        query.setInteger("winning_price", (Integer) SystemVarList.PRIZE_LESS_THAN_100000);
        query.setString("from_date", dateFormat.format((Date) whereStatements.get(0).getValue()));
        query.setString("to_date", dateFormat.format((Date) whereStatements.get(1).getValue()));
        query.setInteger("status", SystemVarList.APPROVED);
        query.setInteger("file_gen_status", SystemVarList.PRIZE_PAY_FILE_GENERATED);
        query.setInteger("user_claimed_status", SystemVarList.USER_CLAIMED);
        query.setInteger("claimedDone", SystemVarList.CLAIMED_DONE);
        query.setInteger("dlb_claimed_status", SystemVarList.DLB_CLAIMED);

        query.setResultTransformer(Transformers.aliasToBean(DlbWbWiningFileDto.class));

//        List<DlbWbWiningFileDto> files = query.list();
        rstList = query.list();

        return rstList;
    }

    public Double getEpicWinningAmount(int id) {
        Double amount = 0.0;
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        DlbWbWiningFile winingFile = (DlbWbWiningFile) session.get(DlbWbWiningFile.class, id);

        String query1 = "SELECT "
                + " Sum(IF(WINING_PRIZE<" + SystemVarList.EPIC_MAX_CLAIMED_AMOUNT + ",WINING_PRIZE,0.00)) AS epicAmount "
                + " FROM "
                + " " + winingFile.getResultTableName()
                + " INNER JOIN "
                + "DLB_SWT_ST_PURCHASE_HISTORY ON " + winingFile.getResultTableName() + ".SERIAL_NO = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO"
                + " WHERE " + winingFile.getResultTableName() + ".WINING_ID='" + winingFile.getId() + "' "
                + " GROUP BY "
                + winingFile.getResultTableName() + ".WINING_ID  ";

        Query createQuery = session.createSQLQuery(query1);

        List list = createQuery.list();

        if (list.size() > 0) {
            amount = (Double) list.get(0);
        }
        return amount;
    }

    public int acceptPayment(int id, String refNo) {

        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        DlbWbWiningFile winingFile = (DlbWbWiningFile) session.get(DlbWbWiningFile.class, id);

        String query1 = "SELECT "
                + " DLB_SWT_ST_PURCHASE_HISTORY.ID "
                + " FROM "
                + " " + winingFile.getResultTableName()
                + " INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ON " + winingFile.getResultTableName() + ".SERIAL_NO = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO AND " + winingFile.getResultTableName() + ".PRODUCT_CODE = DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE"
                + " WHERE " + winingFile.getResultTableName() + ".WINING_ID='" + winingFile.getId() + "' "
                + " AND DLB_SWT_ST_PURCHASE_HISTORY.`STATUS` = 20      AND\n"
                + winingFile.getResultTableName() + ".WINING_PRIZE > 0 ";

        Query createQuery = session.createSQLQuery(query1);

        List list = createQuery.list();

        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            int recordID = (int) iterator.next();
            DlbSwtStPurchaseHistory purchaseHistory = (DlbSwtStPurchaseHistory) session.get(DlbSwtStPurchaseHistory.class, recordID);
            purchaseHistory.setReferenceNo(refNo);
            purchaseHistory.setDlbStatusByStatus(new DlbStatus(39, null, null));
            session.update(purchaseHistory);

        }

        //update dlb file status
        DlbWbWiningFile dlbWbWiningFile = (DlbWbWiningFile) session.get(DlbWbWiningFile.class, id);
        dlbWbWiningFile.setDlbStatus(new DlbStatus(SystemVarList.WINNER_AMT_CLAIMED, null, null));
        session.update(dlbWbWiningFile);

        return 1;

    }

    public List listWinnerListByWiningFile(int id) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();

        DlbWbWiningFile winingFile = (DlbWbWiningFile) session.get(DlbWbWiningFile.class, id);

        String query1 = "SELECT "
                + "	DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO AS 'serialNumber', "
                + "	DLB_SWT_ST_WALLET.NIC AS 'nic', "
                + "	DLB_SWT_ST_PURCHASE_HISTORY.LOTTERY_NUMBERS AS 'lotteryNumbers',  "
                + "	DLB_SWT_ST_PURCHASE_HISTORY.TXN_ID AS 'tid',  "
                + "	DLB_SWT_ST_WALLET.NIC AS 'mobileNo',  "
                + "	IFNULL(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE,0) AS 'winingPrize' "
                + "FROM "
                + winingFile.getResultTableName()
                + " INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ON " + winingFile.getResultTableName() + ".SERIAL_NO = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO  "
                + " LEFT JOIN DLB_SWT_ST_WALLET ON DLB_SWT_ST_PURCHASE_HISTORY.WALLET_ID = DLB_SWT_ST_WALLET.ID "
                + " AND " + winingFile.getResultTableName() + ".PRODUCT_CODE = DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE "
                + " WHERE " + winingFile.getResultTableName() + ".WINING_ID='" + winingFile.getId() + "' "
                + "AND  DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE > 0";

//        String query1 = "SELECT "
//                + " DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO AS 'serialNumber',"
//                + " DLB_SWT_ST_WALLET.NIC AS 'dlbSwtStWallet.nic',  "
//                + " DLB_SWT_ST_WALLET.NIC AS 'dlbSwtStWallet.mobileNo'  "
//                + " FROM "
//                + " " + winingFile.getResultTableName()
//                + " INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ON " + winingFile.getResultTableName() + ".SERIAL_NO = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO "
//                + " AND " + winingFile.getResultTableName() + ".PRODUCT_CODE = DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE"
//                + " LEFT JOIN DLB_SWT_ST_WALLET ON DLB_SWT_ST_PURCHASE_HISTORY.WALLET_ID = DLB_SWT_ST_WALLET.ID "
//                + " WHERE " + winingFile.getResultTableName() + ".WINING_ID='" + winingFile.getId() + "' "             
//                + " AND DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE > 0 ";
        Query createQuery = session.createSQLQuery(query1);
        createQuery.setResultTransformer(Transformers.aliasToBean(TicketViewModel.class));

        List list = createQuery.list();

        return list;

    }

    public List generatePrize(Connection connection) throws SQLException, FileNotFoundException, UnsupportedEncodingException, ParseException {

        List result = new ArrayList();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");

        Statement statement1 = null;
        Statement statement2 = null;
        Statement statement3 = null;

        ResultSet prizePayRst1 = null;

        Date drawDateDt = new Date();

        String fileNameDrawDate = dateFormat2.format(drawDateDt);

        Integer sqNo = 1;

        try {
            statement1 = connection.createStatement();
            prizePayRst1 = statement1.executeQuery("SELECT ID FROM DLB_WB_PRIZE_PAYMENT_HISTORY ORDER BY ID DESC  LIMIT 0,1");

            while (prizePayRst1.next()) {
                sqNo = prizePayRst1.getInt("ID");

            }

            statement1 = null;

            File file = null;
//        File Name: DLB_SALES_<Brand Code>_<Draw No>
            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "DLB_PRIZE_" + fileNameDrawDate + "_" + sqNo + "_" + SystemVarList.DISTRIBUTER_CODE + ".txt";

            //create file
            PrintWriter writer = new PrintWriter(tmpFilepath, "UTF-8");

            statement2 = connection.createStatement();
            statement3 = connection.createStatement();

            String query1 = "SELECT\n"
                    + "DLB_SWT_ST_PURCHASE_HISTORY.ID AS ID,\n"
                    + "DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO AS serialNumber,\n"
                    + "DLB_SWT_ST_WALLET.NIC AS nic,\n"
                    + "DLB_SWT_ST_PURCHASE_HISTORY.DRAW_DATE,\n"
                    + "DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE AS winningPrize\n"
                    + "FROM\n"
                    + "DLB_SWT_ST_PURCHASE_HISTORY\n"
                    + "INNER JOIN DLB_SWT_ST_WALLET ON DLB_SWT_ST_PURCHASE_HISTORY.WALLET_ID = DLB_SWT_ST_WALLET.ID\n"
                    //                    + "INNER JOIN DLB_WB_WINING_FILE ON DLB_SWT_ST_PURCHASE_HISTORY.DRAW_NO = DLB_WB_WINING_FILE.DRAW_NO\n"
                    + "WHERE\n"
                    + "DLB_SWT_ST_PURCHASE_HISTORY.`STATUS` = '" + SystemVarList.USER_CLAIMED + "'";

            ResultSet resultSet = statement2.executeQuery(query1);

            int ticketCount = 0;

            while (resultSet.next()) {

                Integer id = resultSet.getInt("ID");

                String space = "";

                if (resultSet.getString("winningPrize") == null) {
                    space = "           ";
                } else {
                    space = appendSpace(resultSet.getString("winningPrize"));
                }

                String txtLine = resultSet.getString("serialNumber") + " "
                        + resultSet.getString("winningPrize") + ".00" + space + " NIC "
                        + resultSet.getString("nic");

                writer.println(txtLine);

                statement3.executeUpdate("UPDATE  `DLB_SWT_ST_PURCHASE_HISTORY` SET `STATUS` = '" + SystemVarList.PRIZE_PAY_FILE_GENERATED + "' WHERE `ID` = " + id);

                ticketCount++;

            }

            statement1 = connection.createStatement();

            statement1.executeUpdate("INSERT INTO `DLB_WB_PRIZE_PAYMENT_HISTORY`"
                    + "(`CREATED_DATE`, "
                    + "`CREATED_USER`, "
                    + "`COUNT`,`IS_NEW`,`FILE`) "
                    + "VALUES (NOW(), 'admin', " + ticketCount + ",'1','" + tmpFilepath + "')");

            writer.close();
            //assign created file to return varible       
            result.add(tmpFilepath);
            file = new File(tmpFilepath);
            result.add(file.getName());
            connection.commit();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }

            if (statement1 != null) {
                statement1.close();
            }

            if (statement2 != null) {
                statement2.close();
            }
        }

    }

    public String appendSpace(String prize) {
        StringBuilder space = new StringBuilder();

        for (int i = 0; i < 11 - prize.length(); i++) {
            if (10 - prize.length() > 0) {
                space.append(" ");
            } else {
                space.append("");
            }
        }

        return space.toString();
    }

    public Long getWiningFileReportCount(List<WhereStatement> whereStatements) {
        List rstList = new ArrayList();
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(DlbWbWiningFile.class);

        //Add where clause
        for (WhereStatement where : whereStatements) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;

                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;

                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;

                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;

                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;

                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }

        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();

        return count;

    }

    public List getWiningFileDetail(Date date) {

        List rstList = new ArrayList();
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria

        String queryStr = "SELECT\n"
                + "DLB_WB_WINING_FILE.ID AS id,\n"
                + "DLB_WB_PRODUCT.DESCRIPTION AS productDescription,\n"
                + "DLB_WB_WINING_FILE.DRAW_NO AS drawNo,\n"
                + "DLB_WB_WINING_FILE.DATE AS date,\n"
                + "DLB_WB_WINING_FILE.CREATED_TIME AS createdTime,\n"
                + "DLB_WB_WINING_FILE.RECORD_COUNT AS recordCount,\n"
                + "IF(DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`<:winning_price, SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE), 0) AS epicAmount,\n"
                + "IF(DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`>:winning_price, SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE), 0) AS dlbAmount,\n"
                + "IF(DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`=:claimed_status , SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE), 0) AS claimedAmount,\n"
                + "IF(DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`=:dlb_claimed_status , SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE), 0) AS dlbClaimedAmount,\n"
                + "IF(DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`=:file_gen_status , SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE), 0) AS generatedAmount,\n"
                + "IF(DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`=:claimedDone , SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE), 0) AS claimedDoneAmount,\n"
                + "SUM(DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE) AS amount,\n"
                + "WF_STATUS.DESCRIPTION as description,\n"
                + "WF_STATUS.STATUS_CODE  as statusCode "
                + "FROM\n"
                + "DLB_WB_WINING_FILE\n"
                + "INNER JOIN DLB_WB_PRODUCT ON DLB_WB_WINING_FILE.PRODUCT_CODE = DLB_WB_PRODUCT.PRODUCT_CODE\n"
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ON DLB_WB_WINING_FILE.DRAW_NO = DLB_SWT_ST_PURCHASE_HISTORY.DRAW_NO AND DLB_WB_WINING_FILE.PRODUCT_CODE = DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE\n"
                + "INNER JOIN DLB_STATUS WF_STATUS ON DLB_WB_WINING_FILE.PENDING_APPROVAL = WF_STATUS.STATUS_CODE\n"
                + "INNER JOIN DLB_STATUS PH_STATUS ON DLB_SWT_ST_PURCHASE_HISTORY.`STATUS` = PH_STATUS.STATUS_CODE\n"
                + "WHERE WF_STATUS.STATUS_CODE <> :status AND DLB_WB_WINING_FILE.DATE=:date   "
                + "GROUP BY\n"
                + "DLB_WB_WINING_FILE.ID, DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`";

        Query query = session.createSQLQuery(queryStr);

        query.setInteger("winning_price", (Integer) SystemVarList.PRIZE_LESS_THAN_100000);
        query.setDate("date", (Date) date);
        query.setInteger("status", SystemVarList.SUBMITED);
        query.setInteger("claimed_status", SystemVarList.USER_CLAIMED);
        query.setInteger("dlb_claimed_status", SystemVarList.DLB_CLAIMED);
        query.setInteger("file_gen_status", SystemVarList.PRIZE_PAY_FILE_GENERATED);
        query.setInteger("claimedDone", SystemVarList.CLAIMED_DONE);

        query.setResultTransformer(Transformers.aliasToBean(DlbWbWiningFileDetails.class));
        rstList = query.list();

        return rstList;
    }
}
