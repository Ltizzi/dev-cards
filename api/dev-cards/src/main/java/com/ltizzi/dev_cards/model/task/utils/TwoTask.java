package com.ltizzi.dev_cards.model.task.utils;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.repository.TaskRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class TwoTask {
    private TaskEntity child;
    private TaskEntity parent;

    public TwoTask addTasks(Long child_id, Long parent_id, TaskRepository taskRepo) throws NotFoundException {
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
