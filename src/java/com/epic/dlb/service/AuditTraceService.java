/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbAudittrace;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author salinda_r
 */
@Service("auditTraceService")
public class AuditTraceService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(String activity, HttpSession session) throws Exception {
        Date currentDate = new Date();
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        String ipAddress = (String) session.getAttribute(SystemVarList.IP);

        //set object
        DlbWbAudittrace audittrace = new DlbWbAudittrace();
        audittrace.setActivity(activity);
        audittrace.setIp(ipAddress);
        audittrace.setUsername(systemUser.getUsername());
        audittrace.setLastupdateduser(systemUser.getUsername());
        audittrace.setLastupdatedtime(currentDate);
        audittrace.setCreatedtime(currentDate);

        //save
        genericRepository.save(audittrace);

    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbAudittrace.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbAudittrace audittrace = (DlbWbAudittrace) tmpList.get(i);
            //init proxy objects

            list.add(audittrace);

        }
        return list;

    }
}
