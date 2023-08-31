/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbSwtPurSchedule;
import com.epic.dlb.model.DlbSwtStWallet;
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
 * @author kasun_n
 */
@Service("purchaseScheduleService")
public class PurchaseScheduleService {
    
     @Autowired
    private GenericRepository genericRepository;
    
      //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAllByWalletId(Integer walletId) throws Exception {
        
        DlbSwtStWallet dlbSwtStWallet = new DlbSwtStWallet();
        WhereStatement whereStatement=new WhereStatement("dlbSwtStWallet.id", walletId, SystemVarList.EQUAL);
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbSwtPurSchedule.class,whereStatement);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbSwtPurSchedule dlbSwtPurSchedule  = (DlbSwtPurSchedule) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(dlbSwtPurSchedule.getDlbStatusBySchStatus());
            Hibernate.initialize(dlbSwtPurSchedule.getDlbStatusByStatus());
            Hibernate.initialize(dlbSwtPurSchedule.getDlbSwtMtPaymentMethod());
            Hibernate.initialize(dlbSwtPurSchedule.getDlbSwtStWallet());
            Hibernate.initialize(dlbSwtPurSchedule.getDlbWbProduct());
            
            list.add(dlbSwtPurSchedule);

        }
        return list;
    }
    
    
    
}
