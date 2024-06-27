package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;

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

    private WorkspaceLiteDTO project;

    private List<TaskLiteDTO> dependencies = new ArrayList<>();
    private List<String> task_tags = new ArrayList<>();

    private List<TaskUpdate> updates = new ArrayList<>();

    private UserLiteDTO blocked_by;
    private UserLiteDTO owner;
    private List<UserLiteDTO> designated_to = new ArrayList<>();

    private Timestamp created_at;
    private Timestamp updated_at;
}
