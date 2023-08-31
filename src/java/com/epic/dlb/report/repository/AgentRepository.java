/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.AgentDetailSearchDto;
import com.epic.dlb.dto.AgentSalesByDto;
import com.epic.dlb.dto.AgentSalesDto;
import com.epic.dlb.dto.AgentSearchCriteriaDto;
import com.epic.dlb.dto.AgentUserDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.repository.CustomRepository;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository
public class AgentRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CustomRepository customRepository;

    public List<AgentUserDto> findUsers(AgentSearchCriteriaDto dto) throws Exception {
        String queryStr = "SELECT "
                + "`WAL`.ID AS id, "
                + "`WAL`.NIC AS nic, "
                + "`WAL`.`USERNAME` AS username, "
                + "`WAL`.`FIRST_NAME` AS firstName, "
                + "`WAL`.`LAST_NAME` AS lastName, "
                + "`WAL`.`AGENT_CODE` AS agentCode, "
                + "MAX(`WAL`.`LAST_LOGIN_TIME`) AS lastLoginTime, "
                + "`WAL`.`CREATE_TIME` AS createdTime, "
                + "MAX(`TR`.`UPDATEDTIME`) AS transactionDate"
                + " FROM `DLB_SWT_ST_WALLET` AS `WAL` "
                + "INNER JOIN `DLB_SWT_ST_TRANSACTION` AS `TR` "
                + "ON `TR`.`WALLETID` = `WAL`.`ID` "
                + "WHERE `WAL`.`CREATE_TIME` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' "
                + "AND `WAL`.`AGENT_CODE` = '" + dto.getAgentCode() + "' "
                + "GROUP BY `WAL`.ID ";

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        List<AgentUserDto> list = new ArrayList<>();
        result.forEach(row -> {
            AgentUserDto agentUserDto = new AgentUserDto();
            agentUserDto.setNic(row[1].toString());
            agentUserDto.setUsername(row[2].toString());
            agentUserDto.setFirstName(row[3].toString());
            agentUserDto.setLastName(row[4].toString());
            agentUserDto.setAgentCode(row[5].toString());
            agentUserDto.setLastLoginTime(row[6] == null ? "-" : row[6].toString().replace(".0", ""));
            agentUserDto.setCreatedTime(row[7] == null ? "-" : row[7].toString().replace(".0", ""));
            agentUserDto.setTransactionDate(row[8] == null ? "-" : row[8].toString().replace(".0", ""));
            list.add(agentUserDto);
        });

        return list;
    }

    public List<AgentSalesDto> findSales(AgentSearchCriteriaDto dto) throws Exception {
        String queryStr = "SELECT `SR`.`PRODUCT_CODE` product_code, "
                + "`SR`.DESCRIPTION AS product, "
                + "COUNT(*) AS ticket_count "
                + "FROM `SALES_REPORT_AGENT` `SR` "
                + "WHERE `SR`.`DATE` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if (!dto.getAgentCode().equals("")) {
            queryStr = queryStr + "AND `SR`.`AGENT_CODE` = '" + dto.getAgentCode() + "' ";
        }

        queryStr = queryStr + "GROUP BY `SR`.`PRODUCT_CODE` ";
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        List<AgentSalesDto> list = new ArrayList<>();
        result.forEach(row -> {
            AgentSalesDto agentSalesDto = new AgentSalesDto();
            agentSalesDto.setGameType(row[1].toString());
            agentSalesDto.setNoOfTickets(row[2].toString());
            list.add(agentSalesDto);
        });

        return list;
    }

    public Integer findSalesTotal(AgentSearchCriteriaDto dto) throws Exception {
        String queryStr = "SELECT COUNT(*) AS ticket_count "
                + "FROM `SALES_REPORT_AGENT` `SR` "
                + "WHERE `SR`.`DATE` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if (!dto.getAgentCode().equals("")) {
            queryStr = queryStr + "AND `SR`.`AGENT_CODE` = '" + dto.getAgentCode() + "' ";
        }

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        Integer total = Integer.parseInt(result.toString().replace("[", "").replace("]", "").trim());

        return total;
    }

    public List<AgentSalesByDto> findSalesByUser(AgentSearchCriteriaDto dto) throws Exception {
        String queryStr = "SELECT `SR`.`FIRST_NAME` as firstName, "
                + "`SR`.`LAST_NAME` AS lastName, "
                + "COUNT(*) AS ticket_count "
                + "FROM `SALES_REPORT_AGENT` `SR` "
                + "WHERE `SR`.`DATE` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if (!dto.getAgentCode().equals("")) {
            queryStr = queryStr + "AND `SR`.`AGENT_CODE` = '" + dto.getAgentCode() + "' ";
        }
        queryStr = queryStr + "GROUP BY `SR`.`USER_ID` ";

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        List<AgentSalesByDto> list = new ArrayList<>();
        result.forEach(row -> {
            AgentSalesByDto agentSalesByDto = new AgentSalesByDto();
            agentSalesByDto.setNameOfUser(row[0].toString() + " " + row[1].toString());
            agentSalesByDto.setNoOfTickets(row[2].toString());
            list.add(agentSalesByDto);
        });

        return list;
    }

    public Integer findSalesByUserTotal(AgentSearchCriteriaDto dto) throws Exception {
        String queryStr = "SELECT "
                + "COUNT(*) AS ticket_count "
                + "FROM `SALES_REPORT_AGENT` `SR` "
                + "WHERE `SR`.`DATE` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if (!dto.getAgentCode().equals("")) {
            queryStr = queryStr + "AND `SR`.`AGENT_CODE` = '" + dto.getAgentCode() + "' ";
        }
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        Integer total = Integer.parseInt(result.toString().replace("[", "").replace("]", "").trim());

        return total;
    }

    public List<Object[]> findAgents(AgentDetailSearchDto dto) throws Exception {
        String queryStr = "SELECT "
                + "`EM`.`EMPLOYEEID`, "
                + "`EM`.`NAME`, "
                + "`EM`.`CONTACTNO`, "
                + "`EM`.`NIC`, "
                + "`EM`.`EMAIL`, "
                + "`EM`.`ADDRESS`, "
                + "`S`.`DESCRIPTION`, "
                + "`EM`.`CREATEDTIME` "
                + "FROM "
                + "`DLB_WB_SYSTEM_USER` `SU` "
                + "JOIN `DLB_WB_EMPLOYEE` `EM` ON `SU`.`EMPLOYEE` = `EM`.`EMPLOYEEID` "
                + "JOIN `DLB_STATUS` `S` ON `SU`.`STATUS` = `S`.`STATUS_CODE` "
                + "WHERE "
                + "(SU.USERROLE = 'AGENT' OR SU.USERROLE = 'Sub Agents') ";

        if (!dto.getEmpId().equals("-")) {
            queryStr = queryStr + "AND `EM`.`EMPLOYEEID` = '" + dto.getEmpId() + "' ";
        }

        if (!dto.getEmpName().equals("-")) {
            queryStr = queryStr + "AND `EM`.`NAME` LIKE '%" + dto.getEmpName() + "%' ";
        }

        if (dto.getType().equals("-")) {

            int limit = 0;
            int length = 10;

            if (dto.getLength() != null) {
                length = Integer.parseInt(dto.getLength());
            }

            if (!dto.getStart().equals("0")) {
                limit = Integer.parseInt(dto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result;
    }

    public Integer findAgentsCount(AgentDetailSearchDto dto) throws Exception {
        String queryStr = "SELECT "
                + "`EM`.`EMPLOYEEID` "
                + "FROM "
                + "`DLB_WB_SYSTEM_USER` `SU` "
                + "JOIN `DLB_WB_EMPLOYEE` `EM` ON `SU`.`EMPLOYEE` = `EM`.`EMPLOYEEID` "
                + "JOIN `DLB_STATUS` `S` ON `SU`.`STATUS` = `S`.`STATUS_CODE` "
                + "WHERE "
                + "(SU.USERROLE = 'AGENT' OR SU.USERROLE = 'Sub Agents') ";

        if (!dto.getEmpId().equals("-")) {
            queryStr = queryStr + "AND `EM`.`EMPLOYEEID` = '" + dto.getEmpId() + "' ";
        }

        if (!dto.getEmpName().equals("-")) {
            queryStr = queryStr + "AND `EM`.`NAME` LIKE '%" + dto.getEmpName() + "%' ";
        }

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result.size();
    }

    public List<Object[]> getAgentFinanceData(ReportSearchCriteriaDto dto) throws Exception {
        String queryStr = "SELECT "
                + "`SR`.`AGENT_CODE` AS `AGENT_CODE`, "
                + "`EM`.`NAME` AS `AGT_NAME`, "
                + "CONCAT( `SR`.`FIRST_NAME`, \" \", `SR`.`LAST_NAME` ) AS `CUS_NAME`, "
                + "`SR`.`NIC` AS `NIC`, "
                + "COUNT( * ) AS `TICKET_COUNT`, "
                + "MAX( `WL`.`LAST_LOGIN_TIME` ) AS `LAST_LOGIN_TIME` "
                + "FROM "
                + "`SALES_REPORT_AGENT` `SR` "
                + "INNER JOIN `DLB_SWT_ST_WALLET` `WL` ON `SR`.`USER_ID` = `WL`.`ID` "
                + "LEFT JOIN DLB_WB_EMPLOYEE EM ON `SR`.`AGENT_CODE` = EM.EMPLOYEEID ";

        queryStr = queryStr + "WHERE `SR`.`DATE` BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        queryStr = queryStr + "GROUP BY `SR`.`USER_ID` ";

        if (dto.getMode().equals("REP")) {

            int limit = 0;
            int length = 10;

            if (dto.getLength() != null) {
                length = Integer.parseInt(dto.getLength());
            }

            if (!dto.getStart().equals("0")) {
                limit = Integer.parseInt(dto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result;
    }

    public Integer getAgentFinanceDataCount(ReportSearchCriteriaDto dto) throws Exception {

        String queryStr = "SELECT "
                + "`SR`.`AGENT_CODE` AS `AGENT_CODE`, "
                + "`EM`.`NAME` AS `AGT_NAME`, "
                + "CONCAT( `SR`.`FIRST_NAME`, \" \", `SR`.`LAST_NAME` ) AS `CUS_NAME`, "
                + "`SR`.`NIC` AS `NIC`, "
                + "COUNT( * ) AS `TICKET_COUNT`, "
                + "MAX( `WL`.`LAST_LOGIN_TIME` ) AS `LAST_LOGIN_TIME` "
                + "FROM "
                + "`SALES_REPORT_AGENT` `SR` "
                + "INNER JOIN `DLB_SWT_ST_WALLET` `WL` ON `SR`.`USER_ID` = `WL`.`ID` "
                + "LEFT JOIN DLB_WB_EMPLOYEE EM ON `SR`.`AGENT_CODE` = EM.EMPLOYEEID ";
        queryStr = queryStr + "WHERE `SR`.`DATE` BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        queryStr = queryStr + "GROUP BY `SR`.`USER_ID` ";

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return result.size();
    }
}
