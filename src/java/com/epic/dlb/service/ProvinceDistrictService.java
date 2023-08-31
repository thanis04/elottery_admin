/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbDistrict;
import com.epic.dlb.model.DlbWbProvince;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("provinceDistrictService")
public class ProvinceDistrictService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbProvince> getProvinces() {
        return genericRepository.list(DlbWbProvince.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getBuildProvinces() {
        JSONArray jSONArray = new JSONArray();
        List<DlbWbProvince> dlbWbProvinces = getProvinces();
        dlbWbProvinces.forEach(list -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", list.getId());
            jSONObject.put("name", list.getName());
            jSONArray.add(jSONObject);
        });
        System.out.println(jSONArray.toJSONString());
        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbDistrict> getDistrictsByProvince(Integer id) {
        WhereStatement whereStatement1 = new WhereStatement("dlbWbProvince.id", id, SystemVarList.EQUAL);
        return genericRepository.list(DlbWbDistrict.class, whereStatement1);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getBuildDistrictsByProvince(Integer id) {
        JSONArray jSONArray = new JSONArray();
        List<DlbWbDistrict> dlbWbDistricts = getDistrictsByProvince(id);
        dlbWbDistricts.forEach(list -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", list.getId());
            jSONObject.put("name", list.getName());
            jSONArray.add(jSONObject);
        });

        return jSONArray;
    }
}
