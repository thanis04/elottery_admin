/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbDeviceProfile;
import com.epic.dlb.model.DlbWbRiskProfile;
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
@Service("riskProfileService")
public class RiskProfileService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public int save(DlbWbRiskProfile riskProfile) throws Exception {
        return (int) genericRepository.save(riskProfile);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbRiskProfile riskProfile) throws Exception {
        return (int) genericRepository.update(riskProfile);
    }

    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = genericRepository.list(DlbWbRiskProfile.class);
        Iterator<DlbWbRiskProfile> iterator = list.iterator();
        while (iterator.hasNext()) {
            DlbWbRiskProfile next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());
            Hibernate.initialize(next.getDlbDeviceProfile());
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public List listAllProfile() throws Exception {
        List list = genericRepository.list(DlbDeviceProfile.class);
        Iterator<DlbDeviceProfile> iterator = list.iterator();
        while (iterator.hasNext()) {
            DlbDeviceProfile next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public List listActive() throws Exception {
        WhereStatement statement = new WhereStatement("dlbStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
        List list = genericRepository.list(DlbWbRiskProfile.class, statement);
        Iterator<DlbWbRiskProfile> iterator = list.iterator();
        while (iterator.hasNext()) {
            DlbWbRiskProfile next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());
            Hibernate.initialize(next.getDlbDeviceProfile());
        }
        return list;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public DlbWbRiskProfile get(int id) throws Exception {
       
        DlbWbRiskProfile riskProfile = (DlbWbRiskProfile) genericRepository.get(id,DlbWbRiskProfile.class);
       
        if(riskProfile!=null){
            Hibernate.initialize(riskProfile.getDlbStatus());
            Hibernate.initialize(riskProfile.getDlbDeviceProfile());
        }
        
        return riskProfile;
    }
    @Transactional(rollbackFor = Exception.class)
    public DlbWbRiskProfile get(String description) throws Exception {
        
        WhereStatement statement=new WhereStatement("description", description, SystemVarList.EQUAL);       
        DlbWbRiskProfile riskProfile = (DlbWbRiskProfile) genericRepository.get(DlbWbRiskProfile.class, statement);
       
        if(riskProfile!=null){
            Hibernate.initialize(riskProfile.getDlbStatus());
            Hibernate.initialize(riskProfile.getDlbDeviceProfile());
        }
        
        return riskProfile;
    }
    
    

}
