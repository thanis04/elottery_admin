/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStToken;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbSwtStWalletHistory;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author methsiri_h
 */
@Service("deviceService")
public class DeviceService {

    @Autowired
    private GenericRepository genericRepository;

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbSwtStWallet.class);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbSwtStWallet device = (DlbSwtStWallet) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(device.getDlbStatusByStatus());
            list.add(device);
        }
        return list;
    }

    //get 
    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWallet get(int id) throws Exception {
        return (DlbSwtStWallet) genericRepository.get(id, DlbSwtStWallet.class);
    }

    //get 
    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWallet get(WhereStatement... whereStatements) throws Exception {
        return (DlbSwtStWallet) genericRepository.get(DlbSwtStWallet.class, whereStatements);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(DlbSwtStWallet device) throws Exception {
        return (int) genericRepository.update(device);
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbSwtStWallet.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbSwtStWallet device = (DlbSwtStWallet) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(device.getDlbStatusByStatus());
            list.add(device);

        }
        return list;

    }

    //view activity history
    @Transactional(rollbackFor = Exception.class)
    public List viewActivityHistory(int id) {
        WhereStatement whereStatement = new WhereStatement("dlbSwtStWallet.id", id, SystemVarList.EQUAL);
        List<DlbSwtStWalletHistory> listWithQuery = genericRepository.listWithQuery(DlbSwtStWalletHistory.class, whereStatement);
        Iterator<DlbSwtStWalletHistory> iterator = listWithQuery.iterator();
        while (iterator.hasNext()) {
            DlbSwtStWalletHistory next = iterator.next();
            Hibernate.initialize(next.getDlbSwtStWallet());

        }

        return listWithQuery;
    }

//    /viewPurchaseHistory
    @Transactional(rollbackFor = Exception.class)
    public List viewPurchaseHistory(int id) {

        WhereStatement whereStatement = new WhereStatement("dlbSwtStWallet.id", id, SystemVarList.EQUAL);
        List<DlbSwtStPurchaseHistory> listWithQuery = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, whereStatement);

        Iterator<DlbSwtStPurchaseHistory> iterator = listWithQuery.iterator();
        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory next = iterator.next();
            DlbSwtStTransaction transaction = (DlbSwtStTransaction) genericRepository.get(next.getTxnId(), DlbSwtStTransaction.class);
            Hibernate.initialize(next.getDlbSwtStWallet());
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
            if (transaction != null) {
                Hibernate.initialize(transaction.getDlbSwtMtPaymentMethod());
                next.setPaymentMethod(transaction.getDlbSwtMtPaymentMethod().getDescription());
                WhereStatement statement = new WhereStatement("token", transaction.getToken(), SystemVarList.EQUAL);
                DlbSwtStToken stToken = (DlbSwtStToken) genericRepository.get(DlbSwtStToken.class, statement);
                next.setReferenceNo(stToken.getMaskedNumber());

            }
        }

        return listWithQuery;
    }

//    /viewTransactionHistory
    @Transactional(rollbackFor = Exception.class)
    public List viewTransactionHistory(int id) {

        WhereStatement whereStatement = new WhereStatement("dlbSwtStWallet.id", id, SystemVarList.EQUAL);
        List<DlbSwtStTransaction> listWithQuery = genericRepository.listWithQuery(DlbSwtStTransaction.class, whereStatement);
        Iterator<DlbSwtStTransaction> iterator = listWithQuery.iterator();
        while (iterator.hasNext()) {
            DlbSwtStTransaction next = iterator.next();
            Hibernate.initialize(next.getDlbSwtMtTxnType());
            Hibernate.initialize(next.getDlbStatus());

        }
        return listWithQuery;
    }

}
