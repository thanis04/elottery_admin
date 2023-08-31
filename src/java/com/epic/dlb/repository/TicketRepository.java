/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbTicketFile;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.service.EncryptionService;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.viewmodel.SalesFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Repository("ticketRepository")
public class TicketRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EncryptionService encryptionService;

    private SimpleDateFormat simpleDateFormat;

    @Transactional(rollbackFor = Exception.class)
    public void saveTicketData(HttpSession session, DlbWbTicketFile ticketFile, Connection connection, Integer specialPos) throws Exception {
        //build result table data from .txt file

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        BufferedReader br = null;
        JSONObject progressobject = new JSONObject();
        PreparedStatement prepareStatement1 = null;
        PreparedStatement prepareStatement2 = null;
        br = new BufferedReader(new FileReader(ticketFile.getFilePath()));
        String msg = "";

        //get total lines of the text file
        Long totaLinesLng = br.lines().count();
        int totalLines = totaLinesLng.intValue();
        int cmpltdProgress = 0;
        int errorCounts = 0;

        int specialNoCount = 0;

        br = null;

        String sCurrentLine;
        br = new BufferedReader(new FileReader(ticketFile.getFilePath()));

        int index = 1;
        int sectionIndex = 1;
        String insertQuery = "";

        HashMap<String, String> specialItems = new HashMap<>();

        //check is exsiting
        prepareStatement1 = connection.prepareStatement("SELECT "
                + "DLB_WB_TICKET.ID "
                + "FROM "
                + "DLB_WB_TICKET "
                + "WHERE "
                + "DLB_WB_TICKET.PRODUCT_CODE=? AND "
                + "DLB_WB_TICKET.DRAW_NO=?");

        prepareStatement1.setString(1, ticketFile.getDlbWbProduct().getProductCode());
        prepareStatement1.setString(2, ticketFile.getDrawNo());

        ResultSet rs1 = prepareStatement1.executeQuery();
        prepareStatement1 = null;

        if (rs1.next()) {
            //exsting
            //stop proccess
            msg = "Draw no is already exists on the system. Please check it and try again";
            errorCounts = totalLines;

        } else {
            //not exsting
            //do proccess
            while ((sCurrentLine = br.readLine()) != null) { //loop though txt file lines

                try {
                    //get indexes of row
                    int idIndex = sCurrentLine.indexOf(",", 0);
                    int snIndex = sCurrentLine.indexOf(",", idIndex + 1);

                    //read text file coulumn by coulumn
                    String serialNo = sCurrentLine.substring(idIndex + 1, snIndex);
                    String items = sCurrentLine.substring(snIndex + 1);

                    if (ticketFile.getDlbStatusByIsSpecial() != null
                            && ticketFile.getDlbStatusByIsSpecial().getStatusCode() == Integer.parseInt(SystemVarList.YES)) { //check is special draw

                        //special draw
                        String[] spitedItems = items.split(",");
                        String normalItems = "";

                        int itemPos = 1;
                        for (String item : spitedItems) {

                            if (itemPos < specialPos) {
                                if (itemPos == specialPos - 1) {
                                    normalItems += item;
                                } else {
                                    normalItems += item + ",";
                                }

                            } else {
                                specialNoCount = (spitedItems.length + 1) - itemPos;

                                for (int spI = 0; spI < specialNoCount; spI++) {
                                    specialItems.put(serialNo + "_" + spI, item);
                                }

                            }

                            itemPos++;

                        }

                        items = normalItems;

                    }

                    DlbWbTicket ticket = new DlbWbTicket();
                    ticket.setSerialNumber(serialNo);
                    ticket.setDrawNo(ticketFile.getDrawNo());
                    ticket.setProductCode(ticketFile.getDlbWbProduct().getProductCode());
                    ticket.setLotteryNumbers(items);
                    ticket.setDrawDate(ticketFile.getDate());
                    ticket.setProductCode(ticketFile.getDlbWbProduct().getProductCode());
                    ticket.setStatus(SystemVarList.READY_TO_CHECKOUT);
                    ticket.setDlbWbTicketFile(ticketFile);
                    ticket.setCreatedDate(new Date());

                    //generate has value for S/N and tickert numbers                    
                    String plainTextComb = serialNo + "$" + ticket.getLotteryNumbers().trim();
                    String genarateShaVal = encryptionService.genarateShaVal(plainTextComb);
                    ticket.setShaKey(genarateShaVal);

                    if (sectionIndex < 1000) {
                        if (sectionIndex == 1) {
                            //1st Line
                            insertQuery = "INSERT INTO "
                                    + "`DLB_WB_TICKET`(`ID`, `SERIAL_NUMBER`, `PRODUCT_CODE`, `STATUS`, `LOTTERY_NUMBERS`,"
                                    + " `DRAW_DATE`, `LAST_UPDATED`, `CREATED_DATE`,`DRAW_NO`, `SHA_KEY`, `TICKET_FILE_ID`) "
                                    + "VALUES  (null, "
                                    + "'" + ticket.getSerialNumber() + "', "
                                    + "'" + ticket.getProductCode() + "', "
                                    + "" + ticket.getStatus() + ","
                                    + "'" + ticket.getLotteryNumbers() + "',"
                                    + "'" + simpleDateFormat.format(ticket.getDrawDate()) + "',"
                                    + "NOW(),NOW(),"
                                    + "'" + ticket.getDrawNo() + "',"
                                    + "'" + ticket.getShaKey() + "',"
                                    + "" + ticketFile.getId() + ")";

                        } else {
                            //for normal row
                            insertQuery = insertQuery + ",(null, "
                                    + "'" + ticket.getSerialNumber() + "', "
                                    + "'" + ticket.getProductCode() + "', "
                                    + "" + ticket.getStatus() + ","
                                    + "'" + ticket.getLotteryNumbers() + "',"
                                    + "'" + simpleDateFormat.format(ticket.getDrawDate()) + "',"
                                    + "NOW(),NOW(),"
                                    + "'" + ticket.getDrawNo() + "',"
                                    + "'" + ticket.getShaKey() + "',"
                                    + "" + ticketFile.getId() + ")";
                        }

                        sectionIndex++;

                    } else {
                        //for last row
                        insertQuery = insertQuery + ",(null, "
                                + "'" + ticket.getSerialNumber() + "', "
                                + "'" + ticket.getProductCode() + "', "
                                + "" + ticket.getStatus() + ","
                                + "'" + ticket.getLotteryNumbers() + "',"
                                + "'" + simpleDateFormat.format(ticket.getDrawDate()) + "',"
                                + "NOW(),NOW(),"
                                + "'" + ticket.getDrawNo() + "',"
                                + "'" + ticket.getShaKey() + "',"
                                + "" + ticketFile.getId() + ");";
                        sectionIndex = 1;

                        //excute query
                        prepareStatement2 = connection.prepareStatement(insertQuery);
                        prepareStatement2.executeUpdate();
                        prepareStatement2 = null;
                        insertQuery = "";

                    }

                    //Last records after 1000       1000    = 999
                    if (!insertQuery.isEmpty() && totalLines == index) {
                        //excute other
                        //excute query
                        prepareStatement2 = connection.prepareStatement(insertQuery);
                        prepareStatement2.executeUpdate();
                        prepareStatement2 = null;
                        insertQuery = "";

                    }

                    index++;

                    //calculate progress                  
                    BigDecimal bd = new BigDecimal((index - 1) * 100 / totalLines);
                    BigDecimal bdRounded = bd.setScale(0, RoundingMode.CEILING);
                    cmpltdProgress = bdRounded.intValue();

                    //set task progress info to session
                    progressobject.put(SystemVarList.TASK_PROGRESS, new Integer(cmpltdProgress + ""));
                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.PENDING);

                    session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());

                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    //save to error log  
                    errorCounts++;
                    saveErrorLog(ticketFile.getId(), sCurrentLine, e.getMessage(), ticketFile.getLastUpdatedUser(), connection);

                }

            }
        }

        //-----------------------------------------------------------------------------------------------------
        //flush session       
        if (cmpltdProgress == 100) {

            if (!specialItems.isEmpty()) {//specil numbers exits

                //insert special numbers
                int spNumbersInertCount = 0;
                for (Map.Entry<String, String> entry : specialItems.entrySet()) {

                    int lastIndexOf = entry.getKey().lastIndexOf("_");
                    String key = entry.getKey().substring(0, lastIndexOf);
                    String value = entry.getValue();

                    insertQuery = "INSERT INTO "
                            + "`DLB_WB_TICKET_SP_NUMBERS`(`ID`, `TICKET_ID`, `SP_NUMBERS`) "
                            + "VALUES (null, "
                            + "(SELECT DLB_WB_TICKET.ID FROM DLB_WB_TICKET WHERE DLB_WB_TICKET.SERIAL_NUMBER ='" + key + "' "
                            + "AND DLB_WB_TICKET.TICKET_FILE_ID ='" + ticketFile.getId() + "'), '" + value + "');";

                    prepareStatement2 = connection.prepareStatement(insertQuery);
                    prepareStatement2.executeUpdate();

                    spNumbersInertCount++;

                    prepareStatement2 = null;

                    System.out.println(spNumbersInertCount);

                }

                if (specialItems.size() == spNumbersInertCount) {

                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
                    updateStatus(ticketFile.getId(), SystemVarList.APPROVED, totalLines, connection);

                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);

                    progressobject.put(SystemVarList.TASK_PROGRESS, 100);
                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
                    session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());

                    connection.commit();

                }

            } else {
                if (errorCounts == totalLines) {//totaly error
                    progressobject.put(SystemVarList.STATUS, SystemVarList.ERROR);
                    progressobject.put(SystemVarList.MESSAGE, msg);
                    updateStatus(ticketFile.getId(), SystemVarList.SUBMITED, totalLines, connection);

                } else {//success

                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
                    updateStatus(ticketFile.getId(), SystemVarList.APPROVED, totalLines, connection);

                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);

                }

                progressobject.put(SystemVarList.TASK_PROGRESS, 100);
                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
                session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());

                connection.commit();

            }

        }

    }
//    @Transactional(rollbackFor = Exception.class)
//    public void saveTicketData(HttpSession session, DlbWbTicketFile ticketFile, Connection connection) throws Exception {
//        //build result table data from .txt file
//
//            simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            BufferedReader br = null;
//            JSONObject progressobject = new JSONObject();
//            PreparedStatement prepareStatement = null;
//            br = new BufferedReader(new FileReader(ticketFile.getFilePath()));
//
//            //get total lines of the text file
//            int totalLines = 0;
//            int cmpltdProgress = 0;
//            int errorCounts = 0;
//            while (br.readLine() != null) {
//                totalLines++;
//            }
//
//            br = null;
//
//            String sCurrentLine;
//            br = new BufferedReader(new FileReader(ticketFile.getFilePath()));
//
//            int i = 0;
//            while ((sCurrentLine = br.readLine()) != null) { //loop though txt file lines
//
//                try {
//                    //get indexes of row
//                    int idIndex = sCurrentLine.indexOf(",", 0);
//                    int snIndex = sCurrentLine.indexOf(",", idIndex + 1);
//
//                    //read text file coulumn by coulumn
//                    String serialNo = sCurrentLine.substring(idIndex + 1, snIndex);
//                    String items = sCurrentLine.substring(snIndex + 1);
//
//                    DlbWbTicket ticket = new DlbWbTicket();
//                    ticket.setSerialNumber(serialNo);
//                    ticket.setDrawNo(ticketFile.getDrawNo());
//                    ticket.setProductCode(ticketFile.getDlbWbProduct().getProductCode());
//                    ticket.setLotteryNumbers(items);
//                    ticket.setDrawDate(ticketFile.getDate());
//                    ticket.setProductCode(ticketFile.getDlbWbProduct().getProductCode());
//                    ticket.setStatus(SystemVarList.READY_TO_CHECKOUT);
//                    ticket.setDlbWbTicketFile(ticketFile);
//                    ticket.setCreatedDate(new Date());
//
//                    //generate has value for S/N and tickert numbers                    
//                    String plainTextComb = serialNo + "$" + ticket.getLotteryNumbers().trim();
//                    String genarateShaVal = encryptionService.genarateShaVal(plainTextComb);
//                    ticket.setShaKey(genarateShaVal);
//
//                    //check is exsiting
//                    prepareStatement = connection.prepareStatement("SELECT "
//                            + "DLB_WB_TICKET.ID "
//                            + "FROM "
//                            + "DLB_WB_TICKET "
//                            + "WHERE "
//                            + "DLB_WB_TICKET.PRODUCT_CODE=? AND "
//                            + "DLB_WB_TICKET.SERIAL_NUMBER=?");
//
//                    prepareStatement.setString(1, ticketFile.getDlbWbProduct().getProductCode());
//                    prepareStatement.setString(2, serialNo);
//
//                    ResultSet rs1 = prepareStatement.executeQuery();
//                    prepareStatement = null;
//
//                    if (!rs1.next()) {
//                        //not already exsiting and save succesfully
//                        prepareStatement = connection.prepareStatement("INSERT INTO "
//                                + "`DLB_WB_TICKET`(`ID`, `SERIAL_NUMBER`, `PRODUCT_CODE`, `STATUS`, `LOTTERY_NUMBERS`,"
//                                + " `DRAW_DATE`, `LAST_UPDATED`, `CREATED_DATE`,`DRAW_NO`, `SHA_KEY`, `TICKET_FILE_ID`) "
//                                + "VALUES (null, ?, ?, ?, ?,?,NOW(),NOW(),?,?,?);");
//
//                        prepareStatement.setString(1, ticket.getSerialNumber());
//                        prepareStatement.setString(2, ticket.getProductCode());
//                        prepareStatement.setString(3, ticket.getStatus());
//                        prepareStatement.setString(4, ticket.getLotteryNumbers());
//                        prepareStatement.setString(5,simpleDateFormat.format(ticket.getDrawDate()));   
//                        prepareStatement.setString(6, ticket.getDrawNo());
//                        prepareStatement.setString(7, ticket.getShaKey());
//                        prepareStatement.setInt(8, ticketFile.getId());
//
//                        prepareStatement.executeUpdate();
//
//                    } else {
//                        //already exsiting and save to error log                    
//                        saveErrorLog(ticketFile.getId(), sCurrentLine, "Serial Number Already exsits in selected lottery. SN:" + serialNo + ", Lottery:" + ticket.getProductCode(), ticketFile.getLastUpdatedUser(), connection);
//
//                    }
//
//                    i++;
//
//                    //calculate progress                  
//                    BigDecimal bd = new BigDecimal(i * 100 / totalLines);
//                    BigDecimal bdRounded = bd.setScale(0, RoundingMode.CEILING);
//                    cmpltdProgress = bdRounded.intValue();
//
//                    //set task progress info to session
//                    progressobject.put(SystemVarList.TASK_PROGRESS, new Integer(cmpltdProgress + ""));
//                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.PENDING);
//
//                    session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());
//
//                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
//                    //save to error log  
//                    errorCounts++;
//                    saveErrorLog(ticketFile.getId(), sCurrentLine, e.getMessage(), ticketFile.getLastUpdatedUser(), connection);
//
//                }
//
//            }
//
//            //-----------------------------------------------------------------------------------------------------
//            //flush session
//            if (cmpltdProgress == 100) {
//
//                if (errorCounts == totalLines) {//totaly error
//                    progressobject.put(SystemVarList.STATUS, SystemVarList.ERROR);                 
//                    updateStatus(ticketFile.getId(), SystemVarList.SUBMITED,totalLines, connection);
//                   
//                } else {//success
//                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);                
//                    updateStatus(ticketFile.getId(), SystemVarList.APPROVED,totalLines, connection);
//
//                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
//
//                }
//
//                progressobject.put(SystemVarList.TASK_PROGRESS, 100);
//                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
//                session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());
//                
//                connection.commit();
//
//            }
//
//
//    }
//    @Transactional(rollbackFor = Exception.class)
//    public void saveTicketData(HttpSession session, DlbWbTicketFile ticketFile) throws Exception {
//        //build result table data from .txt file
//
//        try {
//            hibernateSession = sessionFactory.openSession();
//            transaction = hibernateSession.getTransaction();
//            transaction.begin();
//
//            BufferedReader br = null;
//            JSONObject progressobject = new JSONObject();
//            br = new BufferedReader(new FileReader(ticketFile.getFilePath()));
//
//            //get total lines of the text file
//            int totalLines = 0;
//            int cmpltdProgress = 0;
//            int errorCounts = 0;
//            while (br.readLine() != null) {
//                totalLines++;
//            }
//
//            br = null;
//
//            String sCurrentLine;
//            br = new BufferedReader(new FileReader(ticketFile.getFilePath()));
//
//            int i = 0;
//            while ((sCurrentLine = br.readLine()) != null) { //loop though txt file lines
//
//                try {
//                    //get indexes of row
//                    int idIndex = sCurrentLine.indexOf(",", 0);
//                    int snIndex = sCurrentLine.indexOf(",", idIndex + 1);
//
//                    //read text file coulumn by coulumn
//                    String serialNo = sCurrentLine.substring(idIndex + 1, snIndex);
//                    String items = sCurrentLine.substring(snIndex + 1);
//
//                    DlbWbTicket ticket = new DlbWbTicket();
//                    ticket.setSerialNumber(serialNo);
//                    ticket.setDrawNo(ticketFile.getDrawNo());
//                    ticket.setProductCode(ticketFile.getDlbWbProduct().getProductCode());
//                    ticket.setLotteryNumbers(items);
//                    ticket.setDrawDate(ticketFile.getDate());
//                    ticket.setProductCode(ticketFile.getDlbWbProduct().getProductCode());
//                    ticket.setStatus(SystemVarList.READY_TO_CHECKOUT);
//                    ticket.setDlbWbTicketFile(ticketFile);
//                    ticket.setCreatedDate(new Date());
//
//                    //generate has value for S/N and tickert numbers                    
//                    String plainTextComb = serialNo + "$" + ticket.getLotteryNumbers().trim();
//                    String genarateShaVal = encryptionService.genarateShaVal(plainTextComb);
//                    ticket.setShaKey(genarateShaVal);
//
//                    //check is exsiting
//                    Criteria criteria = hibernateSession.createCriteria(DlbWbTicket.class);
//                    criteria.add(Restrictions.eq("serialNumber", serialNo));
//                    criteria.add(Restrictions.eq("productCode", ticket.getProductCode()));
//
//                    if (criteria.list().isEmpty()) {
//                        //not already exsiting and save succesfully
//                        hibernateSession.save(ticket);
//                    } else {
//                        errorCounts++;
//                        //already exsiting and save to error log
//                        saveErrorLog(ticketFile.getId(), sCurrentLine, "Serial Number Already exsits in selected lottery. SN:" + serialNo + ", Lottery:" + ticket.getProductCode(), ticketFile.getLastUpdatedUser(), hibernateSession);
//
//                    }
//
//                    i++;
//
//                    //calculate progress                  
//                    BigDecimal bd = new BigDecimal(i * 100 / totalLines);
//                    BigDecimal bdRounded = bd.setScale(0, RoundingMode.CEILING);
//                    cmpltdProgress = bdRounded.intValue();
//
//                    //set task progress info to session
//                    progressobject.put(SystemVarList.TASK_PROGRESS, new Integer(cmpltdProgress + ""));
//                    progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.PENDING);
//
//                    session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());
//
//                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
//                    //save to error log  
//                    errorCounts++;
//                    saveErrorLog(ticketFile.getId(), sCurrentLine, e.getMessage(), ticketFile.getLastUpdatedUser(), hibernateSession);
//
//                }
//
//            }
//
//            //-----------------------------------------------------------------------------------------------------
//            //flush session
//            if (cmpltdProgress == 100) {
//
//                if (errorCounts == totalLines) {//totaly error
//                    progressobject.put(SystemVarList.STATUS, SystemVarList.ERROR);
//                    ticketFile.setDlbStatus(new DlbStatus(SystemVarList.SUBMITED, null, null));
//                    ticketFile.setNoOfTicket(totalLines);
//                    hibernateSession.update(ticketFile);
//                    transaction.commit();
//                    hibernateSession.flush();
//                } else {//success
//                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
//                    ticketFile.setDlbStatus(new DlbStatus(SystemVarList.APPROVED, null, null));
//                    ticketFile.setNoOfTicket(totalLines);
//                    hibernateSession.update(ticketFile);
//                    transaction.commit();
//                    hibernateSession.flush();
//
//                    progressobject.put(SystemVarList.STATUS, SystemVarList.SUCCESS);
//
//                }
//
//                progressobject.put(SystemVarList.TASK_PROGRESS, 100);
//                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
//                session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());
//
//            }
//
////            if (cmpltdProgress == 100 && errorCounts != totalLines) {
////                //done
////                ticketFile.setDlbStatus(new DlbStatus(SystemVarList.APPROVED, null, null));
////                ticketFile.setNoOfTicket(totalLines);
////                hibernateSession.update(ticketFile);
////                transaction.commit();
////                hibernateSession.flush();
////
////            }
////
////            if (errorCounts < totalLines) {
////                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.COMPLTED);
////
////            } else {
////                progressobject.put(SystemVarList.TASK_STATUS, SystemVarList.ERROR);
////                ticketFile.setDlbStatus(new DlbStatus(SystemVarList.SUBMITED, null, null));
////                ticketFile.setNoOfTicket(totalLines);
////                hibernateSession.update(ticketFile);
////                transaction.commit();
////                hibernateSession.flush();
////            }
//            //set task progress info to session
////            progressobject.put(SystemVarList.TASK_PROGRESS, 100);
////            session.setAttribute(SystemVarList.TASK_INFO + "_" + ticketFile.getId(), progressobject.toJSONString());
//        } finally {
//            if (hibernateSession != null) {
//                hibernateSession.close();
//            }
//
//        }
//
//    }

    //save error log
    public void saveErrorLog(int id, String data, String reason, String user, Connection connection) throws SQLException {

        try {

            PreparedStatement prepareStatement = connection.prepareStatement("INSERT "
                    + "INTO `DLB_WB_TICKET_FILE_ERROR_DETAILS`"
                    + "(`ID`, `TICKET_ID`, `DATA`, `REASON`, `LAST_UPDATED_USER`, `LAST_UPDATED_TIME`, `CREATED_TIME`) "
                    + " VALUES (null,?,?,?,?,NOW(),NOW())");

            prepareStatement.setInt(1, id);
            prepareStatement.setString(2, data);
            prepareStatement.setString(3, reason);
            prepareStatement.setString(4, user);

            prepareStatement.executeUpdate();

        } catch (HibernateException e) {
            throw e;
        }

    }

    public int getTicketCountByWiningFile(DlbWbWiningFile dlbWbWiningFile, int status) {
        BigInteger count = null;
        Session currentSession = sessionFactory.getCurrentSession();
        SQLQuery createSQLQuery = currentSession.createSQLQuery("SELECT  "
                + "	Count( DLB_WB_TICKET.ID )  "
                + "FROM "
                + "	DLB_WB_TICKET "
                + "	INNER JOIN DLB_WB_TICKET_FILE ON DLB_WB_TICKET.TICKET_FILE_ID = DLB_WB_TICKET_FILE.ID  "
                + "WHERE "
                + "	DLB_WB_TICKET.`STATUS` =:status "
                + "	AND DLB_WB_TICKET_FILE.PRODUCT_CODE =:productCode  "
                + "	AND DLB_WB_TICKET_FILE.DRAW_NO =:draw_no");

        createSQLQuery.setInteger("status", status);
        createSQLQuery.setString("productCode", dlbWbWiningFile.getDlbWbProduct().getProductCode());
        createSQLQuery.setString("draw_no", dlbWbWiningFile.getDrawNo());

        List list = createSQLQuery.list();
        if (list.size() > 0) {
            count = (BigInteger) list.get(0);
        }
        return count.intValue();

    }

    public void updateStatus(int id, int status, int numOfTicket, Connection conecction) throws SQLException {
        PreparedStatement prepareStatement = conecction.prepareStatement("UPDATE `DLB_WB_TICKET_FILE` SET PENDING_APPROVAL=?,NO_OF_TICKET=?  "
                + " WHERE `DLB_WB_TICKET_FILE`.`ID` = ? ");

        prepareStatement.setInt(1, status);
        prepareStatement.setInt(2, numOfTicket);
        prepareStatement.setInt(3, id);

        prepareStatement.executeUpdate();

    }

    public List getSalesFileList(int fileID) {

        List list = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        String query = "SELECT  "
                + "DLB_WB_TICKET.PRODUCT_CODE AS productCode,  "
                + "DLB_WB_TICKET.SERIAL_NUMBER AS serialNo,  "
                + "DLB_WB_TICKET.`STATUS` AS status,  "
                + "DLB_SWT_ST_WALLET.NIC   AS nic "
                + "FROM  "
                + "DLB_WB_TICKET  "
                + " LEFT JOIN DLB_SWT_ST_PURCHASE_HISTORY ON DLB_WB_TICKET.SERIAL_NUMBER = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO  "
                + " LEFT JOIN DLB_SWT_ST_WALLET ON DLB_SWT_ST_PURCHASE_HISTORY.WALLET_ID = DLB_SWT_ST_WALLET.ID  "
                + " WHERE  "
                + " DLB_WB_TICKET.TICKET_FILE_ID =:fileID";
        SQLQuery createSQLQuery = currentSession.createSQLQuery(query);

        createSQLQuery.setParameter("fileID", fileID);
        createSQLQuery.setResultTransformer(Transformers.aliasToBean(SalesFile.class));
        list = createSQLQuery.list();

        return list;
    }

    public File genarateSalesFile(DlbWbTicketFile dlbWbTicketFile, int reseveCount, Connection connection)
            throws FileNotFoundException, UnsupportedEncodingException, IOException, SQLException {

//       Session currentSession=sessionFactory.getCurrentSession();
        File file = null;

        Statement statement = null;

//        File Name: DLB_SALES_<Brand Code>_<Draw No>
        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "DLB_SALES_" + dlbWbTicketFile.getDlbWbProduct().getProductCode() + "_" + dlbWbTicketFile.getDrawNo() + ".txt";

        //create file
        PrintWriter writer = new PrintWriter(tmpFilepath, "UTF-8");
        try {

            statement = connection.createStatement();

            int remainCount = reseveCount;

            String query = "SELECT  "
                    + "DLB_WB_TICKET.PRODUCT_CODE AS productCode,  "
                    + "DLB_WB_TICKET.SERIAL_NUMBER AS serialNo,  "
                    + "DLB_WB_TICKET.`STATUS` AS status,  "
                    + "DLB_SWT_ST_WALLET.NIC   AS nic "
                    + "FROM  "
                    + "DLB_WB_TICKET  "
                    + " LEFT JOIN DLB_SWT_ST_PURCHASE_HISTORY ON DLB_WB_TICKET.SERIAL_NUMBER = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO  "
                    + " LEFT JOIN DLB_SWT_ST_WALLET ON DLB_SWT_ST_PURCHASE_HISTORY.WALLET_ID = DLB_SWT_ST_WALLET.ID  "
                    + " WHERE  "
                    + " DLB_WB_TICKET.TICKET_FILE_ID =" + dlbWbTicketFile.getId();

            ResultSet executeQuery = statement.executeQuery(query);

            while (executeQuery.next()) {

//            Content: <Serial No> <Status S - Sold / R - Reserve>
                String serialNumber = executeQuery.getString("serialNo");
                String status = executeQuery.getString("status");

                //SALES TICKET
                if (status.equals(SystemVarList.CHECKOUT)) {
                    writer.println(serialNumber + " S NIC " + executeQuery.getString("nic"));
                    
                } else if (status.equals(SystemVarList.TEMP_TO_CHECKOUT)) {
                    writer.println(serialNumber + " R ");
                    remainCount--;
//                    FileNotFoundException e = null;
//                    throw e;
                } else {
                    ////Reciveve Ticket
                    if (remainCount > 0) {
                        remainCount--;
                    } //Return Tickets
                    else {
//                    updateAsReturn(executeQuery.getString("productCode"), executeQuery.getString("serialNo"), connection);
                    }
                }

            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        writer.close();
        //assign created file to return varible
        file = new File(tmpFilepath);
        return file;
    }

    public void updateAsReturn(String productCode, String sn, Connection connection) throws SQLException {

        Statement statement = connection.createStatement();

        int createSQLQuery = statement.executeUpdate("UPDATE  `DLB_WB_TICKET` SET `STATUS` ='" + SystemVarList.RETURNED + "' "
                + "WHERE `SERIAL_NUMBER` ='" + sn + "' AND "
                + "PRODUCT_CODE='" + productCode + "'");

    }

    public void updateAsFail(int fileID) {
        Session openSession = null;
        try {
            openSession = sessionFactory.openSession();
            SQLQuery createSQLQuery = openSession.createSQLQuery("UPDATE  `DLB_WB_TICKET_FILE` SET `PENDING_APPROVAL` =:status  WHERE `ID` =:id");

            createSQLQuery.setParameter("status", SystemVarList.FAILED);
            createSQLQuery.setParameter("id", fileID);
            createSQLQuery.executeUpdate();

            openSession.flush();

        } catch (Exception e) {
            throw e;
        } finally {
            if (openSession != null) {
                openSession.close();
            }

        }

    }

}
