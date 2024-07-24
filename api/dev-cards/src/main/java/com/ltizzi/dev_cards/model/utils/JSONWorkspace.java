package com.ltizzi.dev_cards.model.utils;

import com.ltizzi.dev_cards.model.task.TaskDTO;

import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
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

    @Builder.Default
    private String created_at = Instant.now().toString();
    @Builder.Default
    private String downloaded_at = Instant.now().toString();
    @Builder.Default
    private String updated_at = Instant.now().toString();

}
