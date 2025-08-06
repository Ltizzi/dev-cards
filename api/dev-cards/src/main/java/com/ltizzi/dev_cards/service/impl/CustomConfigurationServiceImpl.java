package com.ltizzi.dev_cards.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidConfigurationException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.customConfiguration.ConfigurationDTO;
import com.ltizzi.dev_cards.model.customConfiguration.ConfigurationMapper;
import com.ltizzi.dev_cards.model.customConfiguration.CustomConfiguration;
import com.ltizzi.dev_cards.model.customConfiguration.utils.*;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.repository.CustomConfigurationRepository;
import com.ltizzi.dev_cards.repository.WorkspaceRepository;
import com.ltizzi.dev_cards.service.CustomConfigurationService;
import com.ltizzi.dev_cards.service.WorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Slf4j
@Service
public class CustomConfigurationServiceImpl implements CustomConfigurationService {

    @Autowired
    private CustomConfigurationRepository configRepo;

    @Autowired
    private ConfigurationMapper configMapper;

    @Autowired
    private WorkspaceRepository wsRepo;

    @Override
    public ConfigurationDTO getConfigurationById(Long id) throws NotFoundException {
        log.info("Trying t fetch configuration id {}", id);
        return configMapper.toConfigDTO(configRepo.findById(id).orElseThrow(()->new NotFoundException("Configuration not found")));
    }

    private CustomConfiguration getConfigById(Long id) throws  NotFoundException{
        log.info("Trying t fetch configuration id {}", id);
        return configRepo.findById(id).orElseThrow(()->new NotFoundException("Configuration not found"));
    }

    @Override
    public ConfigurationDTO getConfigurationByWorkspaceId(Long ws_id) throws NotFoundException {
        log.info("Trying t fetch configuration from the workspace id {}", ws_id);
        if(configRepo.findConfigurationByWorkspaceId(ws_id) != null){
            return configMapper.toConfigDTO(configRepo.findConfigurationByWorkspaceId(ws_id));
        }
        else {
            log.info("Workspace hasn't configuration, adding one...");
            WorkspaceEntity ws = wsRepo.getReferenceById(ws_id);
            CustomConfiguration cg = new CustomConfiguration();
            cg.setWorkspace(ws);
            cg.setCustomGlosaries(new ArrayList<CustomGlosary>());
            cg.setTagPool(new TagPool());
            return configMapper.toConfigDTO(configRepo.save(cg));
        }

    }

    @Override
    public ConfigurationDTO saveConfiguration(ConfigurationDTO configuration) throws InvalidConfigurationException {
        log.info("Saving configuration...");
        return configMapper.toConfigDTO(configRepo.save(configMapper.toConfigEntity(configuration)));
    }

    @Override
    public ConfigurationDTO updateConfiguration(Long id, ConfigurationDTO configuration) throws NotFoundException, InvalidConfigurationException {
        log.info("Updating configuration with id {}", id);
        CustomConfiguration cg = getConfigById(id);
        if(cg.getConfig_id().equals(configuration.getConfig_id())){
            return configMapper.toConfigDTO(configRepo.save(configMapper.toConfigEntity(configuration)));
        }
        else {
            throw new InvalidConfigurationException("Something went wrong");
        }
    }

    @Override
    public APIResponse removeConfigurationById(Long id) throws NotFoundException {
        log.info("Trying to delete configuration by id {}", id);
        CustomConfiguration cg = getConfigById(id);
        APIResponse response = new APIResponse();
        response.setHttp_method("DELETE");
        if(!cg.equals(null)){
            configRepo.deleteById(id);
            response.setMessage("Success!");
        }
        else{
            log.error("Configuration mismatch");
            response.setMessage("Error!");
        }
        return response;
    }

    @Override
    public List<CustomGlosary> addCustomGlosary(Long config_id, CustomGlosary glosary) throws NotFoundException, InvalidConfigurationException, JsonProcessingException {
        log.info("Adding custom glosary to Custom Configuration id {}", config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.addGlosary(glosary);
        return configRepo.save(cg).getCustomGlosaries();
    }

    @Override
    public List<CustomGlosary> removeCustomGlosary(Long config_id, CustomGlosary glosary) throws NotFoundException {
        log.info("Removing custom glosary {} to Custom Configuration id {}", glosary.getId(), config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.removeGlosary(glosary);
        return configRepo.save(cg).getCustomGlosaries();
    }

    @Override
    public List<CustomGlosary> updateGlosary(Long config_id, Long id, CustomGlosary glosary) throws NotFoundException, InvalidConfigurationException {
        log.info("Updating custom glosary {} to Custom Configuration id {}", glosary.getId(), config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.updateGlosary(id, glosary);
        return configRepo.save(cg).getCustomGlosaries();
    }

    @Override
    public TagPool addSpecialTag(Long config_id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException {
        CustomConfiguration cg = getConfigById(config_id);
        cg.addSpecialTag(tag);
        return configRepo.save(cg).getTagPool();
    }

    @Override
    public TagPool removeSpecialTag(Long config_id, Long id) throws NotFoundException {
        log.info("Adding special tag to configuration id {}", config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.removeSpecialTag(id);
        return configRepo.save(cg).getTagPool();
    }

    @Override
    public TagPool updateSpecialTag(Long config_id, Long id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException {
        log.info("Updating special tag id {} to configuration id {}", tag.getId(), config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.updateSpecialTag(id, tag);
        return configRepo.save(cg).getTagPool();
    }

    @Override
    public TagPool addTagToPool(Long config_id, UITag tag) throws NotFoundException, InvalidConfigurationException {
        log.info("adding tag {} to configuration id {}", tag.getName(), config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.addTagToPool(tag);
        return configRepo.save(cg).getTagPool();
    }

    @Override
    public TagPool removeTagFromPool(Long config_id, UITag tag) throws NotFoundException, InvalidConfigurationException {
        log.info("Removing tag {} from configuration id {}", tag.getName(), config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.removeTagFromPool(tag);
        return configRepo.save(cg).getTagPool();
    }

    @Override
    public TagPool updateTagFromPool(Long config_id, UITag tag) throws NotFoundException, InvalidConfigurationException {
        log.info("Updating tag {} from configuration id {}", tag.getName(), config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.updateTagFromPool(tag);
        return configRepo.save(cg).getTagPool();
    }

    @Override
    public TagPool saveTagPool(Long config_id, TagPool pool) throws NotFoundException, InvalidConfigurationException {
        log.info("Saving Tag pool from configuration id {}", config_id);
        CustomConfiguration cg = getConfigById(config_id);
        cg.setTagPool(pool);
        cg = configRepo.save(cg);
        return cg.getTagPool();
    }


//    @Override
//    public ThemeData saveThemeData(Long config_id, ThemeData themeData) throws NotFoundException, InvalidConfigurationException {
//        CustomConfiguration cg = getConfigById(config_id);
//        cg.setThemeData(themeData);
//        return configRepo.save(cg).getThemeData();
//    }
}
