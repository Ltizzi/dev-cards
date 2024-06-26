package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class TaskDTO {

    private Long task_id;
    private String title;
    private String subtitle;
    private String description;
    private Color color;
    private PriorityEnum priority;
    private EffortEnum effort;
    private Status status;
    private ProgressEnum progress;
    private TaskType task_type;

    private WorkspaceDTO project;

    private List<TaskDTO> dependencies;
    private List<String> task_tags;

    private UserDTO blocked_by;
    private UserDTO owner;
    private List<UserDTO> designated_to;

    private Timestamp created_at;
    private Timestamp updated_at;
}
