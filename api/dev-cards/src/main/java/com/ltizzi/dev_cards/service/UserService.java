package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.user.utils.LoginResponse;
import com.ltizzi.dev_cards.model.user.utils.UserRegistration;
import com.ltizzi.dev_cards.model.user.utils.UserLoginCredentials;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface UserService {


    public List<UserDTO> getUsers(int page, int limit);

    public UserDTO getUserById(Long id) throws NotFoundException;

    public UserDTO saveUser(UserDTO user) throws InvalidUserException;

    public LoginResponse registerUser(UserRegistration credentials) throws  InvalidUserException;
    public LoginResponse loginUser(UserLoginCredentials credentials) throws  InvalidUserException;

    public UserDTO updateUser(Long user_id, UserDTO user) throws InvalidUserException, NotFoundException;

    public APIResponse deleteUser(Long user_id) throws NotFoundException;

    public List<WorkspaceDTO> getWorkspacesByUserId(Long user_id) throws NotFoundException;

    public List<TaskDTO> getCreatedTasksByUserId(Long user_id) throws NotFoundException;

    public List<TaskDTO> getDesignatedTasksByUserId(Long user_id) throws NotFoundException;

    public LoginResponse updateToken(String token) throws  NotFoundException;
}
