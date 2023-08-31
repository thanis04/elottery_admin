/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.BrandwiseSearchReportDto;
import com.epic.dlb.dto.ReconciliationReportDto;
import com.epic.dlb.repository.CustomRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("reconciliationReportRepository")
public class ReconciliationReportRepository {

    @Autowired
    private CustomRepository customRepository;

    private SimpleDateFormat dateFormat;

    public List<ReconciliationReportDto> findReconciliationData(ReportSearchCriteriaDto dto) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryStr = "SELECT "
                + "`RV`.`TXNTYPE`, "
                + "`RV`.`LOTTERY`, "
                + "`RV`.`DRAW_NO`, "
                + "`RV`.`DRAW_DATE`, "
                + "`RV`.`REF`, "
                + "`RV`.`WINNING_PRIZE`, "
                + "`RV`.`SERIAL_NO`, "
                + "`RV`.`TICKET_ID`, "
                + "`RV`.`REDEEM_MODE`, "
                + "`RV`.`REDEEM_TIME`, "
                + "`RV`.`RRN`, "
                + "`RV`.`PURCHASE_HIS_ID` "
                + "FROM `RECONCILIATION_VIEW` `RV` "
                + "WHERE `RV`.`REDEEM_TIME` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if ((!dto.getGameType().equals("0"))) {
            queryStr = queryStr + "AND `RV`.`PRODUCT_CODE` = '" + dto.getGameType() + "' ";
        }

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

        System.out.println(queryStr);

        List<Object[]> resultList = customRepository.queryExecuter(queryStr);
        List<ReconciliationReportDto> reconciliationReportDtos = new ArrayList<>();
        resultList.forEach(row -> {
            ReconciliationReportDto reconciliationReportDto = new ReconciliationReportDto();
            reconciliationReportDto.setLotteryType(row[1] == null ? "-" : row[1].toString());
            reconciliationReportDto.setDrawNo(row[2] == null ? "-" : row[2].toString());
            reconciliationReportDto.setDrawDate(row[3] == null ? "-" : dateFormat.format((Date) row[3]));
            reconciliationReportDto.setRedemptionDate(row[9] == null ? "-" : dateFormat.format((Date) row[9]));
            reconciliationReportDto.setRedemptionMode(row[8] == null ? "-" : row[8].toString());
            reconciliationReportDto.setReferenceID(row[4] == null ? "-" : row[4].toString());
            reconciliationReportDto.setWinningPrize(row[5] == null ? "-" : row[5].toString());
            reconciliationReportDto.setTicketReference(row[6] == null ? "-" : row[6].toString());
            reconciliationReportDto.setTicketID(row[7] == null ? "-" : row[7].toString());
            reconciliationReportDto.setRrn(row[10] == null ? "-" : row[10].toString());
            reconciliationReportDto.setPurchaseHis(row[11] == null ? "-" : row[11].toString());
            reconciliationReportDtos.add(reconciliationReportDto);
        });

        return reconciliationReportDtos;
    }

    public Integer findReconciliationCount(
            ReportSearchCriteriaDto dto) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryStr = "SELECT "
                + "COUNT(*) "
                + "FROM `RECONCILIATION_VIEW` `RV` "
                + "WHERE `RV`.`REDEEM_TIME` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if ((!dto.getGameType().equals("0"))) {
            if (!dto.getGameType().equals("0")) {
                queryStr = queryStr + "AND `RV`.`PRODUCT_CODE` = '" + dto.getGameType() + "' ";
            }
        }

        List<Object[]> resultList = customRepository.queryExecuter(queryStr);
        return Integer.parseInt(resultList.toString().replace("[", "").replace("]", "").trim());
    }
}
