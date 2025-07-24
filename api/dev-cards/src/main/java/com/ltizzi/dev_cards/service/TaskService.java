package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.utils.ProgressItem;
import com.ltizzi.dev_cards.model.task.utils.TaskUpdate;
import com.ltizzi.dev_cards.model.utils.APIResponse;



import java.util.List;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */


public interface TaskService {


    public List<TaskDTO>getTasks(int page, int limit);

    public TaskDTO getTaskById(UUID id) throws NotFoundException;

    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException, NotFoundException;

    public List<TaskDTO> saveTasks(List<TaskDTO> tasks, Long ws_id) throws InvalidTaskException, NotFoundException, InvalidUserException;

    public TaskDTO updateTask(UUID task_id, TaskDTO task) throws  InvalidTaskException, NotFoundException;

    public APIResponse deleteTask(UUID task_id) throws NotFoundException;

    public TaskDTO addDependency(UUID task_id, UUID dependency_id)  throws NotFoundException, InvalidTaskException;

    public TaskDTO removeDependency(UUID task_id, UUID dependency_id) throws NotFoundException, InvalidTaskException;

    public TaskDTO assignUser(UUID task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException;

    public TaskDTO unassignUser(UUID task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException;

    public TaskDTO autoAssignTask(String token, UUID task_id) throws NotFoundException, InvalidTaskException, InvalidUserException;
    public TaskDTO addTagToTask(UUID task_id, String tag) throws NotFoundException;

    public TaskDTO removeTagFromTask(UUID task_id, String tag) throws NotFoundException;

    public List<TaskUpdate> addTaskUpdate(UUID task_id, TaskUpdate update) throws NotFoundException;

    public List<TaskUpdate> removeUpdateFromTask(UUID task_id, Long update_id) throws NotFoundException;

    public List<TaskUpdate> updateTaskUpdate(UUID task_id, TaskUpdate task_update) throws NotFoundException;

    public List<TaskDTO> getTaskFromWorkspaceById(Long workspace_id) throws  NotFoundException;

    public TaskDTO updateTaskProgress(UUID task_id, String progress) throws NotFoundException;

    public TaskDTO updateTaskPriority(UUID task_id, String priority) throws  NotFoundException;

    public TaskDTO updateTaskStatus(UUID task_id, String status) throws  NotFoundException;

    public TaskDTO updateTaskEffort(UUID task_id, String effort) throws  NotFoundException;

    public TaskDTO updateTaskType(UUID task_id, String type) throws  NotFoundException;

    public TaskDTO updateTaskTitle(UUID task_id, String title) throws  NotFoundException;

    public TaskDTO updateTaskSubtitle(UUID task_id, String subtitle) throws  NotFoundException;

    public TaskDTO updateDescription(UUID task_id, String description) throws  NotFoundException;

    public TaskDTO updateTaskIssue(UUID task_id, ProgressItem issue) throws  NotFoundException;

    public TaskDTO createTaskIssue(UUID task_id, ProgressItem issue) throws NotFoundException;

    public TaskDTO deleteTaskIssue(UUID task_id, Long issue_id) throws  NotFoundException;
}

