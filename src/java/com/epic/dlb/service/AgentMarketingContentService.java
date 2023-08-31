/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.AgentMarketingContentDto;
import com.epic.dlb.model.DlbWbMarketingContent;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("agentMarketingContentService")
public class AgentMarketingContentService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(AgentMarketingContentDto dto, String name) throws Exception {
        DlbWbMarketingContent dlbWbMarketingContent = new DlbWbMarketingContent();
        dlbWbMarketingContent.setName(dto.getName());
        dlbWbMarketingContent.setDescription(dto.getDescription());
        dlbWbMarketingContent.setUrl(dto.getUrl());
        dlbWbMarketingContent.setStatus("ACTIVE");
        dlbWbMarketingContent.setCreatedBy(name);
        dlbWbMarketingContent.setCreatedTime(new Date());
        dlbWbMarketingContent.setLastUpdatedTime(new Date());
        genericRepository.save(dlbWbMarketingContent);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbMarketingContent> getAll(AgentMarketingContentDto dto) throws Exception {
        List<DlbWbMarketingContent> dlbWbMarketingContent = genericRepository.list(DlbWbMarketingContent.class);
        return dlbWbMarketingContent;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbMarketingContent> getAllByName(AgentMarketingContentDto dto) throws Exception {
        WhereStatement whereStatement = new WhereStatement("name", dto.getName(), SystemVarList.EQUAL);
        List<DlbWbMarketingContent> dlbWbMarketingContent = genericRepository.list(DlbWbMarketingContent.class, whereStatement);
        return dlbWbMarketingContent;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbMarketingContent> getAllActive(AgentMarketingContentDto dto) throws Exception {
        WhereStatement whereStatement = new WhereStatement("status", "ACTIVE", SystemVarList.EQUAL);
        List<DlbWbMarketingContent> dlbWbMarketingContent = genericRepository.list(DlbWbMarketingContent.class, whereStatement);
        return dlbWbMarketingContent;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbMarketingContent getById(int id) throws Exception {
        DlbWbMarketingContent dlbWbMarketingContent = (DlbWbMarketingContent) genericRepository.get(id, DlbWbMarketingContent.class);
        return dlbWbMarketingContent;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(AgentMarketingContentDto dto) throws Exception {
        DlbWbMarketingContent dlbWbMarketingContent
                = (DlbWbMarketingContent) genericRepository.get(dto.getId(),
                        DlbWbMarketingContent.class);
        dlbWbMarketingContent.setName(dto.getName());
        dlbWbMarketingContent.setDescription(dto.getDescription());
        dlbWbMarketingContent.setUrl(dto.getUrl());
        dlbWbMarketingContent.setStatus(dto.getStatus());
        dlbWbMarketingContent.setLastUpdatedTime(new Date());
        genericRepository.update(dlbWbMarketingContent);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) throws Exception {
        DlbWbMarketingContent dlbWbMarketingContent = (DlbWbMarketingContent) genericRepository.get(id, DlbWbMarketingContent.class);
        genericRepository.delete(dlbWbMarketingContent);
    }
}
