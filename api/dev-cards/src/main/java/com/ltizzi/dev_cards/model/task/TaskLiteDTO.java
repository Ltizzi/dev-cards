package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Data
@NoArgsConstructor
public class TaskLiteDTO {

    private Long task_id;
    private String title;
    private String subtitle;
    private Color color;
    private PriorityEnum priority;
    private Status status;
    private ProgressEnum progress;
    private TaskType task_type;
    private WorkspaceLiteDTO project;
    private List<String> task_tags = new ArrayList<>();
    private UserLiteDTO owner;
}
