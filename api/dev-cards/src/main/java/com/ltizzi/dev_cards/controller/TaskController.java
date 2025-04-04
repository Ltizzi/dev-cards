package com.ltizzi.dev_cards.controller;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotAllowedException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.task.utils.ProgressItem;
import com.ltizzi.dev_cards.model.task.utils.TaskUpdate;
import com.ltizzi.dev_cards.model.task.utils.UpdateDescriptionRequest;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.UserWorkspacesRoles;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskServ;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(defaultValue = "0")int page,
                                                  @RequestParam(defaultValue = ""+Integer.MAX_VALUE)int limit){
        return new ResponseEntity<>(taskServ.getTasks(page, limit), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @ResponseBody
    public ResponseEntity<TaskDTO> getTaskById(@RequestParam Long id, @RequestHeader("Authorization") String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanAccessTask(token, id)){
            throw  new NotAllowedException("Can't access task");
        }
        else {
            return new ResponseEntity<>(taskServ.getTaskById(id), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<TaskDTO> saveTask(@RequestBody TaskDTO task, @RequestHeader("Authorization")String token) throws InvalidTaskException, NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsMember(task.getWorkspace().getWorkspace_id(), token)){
            throw new NotAllowedException("User is not a workspace's member");
        }else{
            return  new ResponseEntity<>(taskServ.saveTask(task), HttpStatus.OK);
        }

    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTask(@RequestParam Long id,
                                              @RequestBody TaskDTO task,
                                              @RequestHeader("Authorization")String token) throws InvalidTaskException, NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else{
            return new ResponseEntity<>(taskServ.updateTask(id, task), HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<APIResponse> deleteTask(@RequestParam Long id,
                                                  @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        TaskDTO task = taskServ.getTaskById(id);
        if(!jwtUtils.checkUserCanModifyTask(token, id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.deleteTask(id), HttpStatus.OK);
        }

    }

    @PostMapping("/add_dependency")
    @ResponseBody
    public ResponseEntity<TaskDTO> addDependencyToTask(@RequestParam Long task_id,
                                                       @RequestParam Long parent_id,
                                                       @RequestHeader("Authorization")String token) throws InvalidTaskException, NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.addDependency(task_id, parent_id), HttpStatus.OK);
        }
    }

    @PostMapping("/remove_dependency")
    @ResponseBody
    public ResponseEntity<TaskDTO> removeDependencyFromTask(@RequestParam Long task_id,
                                                            @RequestParam Long parent_id,
                                                            @RequestHeader("Authorization")String token) throws InvalidTaskException, NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.removeDependency(task_id, parent_id), HttpStatus.OK);
        }
    }

    @PostMapping("/assign")
    @ResponseBody
    public ResponseEntity<TaskDTO> assignUserToTask(@RequestParam Long task_id,
                                                    @RequestParam Long user_id,
                                                    @RequestHeader("Authorization")String token) throws InvalidTaskException, NotFoundException, InvalidUserException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
        return new ResponseEntity<>(taskServ.assignUser(task_id,user_id), HttpStatus.OK);}
    }

    @PostMapping("/autoAssign")
    @ResponseBody
    public ResponseEntity<TaskDTO> autoAssignTask(@RequestParam Long task_id,
                                                  @RequestParam Long ws_id,
                                                  @RequestHeader("Authorization")String token) throws NotAllowedException, InvalidTaskException, NotFoundException, InvalidUserException {
        if(!jwtUtils.checkIsCollaborator(ws_id, token) && !jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("Can't autoassign task");
        }
        return new ResponseEntity<>(taskServ.autoAssignTask(token, task_id), HttpStatus.OK);
    }

    @PostMapping("/unassign")
    @ResponseBody
    public ResponseEntity<TaskDTO> unassignUserFromTask(@RequestParam Long task_id,
                                                        @RequestParam Long user_id,
                                                        @RequestHeader("Authorization")String token) throws NotFoundException, InvalidUserException, InvalidTaskException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.unassignUser(task_id, user_id), HttpStatus.OK);
        }
    }

    @PatchMapping("/add_tag")
    @ResponseBody
    public ResponseEntity<TaskDTO> addTagToTask(@RequestParam Long task_id,
                                                @RequestParam String tag,
                                                @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.addTagToTask(task_id,tag), HttpStatus.OK);
        }
    }

    @PatchMapping("/remove_tag")
    @ResponseBody
    public ResponseEntity<TaskDTO> removeTagFromTask(@RequestParam Long task_id,
                                                     @RequestParam String tag,
                                                     @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.removeTagFromTask(task_id, tag), HttpStatus.OK);
        }
    }

    @PostMapping("/add_update")
    @ResponseBody
    public ResponseEntity<List<TaskUpdate>> addTaskUpdate(@RequestParam Long task_id,
                                                          @RequestBody TaskUpdate update
                                                          ) throws NotFoundException, NotAllowedException {
        //@RequestHeader("Authorization")String token
//        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
//            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
//        }
//        else {
            return new ResponseEntity<>(taskServ.addTaskUpdate(task_id, update), HttpStatus.OK);
        //}
    }

    @PatchMapping("/remove_update")
    @ResponseBody
    public ResponseEntity<List<TaskUpdate>> removeUpdateFromTask(@RequestParam Long task_id,
                                                                 @RequestParam  Long update_id,
                                                                 @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.removeUpdateFromTask(task_id,update_id), HttpStatus.OK);
        }
    }

    @PatchMapping("/update_tu")
    @ResponseBody
    public ResponseEntity<List<TaskUpdate>> updateTaskUpdate(@RequestParam Long task_id,
                                                             @RequestBody TaskUpdate task_update,
                                                             @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskUpdate(task_id, task_update), HttpStatus.OK);
        }
    }

    @GetMapping("/byWorkspace")
    @ResponseBody
    public  ResponseEntity<List<TaskDTO>> getTasksByWorkspace(@RequestParam Long id,
                                                              @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsMember(id, token)){
            throw  new NotAllowedException("Can't access workspace's tasks, user is not a member!");
        }
        else {
            return new ResponseEntity<>(taskServ.getTaskFromWorkspaceById(id), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateProgress")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskProgress(@RequestParam Long task_id,
                                                      @RequestParam String progress,
                                                      @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskProgress(task_id, progress), HttpStatus.OK);
        }
    }

    @PatchMapping("/updatePriority")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskPriority(@RequestParam Long task_id,
                                                      @RequestParam String priority,
                                                      @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskPriority(task_id, priority), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskStatus(@RequestParam Long task_id,
                                                    @RequestParam String status,
                                                    @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskStatus(task_id, status), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateEffort")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskEffort(@RequestParam Long task_id,
                                                    @RequestParam String effort,
                                                    @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskEffort(task_id, effort), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateType")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskType(@RequestParam Long task_id,
                                                  @RequestParam String type,
                                                  @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskType(task_id, type), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateTitle")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskTitle(@RequestParam Long task_id,
                                                   @RequestParam String title,
                                                   @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskTitle(task_id, title), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateSubtitle")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskSubtitle(@RequestParam Long task_id,
                                                      @RequestParam String subtitle,
                                                      @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskSubtitle(task_id, subtitle), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateDescription")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateDescription(@RequestParam Long task_id,
                                                     @RequestBody UpdateDescriptionRequest data,
                                                     @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateDescription(task_id, data.getDescription()), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateIssue")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTaskIssue(@RequestParam Long task_id,
                                                   @RequestBody ProgressItem issue,
                                                   @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.updateTaskIssue(task_id, issue), HttpStatus.OK);
        }
    }

    @PostMapping("/addIssue")
    @ResponseBody
    public ResponseEntity<TaskDTO> addIssueToTask(@RequestParam Long task_id,
                                                  @RequestBody ProgressItem issue,
                                                  @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.createTaskIssue(task_id, issue), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteIssue")
    @ResponseBody
    public ResponseEntity<TaskDTO> deleteTaskIssueById(@RequestParam Long task_id,
                                                       @RequestParam Long issue_id,
                                                       @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserCanModifyTask(token, task_id)){
            throw  new NotAllowedException("Can't access task, user is not moderator or is not assigned to task");
        }
        else {
            return new ResponseEntity<>(taskServ.deleteTaskIssue(task_id, issue_id), HttpStatus.OK);
        }
    }
}
