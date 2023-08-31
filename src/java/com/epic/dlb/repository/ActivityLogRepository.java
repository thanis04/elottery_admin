/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import com.epic.dlb.dto.ActivityLogDto;
import com.epic.dlb.model.DlbWbActivityLog;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("activityLogRepository")
public class ActivityLogRepository {

    @Autowired
    private CustomRepository customRepository;

    public JSONArray getList(ActivityLogDto dto) throws Exception {
        String queryStr = "SELECT "
                + "`AL`.`ID` AS `ID`, "
                + "`AL`.`EMPLOYEEID` AS `EMPLOYEEID`, "
                + "`EMP`.`NAME` AS `USER_NAME`, "
                + "`AL`.`ACTION` AS `ACTION`, "
                + "`AL`.`DESCRIPTION` AS `DESCRIPTION`, "
                + "`SEC`.`DESCRIPTION` AS `SECTION`, "
                + "`P`.`DESCRIPTION` AS `PAGE`, "
                + "`AL`.`CREATED_TIME` AS `DATE` "
                + "FROM `DLB_WB_ACTIVITY_LOG` `AL` "
                + "LEFT JOIN `DLB_WB_SYSTEM_USER` `SU` ON `AL`.`USER` = `SU`.`USERNAME` "
                + "LEFT JOIN `DLB_WB_PAGE` `P` ON `AL`.`PAGE` = `P`.`PAGECODE` "
                + "LEFT JOIN `DLB_WB_SECTION` `SEC` ON `AL`.`SECTION` = `SEC`.`SECTIONCODE` "
                + "LEFT JOIN `DLB_WB_EMPLOYEE` `EMP` ON `AL`.`EMPLOYEEID` = `EMP`.`EMPLOYEEID` "
                + "WHERE `AL`.`PAGE` = '" + dto.getDlbWbPage() + "' ";

        if (!dto.getCreatedTime().equals("-") || !dto.getToDate().equals("-")) {
            queryStr = queryStr + "AND `AL`.`CREATED_TIME` BETWEEN "
                    + "'" + dto.getCreatedTime() + " 00:00:00' AND '" + dto.getToDate() + " 23:59:59' ";
        }

        queryStr = queryStr + "ORDER BY `AL`.`CREATED_TIME` DESC ";
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

        List<Object[]> result = customRepository.queryExecuter(queryStr);
        JSONArray jSONArray = new JSONArray();
        result.forEach(row -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", row[0] == null ? "-" : row[0].toString());
            jSONObject.put("emp_id", row[1] == null ? "-" : row[1].toString());
            jSONObject.put("user_name", row[2] == null ? "-" : row[2].toString());
            jSONObject.put("action", row[3] == null ? "-" : row[3].toString());
            jSONObject.put("description", row[4] == null ? "-" : row[4].toString().
                    replace("}", "").replace("{", "").replace("\",", "|").replace(",\"", "|").replace("\"", "").
                    replace("null,", "null|").replace("[", "").replace("]", "").replace(":", "<b> : </b>"));
            jSONObject.put("section", row[5] == null ? "-" : row[5].toString());
            jSONObject.put("page", row[6] == null ? "-" : row[6].toString());
            jSONObject.put("created_time", row[7] == null ? "-" : row[7].toString().replace(".0", ""));
            jSONArray.add(jSONObject);
        });

        return jSONArray;
    }

    public Integer getCount(ActivityLogDto activityLogDto) throws Exception {
        String queryStr = "SELECT "
                + "COUNT(*) AS COUNT "
                + "FROM `DLB_WB_ACTIVITY_LOG` `AL` "
                + "LEFT JOIN `DLB_WB_SYSTEM_USER` `SU` ON `AL`.`USER` = `SU`.`USERNAME` "
                + "LEFT JOIN `DLB_WB_PAGE` `P` ON `AL`.`PAGE` = `P`.`PAGECODE` "
                + "LEFT JOIN `DLB_WB_SECTION` `SEC` ON `AL`.`SECTION` = `SEC`.`SECTIONCODE` "
                + "LEFT JOIN `DLB_WB_EMPLOYEE` `EMP` ON `AL`.`EMPLOYEEID` = `EMP`.`EMPLOYEEID` "
                + "WHERE `AL`.`PAGE` = '" + activityLogDto.getDlbWbPage() + "' ";

        if (!activityLogDto.getCreatedTime().equals("-") || !activityLogDto.getToDate().equals("-")) {
            queryStr = queryStr + "AND `AL`.`CREATED_TIME` BETWEEN "
                    + "'" + activityLogDto.getCreatedTime() + " 00:00:00' AND '" + activityLogDto.getToDate() + " 23:59:59' ";
        }
        List<Object[]> result = customRepository.queryExecuter(queryStr);
        return Integer.parseInt(result.toString().replace("[", "").replace("]", ""));
    }
}
