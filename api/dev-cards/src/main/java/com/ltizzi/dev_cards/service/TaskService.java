package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.NotFoundException;



import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface TaskService {


    public List<TaskEntity>getTasks(int page, int limit);

    public TaskEntity getTaskById(Long id) throws NotFoundException;

    public TaskEntity saveTask(TaskEntity task) throws InvalidTaskException;

    public TaskEntity updateTask(Long task_id, TaskEntity task) throws  InvalidTaskException, NotFoundException;

    public APIResponse deleteTask(Long task_id) throws NotFoundException;

}
