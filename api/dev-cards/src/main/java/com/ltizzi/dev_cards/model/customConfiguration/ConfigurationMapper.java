package com.ltizzi.dev_cards.model.customConfiguration;

import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leonardo Terlizzi
 */
@Component
public class ConfigurationMapper {

    @Autowired
    private WorkspaceMapper wsMapper;

    public ConfigurationDTO toConfigDTO(CustomConfiguration configuration){
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setConfig_id(configuration.getConfig_id());
        dto.setWorkspace(wsMapper.toWorkspaceLiteDTO(configuration.getWorkspace()));
        dto.setCustomGlosaries(configuration.getCustomGlosaries());
        dto.setFlagged_tasks(configuration.getFlagged_tasks());
        dto.setTagPool(configuration.getTagPool());
        return dto;
    }

    public CustomConfiguration toConfigEntity(ConfigurationDTO dto){
        CustomConfiguration configuration = new CustomConfiguration();
        configuration.setConfig_id(dto.getConfig_id());
        configuration.setWorkspace(wsMapper.toWorkSpaceEntity(dto.getWorkspace()));
        configuration.setTagPool(dto.getTagPool());
        configuration.setFlagged_tasks(dto.getFlagged_tasks());
        configuration.setCustomGlosaries(dto.getCustomGlosaries());
        return configuration;
    }
}
