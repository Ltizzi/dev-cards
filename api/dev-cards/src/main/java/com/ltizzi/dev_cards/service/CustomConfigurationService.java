package com.ltizzi.dev_cards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidConfigurationException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.customConfiguration.ConfigurationDTO;
import com.ltizzi.dev_cards.model.customConfiguration.utils.*;
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

    public TagPool addSpecialTag(Long config_id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException;

    public TagPool removeSpecialTag(Long config_id, Long id) throws  NotFoundException;

    public TagPool updateSpecialTag(Long config_id, Long id, SpecialTag tag) throws NotFoundException, InvalidConfigurationException;

    public TagPool addTagToPool(Long config_id, UITag tag) throws  NotFoundException, InvalidConfigurationException;

    public TagPool removeTagFromPool(Long config_id, UITag tag) throws  NotFoundException, InvalidConfigurationException;

    public TagPool updateTagFromPool(Long config_id, UITag tag) throws NotFoundException, InvalidConfigurationException;

    public TagPool saveTagPool(Long config_id, TagPool pool) throws  NotFoundException, InvalidConfigurationException;

    public ThemeData saveThemeData(Long config_id, ThemeData themeData) throws  NotFoundException, InvalidConfigurationException;

}
