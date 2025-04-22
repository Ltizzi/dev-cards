package com.ltizzi.dev_cards.model.utils;

import com.ltizzi.dev_cards.model.customConfiguration.ConfigurationDTO;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JSONWorkspace {

    private UserLiteDTO user;
    private WorkspaceDTO workspace;
    private List<TaskDTO> tasks;
    private ConfigurationDTO customConfiguration;

    private boolean onlineExport;

    @Builder.Default
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date created_at = new Date();

    @Builder.Default

    private Date downloaded_at = new Date();

    @Builder.Default

    private Date updated_at = new Date();

}
