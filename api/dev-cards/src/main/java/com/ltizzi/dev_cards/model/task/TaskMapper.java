package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import com.ltizzi.dev_cards.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
    private UserMapper userMapper;

    @Autowired
    private WorkspaceMapper wsMapper;

    public TaskDTO toTaskDTO(TaskEntity task) {
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

        dto.setWorkspace(wsMapper.toWorkspaceLiteDTO(task.getWorkspace()));
        dto.setProgressItems(task.getProgressItems());
        dto.setDependencies(toArrayTaskLiteDTO(task.getDependencies()));
        dto.setTask_tags(task.getTask_tags());
        dto.setUpdates(task.getUpdates());
        if(task.getBlocked_by() != null){
            dto.setBlocked_by(userMapper.toUserLiteDTO(task.getBlocked_by()));

        }
        dto.setOwner(userMapper.toUserLiteDTO(task.getOwner()));
        dto.setDesignated_to(userMapper.toArrayUserLiteDTO(task.getDesignated_to()));
        dto.setCreated_at(task.getCreated_at());
        dto.setUpdated_at(task.getUpdated_at());

        return dto;
    }

    public List<TaskDTO> toArrayTaskDTO(List<TaskEntity> tasks) {
        List<TaskDTO> dtos = new ArrayList<>();
        for(TaskEntity task: tasks){
            dtos.add(toTaskDTO(task));
        }
        return dtos;
    }

    public TaskEntity toTaskEntity(TaskDTO dto) {
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

        task.setWorkspace(wsMapper.toWorkSpaceEntity(dto.getWorkspace()));

        task.setProgressItems(dto.getProgressItems());
        task.setDependencies(toArrayTaskEntityFromLiteDTO(dto.getDependencies()));

        task.setTask_tags(dto.getTask_tags());
        task.setUpdates(dto.getUpdates());
        if(dto.getBlocked_by()!= null) {
            task.setBlocked_by(userMapper.toUserEntity(dto.getBlocked_by()));
        }
        task.setOwner(userMapper.toUserEntity(dto.getOwner()));
        task.setDesignated_to(userMapper.toArrayUserEntityFromLite(dto.getDesignated_to()));
        task.setCreated_at(dto.getCreated_at());
        task.setUpdated_at(dto.getUpdated_at());

        return task;

    }

    public TaskEntity toTaskEntity(TaskLiteDTO dto){
        TaskEntity task = new TaskEntity();
        if(dto.getTask_id()!= null){
            task = taskRepo.findById(dto.getTask_id()).orElse(null);
        }
        task.setTitle(dto.getTitle());
        task.setSubtitle(dto.getSubtitle());
        task.setColor(dto.getColor());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setProgress(dto.getProgress());
        task.setTask_type(dto.getTask_type());
        task.setWorkspace(wsMapper.toWorkSpaceEntity(dto.getWorkspace()));
        task.setTask_tags(dto.getTask_tags());
        task.setOwner(userMapper.toUserEntity(dto.getOwner()));
        return task;
    }

    public List<TaskEntity> toArrayTaskEntity(List<TaskDTO> dtos) {
        List<TaskEntity> tasks = new ArrayList<>();
        for(TaskDTO dto: dtos){
            tasks.add(toTaskEntity(dto));
        }
        return tasks;
    }

    public List<TaskEntity> toArrayTaskEntityFromLiteDTO(List<TaskLiteDTO> dtos){
        List<TaskEntity> tasks = new ArrayList<>();
        for(TaskLiteDTO dto: dtos) {
            tasks.add(toTaskEntity(dto));
        }
        return tasks;
    }

    public TaskLiteDTO toTaskLiteDTO(TaskEntity task){
        TaskLiteDTO liteDTO = new TaskLiteDTO();
        liteDTO.setTask_id(task.getTask_id());
        liteDTO.setTitle(task.getTitle());
        liteDTO.setSubtitle(task.getSubtitle());
        liteDTO.setColor(task.getColor());
        liteDTO.setPriority(task.getPriority());
        liteDTO.setStatus(task.getStatus());
        liteDTO.setProgress(task.getProgress());
        liteDTO.setTask_type(task.getTask_type());
        liteDTO.setWorkspace(wsMapper.toWorkspaceLiteDTO(task.getWorkspace()));
        liteDTO.setTask_tags(task.getTask_tags());
        liteDTO.setOwner(userMapper.toUserLiteDTO(task.getOwner()));

        return liteDTO;
    }

    public List<TaskLiteDTO> toArrayTaskLiteDTO(List<TaskEntity> tasks) {
        List<TaskLiteDTO> dtos = new ArrayList<>();
        for(TaskEntity task:tasks){
            dtos.add(toTaskLiteDTO(task));
        }
        return dtos;
    }
}
