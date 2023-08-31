/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbProductProfileDetails;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("productProfileService")
public class ProductProfileService {

    @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(DlbWbProductProfile productList, HttpSession session) throws Exception {
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        productList.setLastUpdatedUser(systemUser.getUsername());
        int id = (int) genericRepository.save(productList);
        Iterator<DlbWbProductProfileDetails> items = productList.getDlbWbProductProfileDetailses().iterator();

        while (items.hasNext()) {
            DlbWbProductProfileDetails details = items.next();
            details.setLastUpdatedTime(new Date());
            details.setCreatedTime(new Date());
            details.setLastUpdatedUser(systemUser.getUsername());
            details.setDlbWbProductProfile(productList);

            genericRepository.save(details);

        }
        return genericRepository.save(productList);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbProductProfile productProfile, HttpSession session) throws Exception {
        DlbWbSystemUser systemUser = (DlbWbSystemUser) session.getAttribute(SystemVarList.USER);
        productProfile.setLastUpdatedUser(systemUser.getUsername());
        int id = (int) genericRepository.update(productProfile);
        Iterator<DlbWbProductProfileDetails> items = productProfile.getDlbWbProductProfileDetailses().iterator();

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProductProfile.id", productProfile.getId(), SystemVarList.EQUAL);
        genericRepository.deleteRecords(DlbWbProductProfileDetails.class, whereStatement1);
        while (items.hasNext()) {
            DlbWbProductProfileDetails details = items.next();
            details.setLastUpdatedTime(new Date());
            details.setCreatedTime(new Date());
            details.setLastUpdatedUser(systemUser.getUsername());
            details.setDlbWbProductProfile(productProfile);

            genericRepository.save(details);

        }
        return (int) genericRepository.update(productProfile);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbProductProfile get(int id) throws Exception {
        DlbWbProductProfile dlbWbProductProfile = (DlbWbProductProfile) genericRepository.get(id, DlbWbProductProfile.class);
        if (dlbWbProductProfile != null) {
            Hibernate.initialize(dlbWbProductProfile.getDlbWbProduct().getDescription());
            Hibernate.initialize(dlbWbProductProfile.getDlbWbWeekDay().getDescription());
            Hibernate.initialize(dlbWbProductProfile.getDlbStatusByStatus().getDescription());
            Hibernate.initialize(dlbWbProductProfile.getDlbStatusBySpecialStatus().getDescription());
            Hibernate.initialize(dlbWbProductProfile.getDlbWbProductProfileDetailses());

            if (dlbWbProductProfile.getDlbWbGameProfile() != null) {
                Hibernate.initialize(dlbWbProductProfile.getDlbWbGameProfile());

                if (dlbWbProductProfile.getDlbWbGameProfile().getDlbWbGames() != null) {
                    Hibernate.initialize(dlbWbProductProfile.getDlbWbGameProfile().getDlbWbGames());
                }

            }

        }

        List<DlbWbProductProfileDetails> dlbWbProductProfileDetailses = dlbWbProductProfile.getDlbWbProductProfileDetailses();

        Iterator<DlbWbProductProfileDetails> iterator = dlbWbProductProfileDetailses.iterator();

        while (iterator.hasNext()) {
            DlbWbProductProfileDetails next = iterator.next();
            Hibernate.initialize(next.getDlbWbProductItem());

        }

        dlbWbProductProfile.setDlbWbProductProfileDetailses(dlbWbProductProfileDetailses);

        return dlbWbProductProfile;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean checkAlreadyExsits(DlbWbProductProfile buildedProfile) throws Exception {
        boolean status = false;

        WhereStatement statement1 = new WhereStatement("dlbWbProduct.productCode", buildedProfile.getDlbWbProduct().getProductCode(), SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("dlbWbWeekDay.dayCode", buildedProfile.getDlbWbWeekDay().getDayCode(), SystemVarList.EQUAL);

        DlbWbProductProfile dlbWbProductProfile = (DlbWbProductProfile) genericRepository.get(DlbWbProductProfile.class, statement1, statement2);
        if (dlbWbProductProfile != null) {
            Hibernate.initialize(dlbWbProductProfile.getDlbWbProduct().getDescription());
            Hibernate.initialize(dlbWbProductProfile.getDlbWbWeekDay().getDescription());

            List<DlbWbProductProfileDetails> dlbWbProductProfileDetailses = dlbWbProductProfile.getDlbWbProductProfileDetailses();

            Iterator<DlbWbProductProfileDetails> iterator = dlbWbProductProfileDetailses.iterator();

            while (iterator.hasNext()) {
                DlbWbProductProfileDetails next = iterator.next();
                Hibernate.initialize(next.getDlbWbProductItem());

            }

            if (dlbWbProductProfile.getDlbWbProduct().getProductCode() == null
                    ? buildedProfile.getDlbWbProduct().getProductCode() == null
                    : dlbWbProductProfile.getDlbWbProduct().getProductCode().equals(buildedProfile.getDlbWbProduct().getProductCode())) {
                if (dlbWbProductProfile.getDlbWbWeekDay().getDayCode() == null
                        ? buildedProfile.getDlbWbWeekDay().getDayCode() == null
                        : dlbWbProductProfile.getDlbWbWeekDay().getDayCode().equals(buildedProfile.getDlbWbWeekDay().getDayCode())) {
                    //compare sets
                    if (buildedProfile.getDlbWbProductProfileDetailses().size() == dlbWbProductProfileDetailses.size()) {

                        //create two lists
                        List<DlbWbProductProfileDetails> buildedList = new ArrayList<>();
                        List<DlbWbProductProfileDetails> exitsList = new ArrayList<>();

                        buildedList.addAll(buildedProfile.getDlbWbProductProfileDetailses());
                        exitsList.addAll(dlbWbProductProfile.getDlbWbProductProfileDetailses());

                        //sort two list
                        buildedList.sort((DlbWbProductProfileDetails d1, DlbWbProductProfileDetails d2)
                                -> d1.getDlbWbProductItem().getItemCode().compareTo(d1.getDlbWbProductItem().getItemCode()));

                        exitsList.sort((DlbWbProductProfileDetails d1, DlbWbProductProfileDetails d2)
                                -> d1.getDlbWbProductItem().getItemCode().compareTo(d1.getDlbWbProductItem().getItemCode()));

                        Iterator<DlbWbProductProfileDetails> iterator1 = buildedList.iterator();
                        Iterator<DlbWbProductProfileDetails> iterator2 = exitsList.iterator();
                        int listSize = buildedList.size();

                        int matchCound = 0;

                        for (int i = 0; i < listSize; i++) {
                            DlbWbProductProfileDetails buildItem1 = buildedList.get(i);
                            DlbWbProductProfileDetails exitsItem = buildedList.get(i);

                            if (buildItem1.getDlbWbProductItem().getItemCode().equals(exitsItem.getDlbWbProductItem().getItemCode())) {
                                matchCound++;
                            }
                        }

                        if (listSize != 0 && matchCound != 0) {
                            if (listSize == matchCound) {
                                status = true;
                            }
                        }
                    }
                }
            }
        }

        return status;
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.list(DlbWbProductProfile.class, "id", SystemVarList.ASC);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductProfile productProfile = (DlbWbProductProfile) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
            Hibernate.initialize(productProfile.getDlbStatusByStatus());
            Hibernate.initialize(productProfile.getDlbStatusBySpecialStatus());

            list.add(productProfile);

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        List list = new ArrayList();
        return genericRepository.list(DlbWbProductProfile.class, "id", SystemVarList.ASC, whereStatements);
    }

    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbProductProfile.class, whereStatements, "id", SystemVarList.ASC);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductProfile productProfile = (DlbWbProductProfile) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
            Hibernate.initialize(productProfile.getDlbStatusByStatus());

            list.add(productProfile);

        }
        return list;

    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbProductProfile productProfile) throws Exception {
        DlbWbProductProfile get = (DlbWbProductProfile) genericRepository.get(productProfile.getId(), DlbWbProductProfile.class);
        Iterator<DlbWbProductProfileDetails> dlbWbProductProfileDetailses = get.getDlbWbProductProfileDetailses().iterator();

        while (dlbWbProductProfileDetailses.hasNext()) {
            DlbWbProductProfileDetails next = dlbWbProductProfileDetailses.next();
            genericRepository.delete(next);

        }

        return genericRepository.delete(get);
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listByProduct(DlbWbProduct product) throws Exception {
        List list = new ArrayList();
        DlbStatus dlbStatus = new DlbStatus(SystemVarList.ACTIVE, null, null);
        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct", product, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("dlbStatusByStatus", dlbStatus, SystemVarList.EQUAL);
        List tmpList = genericRepository.list(DlbWbProductProfile.class, "id", SystemVarList.ASC, whereStatement1, whereStatement2);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbProductProfile productProfile = (DlbWbProductProfile) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
//            Hibernate.initialize(productProfile.getDlbStatus());

            list.add(productProfile);

        }
        return list;
    }
    //list all method

    @Transactional(rollbackFor = Exception.class)
    public List listByProductProfile(int profileID) throws Exception {

        List list = new ArrayList();
        DlbStatus dlbStatus = new DlbStatus(SystemVarList.ACTIVE, null, null);
        DlbWbProductProfile productProfile = new DlbWbProductProfile();
        productProfile.setId(profileID);

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProductProfile", productProfile, SystemVarList.EQUAL);
//        List tmpList = genericRepository.list(DlbWbProductProfileDetails.class, "itemOrder", SystemVarList.ASC, whereStatement1);

        genericRepository
                .list(DlbWbProductProfileDetails.class, "itemOrder", SystemVarList.ASC, whereStatement1)
                .stream().sorted();

        List<DlbWbProductProfileDetails> itemList = genericRepository.list(DlbWbProductProfileDetails.class, "itemOrder", SystemVarList.ASC, whereStatement1);

        List sorted = itemList.stream().sorted(Comparator.comparingInt(DlbWbProductProfileDetails::getItemOrder))
                .collect(Collectors.toList());

        Iterator tmpList = sorted.iterator();
        while (tmpList.hasNext()) {
            DlbWbProductProfileDetails productProfileDetail = (DlbWbProductProfileDetails) tmpList.next();

            //initi proxy objects
            Hibernate.initialize(productProfileDetail.getDlbWbProductItem());
            Hibernate.initialize(productProfileDetail.getDlbWbWeekDay());

            list.add(productProfileDetail);

            System.out.println(productProfileDetail.getDlbWbProductItem().getDescription());

        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbProductProfile getByProductCodeAndDayCode(String productCode, String dayCode) throws Exception {
        WhereStatement statement1 = new WhereStatement("dlbWbProduct.productCode", productCode, SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("dlbWbWeekDay.dayCode", dayCode, SystemVarList.EQUAL);

        DlbWbProductProfile dlbWbProductProfile = (DlbWbProductProfile) genericRepository.get(DlbWbProductProfile.class, statement1, statement2);
        return dlbWbProductProfile;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbProductProfile getValidByProductCodeAndDayCode(String productCode, String dayCode) throws Exception {
        WhereStatement statement1 = new WhereStatement("dlbWbProduct.productCode", productCode, SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("dlbWbWeekDay.dayCode", dayCode, SystemVarList.EQUAL);
        WhereStatement statement3 = new WhereStatement("dlbStatusByStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
        DlbWbProductProfile dlbWbProductProfile = (DlbWbProductProfile) genericRepository.get(DlbWbProductProfile.class, statement1, statement2, statement3);
        return dlbWbProductProfile;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbProductProfile getValidByProductCodeAndDayCodeAndSpacialCode(String productCode, String dayCode, int statusCode) throws Exception {
        WhereStatement statement1 = new WhereStatement("dlbWbProduct.productCode", productCode, SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("dlbWbWeekDay.dayCode", dayCode, SystemVarList.EQUAL);
        WhereStatement statement3 = new WhereStatement("dlbStatusBySpecialStatus.statusCode", statusCode, SystemVarList.EQUAL);
        WhereStatement statement4 = new WhereStatement("dlbStatusByStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
        DlbWbProductProfile dlbWbProductProfile = (DlbWbProductProfile) genericRepository.get(
                DlbWbProductProfile.class, statement1, statement2, statement3, statement4);
        return dlbWbProductProfile;
    }
}
