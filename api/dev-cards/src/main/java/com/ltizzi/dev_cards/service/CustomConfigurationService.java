package com.ltizzi.dev_cards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidConfigurationException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.customConfiguration.ConfigurationDTO;
import com.ltizzi.dev_cards.model.customConfiguration.CustomConfiguration;
import com.ltizzi.dev_cards.model.customConfiguration.utils.CustomGlosary;
import com.ltizzi.dev_cards.model.customConfiguration.utils.SpecialTag;
import com.ltizzi.dev_cards.model.utils.APIResponse;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface CustomConfigurationService {

    public ConfigurationDTO getConfigurationById(Long id) throws NotFoundException;

    public ConfigurationDTO getConfigurationByWorkspaceId(Long ws_id) throws  NotFoundException;

    public ConfigurationDTO saveConfiguration(ConfigurationDTO configuration) throws  InvalidConfigurationException;

    public ConfigurationDTO updateConfiguration(Long id, ConfigurationDTO configuration) throws NotFoundException, InvalidConfigurationException;

    public APIResponse removeConfigurationById(Long id) throws NotFoundException;

    public List<CustomGlosary> addCustomGlosary(Long config_id, CustomGlosary glosary) throws NotFoundException, InvalidConfigurationException, JsonProcessingException;

    public List<CustomGlosary> removeCustomGlosary(Long config_id, CustomGlosary glosary) throws NotFoundException;

    public List<CustomGlosary> updateGlosary(Long config_id, Long id, CustomGlosary glosary) throws NotFoundException, InvalidConfigurationException;

    public CustomConfiguration addSpecialTag(Long config_id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException;

    public CustomConfiguration removeSpecialTag(Long config_id, Long id) throws  NotFoundException;

    public CustomConfiguration updateSpecialTag(Long config_id, Long id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException;

}
