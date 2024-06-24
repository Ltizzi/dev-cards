package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.NotFoundException;
import com.ltizzi.dev_cards.repository.TaskRepository;
import com.ltizzi.dev_cards.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepo;

    @Override
    public List<TaskEntity> getTasks(int page, int limit) {
        PageRequest pageReq =PageRequest.of(page, limit);
        Page<TaskEntity> taskPage = taskRepo.findAll(pageReq);
        return taskPage.getContent();
    }

    @Override
    public TaskEntity getTaskById(Long id) throws NotFoundException {
        return taskRepo.findById(id).orElseThrow();
    }

    @Override
    public TaskEntity saveTask(TaskEntity task) throws InvalidTaskException {
        return taskRepo.save(task);
    }

    @Override
    public TaskEntity updateTask(Long task_id, TaskEntity task) throws InvalidTaskException, NotFoundException {

            TaskEntity old_task = getTaskById(task_id);
            if(old_task != null && old_task.getTask_id().equals(task.getTask_id())){
                return taskRepo.save(task);
            }
            else throw  new InvalidTaskException("Something went wrong");

    }

    @Override
    public APIResponse deleteTask(Long task_id) throws NotFoundException {
        APIResponse apiRes = new APIResponse();
        TaskEntity task = getTaskById(task_id);
        if (task != null){
            taskRepo.deleteById(task_id);
            apiRes.setHttp_method("DELETE");
            apiRes.setMessage("Task deleted");
            return apiRes;
        }
        else {
            apiRes.setHttp_method("DELETE");
            apiRes.setMessage("Task not Found!");
            return apiRes;
        }
    }
}
