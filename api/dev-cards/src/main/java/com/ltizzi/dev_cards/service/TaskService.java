package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.utils.ProgressItem;
import com.ltizzi.dev_cards.model.task.utils.TaskUpdate;
import com.ltizzi.dev_cards.model.utils.APIResponse;



import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface TaskService {


    public List<TaskDTO>getTasks(int page, int limit);

    public TaskDTO getTaskById(Long id) throws NotFoundException;

    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException, NotFoundException;

    public TaskDTO updateTask(Long task_id, TaskDTO task) throws  InvalidTaskException, NotFoundException;

    public APIResponse deleteTask(Long task_id) throws NotFoundException;

    public TaskDTO addDependency(Long task_id, Long dependency_id)  throws NotFoundException, InvalidTaskException;

    public TaskDTO removeDependency(Long task_id, Long dependency_id) throws NotFoundException, InvalidTaskException;

    public TaskDTO assignUser(Long task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException;

    public TaskDTO unassignUser(Long task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException;

    public TaskDTO autoAssignTask(String token, Long task_id) throws NotFoundException, InvalidTaskException, InvalidUserException;
    public TaskDTO addTagToTask(Long task_id, String tag) throws NotFoundException;

    public TaskDTO removeTagFromTask(Long task_id, String tag) throws NotFoundException;

    public List<TaskUpdate> addTaskUpdate(Long task_id, TaskUpdate update) throws NotFoundException;

    public List<TaskUpdate> removeUpdateFromTask(Long task_id, Long update_id) throws NotFoundException;

    public List<TaskUpdate> updateTaskUpdate(Long task_id, TaskUpdate task_update) throws NotFoundException;

    public List<TaskDTO> getTaskFromWorkspaceById(Long workspace_id) throws  NotFoundException;

    public TaskDTO updateTaskProgress(Long task_id, String progress) throws NotFoundException;

    public TaskDTO updateTaskPriority(Long task_id, String priority) throws  NotFoundException;

    public TaskDTO updateTaskStatus(Long task_id, String status) throws  NotFoundException;

    public TaskDTO updateTaskEffort(Long task_id, String effort) throws  NotFoundException;

    public TaskDTO updateTaskType(Long task_id, String type) throws  NotFoundException;

    public TaskDTO updateTaskTitle(Long task_id, String title) throws  NotFoundException;

    public TaskDTO updateTaskSubtitle(Long task_id, String subtitle) throws  NotFoundException;

    public TaskDTO updateDescription(Long task_id, String description) throws  NotFoundException;

    public TaskDTO updateTaskIssue(Long task_id, ProgressItem issue) throws  NotFoundException;

    public TaskDTO createTaskIssue(Long task_id, ProgressItem issue) throws NotFoundException;

    public TaskDTO deleteTaskIssue(Long task_id, Long issue_id) throws  NotFoundException;
}

