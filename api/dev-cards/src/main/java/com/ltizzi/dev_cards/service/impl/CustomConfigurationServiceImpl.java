package com.ltizzi.dev_cards.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidConfigurationException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.customConfiguration.CustomConfiguration;
import com.ltizzi.dev_cards.model.customConfiguration.utils.CustomGlosary;
import com.ltizzi.dev_cards.model.customConfiguration.utils.SpecialTag;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.repository.CustomConfigurationRepository;
import com.ltizzi.dev_cards.service.CustomConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Service
public class CustomConfigurationServiceImpl implements CustomConfigurationService {

    @Autowired
    private CustomConfigurationRepository configRepo;

    @Override
    public CustomConfiguration getConfigurationById(Long id) throws NotFoundException {
        return configRepo.findById(id).orElseThrow(()->new NotFoundException("Configuration not found"));
    }

    @Override
    public CustomConfiguration getConfigurationByWorkspaceId(Long ws_id) throws NotFoundException {
        return configRepo.findConfigurationByWorkspaceId(ws_id);
    }

    @Override
    public CustomConfiguration saveConfiguration(CustomConfiguration configuration) throws InvalidConfigurationException {
        return configRepo.save(configuration);
    }

    @Override
    public CustomConfiguration updateConfiguration(Long id, CustomConfiguration configuration) throws NotFoundException, InvalidConfigurationException {
        CustomConfiguration cg = getConfigurationById(id);
        if(cg.getConfig_id().equals(configuration.getConfig_id())){
            return configRepo.save(configuration);
        }
        else {
            throw new InvalidConfigurationException("Something went wrong");
        }
    }

    @Override
    public APIResponse removeConfigurationById(Long id) throws NotFoundException {
        CustomConfiguration cg = getConfigurationById(id);
        APIResponse response = new APIResponse();
        response.setHttp_method("DELETE");
        if(!cg.equals(null)){
            configRepo.deleteById(id);
            response.setMessage("Success!");
        }
        else{
            response.setMessage("Error!");
        }
        return response;
    }

    @Override
    public List<CustomGlosary> addCustomGlosary(Long config_id, CustomGlosary glosary) throws NotFoundException, InvalidConfigurationException, JsonProcessingException {
        CustomConfiguration cg = getConfigurationById(config_id);
        cg.addGlosary(glosary);
        return configRepo.save(cg).getCustomGlosaries();
    }

    @Override
    public List<CustomGlosary> removeCustomGlosary(Long config_id, CustomGlosary glosary) throws NotFoundException {
        CustomConfiguration cg = getConfigurationById(config_id);
        cg.removeGlosary(glosary);
        return configRepo.save(cg).getCustomGlosaries();
    }

    @Override
    public List<CustomGlosary> updateGlosary(Long config_id, Long id, CustomGlosary glosary) throws NotFoundException, InvalidConfigurationException {
        CustomConfiguration cg = getConfigurationById(config_id);
        cg.updateGlosary(id, glosary);
        return configRepo.save(cg).getCustomGlosaries();
    }

    @Override
    public CustomConfiguration addSpecialTag(Long config_id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException {
        CustomConfiguration cg = getConfigurationById(config_id);
        cg.addSpecialTag(tag);
        return configRepo.save(cg);
    }

    @Override
    public CustomConfiguration removeSpecialTag(Long config_id, Long id) throws NotFoundException {
        CustomConfiguration cg = getConfigurationById(config_id);
        cg.removeSpecialTag(id);
        return configRepo.save(cg);
    }

    @Override
    public CustomConfiguration updateSpecialTag(Long config_id, Long id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException {
        CustomConfiguration cg = getConfigurationById(config_id);
        cg.updateSpecialTag(id, tag);
        return configRepo.save(cg);
    }
}
