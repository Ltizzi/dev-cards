package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Component
public class TaskMapper {

    @Autowired
    private TaskRepository taskRepo;

    private TaskDTO toTaskDTO(TaskEntity task) {
        TaskDTO dto = new TaskDTO();
        dto.setTask_id(task.getTask_id());
        dto.setTitle(task.getTitle());
        dto.setSubtitle(task.getSubtitle());
        dto.setDescription(task.getDescription());
        dto.setColor(task.getColor());
        dto.setPriority(task.getPriority());
        dto.setEffort(task.getEffort());
        dto.setStatus(task.getStatus());
        dto.setProgress(task.getProgress());
        dto.setTask_type(task.getTask_type());

        dto.setProject(task.getProject());

        dto.setDependencies(toArrayTaskDTO(task.getDependencies()));
        dto.setTask_tags(task.getTask_tags());

        dto.setBlocked_by(task.getBlocked_by());
        dto.setOwner(task.getOwner());
        dto.setDesignated_to(task.getDesignated_to());
        dto.setCreated_at(task.getCreated_at());
        dto.setUpdated_at(task.getUpdated_at());

        return dto;
    }

    List<TaskDTO> toArrayTaskDTO(List<TaskEntity> tasks) {
        List<TaskDTO> dtos = new ArrayList<>();
        for(TaskEntity task: tasks){
            dtos.add(toTaskDTO(task));
        }
        return dtos;
    }

    TaskEntity toTaskEntity(TaskDTO dto) {
        TaskEntity task = new TaskEntity();

        if(dto.getTask_id() != null) {
            task = taskRepo.findById(dto.getTask_id()).orElse(null);
            if (task != null){
                task.setTask_id(dto.getTask_id());
            }
        }
        task.setTitle(dto.getTitle());
        task.setSubtitle(dto.getSubtitle());
        task.setDescription(dto.getDescription());
        task.setColor(dto.getColor());
        task.setPriority(dto.getPriority());
        task.setEffort(dto.getEffort());
        task.setStatus(dto.getStatus());
        task.setProgress(dto.getProgress());
        task.setTask_type(dto.getTask_type());

        task.setProject(dto.getProject());

        task.setDependencies(toArrayTaskEntity(dto.getDependencies()));

        task.setTask_tags(dto.getTask_tags());

        task.setBlocked_by(dto.getBlocked_by());
        task.setOwner(dto.getOwner());
        task.setDesignated_to(dto.getDesignated_to());
        task.setCreated_at(dto.getCreated_at());
        task.setUpdated_at(dto.getUpdated_at());

        return task;

    }

    List<TaskEntity> toArrayTaskEntity(List<TaskDTO> dtos) {
        List<TaskEntity> tasks = new ArrayList<>();
        for(TaskDTO dto: dtos){
            tasks.add(toTaskEntity(dto));
        }
        return tasks;
    }
}
