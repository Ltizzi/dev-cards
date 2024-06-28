package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.task.TaskMapper;
import com.ltizzi.dev_cards.model.task.utils.TwoTask;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.repository.TaskRepository;
import com.ltizzi.dev_cards.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<TaskDTO> getTasks(int page, int limit) {
        PageRequest pageReq =PageRequest.of(page, limit);
        Page<TaskEntity> taskPage = taskRepo.findAll(pageReq);
        return taskMapper.toArrayTaskDTO(taskPage.getContent());
    }

    @Override
    public TaskDTO getTaskById(Long id) throws NotFoundException {
        return taskMapper.toTaskDTO(taskRepo.findById(id).orElseThrow(()-> new NotFoundException("Task not Found")));
    }

    @Override
    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException {
        return taskMapper.toTaskDTO(taskRepo.save(taskMapper.toTaskEntity(task)));
    }

    @Override
    public TaskDTO updateTask(Long task_id, TaskDTO task) throws InvalidTaskException, NotFoundException {

            TaskDTO old_task = getTaskById(task_id);
            if(old_task != null && old_task.getTask_id().equals(task.getTask_id())){
                return taskMapper.toTaskDTO(taskRepo.save(taskMapper.toTaskEntity(task)));
            }
            else throw  new InvalidTaskException("Something went wrong");

    }

    @Override
    public TaskDTO addDependency(Long task_id, Long dependency_id) throws NotFoundException, InvalidTaskException {
        TwoTask tasks = new TwoTask().addTasks(task_id, dependency_id, taskRepo);
        if(!tasks.sameProjectChecker()){
            throw new InvalidTaskException("Tasks must belong to the same project");
        }
        if(tasks.dependencyChecker()){
            throw new InvalidTaskException("parent is already a dependency");
        }
        TaskEntity child_task = tasks.getChild();
        child_task.addDependency(tasks.getParent());
        return taskMapper.toTaskDTO(taskRepo.save(child_task));
    }

    @Override
    public TaskDTO removeDependency(Long task_id, Long dependency_id) throws NotFoundException, InvalidTaskException {
        TwoTask tasks = new TwoTask().addTasks(task_id, dependency_id, taskRepo);
        if(!tasks.sameProjectChecker())  {
            throw new InvalidTaskException("Tasks must belong to the same project");
        }
        if(!tasks.dependencyChecker()){
            throw new InvalidTaskException("Parent task is not a child's dependency");
        }
        TaskEntity child_task = tasks.getChild();
        child_task.removeDependency(tasks.getParent());
        return taskMapper.toTaskDTO(taskRepo.save(child_task));
    }

    @Override
    public APIResponse deleteTask(Long task_id) throws NotFoundException {
        APIResponse apiRes = new APIResponse();
        TaskDTO task = getTaskById(task_id);
        if (task != null){
            taskRepo.deleteById(task_id);
            apiRes.setHttp_method("DELETE");
            apiRes.setMessage("Task deleted");

        }
        else {
            apiRes.setHttp_method("DELETE");
            apiRes.setMessage("Task not Found!");

        }
        return apiRes;
    }
}
