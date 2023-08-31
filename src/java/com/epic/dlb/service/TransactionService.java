/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbSwtMtBank;
import com.epic.dlb.model.DlbSwtMtPaymentMethod;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author methsiri_h
 */
@Service("transactionService")
public class TransactionService {

    @Autowired
    private GenericRepository genericRepository;

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();

        List tmpList = genericRepository.list(DlbSwtStTransaction.class);
        for (int i = 0; i < tmpList.size(); i++) {
            DlbSwtStTransaction transaction = (DlbSwtStTransaction) tmpList.get(i);
            Hibernate.initialize(transaction.getDlbSwtStWallet());
            Hibernate.initialize(transaction.getDlbSwtMtTxnType());

            //add to list
            list.add(transaction);

        }
        return list;
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listTypes() throws Exception {
        WhereStatement whereStatement = new WhereStatement("code", 0, SystemVarList.NOT_EQUAL);
        return genericRepository.list(DlbSwtMtPaymentMethod.class, whereStatement);

    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbSwtStTransaction.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbSwtStTransaction transaction = (DlbSwtStTransaction) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(transaction.getDlbSwtStWallet());
            Hibernate.initialize(transaction.getDlbSwtMtTxnType());
            list.add(transaction);

        }
        return list;

    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStTransaction get(String id) throws Exception { 
        DlbSwtStTransaction transaction = (DlbSwtStTransaction) genericRepository.get(id, DlbSwtStTransaction.class);
        //init device in tranaction
        Hibernate.initialize(transaction.getDlbSwtStWallet());
        Hibernate.initialize(transaction.getDlbSwtMtTxnType());
        return transaction;
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(DlbSwtStTransaction transaction) throws Exception {
        return (int) genericRepository.update(transaction);

    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbSwtMtBank> listBank() {
        return genericRepository.list(DlbSwtMtBank.class); 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public DlbSwtMtBank getBank(String id) {
        
        return (DlbSwtMtBank) genericRepository.get(id, DlbSwtMtBank.class); 
    }

}
