package com.ltizzi.dev_cards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.InvalidWorkspaceException;
import com.ltizzi.dev_cards.exception.NotAllowedException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
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

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<WorkspaceDTO>> getWorkspaces(@RequestParam(defaultValue = "0")int page,
                                                            @RequestParam(defaultValue = ""+Integer.MAX_VALUE)int limit){
        return new ResponseEntity<>(wsServ.getWorkspaces(page, limit), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@RequestParam Long id) throws NotFoundException {
        return new ResponseEntity<>(wsServ.getWorkspaceDTOById(id), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> saveWorkspace(@RequestBody WorkspaceDTO workspace) throws InvalidWorkspaceException, NotFoundException {
        return new ResponseEntity<>(wsServ.saveWorkspace(workspace), HttpStatus.OK);
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<WorkspaceDTO> updateWorkspace(@RequestParam Long id, @RequestBody WorkspaceDTO workspace) throws InvalidWorkspaceException, NotFoundException {
        return new ResponseEntity<>(wsServ.updateWorkspace(id, workspace), HttpStatus.OK);
    }

    @PatchMapping("/add_user")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> addUserToWorkspace(@RequestParam Long ws_id, @RequestParam Long user_id) throws NotFoundException {
        return new ResponseEntity<>(wsServ.addUserToWorkspace(ws_id,user_id), HttpStatus.OK);
    }

    @PatchMapping("/remove_user")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> removeUserFromWorkspace(@RequestParam Long ws_id, @RequestParam Long user_id) throws NotFoundException {
        return new ResponseEntity<>(wsServ.removeUserFromWorkspace(ws_id, user_id), HttpStatus.OK);
    }

    @PatchMapping("/add_mod")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> addModToUser(@RequestParam Long ws_id, @RequestParam Long user_id) throws NotFoundException, InvalidUserException {
        return new ResponseEntity<>(wsServ.addUserAsMod(ws_id, user_id), HttpStatus.OK);
    }

    @PatchMapping("/remove_mod")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> removeUserAsMod(@RequestParam Long ws_id, @RequestParam Long user_id) throws NotFoundException, InvalidUserException {
        return new ResponseEntity<>(wsServ.removeUserAsMod(ws_id, user_id), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<APIResponse> deleteWorkspaceById(@RequestParam Long id) throws NotFoundException {
        return new ResponseEntity<>(wsServ.deleteWorkspace(id), HttpStatus.OK);
    }

    @PostMapping("/inviteByEmail")
    @ResponseBody
    public ResponseEntity<List<UserLiteDTO>> inviteUserByEmail(@RequestParam Long id, @RequestParam String email) throws NotFoundException {
        return new ResponseEntity<>(wsServ.addUserByEmail(id, email), HttpStatus.OK);
    }

//    @GetMapping("/tasks")
//    @ResponseBody
//    public ResponseEntity<List<TaskDTO>> getTasksByWorkspaceId(@RequestParam Long id) throws NotFoundException {
//        return new ResponseEntity<>(wsServ.getTasksByWorkspace(id), HttpStatus.OK);
//    }

    @GetMapping("/json")
    @ResponseBody
    public ResponseEntity<InputStreamResource> donwloadJson(@RequestParam Long ws_id, @RequestParam Long user_id) throws NotFoundException, NotAllowedException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String project_name = wsServ.getWorkspaceName(ws_id);
        String headerString = "attachment; filename=" + project_name +".json";
        headers.add("Content-Disposition", headerString );
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(wsServ.donwloadJSON(ws_id, user_id)));
    }

}
