/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbPage;
import com.epic.dlb.model.DlbWbSection;
import com.epic.dlb.model.DlbWbUserPriviledge;
import com.epic.dlb.model.DlbWbUserPriviledgeId;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.Serializable;
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
@Service("userPrivilegeService")
public class UserPrivilegeService {

    @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(List<DlbWbUserPriviledge> userPriviledges) throws Exception {
        int res = 0;

        //delete exsting privilgaes for current use role
        if (userPriviledges.size() > 0) {
            String userrolecode = userPriviledges.get(0).getDlbWbUserRole().getUserrolecode();
            String sectionCode = userPriviledges.get(0).getDlbWbSection().getSectioncode();
            this.deleteExsitingRecords(userrolecode, sectionCode);
        }

        for (int i = 0; i < userPriviledges.size(); i++) {

            DlbWbUserPriviledge userPriviledge = userPriviledges.get(i);

            //delete existing record - if exsits
            String userRoleCode = userPriviledge.getDlbWbUserRole().getUserrolecode();
            String sectionCode = userPriviledge.getDlbWbSection().getSectioncode();
            String pageCode = userPriviledge.getDlbWbPage().getPagecode();
            String subSectionCode = userPriviledge.getDlbWbSubSection().getSubsectioncode();

            DlbWbUserPriviledgeId id = new DlbWbUserPriviledgeId(userRoleCode, sectionCode, pageCode, subSectionCode);
            userPriviledge.setId(id);
            //------------end of delete---------------
            //save
            genericRepository.save(userPriviledge);
            res = 1;

        }
        return res;
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbUserPriviledge get(String id) throws Exception {
        return (DlbWbUserPriviledge) genericRepository.get(id, DlbWbUserPriviledge.class);
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        return genericRepository.list(DlbWbUserPriviledge.class);
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        return genericRepository.list(DlbWbUserPriviledge.class, whereStatements);
    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int deleteExsitingRecords(String userrolecode, String section) throws Exception {
        WhereStatement whereStatement1 = new WhereStatement("dlbWbSection.sectioncode", section, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbWbUserRole.userrolecode", userrolecode, SystemVarList.EQUAL);
        return genericRepository.deleteRecords(DlbWbUserPriviledge.class, whereStatement1, whereStatement2);
    }

    //list user privilages by user role
    @Transactional(rollbackFor = Exception.class)
    public List getUserPrivilages(DlbWbUserRole userRole) {
        List tmpPrivilages = new ArrayList();
        List privilages = new ArrayList();
        WhereStatement whereStatement = new WhereStatement("dlbWbUserRole", userRole, SystemVarList.EQUAL);

        tmpPrivilages = genericRepository.list(DlbWbUserPriviledge.class, whereStatement);

        //initialize proxy objects
        for (int i = 0; i < tmpPrivilages.size(); i++) {
            DlbWbUserPriviledge priviledge = (DlbWbUserPriviledge) tmpPrivilages.get(i);

            //initialize pages
            Hibernate.initialize(priviledge.getDlbWbPage());
            privilages.add(priviledge);

        }

        return privilages;

    }
    //list user privilages by user role

    @Transactional(rollbackFor = Exception.class)
    public boolean hasUserPrivilage(DlbWbUserRole userRole, DlbWbPage page) {

        WhereStatement whereStatement1 = new WhereStatement("dlbWbUserRole", userRole, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbWbPage", page, SystemVarList.EQUAL);

        DlbWbUserPriviledge userPriviledge = (DlbWbUserPriviledge) genericRepository.get(DlbWbUserPriviledge.class, whereStatement1, whereStatement2);

        if (userPriviledge != null) {
            return true;
        }

        return false;

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean hasUserPrivilage(DlbWbUserRole userRole, String pageUrl) {

        //get user page by url
        WhereStatement whereUrl = new WhereStatement("url", pageUrl, SystemVarList.EQUAL);
        DlbWbPage page = (DlbWbPage) genericRepository.get(DlbWbPage.class, whereUrl);

        WhereStatement whereStatement1 = new WhereStatement("dlbWbUserRole", userRole, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbWbPage", page, SystemVarList.EQUAL);

        DlbWbUserPriviledge userPriviledge = (DlbWbUserPriviledge) genericRepository.get(DlbWbUserPriviledge.class, whereStatement1, whereStatement2);

        if (userPriviledge != null) {
            return true;
        }

        return false;

    }

    //list user privilages sections by user role 
    @Transactional(rollbackFor = Exception.class)
    public List getUserPrivilagedSections(DlbWbUserRole userRole) {
        List tmpPrivilages = new ArrayList();
        List privilages = new ArrayList();
        WhereStatement whereStatement = new WhereStatement("dlbWbUserRole", userRole, SystemVarList.EQUAL);

        tmpPrivilages = genericRepository.listWithGrouping(DlbWbUserPriviledge.class, "dlbWbSection", whereStatement);

        //initialize proxy objects
        for (int i = 0; i < tmpPrivilages.size(); i++) {
            DlbWbSection section = (DlbWbSection) tmpPrivilages.get(i);

            //initialize pages
            Hibernate.initialize(section.getDescription());
            privilages.add(section);

        }

        return privilages;

    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbUserPriviledge> getUserPrivilagesBySectionsAndUserRole(String section, String userRole) {
        WhereStatement whereStatement1 = new WhereStatement(
                "dlbWbUserRole.userrolecode", userRole, SystemVarList.EQUAL, SystemVarList.AND);

        WhereStatement whereStatement2 = new WhereStatement(
                "dlbWbSection.sectioncode", section, SystemVarList.EQUAL);

        List<DlbWbUserPriviledge> dlbWbUserPriviledges = genericRepository.
                list(DlbWbUserPriviledge.class, whereStatement1, whereStatement2);
        List<DlbWbUserPriviledge> privilageList = new ArrayList<>();
        
        for (int i = 0; i < dlbWbUserPriviledges.size(); i++) {
            DlbWbUserPriviledge priviledge = (DlbWbUserPriviledge) dlbWbUserPriviledges.get(i);

            //initialize pages
            Hibernate.initialize(priviledge.getDlbWbPage());
            privilageList.add(priviledge);

        }
        return privilageList;
    }
}
