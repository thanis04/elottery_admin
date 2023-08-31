/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbPage;
import com.epic.dlb.model.DlbWbSection;
import com.epic.dlb.model.DlbWbSystemPriviledge;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("pageService")
public class PageService {

    @Autowired
    private GenericRepository genericRepository;

    //list all active pages
    @Transactional(rollbackFor = Exception.class)
    public List listActivePages() {

        //create where statement
        WhereStatement whereStatement = new WhereStatement("dlbStatus.statusCode",
                SystemVarList.ACTIVE, SystemVarList.EQUAL);
        return genericRepository.list(DlbWbPage.class, whereStatement);
    }

    //list all active page sections
    @Transactional(rollbackFor = Exception.class)
    public List listActivePageSections() {

        //create where statement
        WhereStatement whereStatement = new WhereStatement("dlbStatus.statusCode",
                SystemVarList.ACTIVE, SystemVarList.EQUAL);
        return genericRepository.list(DlbWbSection.class, whereStatement);
    }

    //list all active page sections
    @Transactional(rollbackFor = Exception.class)
    public List listActivePagesBySection(DlbWbSection section) {
        List<DlbWbSystemPriviledge> tmpList = new ArrayList();
        List list = new ArrayList();
        //create where statement
        WhereStatement whereStatement1 = new WhereStatement("dlbWbSection.sectioncode",
                section.getSectioncode(), SystemVarList.EQUAL);

        //list 
        tmpList = genericRepository.list(DlbWbSystemPriviledge.class, whereStatement1);

        //init proxy objects
        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbSystemPriviledge systemPriviledge = tmpList.get(i);
            Hibernate.initialize(systemPriviledge.getDlbWbPage());

            //add to list
            list.add(systemPriviledge);

        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getPageList(JSONArray pageListJSON) {
        JSONArray finalList = new JSONArray();
        pageListJSON.forEach(page -> {
            WhereStatement whereStatement = new WhereStatement("pagecode", page, SystemVarList.EQUAL);
            DlbWbPage dlbWbPage = (DlbWbPage) genericRepository.get(DlbWbPage.class, whereStatement);
            finalList.add(dlbWbPage.getDescription());
        });
        return finalList;
    }

    @Transactional(rollbackFor = Exception.class)
    public String getSection(String sectionCode) {
        WhereStatement whereStatement = new WhereStatement("sectioncode", sectionCode, SystemVarList.EQUAL);
        DlbWbSection dlbWbSection = (DlbWbSection) genericRepository.get(DlbWbSection.class, whereStatement);
        return dlbWbSection.getDescription();
    }

}
