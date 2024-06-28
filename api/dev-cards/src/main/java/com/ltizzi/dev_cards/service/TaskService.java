package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.utils.TaskUpdate;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import org.aspectj.weaver.ast.Not;


import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface TaskService {


    public List<TaskDTO>getTasks(int page, int limit);

    public TaskDTO getTaskById(Long id) throws NotFoundException;

    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException;

    public TaskDTO updateTask(Long task_id, TaskDTO task) throws  InvalidTaskException, NotFoundException;

    public APIResponse deleteTask(Long task_id) throws NotFoundException;

    public TaskDTO addDependency(Long task_id, Long dependency_id)  throws NotFoundException, InvalidTaskException;

    public TaskDTO removeDependency(Long task_id, Long dependency_id) throws NotFoundException, InvalidTaskException;

    public TaskDTO assignUser(Long task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException;

    public TaskDTO unassignUser(Long task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException;

    public APIResponse addTagToTask(Long task_id, String tag) throws NotFoundException;

    public APIResponse removeTagFromTask(Long task_id, String tag) throws NotFoundException;

    public List<TaskUpdate> addTaskUpdate(Long task_id, TaskUpdate update) throws NotFoundException;

    public List<TaskUpdate> removeUpdateFromTask(Long task_id, Long update_id) throws NotFoundException;

    public List<TaskUpdate> updateTaskUpdate(Long task_id, Long update_id, Long editor_id, String editor_username, String new_description) throws NotFoundException;

}
