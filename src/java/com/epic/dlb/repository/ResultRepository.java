/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbMainDrawPrizeCode;
import com.epic.dlb.model.DlbWbMainDrawPrizeCodeId;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.model.DlbWbWiningFileErrorDetails;
import com.epic.dlb.service.EncryptionService;
import com.epic.dlb.util.common.SystemVarList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kasun_n
 */
@Repository("resultRepository")
public class ResultRepository {

    /*-----------------------------
    Dependancy Injection
    -----------------------------*/
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EncryptionService encryptionService;

    private Session newSession;

    public void saveResultData(HttpSession session, String fileName, DlbWbWiningFile winingFile, Connection connection) throws IOException, java.text.ParseException, NoSuchAlgorithmException {

        //build result table data from .txt file
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        BufferedReader br = null;
        PreparedStatement prepareStatement1 = null;
        PreparedStatement prepareStatement2 = null;
        PreparedStatement prepareStatement3 = null;

        PreparedStatement preparePurUpdate = null;
        PreparedStatement preparePurSelect = null;
        JSONObject progressobject = new JSONObject();

        ResultSet isTableExit = null;
        ResultSet resultSet2 = null;

        newSession = sessionFactory.openSession();

        try {

            br = new BufferedReader(new FileReader(fileName));

            //get total lines of the text file
            Long totaLinesLng = br.lines().count();
            int totalLines = totaLinesLng.intValue();
            int cmpltdProgress = 0;
            int sectionIndex = 1;

            br = null;
            br = new BufferedReader(new FileReader(fileName));

            //create table name
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String tableName = "DLB_WB_RESULT_" + sdf.format(winingFile.getDate());

            //check table is exsits  
            prepareStatement1 = connection.prepareStatement("SELECT *   "
                    + "FROM information_schema.tables  "
                    + "WHERE table_name = '" + tableName + "'  "
                    + "LIMIT 1;");

            isTableExit = prepareStatement1.executeQuery();

            if (!isTableExit.next()) {
                //table is not exsits
                //create result table

                Statement createStatement = connection.createStatement();
                createStatement.execute(""
                        + "CREATE TABLE " + tableName + " (  "
                        + "  `PRODUCT_CODE` varchar(16) NOT NULL,  "
                        + "  `SERIAL_NO` varchar(64) CHARACTER SET utf8 NOT NULL,  "
                        + "  `DRAW_DATE` date NOT NULL,  "
                        + "  `BOOK_NO` varchar(64) CHARACTER SET utf8 NOT NULL,  "
                        + "  `TICKERT_NO` varchar(32) CHARACTER SET utf8 NOT NULL,  "
                        + "  `PRIZE_CODE` varchar(16) CHARACTER SET utf8 NOT NULL,  "
                        + "  `DISTRICT_CODE` varchar(16) CHARACTER SET utf8 NOT NULL,  "
                        + "  `AGENT_NO` varchar(16) CHARACTER SET utf8 NOT NULL,  "
                        + "  `WINING_PRIZE` varchar(64) CHARACTER SET utf8 NOT NULL,  "
                        + "  `WINING_ID` int(11) NOT NULL,  "
                        + "  `LAST_UPDATED_USER` varchar(128) DEFAULT NULL,  "
                        + "  `LAST_UPDATED_TIME` datetime DEFAULT NULL,  "
                        + "  `CREATED_TIME` datetime DEFAULT NULL, "
                        + " `SHA_KEY` varchar(255) NOT NULL,  "
                        + "  PRIMARY KEY (`PRODUCT_CODE`,`SERIAL_NO`)  "
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

                //create trigger for purchase history update (Ticket winning status update)  
//                String triggerCreateSql = ""
//                        + " CREATE TRIGGER update_win_ticket_status_" + tableName + " AFTER INSERT ON " + tableName + "  "
//                        + " FOR EACH ROW  "
//                        + "	BEGIN  "
//                        + "	DECLARE  "
//                        + "		status_code INT DEFAULT " + SystemVarList.PRIZE_LESS_THAN_100000 + ";  "
//                        + "	IF  "
//                        + "	  NEW.PRIZE_CODE=1 THEN  "
//                        + "		SET status_code = " + SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000 + ";  "
//                        + "	ELSEIF  "
//                        + "		NEW.WINING_PRIZE > " + SystemVarList.EPIC_MAX_CLAIMED_AMOUNT + " THEN	  "
//                        + "		SET status_code = " + SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000 + ";  "
//                        + "	END IF;  "
//                        + "	UPDATE `DLB_SWT_ST_PURCHASE_HISTORY`   "
//                        + "	SET `STATUS` = status_code,  "
//                        + "	`WINNING_PRIZE` = `WINNING_PRIZE` + NEW.WINING_PRIZE ,  "
//                        + "	`SHA_KEY` = NEW.SHA_KEY   "
//                        + "	WHERE  "
//                        + "		( `DLB_SWT_ST_PURCHASE_HISTORY`.`SERIAL_NO` = NEW.SERIAL_NO AND `DLB_SWT_ST_PURCHASE_HISTORY`.`PRODUCT_CODE` = NEW.PRODUCT_CODE );  "
//                        + " END ";
//                createStatement.execute(triggerCreateSql);
                createStatement.execute("UPDATE "
                        + "`DLB_SWT_ST_PURCHASE_HISTORY` SET `NOTIFY_STATUS` = 0  "
                        + "WHERE `DRAW_NO` = '" + winingFile.getDrawNo() + "' AND `PRODUCT_CODE` = '" + winingFile.getDlbWbProduct().getProductCode() + "' ");

            }

            //-----------------------------------------------------------------------------------------------------
            //Write Results
            StringBuilder insertQuery = new StringBuilder();
            String purchaseQuery = "";

            String genarateShaVal = "";
            String sCurrentLine;
            br = new BufferedReader(new FileReader(fileName));

            int index = 1;

            preparePurSelect = connection.prepareStatement("UPDATE `DLB_SWT_ST_PURCHASE_HISTORY`"
                    + " SET `WINNING_PRIZE` = '0' WHERE DLB_SWT_ST_PURCHASE_HISTORY.`DRAW_NO` = ? AND DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE=?");

            preparePurSelect.setString(1, winingFile.getDrawNo());
            preparePurSelect.setString(2, winingFile.getDlbWbProduct().getProductCode());

            preparePurSelect.executeUpdate();
            preparePurSelect = null;

            Integer tickertStatus = 0;

            while ((sCurrentLine = br.readLine()) != null) {//loop though txt file lines

                //read text file coulumn by coulumn
//                 <Index No>,<Serial No>,<Combinations Numbers Comma Separated>
                String[] arr = sCurrentLine.split(" ");
                try {

                    String drawDateStr = arr[0];
                    String drawNoNbookNo = arr[1];
                    String lineDrawNo = drawNoNbookNo.substring(0, 4);
                    String bookNo = drawNoNbookNo.substring(4, drawNoNbookNo.length());

                    String tickertNo = arr[2];
                    String prizeCode = arr[3];
                    String[] arrDisAgent = arr[4].split("/");

                    int checkDigit = calculateTotal(drawNoNbookNo + tickertNo);
                    String serialNo = lineDrawNo + bookNo + checkDigit + tickertNo;

                    String districtCode = arrDisAgent[0];
                    String agentCode = arrDisAgent[1];

//                    //get prize value-------------------ready to remove
//                    prepareStatement3 = connection.prepareStatement("SELECT "
//                            + "DLB_WB_MAIN_DRAW_PRIZE_CODE.DRAW_PRIZE  "
//                            + "FROM DLB_WB_MAIN_DRAW_PRIZE_CODE "
//                            + "WHERE DLB_WB_MAIN_DRAW_PRIZE_CODE.PRODUCT_CODE=? "
//                            + "AND DLB_WB_MAIN_DRAW_PRIZE_CODE.PRIZE_CODE=?");
//                    prepareStatement3.setString(1, winingFile.getDlbWbProduct().getProductCode());
//                    prepareStatement3.setString(2, prizeCode);
//                    ResultSet executeQuery = prepareStatement3.executeQuery();
//                    String drawPrizeCode = "";
//                    while (executeQuery.next()) {
//                        drawPrizeCode = executeQuery.getString("DRAW_PRIZE");
//
//                    }
                    //update purchase ticket status
                    //get wining tickert details from DB
                    preparePurSelect = connection.prepareStatement("SELECT DLB_WB_TICKET.LOTTERY_NUMBERS "
                            + "FROM DLB_WB_TICKET "
                            + "WHERE DLB_WB_TICKET.PRODUCT_CODE=? AND DLB_WB_TICKET.SERIAL_NUMBER=?");

                    preparePurSelect.setString(1, winingFile.getDlbWbProduct().getProductCode());
                    preparePurSelect.setString(2, serialNo);

                    ResultSet ticketRst = preparePurSelect.executeQuery();

//                     get prize value
                    prepareStatement3 = connection.prepareStatement("SELECT  "
                            + "	DLB_WB_MAIN_DRAW_PRIZE_CODE.DRAW_PRIZE AS  DRAW_PRIZE   "
                            + " FROM  "
                            + "	DLB_WB_MAIN_DRAW_PRIZE_CODE   "
                            + " WHERE  "
                            + "	DLB_WB_MAIN_DRAW_PRIZE_CODE.PRODUCT_CODE = ?    "
                            + "	AND DLB_WB_MAIN_DRAW_PRIZE_CODE.PRIZE_CODE = ? ");

                    prepareStatement3.setString(1, winingFile.getDlbWbProduct().getProductCode());
                    prepareStatement3.setString(2, prizeCode);
                    resultSet2 = prepareStatement3.executeQuery();

                    String prizeValue = "";
                    while (resultSet2.next()) {
                        prizeValue = resultSet2.getString("DRAW_PRIZE");

                    }

                    resultSet2 = null;
                    prepareStatement3 = null;

                    if (ticketRst.next()) {

                        String lotteryNumbers = ticketRst.getString("LOTTERY_NUMBERS");

//                        generate has value for S/N,Prize and tickert numbers                    
                        String plainTextComb = bookNo + tickertNo + "$" + prizeValue + "$" + lotteryNumbers;
                        genarateShaVal = encryptionService.genarateShaVal(plainTextComb);
//                        //set updated values
                        purchaseQuery = "UPDATE "
                                + "`DLB_SWT_ST_PURCHASE_HISTORY` SET `STATUS`=?,`WINNING_PRIZE`=?,`SHA_KEY`=? "
                                + "WHERE "
                                + "(`DLB_SWT_ST_PURCHASE_HISTORY`.`SERIAL_NO`=? AND "
                                + "`DLB_SWT_ST_PURCHASE_HISTORY`.`PRODUCT_CODE`=? )  ";

                        preparePurUpdate = connection.prepareStatement(purchaseQuery);

                        if (Double.parseDouble(prizeValue) >= SystemVarList.EPIC_MAX_CLAIMED_AMOUNT) {
                            tickertStatus = SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000;
                        } else {
                            tickertStatus = SystemVarList.PRIZE_LESS_THAN_100000;
                        }

                        //generate has value for S/N,Prize and tickert numbers                    
                        preparePurUpdate.setInt(1, tickertStatus);
                        preparePurUpdate.setDouble(2, Double.parseDouble(prizeValue));
                        preparePurUpdate.setString(3, genarateShaVal);
                        preparePurUpdate.setString(4, serialNo);
                        preparePurUpdate.setString(5, winingFile.getDlbWbProduct().getProductCode());

                        preparePurUpdate.executeUpdate();
                        preparePurSelect = null;
                        preparePurUpdate = null;

                        tickertStatus = 0;

                    } else {
                        saveErrorLog(winingFile.getId(), sCurrentLine, "Warning: Wining SN is not matched with any purchased ticket SN SN:" + serialNo, winingFile.getLastUpdatedUser(), newSession);
                    }

                    //---------------------------------
                    //separate records to 1000 packets
                    if (sectionIndex < 1000) {
                        if (sectionIndex == 1) {

                            insertQuery.append("INSERT INTO " + tableName + " ("
                                    + " `PRODUCT_CODE`, `SERIAL_NO`, `DRAW_DATE`, `BOOK_NO`, `TICKERT_NO`,"
                                    + " `PRIZE_CODE`, `DISTRICT_CODE`, `AGENT_NO`,`WINING_PRIZE`, `WINING_ID`,`SHA_KEY`) "
                                    + " VALUES  ( "
                                    + "'" + winingFile.getDlbWbProduct().getProductCode() + "', "
                                    + "'" + serialNo + "', "
                                    + "'" + winingFile.getDate() + "',"
                                    + "'" + bookNo + "',"
                                    + "'" + tickertNo + "',"
                                    + "'" + prizeCode + "',"
                                    + "'" + districtCode + "',"
                                    + "'" + agentCode + "',"
                                    + "'" + prizeValue + "',"
                                    + "'" + winingFile.getId() + "',"
                                    + "'" + genarateShaVal + "')");
                            //1st Line

                        } else {
                            //for normal row                            
                            insertQuery.append(",( "
                                    + "'" + winingFile.getDlbWbProduct().getProductCode() + "', "
                                    + "'" + serialNo + "', "
                                    + "'" + winingFile.getDate() + "',"
                                    + "'" + bookNo + "',"
                                    + "'" + tickertNo + "',"
                                    + "'" + prizeCode + "',"
                                    + "'" + districtCode + "',"
                                    + "'" + agentCode + "',"
                                    + "'" + prizeValue + "',"
                                    + "'" + winingFile.getId() + "',"
                                    + "'" + genarateShaVal + "')");
                        }

                        sectionIndex++;
                    } else {
                        //for last row
                        insertQuery.append(",( "
                                + "'" + winingFile.getDlbWbProduct().getProductCode() + "', "
                                + "'" + serialNo + "', "
                                + "'" + winingFile.getDate() + "',"
                                + "'" + bookNo + "',"
                                + "'" + tickertNo + "',"
                                + "'" + prizeCode + "',"
                                + "'" + districtCode + "',"
                                + "'" + agentCode + "',"
                                + "'" + prizeValue + "',"
                                + "'" + winingFile.getId() + "',"
                                + "'" + genarateShaVal + "');");

                        sectionIndex = 1;
//
//                        //excute query
                        prepareStatement2 = connection.prepareStatement(insertQuery.toString());
                        prepareStatement2.executeUpdate();
                        prepareStatement2 = null;
                        System.out.println(insertQuery.toString());
                        insertQuery = new StringBuilder("");

                    }

                    //Last records after 1000 
                    if (!insertQuery.toString().isEmpty() && totalLines == index) {
                        //excute other
                        //excute query
                        prepareStatement2 = connection.prepareStatement(insertQuery.toString());
                        prepareStatement2.executeUpdate();
                        prepareStatement2 = null;
                        insertQuery = new StringBuilder("");
                    }

                    index++;

                    //calculate progress
                    Double cmpltdCountDbl = new Double(index * 100 / totalLines);
                    cmpltdProgress = cmpltdCountDbl.intValue();

                    //set task progress info to session              
                    progressobject.put(SystemVarList.TASK_PROGRESS, new Integer(cmpltdProgress + ""));
                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.PENDING);

                    session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());

                } catch (NumberFormatException e) {
                    //save to error log  
                    saveErrorLog(winingFile.getId(), sCurrentLine, e.getMessage(), winingFile.getLastUpdatedUser(), newSession);

                } catch (ConstraintViolationException e) {
                    //handel hibernate exception
                    int errorCode = e.getErrorCode();
                    if (errorCode == 1062) {//Duplicate entry 
                        saveErrorLog(winingFile.getId(), sCurrentLine, "Result is already uploaded", winingFile.getLastUpdatedUser(), newSession);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //-----------------------------------------------------------------------------------------------------
            //fluesh session
            if (cmpltdProgress >= 100) {
                winingFile.setDlbStatus(new DlbStatus(SystemVarList.APPROVED, null, null));
                winingFile.setRecordCount(totalLines);
                winingFile.setResultTableName(tableName);
                newSession.update(winingFile);
                newSession.flush();

                //set task progress info to session            
                progressobject.put(SystemVarList.TASK_PROGRESS, 100);
                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
                session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
            }

        } catch (IOException | NumberFormatException e) {
            progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.ERROR);
            session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
            Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, e);
            if (newSession != null) {
                //close session
                newSession.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {

                if (br != null) {
                    br.close();
                }
                if (newSession != null) {
                    //close session
                    newSession.close();
                }
            } catch (IOException ex) {
                throw ex;
            }
        }

    }
//    public void saveResultData(HttpSession session, String fileName, DlbWbWiningFile winingFile) throws IOException, java.text.ParseException {
//
//        //build result table data from .txt file
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        BufferedReader br = null;
//        JSONObject progressobject = new JSONObject();
//        newSession = sessionFactory.openSession();
//
//        try {
//
//            br = new BufferedReader(new FileReader(fileName));
//
//            //get total lines of the text file
//            int totalLines = 0;
//            int cmpltdProgress = 0;
//            while (br.readLine() != null) {
//                totalLines++;
//            }
//
//            br = null;
//
//            //create table name
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            String tableName = "DLB_WB_RESULT_" + sdf.format(winingFile.getDate());
//
//            //check table is exsits  
//            SQLQuery createSQLQuery1 = newSession.createSQLQuery("SELECT *   "
//                    + "FROM information_schema.tables  "
//                    + "WHERE table_name = '" + tableName + "'  "
//                    + "LIMIT 1;");
//
//            List list = createSQLQuery1.list();
//            if (list.isEmpty()) {
//                //table is not exsits
//                //create result table
//                SQLQuery createSQLQuery2 = newSession.createSQLQuery(""
//                        + "CREATE TABLE " + tableName + " (  "
//                        + "  `PRODUCT_CODE` varchar(16) NOT NULL,  "
//                        + "  `SERIAL_NO` varchar(64) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `DRAW_DATE` date NOT NULL,  "
//                        + "  `BOOK_NO` varchar(64) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `TICKERT_NO` varchar(32) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `PRIZE_CODE` varchar(16) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `DISTRICT_CODE` varchar(16) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `AGENT_NO` varchar(16) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `WINING_PRIZE` varchar(64) CHARACTER SET utf8 NOT NULL,  "
//                        + "  `WINING_ID` int(11) NOT NULL,  "
//                        + "  `LAST_UPDATED_USER` varchar(128) DEFAULT NULL,  "
//                        + "  `LAST_UPDATED_TIME` datetime DEFAULT NULL,  "
//                        + "  `CREATED_TIME` datetime DEFAULT NULL,  "
//                        + "  PRIMARY KEY (`PRODUCT_CODE`,`SERIAL_NO`)  "
//                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
//                createSQLQuery2.executeUpdate();
//            }
//
//            //-----------------------------------------------------------------------------------------------------
//            //Write Results
//            String insertQuery = "INSERT INTO " + tableName + " ("
//                    + "`PRODUCT_CODE`,"
//                    + "`SERIAL_NO`,"
//                    + "`DRAW_DATE`,"
//                    + "`BOOK_NO`,"
//                    + "`TICKERT_NO`,"
//                    + "`PRIZE_CODE`,"
//                    + "`DISTRICT_CODE`,"
//                    + "`AGENT_NO`,"
//                    + "`WINING_PRIZE`,"
//                    + "`WINING_ID`) VALUES (?,?,?,?,?,?,?,?,?,?)";
//
//            String sCurrentLine;
//            br = new BufferedReader(new FileReader(fileName));
//
//            int i = 0;
//            while ((sCurrentLine = br.readLine()) != null) {//loop though txt file lines
//
//                //read text file coulumn by coulumn
////                 <Index No>,<Serial No>,<Combinations Numbers Comma Separated>
//                String[] arr = sCurrentLine.split(" ");
//                try {
//
//                    String drawDateStr = arr[0];
//                    String drawNoNbookNo = arr[1];
//                    String lineDrawNo=drawNoNbookNo.substring(0, 4);
//                    String bookNo=drawNoNbookNo.substring(4, drawNoNbookNo.length());
//                 
//                    String tickertNo = arr[2];
//                    String prizeCode = arr[3];
//                    String[] arrDisAgent = arr[4].split("/");
//                    
//                    int calculateTotal = calculateTotal(bookNo+tickertNo);
//                    int checkDigit=calculateTotal%10;
//                    String serialNo=lineDrawNo+bookNo+checkDigit+tickertNo;
//
//                    String districtCode = arrDisAgent[0];
//                    String agentCode = arrDisAgent[1];
//
//                    //get prize value
//                    DlbWbMainDrawPrizeCodeId drawPrizeCodeId = new DlbWbMainDrawPrizeCodeId(winingFile.getDlbWbProduct().getProductCode(), prizeCode);
//                    DlbWbMainDrawPrizeCode drawPrizeCode = (DlbWbMainDrawPrizeCode) newSession.get(DlbWbMainDrawPrizeCode.class, drawPrizeCodeId);
//                    //---------------------------------
//
//                    SQLQuery createSQLQuery3 = newSession.createSQLQuery(insertQuery);
//
//                    createSQLQuery3.setParameter(0, winingFile.getDlbWbProduct().getProductCode());
//                    createSQLQuery3.setParameter(1, serialNo);
//                    createSQLQuery3.setParameter(2, dateFormat.parse(drawDateStr));
//                    createSQLQuery3.setParameter(3, bookNo);
//                    createSQLQuery3.setParameter(4, tickertNo);
//                    createSQLQuery3.setParameter(5, prizeCode);
//                    createSQLQuery3.setParameter(6, districtCode);
//                    createSQLQuery3.setParameter(7, agentCode);
//                    createSQLQuery3.setParameter(8, drawPrizeCode.getDrawPrize());
//                    createSQLQuery3.setParameter(9, winingFile.getId());
//
//                    createSQLQuery3.executeUpdate();
//
//                    //update purchase ticket status
//                    //get wining tickert details from DB
//                    Criteria createCriteria1 = newSession.createCriteria(DlbWbTicket.class);
//                    createCriteria1.add(Restrictions.eq("serialNumber", bookNo + tickertNo));
//                    createCriteria1.add(Restrictions.eq("productCode", winingFile.getDlbWbProduct().getProductCode()));
//                    List tickertList = createCriteria1.list();
//
//                    if (tickertList.size() > 0) {
//                        //wining  for selected S/N
//                        DlbWbTicket winingTickert = (DlbWbTicket) tickertList.get(0);
//                        String tickertStatus = Integer.parseInt(drawPrizeCode.getDrawPrize()) < 100000 ? "20" : "21";
//
//                        //generate has value for S/N,Prize and tickert numbers                    
//                        String plainTextComb = bookNo + tickertNo + "$" + drawPrizeCode.getDrawPrize() + "$" + winingTickert.getLotteryNumbers();
//                        String genarateShaVal = encryptionService.genarateShaVal(plainTextComb);
//
//                        //set updated values
//                        String purchaseQuery = "UPDATE `DLB_SWT_ST_PURCHASE_HISTORY` SET `STATUS`=?,`WINNING_PRIZE`=?,`SHA_KEY`=? "
//                                + "WHERE "
//                                + "(`DLB_SWT_ST_PURCHASE_HISTORY`.`SERIAL_NO`=? AND "
//                                + "`DLB_SWT_ST_PURCHASE_HISTORY`.`PRODUCT_CODE`=? )  ";
//
//                        SQLQuery createSQLQuery4 = newSession.createSQLQuery(purchaseQuery);
//
//                        //generate has value for S/N,Prize and tickert numbers                    
//                        createSQLQuery4.setParameter(0, tickertStatus);
//                        createSQLQuery4.setParameter(1, drawPrizeCode.getDrawPrize());
//                        createSQLQuery4.setParameter(2, genarateShaVal);
//                        createSQLQuery4.setParameter(3, serialNo);
//                        createSQLQuery4.setParameter(4, winingFile.getDlbWbProduct().getProductCode());
//
//                        createSQLQuery4.executeUpdate();
//
//                    }
//
//                    i++;
//
//                    //calculate progress
//                    Double cmpltdCountDbl = new Double(i * 100 / totalLines);
//                    cmpltdProgress = cmpltdCountDbl.intValue();
//
//                    //set task progress info to session              
//                    progressobject.put(SystemVarList.TASK_PROGRESS, new Integer(cmpltdProgress + ""));
//                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.PENDING);
//
//                    session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
//
//                } catch (NumberFormatException | ParseException e) {
//                    //save to error log  
//                    saveErrorLog(winingFile.getId(), sCurrentLine, e.getMessage(), winingFile.getLastUpdatedUser(), newSession);
//
//                } catch (ConstraintViolationException e) {
//                    //handel hibernate exception
//                    int errorCode = e.getErrorCode();
//                    if (errorCode == 1062) {//Duplicate entry 
//                        saveErrorLog(winingFile.getId(), sCurrentLine, "Result is already uploaded", winingFile.getLastUpdatedUser(), newSession);
//                    }
//
//                } catch (NoSuchAlgorithmException ex) {
//                    Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (UnsupportedEncodingException ex) {
//                    Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//
//            //-----------------------------------------------------------------------------------------------------
//            //fluesh session
//            if (cmpltdProgress == 100) {
//                winingFile.setDlbStatus(new DlbStatus(SystemVarList.APPROVED, null, null));
//                winingFile.setRecordCount(totalLines);
//                winingFile.setResultTableName(tableName);
//                newSession.update(winingFile);
//                newSession.flush();
//
//                //set task progress info to session            
//                progressobject.put(SystemVarList.TASK_PROGRESS, 100);
//                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
//                session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
//            }
//
//        } catch (IOException | NumberFormatException e) {
//            progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.ERROR);
//            session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
//            Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, e);
//            if (newSession != null) {
//                //close session
//                newSession.close();
//            }
//        } finally {
//            try {
//
//                if (br != null) {
//                    br.close();
//                }
//                if (newSession != null) {
//                    //close session
//                    newSession.close();
//                }
//            } catch (IOException ex) {
//                throw ex;
//            }
//        }
//
//    }

    public void updateStatus(DlbWbWiningFile winingFile, int status) throws Exception {

        //update status
        winingFile.setDlbStatus(new DlbStatus(status, null, null));
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            currentSession.update(winingFile);
        } catch (HibernateException e) {
            throw e;
        }

    }

    //save error log
    public void saveErrorLog(int id, String data, String reason, String user, Session session) {

        try {
            DlbWbWiningFileErrorDetails errorDetails = new DlbWbWiningFileErrorDetails();
            DlbWbWiningFile dlbWbWiningFile = new DlbWbWiningFile();
            dlbWbWiningFile.setId(id);
            errorDetails.setDlbWbWiningFile(dlbWbWiningFile);
            errorDetails.setData(data);
            errorDetails.setReason(reason);
            errorDetails.setLastUpdatedTime(new Date());
            errorDetails.setLastUpdatedUser(user);

            session.save(errorDetails);

        } catch (Exception e) {
            throw e;
        } finally {
            session.flush();
        }

    }

    public int calculateTotal(String numbers) {
        int total = 0;
        for (int i = 0; i < numbers.length(); i++) {
            char charAt = numbers.charAt(i);
            total += Character.getNumericValue(charAt);
        }

        total = total % 10;

        return total;
    }

    public void saveResultDataNew(HttpSession session, String fileName, DlbWbWiningFile winingFile, Connection connection) throws IOException, java.text.ParseException, NoSuchAlgorithmException, SQLException {

        //build result table data from .txt file
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        BufferedReader br = null;
        PreparedStatement prepareStatement = null;
        String specialCode = "";
        Integer isSpecial = 0;

        JSONObject progressobject = new JSONObject();

        ResultSet resultSet = null;
        connection.setAutoCommit(false);

        newSession = sessionFactory.openSession();

        try {

            br = new BufferedReader(new FileReader(fileName));

            //get total lines of the text file
            Long totaLinesLng = br.lines().count();
            int totalLines = totaLinesLng.intValue();
            int cmpltdProgress = 0;

            br = null;
            br = new BufferedReader(new FileReader(fileName));

            //create table name
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String tableName = "DLB_WB_RESULT_" + sdf.format(winingFile.getDate());

            //check table is exsits  
            prepareStatement = connection.prepareStatement("SELECT *   "
                    + "FROM information_schema.tables  "
                    + "WHERE table_name = '" + tableName + "'  "
                    + "LIMIT 1;");

            resultSet = prepareStatement.executeQuery();

            if (!resultSet.next()) {
                //table is not exsits
                //create result table

                prepareStatement = connection.prepareStatement(""
                        + "CREATE TABLE " + tableName + " (  "
                        + "  `PRODUCT_CODE` varchar(16) NOT NULL,  "
                        + "  `SERIAL_NO` varchar(64) CHARACTER SET utf8 NOT NULL,  "
                        + "  `DRAW_DATE` date NOT NULL,  "
                        + "  `BOOK_NO` varchar(64) CHARACTER SET utf8 NOT NULL,  "
                        + "  `TICKERT_NO` varchar(32) CHARACTER SET utf8 NOT NULL,  "
                        + "  `PRIZE_CODE` varchar(16) CHARACTER SET utf8 NOT NULL,  "
                        + "  `DISTRICT_CODE` varchar(16) CHARACTER SET utf8 NOT NULL,  "
                        + "  `AGENT_NO` varchar(16) CHARACTER SET utf8 NOT NULL,  "
                        + "  `WINING_PRIZE` varchar(64) CHARACTER SET utf8 NOT NULL,  "
                        + "  `WINING_ID` int(11) NOT NULL,  "
                        + "  `LAST_UPDATED_USER` varchar(128) DEFAULT NULL,  "
                        + "  `CREATED_TIME` datetime DEFAULT NULL, "
                        + " `SHA_KEY` varchar(255) NOT NULL,  "
                        + " `SP_GAME` varchar(50) DEFAULT NULL,  "
                        + " `IS_SPECIAL` tinyint(1) DEFAULT NULL,  "
                        + "  PRIMARY KEY (`PRODUCT_CODE`,`SERIAL_NO`,`PRIZE_CODE`)  "
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

                prepareStatement.execute();

            }

            //-----------------------------------------------------------------------------------------------------
            //Write Results
            String genarateShaVal = "";
            String sCurrentLine;
            br = new BufferedReader(new FileReader(fileName));

            int index = 1;

            prepareStatement = connection.prepareStatement("UPDATE `DLB_SWT_ST_PURCHASE_HISTORY`"
                    + " SET `WINNING_PRIZE` = '0',`NOTIFY_STATUS` = 0 "
                    + "WHERE DLB_SWT_ST_PURCHASE_HISTORY.`DRAW_NO` = ? AND DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE=?");

            prepareStatement.setString(1, winingFile.getDrawNo());
            prepareStatement.setString(2, winingFile.getDlbWbProduct().getProductCode());

            prepareStatement.executeUpdate();

            Integer tickertStatus = 0;

            while ((sCurrentLine = br.readLine()) != null) {//loop though txt file lines

                //read text file coulumn by coulumn
//                 <Index No>,<Serial No>,<Combinations Numbers Comma Separated>
                String[] arr = sCurrentLine.split(" ");
                try {

                    String drawDateStr = arr[0];
                    String drawNoNbookNo = arr[1];
                    String lineDrawNo = drawNoNbookNo.substring(0, 4);
                    String bookNo = drawNoNbookNo.substring(4, drawNoNbookNo.length());

                    String tickertNo = arr[2];
                    String prizeCode = arr[3];
                    String[] arrDisAgent = arr[4].split("/");

                    int checkDigit = calculateTotal(drawNoNbookNo + tickertNo);
                    String serialNo = lineDrawNo + bookNo + checkDigit + tickertNo;

                    String districtCode = arrDisAgent[0];
                    String agentCode = arrDisAgent[1];

                    specialCode = "";

                    if (arr.length > 5) {//for special draw
                        specialCode = arr[5];
                        isSpecial = 1;
                    } else {
                        specialCode="";
                        isSpecial = 0;
                    }

                    //get prize value
                    prepareStatement = connection.prepareStatement("SELECT "
                            + "DLB_WB_MAIN_DRAW_PRIZE_CODE.DRAW_PRIZE  "
                            + "FROM DLB_WB_MAIN_DRAW_PRIZE_CODE "
                            + "WHERE DLB_WB_MAIN_DRAW_PRIZE_CODE.PRODUCT_CODE=? "
                            + "AND DLB_WB_MAIN_DRAW_PRIZE_CODE.PRIZE_CODE=?");
                    prepareStatement.setString(1, winingFile.getDlbWbProduct().getProductCode());
                    prepareStatement.setString(2, prizeCode);
                    resultSet = prepareStatement.executeQuery();

                    String prizeValue = "";
                    while (resultSet.next()) {
                        prizeValue = resultSet.getString("DRAW_PRIZE");

                    }
                    //update purchase ticket status
                    //get wining tickert details from DB
                    prepareStatement = connection.prepareStatement("SELECT DLB_WB_TICKET.LOTTERY_NUMBERS "
                            + "FROM DLB_WB_TICKET "
                            + "WHERE DLB_WB_TICKET.PRODUCT_CODE=? AND DLB_WB_TICKET.SERIAL_NUMBER=?");

                    prepareStatement.setString(1, winingFile.getDlbWbProduct().getProductCode());
                    prepareStatement.setString(2, serialNo);

                    resultSet = prepareStatement.executeQuery();

                    if (resultSet.next()) {

                        String lotteryNumbers = resultSet.getString("LOTTERY_NUMBERS");

//                        generate has value for S/N,Prize and tickert numbers                    
                        String plainTextComb = bookNo + tickertNo + "$" + prizeValue + "$" + lotteryNumbers;
                        genarateShaVal = encryptionService.genarateShaVal(plainTextComb);
//                        //set updated values
                        prepareStatement = connection.prepareCall("UPDATE "
                                + "`DLB_SWT_ST_PURCHASE_HISTORY` SET `STATUS`=?,`WINNING_PRIZE`=`WINNING_PRIZE`+?,`SHA_KEY`=? "
                                + "WHERE "
                                + "(`DLB_SWT_ST_PURCHASE_HISTORY`.`SERIAL_NO`=? AND "
                                + "`DLB_SWT_ST_PURCHASE_HISTORY`.`PRODUCT_CODE`=? )  ");
                        
                       

                        if (Double.parseDouble(prizeValue) > SystemVarList.EPIC_MAX_CLAIMED_AMOUNT) {
                            tickertStatus = SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000;
                        } else {
                            tickertStatus = SystemVarList.PRIZE_LESS_THAN_100000;
                        }

                        //generate has value for S/N,Prize and tickert numbers                    
                        prepareStatement.setInt(1, tickertStatus);
                        prepareStatement.setString(2, prizeValue);
                        prepareStatement.setString(3, genarateShaVal);
                        prepareStatement.setString(4, serialNo);
                        prepareStatement.setString(5, winingFile.getDlbWbProduct().getProductCode());
                        
                        System.out.println("purchase history updated"+ winingFile.getProductDescription()
                                   +"-"+winingFile.getDrawNo()+":"+serialNo+", Amount added:"+prizeValue);

                        prepareStatement.executeUpdate();
                        tickertStatus = 0;

                        prepareStatement.executeUpdate("INSERT INTO " + tableName + " ("
                                + " `PRODUCT_CODE`, `SERIAL_NO`, `DRAW_DATE`, `BOOK_NO`, `TICKERT_NO`,"
                                + " `PRIZE_CODE`, `DISTRICT_CODE`, `AGENT_NO`,`WINING_PRIZE`, `WINING_ID`,"
                                + "`LAST_UPDATED_USER`,`CREATED_TIME`,`SHA_KEY`,`SP_GAME`,`IS_SPECIAL`) "
                                + " VALUES  ( "
                                + "'" + winingFile.getDlbWbProduct().getProductCode() + "', "
                                + "'" + serialNo + "', "
                                + "'" + winingFile.getDate() + "',"
                                + "'" + bookNo + "',"
                                + "'" + tickertNo + "',"
                                + "'" + prizeCode + "',"
                                + "'" + districtCode + "',"
                                + "'" + agentCode + "',"
                                + "'" + prizeValue + "',"
                                + "'" + winingFile.getId() + "',"
                                + "'" + winingFile.getLastUpdatedUser() + "',"
                                + "NOW(),"
                                + "'" + genarateShaVal + "',"
                                + "'" + specialCode + "',"
                                + "'" + isSpecial + "')");
                        
                        System.out.println("winning table inserted"+ winingFile.getProductDescription()+"-"+winingFile.getDrawNo()+":"+serialNo);

                    } else {
                        saveErrorLog(winingFile.getId(), sCurrentLine, "Warning: Wining SN is not matched with any purchased ticket SN SN:" + serialNo, winingFile.getLastUpdatedUser(), newSession);
                    }

                    index++;

                    //calculate progress
                    Double cmpltdCountDbl = new Double(index * 100 / totalLines);
                    cmpltdProgress = cmpltdCountDbl.intValue();

                    //set task progress info to session              
                    progressobject.put(SystemVarList.TASK_PROGRESS, new Integer(cmpltdProgress + ""));
                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.PENDING);

                    session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());

                } catch (NumberFormatException e) {
                    //save to error log  
                    saveErrorLog(winingFile.getId(), sCurrentLine, e.getMessage(), winingFile.getLastUpdatedUser(), newSession);
                      System.out.println("Error occured when reading" + winingFile+", "+sCurrentLine);

                } catch (ConstraintViolationException e) {
                    //handel hibernate exception
                    int errorCode = e.getErrorCode();
                    if (errorCode == 1062) {//Duplicate entry 
                        saveErrorLog(winingFile.getId(), sCurrentLine, "Result is already uploaded", winingFile.getLastUpdatedUser(), newSession);
                        System.out.println("Result is already uploaded" + winingFile+", "+sCurrentLine);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //-----------------------------------------------------------------------------------------------------
            //fluesh session
            if (cmpltdProgress >= 100) {
                winingFile.setDlbStatus(new DlbStatus(SystemVarList.APPROVED, null, null));
                winingFile.setRecordCount(totalLines);
                winingFile.setResultTableName(tableName);
                newSession.update(winingFile);
                newSession.flush();
                
                //commit queries
                connection.commit();
                
                System.out.println("Winner File approved - file ID"+winingFile.getId()
                        +" lottery: "+winingFile.getProductDescription()+"-"+winingFile.getDrawNo());

                //set task progress info to session            
                progressobject.put(SystemVarList.TASK_PROGRESS, 100);
                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
                session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
            }

        } catch (IOException | NumberFormatException e) {
            progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.ERROR);
            session.setAttribute(SystemVarList.TASK_INFO + "_" + winingFile.getId(), progressobject.toJSONString());
            Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, e);
            if (newSession != null) {
                //close session
                newSession.close();
            }
            connection.rollback();
            
             System.out.println("Error occured when file reading...");
        } catch (SQLException ex) {
             connection.rollback();
            Logger.getLogger(ResultRepository.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error occured when SQL reading...");
        } finally {
            try {

                if (br != null) {
                    br.close();
                }
                if (newSession != null) {
                    //close session
                    newSession.close();
                }
            } catch (IOException ex) {
                throw ex;
            }
        }

    }

}
