/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.repository.GenericRepository;
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
@Service("employeeService")
public class EmployeeService {

    @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(DlbWbEmployee employee) throws Exception {
        return genericRepository.save(employee);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbEmployee employee) throws Exception {
        return (int) genericRepository.update(employee);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbEmployee get(String id) throws Exception {
        return (DlbWbEmployee) genericRepository.get(id, DlbWbEmployee.class);
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbWbEmployee.class);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbEmployee employee = (DlbWbEmployee) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(employee.getDlbStatus());
            list.add(employee);

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbWbEmployee.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbEmployee employee = (DlbWbEmployee) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(employee.getDlbStatus());
            list.add(employee);

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbEmployee.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbEmployee employee = (DlbWbEmployee) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(employee.getDlbStatus());
            list.add(employee);

        }
        return list;
    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbEmployee employee) throws Exception {
        return genericRepository.delete(employee);
    }

}
