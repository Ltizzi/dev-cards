package com.ltizzi.dev_cards.model.user;

import com.ltizzi.dev_cards.model.task.TaskMapper;
import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import com.ltizzi.dev_cards.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Component
public class UserMapper {

    @Autowired
    @Lazy
    private TaskMapper taskMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    @Lazy
    private WorkspaceMapper wsMapper;

    public UserDTO toUserDTO(UserEntity user){
        UserDTO dto = new UserDTO();
        dto.setUser_id(user.getUser_id());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setAbout(user.getAbout());
        dto.setGithubProfile(user.getGithubProfile());
        dto.setWorkspaces(wsMapper.toArrayWorkspaceLiteDTO(user.getWorkspaces()));
        dto.setCreated_tasks(taskMapper.toArrayTaskLiteDTO(user.getCreated_tasks()));
        dto.setDesignated_tasks(taskMapper.toArrayTaskLiteDTO(user.getDesignated_tasks()));
        dto.setCreated_at(user.getCreated_at());
        dto.setUpdated_at(user.getUpdated_at());
        return dto;
    }

    public List<UserDTO> toArrayUserDTO(List<UserEntity> users){
        List<UserDTO> dtos = new ArrayList<>();
        for(UserEntity user: users){
            dtos.add(toUserDTO(user));
        }
        return dtos;
    }

    public UserEntity toUserEntity(UserDTO dto) {
        UserEntity user = new UserEntity();
        if(dto.getUser_id() != null) {
            user = userRepo.findById(dto.getUser_id()).orElse(null);
            if(user != null){
                user.setUser_id(dto.getUser_id());
            }
        }
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        user.setAbout(dto.getAbout());
        user.setGithubProfile(dto.getGithubProfile());
        user.setWorkspaces(wsMapper.toArrayWorkSpaceFromLite(dto.getWorkspaces()));
        user.setCreated_tasks(taskMapper.toArrayTaskEntityFromLiteDTO(dto.getCreated_tasks()));
        user.setDesignated_tasks(taskMapper.toArrayTaskEntityFromLiteDTO(dto.getDesignated_tasks()));
        user.setCreated_at(dto.getCreated_at());
        user.setUpdated_at(dto.getUpdated_at());
        return user;

    }

    public UserEntity toUserEntity(UserLiteDTO dto){
        UserEntity user = new UserEntity();
        if(dto.getUser_id()!= null){
            user = userRepo.findById(dto.getUser_id()).orElse(null);
            if(user != null){
                user.setUser_id(dto.getUser_id());
            }
        }
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        return user;
    }



    public List<UserEntity> toArrayUserEntity(List<UserDTO> dtos) {
        List<UserEntity> users = new ArrayList<>();
        for(UserDTO dto: dtos){
            users.add(toUserEntity(dto));
        }
        return users;
    }

    public List<UserEntity> toArrayUserEntityFromLite(List<UserLiteDTO> dtos) {
        List<UserEntity> users = new ArrayList<>();
        for(UserLiteDTO dto: dtos){
            users.add(toUserEntity(dto));
        }
        return users;
    }

    public UserLiteDTO toUserLiteDTO(UserEntity user) {
        UserLiteDTO dto = new UserLiteDTO();
        dto.setUser_id(user.getUser_id());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        return dto;
    }

    public List<UserLiteDTO> toArrayUserLiteDTO(List<UserEntity> users){
        List<UserLiteDTO> dtos = new ArrayList<>();
        for(UserEntity user: users){
            dtos.add(toUserLiteDTO(user));
        }
        return dtos;
    }


}
