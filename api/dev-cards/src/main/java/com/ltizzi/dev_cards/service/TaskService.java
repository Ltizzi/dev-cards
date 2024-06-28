package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;




import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface TaskService {


    public List<TaskDTO>getTasks(int page, int limit);

    public TaskDTO getTaskById(Long id) throws NotFoundException;

    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException;

    public TaskDTO updateTask(Long task_id, TaskDTO task) throws  InvalidTaskException, NotFoundException;

    public TaskDTO addDependency(Long task_id, Long dependency_id)  throws NotFoundException, InvalidTaskException;

    public TaskDTO removeDependency(Long task_id, Long dependency_id) throws NotFoundException, InvalidTaskException;

    public APIResponse deleteTask(Long task_id) throws NotFoundException;

}
