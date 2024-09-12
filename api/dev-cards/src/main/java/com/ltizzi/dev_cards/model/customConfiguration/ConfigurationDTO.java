package com.ltizzi.dev_cards.model.customConfiguration;

import com.ltizzi.dev_cards.model.customConfiguration.utils.CustomGlosary;
import com.ltizzi.dev_cards.model.customConfiguration.utils.SpecialTag;
import com.ltizzi.dev_cards.model.customConfiguration.utils.TagPool;
import com.ltizzi.dev_cards.model.task.MiniTaskDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Data
@NoArgsConstructor
public class ConfigurationDTO {

    private Long config_id;
    private WorkspaceLiteDTO workspace;
    private List<CustomGlosary> customGlosaries;
    private TagPool tagPool;
    private List<MiniTaskDTO> flagged_tasks;


}
