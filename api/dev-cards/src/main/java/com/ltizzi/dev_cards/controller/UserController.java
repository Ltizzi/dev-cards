package com.ltizzi.dev_cards.controller;

import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.user.UserDTO;
import com.ltizzi.dev_cards.model.user.utils.LoginResponse;
import com.ltizzi.dev_cards.model.user.utils.UserLoginCredentials;
import com.ltizzi.dev_cards.model.user.utils.UserRegistration;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServ;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(defaultValue = "0")int page,
                                                  @RequestParam(defaultValue = ""+Integer.MAX_VALUE)int limit){
        return new ResponseEntity<>(userServ.getUsers(page,limit), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @ResponseBody
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long user_id) throws NotFoundException{
        return new ResponseEntity<>(userServ.getUserById(user_id), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO user) throws InvalidUserException {
        return new ResponseEntity<>(userServ.saveUser(user), HttpStatus.OK);
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<UserDTO> updateUser(@RequestParam Long user_id, @RequestBody UserDTO user) throws NotFoundException, InvalidUserException {
        return new ResponseEntity<>(userServ.updateUser(user_id, user), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<APIResponse> deleteUser(@RequestParam Long user_id) throws NotFoundException {
        return new ResponseEntity<>(userServ.deleteUser(user_id), HttpStatus.OK);
    }

    @GetMapping("/workspaces")
    @ResponseBody
    public ResponseEntity<List<WorkspaceDTO>> getWorkspacesFromUser(@RequestParam Long user_id ) throws NotFoundException {
        return new ResponseEntity<>(userServ.getWorkspacesByUserId(user_id), HttpStatus.OK);
    }

    @GetMapping("/createdTasks")
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getCreatedTasksFromUser(@RequestParam Long user_id) throws NotFoundException {
        return new ResponseEntity<>(userServ.getCreatedTasksByUserId(user_id), HttpStatus.OK);
    }

    @GetMapping("/designatedTasks")
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getDesignatedTasksFromUser(@RequestParam Long user_id) throws NotFoundException {
        return new ResponseEntity<>(userServ.getDesignatedTasksByUserId(user_id), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<LoginResponse> registerUser(@RequestBody UserRegistration registrationReq) throws InvalidUserException {
        return new ResponseEntity<>(userServ.registerUser(registrationReq), HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserLoginCredentials credentials) throws InvalidUserException {
        return new ResponseEntity<>(userServ.loginUser(credentials), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    @ResponseBody
    public ResponseEntity<LoginResponse> refreshJwt(@RequestHeader("Authorization")String token) throws NotFoundException {
        return new ResponseEntity<>(userServ.updateToken(token), HttpStatus.OK);
    }
}
