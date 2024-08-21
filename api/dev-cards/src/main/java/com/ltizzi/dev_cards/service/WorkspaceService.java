package com.ltizzi.dev_cards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.InvalidWorkspaceException;
import com.ltizzi.dev_cards.exception.NotAllowedException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.WorkspaceDtoWithJwtResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;

import java.io.InputStream;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface WorkspaceService {

    public List<WorkspaceDTO> getWorkspaces(int page, int limit);

    public WorkspaceDTO getWorkspaceDTOById(Long id) throws NotFoundException;

    public WorkspaceDtoWithJwtResponse saveWorkspace(WorkspaceDTO workspace, String token) throws InvalidWorkspaceException, NotFoundException;

    public WorkspaceDTO updateWorkspace(Long workspace_id, WorkspaceDTO workspace) throws NotFoundException, InvalidWorkspaceException;

    public List<UserLiteDTO> addUserToWorkspace(Long workspace_id, Long user_id) throws  NotFoundException;

    public List<UserLiteDTO> addUserByEmail(Long workspace_id, String email) throws NotFoundException, InvalidUserException;

    public List<UserLiteDTO> removeUserFromWorkspace(Long workspace_id, Long user_id) throws  NotFoundException;

//    public boolean isUserInWorkspace(WorkspaceEntity ws, UserEntity user) throws  NotFoundException;

    public List<UserLiteDTO> addUserAsMod(Long workspace_id, Long user_id) throws NotFoundException, InvalidUserException;

    public List<UserLiteDTO> removeUserAsMod(Long workspace_id, Long user_id) throws NotFoundException, InvalidUserException;

    public APIResponse deleteWorkspace(Long workspace_id) throws  NotFoundException;

//    public List<TaskDTO> getTasksByWorkspace(Long workspace_id) throws NotFoundException;

//    public List<UserDTO> getUsersByWorkspace(Long workspace_id) throws  NotFoundException;

    public InputStream donwloadJSON(Long ws_id, Long user_id) throws NotFoundException, NotAllowedException, JsonProcessingException;

    public String getWorkspaceName(Long ws_id) throws NotFoundException;

    public WorkspaceDTO updateWorkspaceDescription(Long ws_id, String description) throws NotFoundException, NotAllowedException;

    public WorkspaceDTO updateWorkspaceAvatar(Long ws_id, String img_url) throws NotFoundException, NotAllowedException;


    public WorkspaceDTO updateWorkspaceName(Long ws_id, String name) throws NotFoundException, NotAllowedException;

}


