/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductList;
import com.epic.dlb.model.DlbWbProductListId;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbWeekDay;
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
@Service("productListService")
public class ProductListService {

    @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(DlbWbProductList productList) throws Exception {
        return (Object) genericRepository.save(productList);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbProductList productList) throws Exception {
        return (int) genericRepository.update(productList);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbProductList get(DlbWbProductListId id) throws Exception {

        DlbWbProduct dlbWbProduct = new DlbWbProduct(id.getProductCode(), null, null);
        DlbWbWeekDay dlbWbWeekDay = new DlbWbWeekDay(id.getDayCode(), null, null);

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct", dlbWbProduct, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbWbWeekDay", dlbWbWeekDay, SystemVarList.EQUAL);
        
        DlbWbProductList dlbWbProductList = (DlbWbProductList) genericRepository.get(DlbWbProductList.class, whereStatement1, whereStatement2);
        if(dlbWbProductList != null){
            Hibernate.initialize(dlbWbProductList.getDlbStatus());
        }
        return dlbWbProductList;
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();

        List tmpList = genericRepository.list(DlbWbProductList.class);
        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductList productList = (DlbWbProductList) tmpList.get(i);
            Hibernate.initialize(productList.getDlbStatus());
            Hibernate.initialize(productList.getDlbWbProduct());
            Hibernate.initialize(productList.getDlbWbWeekDay());
            Hibernate.initialize(productList.getDlbStatus());

            //add to list
            list.add(productList);

        }

        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        List list = new ArrayList();

        List tmpList = genericRepository.list(DlbWbProductList.class, whereStatements);
        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductList productList = (DlbWbProductList) tmpList.get(i);
            Hibernate.initialize(productList.getDlbStatus());
            Hibernate.initialize(productList.getDlbWbProduct());
            Hibernate.initialize(productList.getDlbWbWeekDay());
            Hibernate.initialize(productList.getDlbStatus());
            //add to list
            list.add(productList);

        }

        return list;
    }

    //search
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();

        List tmpList = genericRepository.search(DlbWbProductList.class, whereStatements);
        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductList productList = (DlbWbProductList) tmpList.get(i);
            Hibernate.initialize(productList.getDlbStatus());
            Hibernate.initialize(productList.getDlbWbProduct());
            Hibernate.initialize(productList.getDlbWbWeekDay());
            Hibernate.initialize(productList.getDlbStatus());
            //add to list
            list.add(productList);

        }

        return list;
    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbProductList productList) throws Exception {
        return genericRepository.delete(productList);
    }

    //load weekdays using product and day mapping
    @Transactional(rollbackFor = Exception.class)
    public List loadSelectBoxProductList(DlbWbProduct product, int status) throws Exception {
//        return genericRepository.loadSelectBox(DlbWbProduct.class,"productCode","description",status);
        List list = new ArrayList();
        DlbStatus dlbStatus = new DlbStatus(status, null, null);
        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct", product, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbStatus", dlbStatus, SystemVarList.EQUAL);

        List tmpList = genericRepository.loadSelectBox(DlbWbProductList.class, null, null, whereStatement1, whereStatement2);

        genericRepository.loadSelectBox(DlbWbProductList.class, null, null, whereStatement1, whereStatement2);
        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductList productList = (DlbWbProductList) tmpList.get(i);

            //init proxy objects
            Hibernate.initialize(productList.getDlbWbProduct());
            Hibernate.initialize(productList.getDlbWbWeekDay());
            list.add(productList);

        }

        return list;
    }

}
