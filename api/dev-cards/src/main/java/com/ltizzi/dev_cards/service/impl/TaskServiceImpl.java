package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskDTO;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.task.TaskMapper;
import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.RandomIdGenerator;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.repository.TaskRepository;
import com.ltizzi.dev_cards.repository.UserRepository;
import com.ltizzi.dev_cards.repository.WorkspaceRepository;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private WorkspaceRepository wsRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public List<TaskDTO> getTasks(int page, int limit) {
        PageRequest pageReq =PageRequest.of(page, limit);
        Page<TaskEntity> taskPage = taskRepo.findAll(pageReq);
        return taskMapper.toArrayTaskDTO(taskPage.getContent());
    }

    private TaskEntity findTaskById(Long id) throws NotFoundException{
        return taskRepo.findById(id).orElseThrow(()->new NotFoundException("Task not found!"));
    }

    @Override
    public TaskDTO getTaskById(Long id) throws NotFoundException {
//        return taskMapper.toTaskDTO(taskRepo.findById(id).orElseThrow(()-> new NotFoundException("Task not Found")));
        return taskMapper.toTaskDTO(findTaskById(id));
    }

    @Override
    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException, NotFoundException {
        WorkspaceEntity ws = wsRepo.findById(task.getWorkspace().getWorkspace_id()).orElseThrow(
                ()->new NotFoundException("Workspace not found!"));
        TaskEntity new_task = taskMapper.toTaskEntity(task);
        new_task.addWorkspace(ws);
        new_task.setProgressItems(addIdToIssues(new_task.getProgressItems()));
        new_task = taskRepo.save(new_task);
        wsRepo.save(ws);
        return taskMapper.toTaskDTO(new_task);
    }

    public List<ProgressItem> addIdToIssues(List<ProgressItem> issues){
        List<ProgressItem> serializedIssues = new ArrayList<>();
        for(ProgressItem issue: issues){
            //issue.setIssue_id(UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE);
            issue.setIssue_id(UUID.randomUUID().getLeastSignificantBits()  & 0x1FFFFFFFFFFFFFL);
            serializedIssues.add(issue);
        }
        return serializedIssues;
    }

    @Override
    public TaskDTO updateTask(Long task_id, TaskDTO task) throws InvalidTaskException, NotFoundException {

            TaskEntity old_task = findTaskById(task_id);//getTaskById(task_id);
            if(old_task != null && old_task.getTask_id().equals(task.getTask_id())){
                return taskMapper.toTaskDTO(taskRepo.save(taskMapper.toTaskEntity(task)));
            }
            else throw  new InvalidTaskException("Something went wrong");

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
        TaskEntity parent_task = tasks.getParent();
        parent_task.addChildDependency(child_task);
        taskRepo.save(parent_task);
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
        TaskEntity parent_task = tasks.getParent();
        parent_task.removeChildDependency(child_task);
        taskRepo.save(parent_task);
        return taskMapper.toTaskDTO(taskRepo.save(child_task));
    }

    private boolean sameProjectChecker(TaskEntity task, UserEntity user){
        return task.getWorkspace().getUsers().contains(user);
    }

    private boolean assignChecker(TaskEntity task, UserEntity user){
        return task.getDesignated_to().contains(user);
    }


    @Override
    public TaskDTO assignUser(Long task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException {
        TaskEntity task = findTaskById(task_id);
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found"));
        if(!sameProjectChecker(task, user)){
            throw new InvalidUserException("User must be a member of the task's project");
        }
        if(assignChecker(task, user)){
            throw new InvalidUserException("User is already assigned to the task");
        }
        task.assignUser(user);
        task.setStatus(Status.PROGRESS);
        return taskMapper.toTaskDTO(taskRepo.save(task));

    }

    @Override
    public TaskDTO unassignUser(Long task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException {
        TaskEntity task = findTaskById(task_id);
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found"));
        if(!sameProjectChecker(task, user)){
            throw  new InvalidUserException("User must be a member of the task's project");
        }
        if(!assignChecker(task, user)){
            throw new InvalidUserException("User is not assigned to task");
        }
        task.unassingUser(user);
        if(task.getDesignated_to().size() ==0){
            task.setStatus(Status.PENDING);
        }
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO autoAssignTask(String token, Long task_id) throws NotFoundException, InvalidTaskException, InvalidUserException {
        String username = jwtUtils.extractUsername(token);
        List<UserEntity> user = userRepo.findByUsername(username);
        if(user.get(0)== null){
            throw  new NotFoundException("User not found!");
        }
        return assignUser(task_id, user.get(0).getUser_id());

    }

    @Override
    public TaskDTO addTagToTask(Long task_id, String tag) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        APIResponse res = task.addTag(tag);
        return taskMapper.toTaskDTO(taskRepo.save(task));

    }

    @Override
    public TaskDTO removeTagFromTask(Long task_id, String tag) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        APIResponse res  = task.removeTag(tag);
        return taskMapper.toTaskDTO(taskRepo.save(task));

    }

    @Override
    public List<TaskUpdate> addTaskUpdate(Long task_id, TaskUpdate update) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        //update.setUpdate_id(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        RandomIdGenerator randIdGen = new RandomIdGenerator();
        update.setUpdate_id(randIdGen.generateRandomLong());
        task.addUpdate(update);
        return taskRepo.save(task).getUpdates();
    }

    @Override
    public List<TaskUpdate> removeUpdateFromTask(Long task_id, Long update_id) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        task.removeUpdate(update_id);
        return taskRepo.save(task).getUpdates();
    }

    @Override
    public List<TaskUpdate> updateTaskUpdate(Long task_id, TaskUpdate taskUpdate) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        task.updateUpdate(taskUpdate.getUpdate_id(), taskUpdate.getCreator_user_id(), taskUpdate.getCreator_username(), taskUpdate.getDescription());
        return taskRepo.save(task).getUpdates();
    }

    @Override
    public List<TaskDTO> getTaskFromWorkspaceById(Long workspace_id) throws NotFoundException {
        List<TaskEntity> tasks = taskRepo.findTasksByWorkspaceId(workspace_id);
        return taskMapper.toArrayTaskDTO(tasks);
    }


    @Override
    public TaskDTO updateTaskProgress(Long task_id, String progress) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        ProgressEnum progEnum = ProgressEnum.valueOf(progress);
        task.setProgress(progEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskPriority(Long task_id, String priority) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        PriorityEnum priorEnum = PriorityEnum.valueOf(priority);
        task.setPriority(priorEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskStatus(Long task_id, String status) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        Status statusEnum = Status.valueOf(status);
        task.setStatus(statusEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskEffort(Long task_id, String effort) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        EffortEnum effortEnum = EffortEnum.valueOf(effort);
        task.setEffort(effortEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskType(Long task_id, String type) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        TaskType typeEnum = TaskType.valueOf(type);
        task.setTask_type(typeEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskTitle(Long task_id, String title) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        task.setTitle(title);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskSubtitle(Long task_id, String subtitle) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        task.setSubtitle(subtitle);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateDescription(Long task_id, String description) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        task.setDescription(description);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskIssue(Long task_id, ProgressItem issue) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        List<ProgressItem> issues = task.getProgressItems();

        Optional<ProgressItem> old_issue = issues.stream().filter(i->i.getIssue_id().equals(issue.getIssue_id())).findAny();
        //List<ProgressItem> old_issues = issues.stream().filter(i->i.getIssue_id() ==issue.getIssue_id()).collect(Collectors.toList());
        if(old_issue.isEmpty()){
        //if(old_issues.get(0) == null){
            throw  new NotFoundException("Issue not found!");
        }
        task.setProgressItems(issues.stream().map(i->{
            if(i.getIssue_id().equals(issue.getIssue_id())){
                i.setSentence(issue.getSentence());
                i.setCompleted(issue.getIsCompleted());
            }
            return i;
        }).collect(Collectors.toList()));
    return taskMapper.toTaskDTO(taskRepo.save(task));

    }

    @Override
    public TaskDTO createTaskIssue(Long task_id, ProgressItem issue) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        issue.setIssue_id(UUID.randomUUID().getLeastSignificantBits()  & 0x1FFFFFFFFFFFFFL);
        List<ProgressItem> issues = task.getProgressItems();
        issues.add(issue);
        task.setProgressItems(issues);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO deleteTaskIssue(Long task_id, Long issue_id) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        List<ProgressItem> issues = task.getProgressItems();
        issues = issues.stream().filter(i-> !i.getIssue_id().equals(issue_id)).collect(Collectors.toList());
        task.setProgressItems(issues);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }
}
