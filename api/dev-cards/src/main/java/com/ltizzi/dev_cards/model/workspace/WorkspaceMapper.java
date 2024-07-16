package com.ltizzi.dev_cards.model.workspace;

import com.ltizzi.dev_cards.model.task.TaskMapper;
import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Component
public class WorkspaceMapper {

    @Autowired
    @Lazy
    private UserMapper userMapper;

    @Autowired
    @Lazy
    private TaskMapper taskMapper;

    @Autowired
    private WorkspaceRepository workRepo;

    public WorkspaceDTO toWorkspaceDTO(WorkspaceEntity workspace){
        WorkspaceDTO dto = new WorkspaceDTO();
        dto.setWorkspace_id(workspace.getWorkspace_id());
        dto.setProject_name(workspace.getProject_name());
        dto.setDescription(workspace.getDescription());
        dto.setOwner(userMapper.toUserLiteDTO(workspace.getOwner()));
        dto.setTasks(taskMapper.toArrayTaskLiteDTO(workspace.getTasks()));
        dto.setModerators(userMapper.toArrayUserLiteDTO(workspace.getModerators()));
        dto.setUsers(userMapper.toArrayUserLiteDTO(workspace.getUsers()));
        dto.setCreated_at(workspace.getCreated_at());
        dto.setUpdated_at(workspace.getUpdated_at());
        dto.setProject_avatar(workspace.getProject_avatar());
        return dto;
    }

    public List<WorkspaceDTO> toArrayWorkSpaceDTO(List<WorkspaceEntity> workspaces) {
        List<WorkspaceDTO> dtos = new ArrayList<>();
        for(WorkspaceEntity workspace: workspaces){
            dtos.add(toWorkspaceDTO(workspace));
        }
        return dtos;
    }

    public WorkspaceEntity toWorkspaceEntity(WorkspaceDTO dto){
        WorkspaceEntity workspace = new WorkspaceEntity();
        if(dto.getWorkspace_id()!=null){
            workspace = workRepo.findById(dto.getWorkspace_id()).orElse(null);
            if (workspace != null){
                workspace.setWorkspace_id(dto.getWorkspace_id());
            }
        }
        workspace.setProject_name(dto.getProject_name());
        workspace.setDescription(dto.getDescription());
        workspace.setOwner(userMapper.toUserEntity(dto.getOwner()));
        workspace.setTasks(taskMapper.toArrayTaskEntityFromLiteDTO(dto.getTasks()));
        workspace.setModerators(userMapper.toArrayUserEntityFromLite(dto.getModerators()));
        workspace.setUsers(userMapper.toArrayUserEntityFromLite(dto.getUsers()));
        workspace.setCreated_at(dto.getCreated_at());
        workspace.setUpdated_at(dto.getUpdated_at());
        workspace.setProject_avatar(dto.getProject_avatar());
        return workspace;
    }

    public List<WorkspaceEntity> toArrayWorkspaceEntity(List<WorkspaceDTO> dtos){
        List<WorkspaceEntity> workspaces = new ArrayList<>();
        for(WorkspaceDTO dto: dtos){
            workspaces.add(toWorkspaceEntity(dto));
        }
        return workspaces;
    }

    public WorkspaceLiteDTO toWorkspaceLiteDTO(WorkspaceEntity workspace){
        WorkspaceLiteDTO dto = new WorkspaceLiteDTO();
        dto.setWorkspace_id(workspace.getWorkspace_id());
        dto.setOwner(userMapper.toUserLiteDTO(workspace.getOwner()));
        dto.setProject_name(workspace.getProject_name());
        dto.setProject_avatar(workspace.getProject_avatar());
        return dto;

    }

    public List<WorkspaceLiteDTO> toArrayWorkspaceLiteDTO(List<WorkspaceEntity> workspaces){
        List<WorkspaceLiteDTO> dtos = new ArrayList<>();
        for(WorkspaceEntity ws: workspaces){
            dtos.add(toWorkspaceLiteDTO(ws));
        }
        return dtos;
    }

    public WorkspaceEntity toWorkSpaceEntity(WorkspaceLiteDTO dto){
        WorkspaceEntity workspace = new WorkspaceEntity();
        if(dto.getWorkspace_id()!= null){
            workspace = workRepo.findById(dto.getWorkspace_id()).orElse(null);
        }
        workspace.setProject_name(dto.getProject_name());
        workspace.setOwner(userMapper.toUserEntity(dto.getOwner()));

        return workspace;
    }

    public List<WorkspaceEntity> toArrayWorkSpaceFromLite(List<WorkspaceLiteDTO> dtos){
        List<WorkspaceEntity> workspaces = new ArrayList<>();
        for(WorkspaceLiteDTO dto: dtos){
            workspaces.add(toWorkSpaceEntity(dto));
        }
        return workspaces;
    }
}
