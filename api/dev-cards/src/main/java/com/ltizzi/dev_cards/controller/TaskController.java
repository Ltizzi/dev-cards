package com.ltizzi.dev_cards.controller;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.NotFoundException;
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
    public ResponseEntity<TaskDTO> saveTask(@RequestBody TaskDTO task) throws InvalidTaskException {
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
}
