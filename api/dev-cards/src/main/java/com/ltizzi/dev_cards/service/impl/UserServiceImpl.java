package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.repository.UserRepository;
import com.ltizzi.dev_cards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDTO> getUsers(int page, int limit) {
        PageRequest pageReq = PageRequest.of(page, limit);
        Page<UserEntity> userPage = userRepo.findAll(pageReq);
        return userMapper.toArrayUserDTO(userPage.getContent());
    }

    @Override
    public UserDTO getUserById(Long id) throws NotFoundException {
        return userMapper.toUserDTO(userRepo.findById(id).orElseThrow());
    }

    @Override
    public UserDTO saveUser(UserDTO user) throws InvalidUserException {
        return userMapper.toUserDTO(userRepo.save(userMapper.toUserEntity(user)));
    }

    @Override
    public UserDTO updateUser(Long user_id, UserDTO user) throws InvalidUserException, NotFoundException {
        UserDTO old_user = getUserById(user_id);
        if(old_user != null && old_user.getUser_id().equals(user.getUser_id())){
            return userMapper.toUserDTO(userRepo.save(userMapper.toUserEntity(user)));
        }
        else throw  new InvalidUserException("Something went wrong");
    }

    @Override
    public APIResponse deleteUser(Long user_id) throws NotFoundException {
        APIResponse apiRes = new APIResponse();
        apiRes.setHttp_method("DELETE");
        UserDTO user = getUserById(user_id);
        if(user!=null){
            userRepo.deleteById(user_id);
            apiRes.setMessage("User deleted");
        }
        else {
            apiRes.setMessage("User not found!");
        }
        return apiRes;
    }
}
