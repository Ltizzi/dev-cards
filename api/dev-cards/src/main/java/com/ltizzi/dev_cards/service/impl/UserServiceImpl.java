package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.task.TaskMapper;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.model.user.utils.LoginResponse;
import com.ltizzi.dev_cards.model.user.utils.Role;
import com.ltizzi.dev_cards.model.user.utils.UserLoginCredentials;
import com.ltizzi.dev_cards.model.user.utils.UserRegistration;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import com.ltizzi.dev_cards.repository.UserRepository;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private WorkspaceMapper wsMapper;

    @Autowired
    private TaskMapper taskMapper;


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public List<UserDTO> getUsers(int page, int limit) {
        PageRequest pageReq = PageRequest.of(page, limit);
        Page<UserEntity> userPage = userRepo.findAll(pageReq);
        return userMapper.toArrayUserDTO(userPage.getContent());
    }

    @Override
    public UserDTO getUserById(Long id) throws NotFoundException {
        return userMapper.toUserDTO(userRepo.findById(id).orElseThrow(()-> new NotFoundException("User not found!")));
    }

    @Override
    public UserDTO saveUser(UserDTO user) throws InvalidUserException {
        return userMapper.toUserDTO(userRepo.save(userMapper.toUserEntity(user)));
    }

    @Override
    public LoginResponse registerUser(UserRegistration credentials) throws InvalidUserException {
        if(!userRepo.findByEmail(credentials.getEmail()).isEmpty()){
            throw new InvalidUserException("Email already in use");
        }
        if(!userRepo.findByUsername(credentials.getUsername()).isEmpty()){
            throw  new InvalidUserException("Username already in use");
        }
        UserEntity newUser = new UserEntity();
        String password = credentials.getPassword();
        password = passwordEncoder.encode(password);
        newUser.setPassword(password);
        newUser.setUsername(credentials.getUsername());
        newUser.setEmail(credentials.getEmail());
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);
        newUser.setRoles(roles);
        UserDTO registerUser =userMapper.toUserDTO(userRepo.save(newUser));
        String token = jwtUtils.generateToken(authManager.authenticate(
                new UsernamePasswordAuthenticationToken(newUser.getUsername(), credentials.getPassword())),
                credentials.getUsername());
        return LoginResponse.builder()
                .user(registerUser)
                .token(token)
                .build();
        //return userMapper.toUserDTO(userRepo.save(newUser));


    }

    @Override
    public LoginResponse loginUser(UserLoginCredentials credentials) throws InvalidUserException {


            UserEntity user = userRepo.findByUsername(credentials.getUsername()).get(0);
            if(user == null){
                throw new InvalidUserException("Invalid username");
            }
            String token = jwtUtils.generateToken(authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())),
                    credentials.getUsername());
            return LoginResponse.builder()
                    .user(userMapper.toUserDTO(user))
                    .token(token)
                    .build();


//        UserEntity user = userRepo.findByUsername(credentials.getUsername()).get(0);
//        if(user != null){
//            Authentication auth = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
//            if(auth.isAuthenticated()){
//                return userMapper.toUserDTO(user);
//            }
//            else throw  new InvalidUserException("Invalid credentials.");
//        }
//        else throw new InvalidUserException("Invalid credentials, user is null");
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

    @Override
    public List<WorkspaceDTO> getWorkspacesByUserId(Long user_id) throws NotFoundException {
        //List<WorkspaceEntity> workspaces = userRepo.findWorkspacesByUserId(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        List<WorkspaceEntity> workspaces = user.getWorkspaces();
        return wsMapper.toArrayWorkSpaceDTO(workspaces);
    }

    @Override
    public List<TaskDTO> getCreatedTasksByUserId(Long user_id) throws NotFoundException {
        //List<TaskEntity> tasks = userRepo.findCreatedTasksByUserId(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        List<TaskEntity> tasks = user.getCreated_tasks();
        return taskMapper.toArrayTaskDTO(tasks);
    }

    @Override
    public List<TaskDTO> getDesignatedTasksByUserId(Long user_id) throws NotFoundException {
        //List<TaskEntity> tasks = userRepo.findDesignatedTasks(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found!"));
        List<TaskEntity> tasks = user.getDesignated_tasks();
        return taskMapper.toArrayTaskDTO(tasks);
    }
}
