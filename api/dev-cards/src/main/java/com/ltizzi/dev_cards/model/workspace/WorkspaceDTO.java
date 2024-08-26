package com.ltizzi.dev_cards.model.workspace;


import com.ltizzi.dev_cards.model.task.TaskLiteDTO;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class WorkspaceDTO {

    private Long workspace_id;
    private String project_name;
    private String project_avatar;
    private String description;
    private UserLiteDTO owner;
    private List<TaskLiteDTO> tasks = new ArrayList<>();
    private List<UserLiteDTO> moderators = new ArrayList<>();
    private List<UserLiteDTO> collaborators = new ArrayList<>();
    private List<UserLiteDTO> users = new ArrayList<>();
    private Timestamp created_at;
    private Timestamp updated_at;

}
