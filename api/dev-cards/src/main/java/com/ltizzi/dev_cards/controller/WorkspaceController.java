package com.ltizzi.dev_cards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.InvalidWorkspaceException;
import com.ltizzi.dev_cards.exception.NotAllowedException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.utils.UpdateDescriptionRequest;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.WorkspaceDtoWithJwtResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService wsServ;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<WorkspaceDTO>> getWorkspaces(@RequestParam(defaultValue = "0")int page,
                                                            @RequestParam(defaultValue = ""+Integer.MAX_VALUE)int limit){
        return new ResponseEntity<>(wsServ.getWorkspaces(page, limit), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@RequestParam Long id, @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsMember(id, token)){
            throw new NotAllowedException("No puede acceder al workspace");
        }
        else {
            return new ResponseEntity<>(wsServ.getWorkspaceDTOById(id), HttpStatus.OK);
        }

    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<WorkspaceDtoWithJwtResponse> saveWorkspace(@RequestBody WorkspaceDTO workspace, @RequestHeader("Authorization")String token) throws InvalidWorkspaceException, NotFoundException {
        return new ResponseEntity<>(wsServ.saveWorkspace(workspace, token), HttpStatus.OK);
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> updateWorkspace(@RequestParam Long id,
                                                        @RequestBody WorkspaceDTO workspace,
                                                        @RequestHeader("Authorization")String token) throws InvalidWorkspaceException, NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsModerator(id, token) && !jwtUtils.checkIsOwner(id, token)){
            throw  new NotAllowedException("User can't modify workspace data");
        }
        else{
            return new ResponseEntity<>(wsServ.updateWorkspace(id, workspace), HttpStatus.OK);
        }

    }

    @PatchMapping("/add_user")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> addUserToWorkspace(@RequestParam Long ws_id,
                                                                @RequestParam Long user_id,
                                                                @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsModerator(ws_id, token) && !jwtUtils.checkIsOwner(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else{
            return new ResponseEntity<>(wsServ.addUserToWorkspace(ws_id,user_id), HttpStatus.OK);
        }

    }

    @PatchMapping("/remove_user")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> removeUserFromWorkspace(@RequestParam Long ws_id,
                                                                     @RequestParam Long user_id,
                                                                     @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsModerator(ws_id, token) && !jwtUtils.checkIsOwner(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.removeUserFromWorkspace(ws_id, user_id), HttpStatus.OK);
        }
    }

    @PatchMapping("/add_mod")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> addModToUser(@RequestParam Long ws_id,
                                                          @RequestParam Long user_id,
                                                          @RequestHeader("Authorization")String token) throws NotFoundException, InvalidUserException, NotAllowedException {
        if(!jwtUtils.checkIsOwner(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.addUserAsMod(ws_id, user_id), HttpStatus.OK);
        }
    }

    @PatchMapping("/remove_mod")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> removeUserAsMod(@RequestParam Long ws_id,
                                                             @RequestParam Long user_id,
                                                             @RequestHeader("Authorization")String token) throws NotFoundException, InvalidUserException, NotAllowedException {
        if(!jwtUtils.checkIsOwner(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.removeUserAsMod(ws_id, user_id), HttpStatus.OK);
        }
    }

    @PatchMapping("/addCollaborator")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> addUserAsCollaborator(@RequestParam Long ws_id,
                                                                   @RequestParam Long user_id,
                                                                   @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidUserException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.addUserAsCollaborator(ws_id,user_id), HttpStatus.OK);
        }
    }

    @PatchMapping("/removeCollaborator")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> removeUserAsCollaborator(@RequestParam Long ws_id,
                                                                      @RequestParam Long user_id,
                                                                      @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidUserException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.removeUserAsCollaborator(ws_id, user_id), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<APIResponse> deleteWorkspaceById(@RequestParam Long id, @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsOwner(id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.deleteWorkspace(id), HttpStatus.OK);
        }
    }

    @PostMapping("/inviteByEmail")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> inviteUserByEmail(@RequestParam Long ws_id,
                                                               @RequestParam String email,
                                                               @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException, InvalidUserException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            return new ResponseEntity<>(wsServ.addUserByEmail(ws_id, email), HttpStatus.OK);
        }
    }

//    @GetMapping("/tasks")
//    @ResponseBody
//    public ResponseEntity<List<TaskDTO>> getTasksByWorkspaceId(@RequestParam Long id) throws NotFoundException {
//        return new ResponseEntity<>(wsServ.getTasksByWorkspace(id), HttpStatus.OK);
//    }

    @GetMapping("/json")
    @ResponseBody
    public ResponseEntity<InputStreamResource> donwloadJson(@RequestParam Long ws_id,
                                                            @RequestParam Long user_id,
                                                            @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException, JsonProcessingException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace data");
        }
        else {
            HttpHeaders headers = new HttpHeaders();
            String project_name = wsServ.getWorkspaceName(ws_id);
            String headerString = "attachment; filename=" + project_name + ".json";
            headers.add("Content-Disposition", headerString);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(wsServ.donwloadJSON(ws_id, user_id)));
        }
    }

    @PatchMapping("/updateDescription")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> updateDescription(@RequestParam Long ws_id,
                                                          @RequestBody UpdateDescriptionRequest data,
                                                          @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User can't modify workspace description");
        }
        else{
            return new ResponseEntity<>(wsServ.updateWorkspaceDescription(ws_id, data.getDescription()), HttpStatus.OK);
                    }
    }

    @PatchMapping("/updateName")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> updateWorkspaceName(@RequestParam Long ws_id,
                                                            @RequestParam String name,
                                                            @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwner(ws_id,token)){
            throw  new NotAllowedException("User can't modify workspace's name");
        }
        else{
            return new ResponseEntity<>(wsServ.updateWorkspaceName(ws_id, name), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateAvatar")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> updateWorkspaceAvatar(@RequestParam Long ws_id,
                                                              @RequestParam String url,
                                                              @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modifiy workspace's avatar");
        }
        else{
            return new ResponseEntity<>(wsServ.updateWorkspaceAvatar(ws_id,url), HttpStatus.OK);
        }
    }

}
