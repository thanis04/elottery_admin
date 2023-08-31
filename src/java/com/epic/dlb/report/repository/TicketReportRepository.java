/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kasun_n
 */
@Repository("ticketReportRepository")
public class TicketReportRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public List getTicketFile(List<WhereStatement> whereStatements, int start, int end) {
        Session session = sessionFactory.getCurrentSession();
        String whereString = "";
        String query = "SELECT\n"
                + "DLB_WB_TICKET_FILE.ID,\n"
                + "DLB_WB_TICKET_FILE.PRODUCT_DESCRIPTION,\n"
                + "DLB_WB_TICKET_FILE.DRAW_NO,\n"
                + "DLB_WB_TICKET_FILE.DATE,\n"
                + "DLB_WB_TICKET_FILE.NO_OF_TICKET,\n"
                + "Count(DLB_WB_TICKET.`STATUS`) AS SALES,\n"
                + "DLB_WB_TICKET.TICKET_FILE_ID\n"
                + "FROM\n"
                + "DLB_WB_TICKET_FILE\n"
                + "INNER JOIN DLB_WB_TICKET ON DLB_WB_TICKET.TICKET_FILE_ID = DLB_WB_TICKET_FILE.ID\n"
                + " " + whereString + " "
                + "DLB_WB_TICKET_FILE.CREATED_TIME BETWEEN '2017-09-18 00:00:00' AND '2019-05-06 00:00:00' AND\n"
                + "DLB_WB_TICKET.`STATUS` = '\n" + SystemVarList.CHECKOUT + "' "
                + "GROUP BY\n"
                + "DLB_WB_TICKET.TICKET_FILE_ID\n"
                + "LIMIT " + start + ", " + end;

        //Add where clause
        int index = 1;
        for (WhereStatement where : whereStatements) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (index == whereStatements.size()) {

                switch (where.getOperator()) {
                    case "=":
                        whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' ";
                        break;
                    case "like":
                        whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' ";
                        break;
                    case "<>":
                        whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' ";
                        break;
                    case "IS NULL":
                        whereString += where.getProperty() + " " + where.getOperator() + "  ";
                        break;
                    case "IS NOT NULL":
                        whereString += where.getProperty() + " " + where.getOperator() + "  ";
                        break;
                    case "less_than":
                        whereString += where.getProperty() + " < '" + where.getValue() + "' ";
                        break;

                    case "greater_than":
                        whereString += where.getProperty() + " > '" + where.getValue() + "' ";
                        break;

                    case "less_than_or_equal":
                        whereString += where.getProperty() + " <='" + where.getValue() + "' ";
                        break;

                    case "greater_than_or_equal":
                        whereString += where.getProperty() + " >='" + where.getValue() + "' ";
                        break;

                }
            } else {

                switch (where.getOperator()) {
                    case "=":
                        whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' AND ";
                        break;
                    case "like":
                        whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' AND ";
                        break;
                    case "<>":
                        whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' AND ";
                        break;
                    case "IS NULL":
                        whereString += where.getProperty() + " " + where.getOperator() + "  AND ";
                        break;
                    case "IS NOT NULL":
                        whereString += where.getProperty() + " " + where.getOperator() + "  AND ";
                        break;
                    case "less_than":
                        whereString += where.getProperty() + " < '" + where.getValue() + "' AND ";
                        break;

                    case "greater_than":
                        whereString += where.getProperty() + " > '" + where.getValue() + "' AND ";
                        break;

                    case "less_than_or_equal":
                        whereString += where.getProperty() + " <='" + where.getValue() + "' AND ";
                        break;

                    case "greater_than_or_equal":
                        whereString += where.getProperty() + " >='" + where.getValue() + "' AND ";
                        break;

                }

            }

            index++;

        }

        Query createQuery = session.createSQLQuery(query);

        return createQuery.list();
    }

    public List getAllTicketByTicketFile(int id, int start, int end) {
        Session session = sessionFactory.getCurrentSession();

        String query = "SELECT\n"
                + "DLB_WB_TICKET.ID,\n"
                + "DLB_WB_TICKET.SERIAL_NUMBER,\n"
                + "DLB_WB_TICKET.LOTTERY_NUMBERS,\n"
                + "DLB_STATUS.DESCRIPTION\n"
                + "FROM\n"
                + "DLB_WB_TICKET\n"
                + "INNER JOIN DLB_STATUS ON DLB_WB_TICKET.`STATUS` = DLB_STATUS.STATUS_CODE\n"
                + "WHERE\n"
                + "DLB_WB_TICKET.TICKET_FILE_ID = ?"
                + "LIMIT " + start + ", " + end;

        Query createQuery = session.createSQLQuery(query);
        createQuery.setInteger(1, id);
        return createQuery.list();
    }

    public List getSalesTicketByTicketFile(int id, int start, int end) {
        Session session = sessionFactory.getCurrentSession();

        String query = "SELECT\n"
                + "DLB_WB_TICKET.ID,\n"
                + "DLB_WB_TICKET.SERIAL_NUMBER,\n"
                + "DLB_WB_TICKET.LOTTERY_NUMBERS,\n"
                + "DLB_STATUS.DESCRIPTION,\n"
                + "DLB_WB_TICKET.WALLET_ID,\n"
                + "DLB_SWT_ST_WALLET.MOBILE_NO,\n"
                + "DLB_SWT_ST_WALLET.NIC\n"
                + "FROM\n"
                + "DLB_WB_TICKET\n"
                + "INNER JOIN DLB_STATUS ON DLB_WB_TICKET.`STATUS` = DLB_STATUS.STATUS_CODE\n"
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ON DLB_WB_TICKET.SERIAL_NUMBER = DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO\n"
                + "INNER JOIN DLB_SWT_ST_WALLET ON DLB_SWT_ST_PURCHASE_HISTORY.WALLET_ID = DLB_SWT_ST_WALLET.ID\n"
                + "WHERE\n"
                + "DLB_WB_TICKET.TICKET_FILE_ID = 14 AND\n"
                + " STATUS ='" + SystemVarList.CHECKOUT + "'"
                + "DLB_WB_TICKET.TICKET_FILE_ID = ?"
                + "LIMIT " + start + ", " + end;


        Query createQuery = session.createSQLQuery(query);
        createQuery.setInteger(1, id);

        return createQuery.list();
    }
}
