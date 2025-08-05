package com.ltizzi.dev_cards.model.task;


import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */
@Data
@NoArgsConstructor
public class TaskLiteDTO {

    private UUID task_id;
    private String title;
    //private String subtitle;
    private Color color;
    private PriorityEnum priority;
    private Status status;
    private ProgressEnum progress;
    private TaskType task_type;
    private WorkspaceLiteDTO workspace;
    private List<String> task_tags = new ArrayList<>();
    private boolean hasUsers;
    //private UserLiteDTO owner;

    public boolean getHasUsers(){
        return this.hasUsers;
    }

    public void  setHasUsers(boolean condition){
        this.hasUsers = condition;
    }
}
