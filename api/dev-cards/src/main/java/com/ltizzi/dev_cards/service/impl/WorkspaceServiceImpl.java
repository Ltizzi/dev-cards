package com.ltizzi.dev_cards.service.impl;


import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.InvalidWorkspaceException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.task.TaskMapper;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import com.ltizzi.dev_cards.repository.UserRepository;
import com.ltizzi.dev_cards.repository.WorkspaceRepository;
import com.ltizzi.dev_cards.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository wsRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WorkspaceMapper wsMapper;

//    @Autowired
//    private TaskMapper taskMapper;
//
//    @Autowired
//    private UserMapper userMapper;

    @Override
    public List<WorkspaceDTO> getWorkspaces(int page, int limit) {
        PageRequest pageReq = PageRequest.of(page, limit);
        Page<WorkspaceEntity> wsPage = wsRepo.findAll(pageReq);
        return wsMapper.toArrayWorkSpaceDTO(wsPage.getContent());
    }

    @Override
    public WorkspaceDTO getWorkspaceDTOById(Long id) throws NotFoundException {
        return wsMapper.toWorkspaceDTO(wsRepo.findById(id).orElseThrow(()-> new NotFoundException("Workspace not Found")));
    }

    @Override
    public WorkspaceDTO saveWorkspace(WorkspaceDTO workspace) throws InvalidWorkspaceException, NotFoundException {
        WorkspaceEntity ws = wsMapper.toWorkspaceEntity(workspace);
        UserEntity user = userRepo.findById(ws.getOwner().getUser_id()).orElseThrow(()->new NotFoundException("Owner not found!"));
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        ws.setUsers(users);
        return wsMapper.toWorkspaceDTO(wsRepo.save(ws));
    }

    @Override
    public WorkspaceDTO updateWorkspace(Long workspace_id, WorkspaceDTO workspace) throws NotFoundException, InvalidWorkspaceException {
        WorkspaceDTO old_ws = getWorkspaceDTOById(workspace_id);
        if(old_ws !=null && old_ws.getWorkspace_id().equals(workspace.getWorkspace_id())){
            return wsMapper.toWorkspaceDTO(wsRepo.save(wsMapper.toWorkspaceEntity(workspace)));
        }
        else throw  new InvalidWorkspaceException("Something went wrong");
    }

    @Override
    public List<UserLiteDTO> addUserToWorkspace(Long workspace_id, Long user_id) throws NotFoundException {
        WorkspaceEntity ws = wsRepo.findById(workspace_id).orElseThrow(()->new NotFoundException("Workspace not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        ws.addUser(user);
        ws = wsRepo.save(ws);
        return wsMapper.toWorkspaceDTO(ws).getUsers();

    }



    @Override
    public List<UserLiteDTO> removeUserFromWorkspace(Long workspace_id, Long user_id) throws NotFoundException {
        WorkspaceEntity ws = wsRepo.findById(workspace_id).orElseThrow(()-> new NotFoundException("Workspace not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        ws.removeUser(user);;
        ws = wsRepo.save(ws);
        return wsMapper.toWorkspaceDTO(ws).getUsers();
    }




    @Override
    public List<UserLiteDTO> addUserAsMod(Long workspace_id, Long user_id) throws NotFoundException, InvalidUserException {
        WorkspaceEntity ws = wsRepo.findById(workspace_id).orElseThrow(()->new NotFoundException("Workspace not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()-> new NotFoundException("User not found!"));
        if (ws.getUsers().contains(user)){
            ws.addUserAsMod(user);
            ws = wsRepo.save(ws);
            return wsMapper.toWorkspaceDTO(ws).getModerators();
        }
        else throw new InvalidUserException("User can't be moderator because is not in current workspace");
    }

    @Override
    public List<UserLiteDTO> removeUserAsMod(Long workspace_id, Long user_id) throws NotFoundException, InvalidUserException {
        WorkspaceEntity ws = wsRepo.findById(workspace_id).orElseThrow(()->new NotFoundException("Workspace not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()-> new NotFoundException("User not found!"));
        if(ws.getModerators().contains(user)){
            ws.removeUserAsMod(user);
            ws = wsRepo.save(ws);
            return wsMapper.toWorkspaceDTO(ws).getModerators();
        }
        else throw  new InvalidUserException("User can't be moderator because is not in current workspace");
    }

    @Override
    public APIResponse deleteWorkspace(Long workspace_id) throws NotFoundException {
        APIResponse apiRes = new APIResponse();
        apiRes.setHttp_method("DELETE");
        WorkspaceDTO ws = getWorkspaceDTOById(workspace_id);
        if(ws!=null){
            wsRepo.deleteById(workspace_id);
            apiRes.setMessage("Workspace deleted!");
        }
        else{
            apiRes.setMessage("Workspace not found!");
        }
        return apiRes;
    }

//    @Override
//    public List<TaskDTO> getTasksByWorkspace(Long workspace_id) throws NotFoundException {
//        List<TaskEntity> tasks = wsRepo.findTasksByWorkspaceId(workspace_id).orElseThrow(()-> new NotFoundException("Workspace not found"));
//        return taskMapper.toArrayTaskDTO(tasks);
//    }
//
//    @Override
//    public List<UserDTO> getUsersByWorkspace(Long workspace_id) throws NotFoundException {
//        List<UserEntity> users = wsRepo.findUsersByWorkspaceId(workspace_id).orElseThrow(()-> new NotFoundException("Workspace not found"));
//        return userMapper.toArrayUserDTO(users);
//    }
}
