package com.ltizzi.dev_cards.controller;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.utils.TaskUpdate;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(defaultValue = "0")int page,
                                                  @RequestParam(defaultValue = ""+Integer.MAX_VALUE)int limit){
        return new ResponseEntity<>(taskServ.getTasks(page, limit), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @ResponseBody
    public ResponseEntity<TaskDTO> getTaskById(@RequestParam Long id) throws NotFoundException {
        return new ResponseEntity<>(taskServ.getTaskById(id), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<TaskDTO> saveTask(@RequestBody TaskDTO task) throws InvalidTaskException, NotFoundException {
        return  new ResponseEntity<>(taskServ.saveTask(task), HttpStatus.OK);
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTask(@RequestParam Long id, @RequestBody TaskDTO task) throws InvalidTaskException, NotFoundException {
        return new ResponseEntity<>(taskServ.updateTask(id, task), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<APIResponse> deleteTask(@RequestParam Long id) throws NotFoundException {
        return new ResponseEntity<>(taskServ.deleteTask(id), HttpStatus.OK);
    }

    @PostMapping("/add_dependency")
    @ResponseBody
    public ResponseEntity<TaskDTO> addDependencyToTask(@RequestParam Long task_id, @RequestParam Long parent_id) throws InvalidTaskException, NotFoundException {
        return new ResponseEntity<>(taskServ.addDependency(task_id, parent_id), HttpStatus.OK);
    }

    @PostMapping("/remove_dependency")
    @ResponseBody
    public ResponseEntity<TaskDTO> removeDependencyFromTask(@RequestParam Long task_id, @RequestParam Long parent_id) throws InvalidTaskException, NotFoundException {
        return new ResponseEntity<>(taskServ.removeDependency(task_id, parent_id), HttpStatus.OK);
    }

    @PostMapping("/assign")
    @ResponseBody
    public ResponseEntity<TaskDTO> assignUserToTask(@RequestParam Long task_id, @RequestParam Long user_id) throws InvalidTaskException, NotFoundException, InvalidUserException {
        return new ResponseEntity<>(taskServ.assignUser(task_id,user_id), HttpStatus.OK);
    }

    @PostMapping("/unassign")
    @ResponseBody
    public ResponseEntity<TaskDTO> unassignUserFromTask(@RequestParam Long task_id, @RequestParam Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException {
        return new ResponseEntity<>(taskServ.unassignUser(task_id,user_id), HttpStatus.OK);
    }

    @PatchMapping("/add_tag")
    @ResponseBody
    public ResponseEntity<TaskDTO> addTagToTask(@RequestParam Long task_id, @RequestParam String tag) throws NotFoundException {
        return new ResponseEntity<>(taskServ.addTagToTask(task_id,tag), HttpStatus.OK);
    }

    @PatchMapping("/remove_tag")
    @ResponseBody
    public ResponseEntity<TaskDTO> removeTagFromTask(@RequestParam Long task_id, @RequestParam String tag) throws NotFoundException {
        return new ResponseEntity<>(taskServ.removeTagFromTask(task_id, tag), HttpStatus.OK);
    }

    @PostMapping("/add_update")
    @ResponseBody
    public ResponseEntity<List<TaskUpdate>> addTaskUpdate(@RequestParam Long task_id, @RequestBody TaskUpdate update) throws NotFoundException {
        return new ResponseEntity<>(taskServ.addTaskUpdate(task_id, update), HttpStatus.OK);
    }

    @PatchMapping("/remove_update")
    @ResponseBody
    public ResponseEntity<List<TaskUpdate>> removeUpdateFromTask(@RequestParam Long task_id, @RequestParam  Long update_id ) throws NotFoundException {
        return new ResponseEntity<>(taskServ.removeUpdateFromTask(task_id,update_id), HttpStatus.OK);
    }

    @PatchMapping("/update_tu")
    @ResponseBody
    public ResponseEntity<List<TaskUpdate>> updateTaskUpdate(@RequestParam Long task_id,
                                                             @RequestBody TaskUpdate task_update) throws NotFoundException {
        return new ResponseEntity<>(taskServ.updateTaskUpdate(task_id, task_update), HttpStatus.OK);
    }

    @GetMapping("/byWorkspace")
    @ResponseBody
    public  ResponseEntity<List<TaskDTO>> getTasksByWorkspace(@RequestParam Long id) throws NotFoundException {
        return new ResponseEntity<>(taskServ.getTaskFromWorkspaceById(id), HttpStatus.OK);
    }

}
