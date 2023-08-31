/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbPromotion;
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
@Service("promotionService")
public class PromotionService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public int save(DlbWbPromotion promotion) throws Exception {

        //save to db
        return (int) genericRepository.save(promotion);
    }
    @Transactional
    public int update(DlbWbPromotion promotion) throws Exception {

        //save to db
        return (int) genericRepository.update(promotion);
    }

    @Transactional
    public List listAll() {
        List list = genericRepository.list(DlbWbPromotion.class);
        Iterator<DlbWbPromotion> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbPromotion next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());
        }

        return list;

    }

    @Transactional
    public List list(String description) {

        WhereStatement whereStatement = new WhereStatement("description", description, SystemVarList.LIKE);
        List list = genericRepository.list(DlbWbPromotion.class, whereStatement);
        Iterator<DlbWbPromotion> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbPromotion next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());
        }

        return list;

    }

    @Transactional
    public DlbWbPromotion get(int id) {
        DlbWbPromotion promotion = (DlbWbPromotion) genericRepository.get(id, DlbWbPromotion.class);
        if (promotion != null) {
            Hibernate.initialize(promotion.getDlbStatus());
        }

        return promotion;

    }
    
    @Transactional
    public int delete(DlbWbPromotion dlbWbPromotion) throws Exception {
       
      
        int delete = genericRepository.delete(dlbWbPromotion);

        return delete;

    }

}
