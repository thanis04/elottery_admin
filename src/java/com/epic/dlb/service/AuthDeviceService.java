/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbAuthorizedDevice;
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
@Service("authDeviceService")
public class AuthDeviceService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean checkIsAuthDevice(String macAddress) {

        WhereStatement whereStatement = new WhereStatement("macAddress", macAddress, SystemVarList.EQUAL);
        DlbWbAuthorizedDevice authorizedDevice = (DlbWbAuthorizedDevice) genericRepository.get(DlbWbAuthorizedDevice.class, whereStatement);

        return authorizedDevice != null;

    }

    @Transactional(rollbackFor = Exception.class)
    public int save(DlbWbAuthorizedDevice authorizedDevice) throws Exception {
        return (int) genericRepository.save(authorizedDevice);

    }

    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbAuthorizedDevice authorizedDevice) throws Exception {
        return (int) genericRepository.update(authorizedDevice);

    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbAuthorizedDevice get(int id) throws Exception {
        DlbWbAuthorizedDevice authorizedDevice = (DlbWbAuthorizedDevice) genericRepository.get(id, DlbWbAuthorizedDevice.class);
        if (authorizedDevice != null) {
            Hibernate.initialize(authorizedDevice.getDlbStatus());
        }
        return authorizedDevice;

    }
    
    
    @Transactional(rollbackFor = Exception.class)
    public DlbWbAuthorizedDevice get(String name,String mac) throws Exception {
        
        WhereStatement whereStatement1=new WhereStatement("description", name, SystemVarList.EQUAL);
        WhereStatement whereStatement2=new WhereStatement("macAddress", mac, SystemVarList.EQUAL);
        DlbWbAuthorizedDevice authorizedDevice = (DlbWbAuthorizedDevice) genericRepository.get(DlbWbAuthorizedDevice.class,whereStatement1,whereStatement2);
        
        if (authorizedDevice != null) {
            Hibernate.initialize(authorizedDevice.getDlbStatus());
        }
        return authorizedDevice;

    }
    @Transactional(rollbackFor = Exception.class)
    public DlbWbAuthorizedDevice get(String mac) throws Exception {
        WhereStatement whereStatement2=new WhereStatement("macAddress", mac, SystemVarList.EQUAL);
        DlbWbAuthorizedDevice authorizedDevice = (DlbWbAuthorizedDevice) genericRepository.get(DlbWbAuthorizedDevice.class,whereStatement2);
        
        if (authorizedDevice != null) {
            Hibernate.initialize(authorizedDevice.getDlbStatus());
        }
        return authorizedDevice;

    }

    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List<DlbWbAuthorizedDevice> list = genericRepository.list(DlbWbAuthorizedDevice.class);
        Iterator<DlbWbAuthorizedDevice> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbAuthorizedDevice next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());

        }
        return list;

    }

}
