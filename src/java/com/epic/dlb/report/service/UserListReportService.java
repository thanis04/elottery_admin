/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.UserListReportDto;
import com.epic.dlb.repository.CustomRepository;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service("userListReportService")
public class UserListReportService {

    @Autowired
    private CustomRepository customRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getUserListByFromDateToDate(String fromDate, String toDate) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        if (fromDate != null) {
            fromDate = fromDate + " 00:00:00";
        }

        if (toDate != null) {
            toDate = toDate + " 23:59:59";
        }

        String query = "SELECT\n"
                + "DLB_SWT_ST_WALLET.FIRST_NAME,\n"
                + "DLB_SWT_ST_WALLET.LAST_NAME,\n"
                + "DLB_SWT_ST_WALLET.USERNAME,\n"
                + "DLB_SWT_ST_WALLET.NIC,\n"
                + "DLB_SWT_ST_WALLET.BRAND,\n"
                + "DLB_SWT_ST_WALLET.OS_VERSION,\n"
                + "DLB_SWT_ST_WALLET.OS_TYPE,\n"
                + "DLB_SWT_ST_WALLET.MOBILE_NO,\n"
                + "DLB_SWT_ST_WALLET.EMAIL,\n"
                + "DLB_SWT_ST_WALLET.CREATE_TIME,\n"
                + "DLB_SWT_ST_WALLET.LAST_UPDATED_TIME,\n"
                + "DLB_SWT_ST_WALLET.LAST_LOGIN_TIME,\n"
                + "DLB_SWT_ST_TRANSACTION.UPDATEDTIME,\n"
                + "DLB_SWT_ST_WALLET.AGENT_CODE\n"
                + "FROM DLB_SWT_ST_WALLET\n"
                + "LEFT JOIN DLB_SWT_ST_TRANSACTION \n"
                + "ON DLB_SWT_ST_TRANSACTION.WALLETID=DLB_SWT_ST_WALLET.ID\n"
                + "WHERE DLB_SWT_ST_WALLET.CREATE_TIME \n"
                + "BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY DLB_SWT_ST_WALLET.ID "
                + "ORDER BY DLB_SWT_ST_WALLET.ID ASC";

        String optionalWhere = "";

        String fullQuery = query + optionalWhere;

        Iterator<Object[]> list = customRepository.queryExecuter(fullQuery).iterator();
        List<UserListReportDto> userListReportDtos = new ArrayList<>();

        while (list.hasNext()) {
            Object[] next = list.next();

            UserListReportDto userListReportDto = new UserListReportDto();

            Timestamp createdTime = (Timestamp) next[9];
            Timestamp lastUpdatedTime = (Timestamp) next[10];
            Timestamp lastLoginTime = (Timestamp) next[11];
            Timestamp lastTransactionTime = (Timestamp) next[12];

            userListReportDto.setFirstName((String) next[0]);
            userListReportDto.setLastName((String) next[1]);
            userListReportDto.setUserName((String) next[2]);
            userListReportDto.setNic((String) next[3]);
            userListReportDto.setBrand((String) next[4]);
            userListReportDto.setOsVersion((String) next[5]);
            userListReportDto.setOsType((String) next[6]);
            userListReportDto.setMobileNumber((String) next[7]);
            userListReportDto.setEmail((String) next[8]);
            userListReportDto.setCreateTime(dateFormat.format(createdTime));
            userListReportDto.setLastUpdatedTime(dateFormat.format(lastUpdatedTime));
            if (lastLoginTime != null) {
                userListReportDto.setLastLoginTime(dateFormat.format(lastLoginTime));
            }

            if (lastTransactionTime != null) {
                userListReportDto.setLastTransactionTime(dateFormat.format(lastTransactionTime));
            }

            userListReportDto.setAgentCode((String) next[13]);

            userListReportDtos.add(userListReportDto);

        }

        return userListReportDtos;
    }

}
