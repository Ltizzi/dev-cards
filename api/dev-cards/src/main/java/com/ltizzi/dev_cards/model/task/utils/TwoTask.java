package com.ltizzi.dev_cards.model.task.utils;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.repository.TaskRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class TwoTask {
    private TaskEntity child;
    private TaskEntity parent;

    public TwoTask addTasks(UUID child_id, UUID parent_id, TaskRepository taskRepo) throws NotFoundException {
        this.child = taskRepo.findById(child_id).orElseThrow(()->new NotFoundException("Task not found"));
        this.parent = taskRepo.findById(parent_id).orElseThrow(()-> new NotFoundException("Task not found!"));

        return this;
    }

    public boolean sameProjectChecker(){
        return child.getWorkspace().equals(parent.getWorkspace());
    }

    public boolean dependencyChecker(){
        return child.getDependencies().contains(parent);
    }

}
