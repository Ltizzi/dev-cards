package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface UserService {


    public List<UserDTO> getUsers(int page, int limit);

    public UserDTO getUserById(Long id) throws NotFoundException;

    public UserDTO saveUser(UserDTO user) throws InvalidUserException;

    public UserDTO updateUser(Long user_id, UserDTO user) throws InvalidUserException, NotFoundException;

    public APIResponse deleteUser(Long user_id) throws NotFoundException;
}
