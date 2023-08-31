/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbCeftTxnHistory;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
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
@Service("ceftTransactionService")
public class CeftTransactionService {

    @Autowired
    private GenericRepository genericRepository; 

    @Transactional(rollbackFor = Exception.class)
    public int save(DlbWbCeftTxnHistory ceftTxnHistory) throws Exception {
        return (int) genericRepository.save(ceftTxnHistory);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbCeftTxnHistory ceftTxnHistory) throws Exception {
        return (int) genericRepository.update(ceftTxnHistory);
    }
    @Transactional(rollbackFor = Exception.class)
    public DlbWbCeftTxnHistory get(int id) throws Exception {
        return (DlbWbCeftTxnHistory) genericRepository.get(id,DlbWbCeftTxnHistory.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public List listByStatus(int status) throws Exception {
        
        WhereStatement whereStatement=new WhereStatement("dlbStatus.statusCode", status, SystemVarList.EQUAL);

        List<DlbWbCeftTxnHistory> list = genericRepository.list(DlbWbCeftTxnHistory.class, whereStatement);

        Iterator<DlbWbCeftTxnHistory> iterator = list.iterator();
        while (iterator.hasNext()) {
            DlbWbCeftTxnHistory next = iterator.next();
            Hibernate.initialize(next.getDlbWbEmployee());
            Hibernate.initialize(next.getDlbStatus());
        }

        return list;
    }

}
