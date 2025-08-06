package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.*;
import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.utils.RandomIdGenerator;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.repository.TaskRepository;
import com.ltizzi.dev_cards.repository.UserRepository;
import com.ltizzi.dev_cards.repository.WorkspaceRepository;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.TaskService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */
@Slf4j
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TaskDTO> getTasks(int page, int limit) {
        log.info("\nInfo getting all tasks...");
        PageRequest pageReq =PageRequest.of(page, limit);
        Page<TaskEntity> taskPage = taskRepo.findAll(pageReq);
        return taskMapper.toArrayTaskDTO(taskPage.getContent());
    }

    private TaskEntity findTaskById(UUID id) throws NotFoundException{
        log.info("\nFetching task id {}", id);
        return taskRepo.findById(id).orElseThrow(()->new NotFoundException("Task not found!"));
    }

    @Override
    public TaskDTO getTaskById(UUID id) throws NotFoundException {
//        return taskMapper.toTaskDTO(taskRepo.findById(id).orElseThrow(()-> new NotFoundException("Task not Found")));
        return taskMapper.toTaskDTO(findTaskById(id));
    }

    @Override
    public TaskDTO saveTask(TaskDTO task) throws InvalidTaskException, NotFoundException {
        log.info("\nSaving new task...");
        WorkspaceEntity ws = wsRepo.findById(task.getWorkspace().getWorkspace_id()).orElseThrow(
                ()->new NotFoundException("Workspace not found!"));


        TaskEntity new_task = taskMapper.toTaskEntity(task);

        new_task.addWorkspace(ws);
        new_task.setProgressItems(addIdToIssues(new_task.getProgressItems()));
        new_task = taskRepo.save(new_task);
        wsRepo.save(ws);
        return taskMapper.toTaskDTO(new_task);
    }

    @Override
    public List<TaskDTO> saveTasks(List<TaskDTO> tasks, Long ws_id) throws InvalidTaskException, NotFoundException, InvalidUserException {
        log.info("\nSaving tasks...");
        WorkspaceEntity ws = wsRepo.findById(ws_id).orElseThrow(
                ()->new NotFoundException("Workspace not found!"));
        List<TaskEntity> new_tasks = new ArrayList<>();
        List<TwoTask> twoTasks = new ArrayList<>();

        for(TaskDTO task: tasks){
            TaskEntity new_task = taskMapper.toTaskEntityFresh(task);
            new_task.addWorkspace(ws);
            new_task.setProgressItems(addIdToIssues(task.getProgressItems()));
            //new_task = taskRepo.saveAndFlush(new_task);

            if(!task.getTask_tags().isEmpty()){
                for(String tag: new_task.getTask_tags()){
                    addTagToTask(new_task.getTask_id(), tag);
                }
            }
            if(!task.getUpdates().isEmpty()){
                for(TaskUpdate update: task.getUpdates()){
                    addTaskUpdate(new_task.getTask_id(), update);
                }

            }
            if(task.getBlocked_by() != null){
                UserEntity user = userRepo.findById(task.getBlocked_by().getUser_id()).orElseThrow(
                        ()->new NotFoundException("blocked by user not found!")
                );
                new_task.setBlocked_by(user);

            }
            if(!task.getDesignated_to().isEmpty()){
                for(UserLiteDTO user: task.getDesignated_to()){
                    assignUser(new_task.getTask_id(), user.getUser_id());
                }

            }
            if(!task.getDependencies().isEmpty()){


                List<TaskLiteDTO> dependencies = task.getDependencies();
                for(TaskLiteDTO dep: dependencies){
                    TaskEntity parent = taskRepo.findById(dep.getTask_id()).orElse(null);
                    if(parent != null){
                        new_task.addDependency(parent);
                        TwoTask dependentTasks = new TwoTask();
                        dependentTasks.addTasks(new_task,parent);
                        twoTasks.add(dependentTasks);
                    }
                }
            }

            //if(updatedTask) new_task = taskRepo.save(new_task);
            new_tasks.add(new_task);
        }
        wsRepo.save(ws);
        List<TaskEntity> createdTasks =taskRepo.saveAllAndFlush(new_tasks);

        List<TaskEntity> parents = new ArrayList<>();
        for(TwoTask tts: twoTasks){
            TaskEntity parent = tts.getParent();
            TaskEntity child = taskRepo.findById(tts.getChild().getTask_id()).orElse(null);
            if(child != null){
                parent.addChildDependency(child);
                parents.add(parent);
            }
        }
        taskRepo.saveAllAndFlush(parents);

        return taskMapper.toArrayTaskDTO(createdTasks);
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
    public TaskDTO updateTask(UUID task_id, TaskDTO task) throws InvalidTaskException, NotFoundException {
            log.info("\nUpdating task id {}", task_id);
            TaskEntity old_task = findTaskById(task_id);//getTaskById(task_id);
            if(old_task != null && old_task.getTask_id().equals(task.getTask_id())){
                return taskMapper.toTaskDTO(taskRepo.save(taskMapper.toTaskEntity(task)));
            }
            else throw  new InvalidTaskException("Something went wrong");

    }

    @Override
    public APIResponse deleteTask(UUID task_id) throws NotFoundException {
        log.info("\nDeleting tasks id {} ...", task_id);;
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
    public TaskDTO addDependency(UUID task_id, UUID dependency_id) throws NotFoundException, InvalidTaskException {
        log.info("\nAdding dependency id {} to task id {}", dependency_id, task_id);
        TwoTask tasks = new TwoTask().addTasks(task_id, dependency_id, taskRepo);
        if(!tasks.sameProjectChecker()){
            throw new InvalidTaskException("Tasks must belong to the same project, parent_ws: "+ tasks.getParent().getWorkspace().getWorkspace_id().toString() + " , child_ws:"+ tasks.getChild().getWorkspace().getWorkspace_id().toString());
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
    public TaskDTO removeDependency(UUID task_id, UUID dependency_id) throws NotFoundException, InvalidTaskException {
        log.info("\nRemoving dependency id {} from task id {}", dependency_id, task_id);
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
//        return task.getDesignated_to().contains(user);
        return task.getDesignated_to().stream()
                .anyMatch(u->u.getUser_id().equals(user.getUser_id()));
    }


    @Override
    public TaskDTO assignUser(UUID task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException {
        TaskEntity task = findTaskById(task_id);
        UserEntity user = userRepo.findById(user_id).orElseThrow(()->new NotFoundException("User not found"));
        log.info("\nTrying to assing user {} to task {}", user_id, task_id);
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
    public TaskDTO unassignUser(UUID task_id, Long user_id) throws NotFoundException, InvalidUserException, InvalidTaskException {
        log.info("\nTrying to unassing user {} from task {}", user_id, task_id);
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
    public TaskDTO autoAssignTask(String token, UUID task_id) throws NotFoundException, InvalidTaskException, InvalidUserException {
        log.info("\nTask {} assigning to owner...", task_id);
        String username = jwtUtils.extractUsername(token);
        List<UserEntity> user = userRepo.findByUsername(username);
        if(user.get(0)== null){
            throw  new NotFoundException("User not found!");
        }
        return assignUser(task_id, user.get(0).getUser_id());

    }

    @Override
    public TaskDTO addTagToTask(UUID task_id, String tag) throws NotFoundException {
        log.info("\nAdding tag '{}' to task id {}", tag, task_id);
        TaskEntity task = findTaskById(task_id);
        APIResponse res = task.addTag(tag);
        return taskMapper.toTaskDTO(taskRepo.save(task));

    }

    @Override
    public TaskDTO removeTagFromTask(UUID task_id, String tag) throws NotFoundException {
        log.info("\nRemoving tag '{}' from task id {}",tag, task_id);
        TaskEntity task = findTaskById(task_id);
        APIResponse res  = task.removeTag(tag);
        return taskMapper.toTaskDTO(taskRepo.save(task));

    }

    @Override
    public List<TaskUpdate> addTaskUpdate(UUID task_id, TaskUpdate update) throws NotFoundException {
        log.info("\nAdding task update to task {}", task_id);
        TaskEntity task = findTaskById(task_id);
        //update.setUpdate_id(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        RandomIdGenerator randIdGen = new RandomIdGenerator();
        update.setUpdate_id(randIdGen.generateRandomLong());
        task.addUpdate(update);
        return taskRepo.save(task).getUpdates();
    }

    @Override
    public List<TaskUpdate> removeUpdateFromTask(UUID task_id, Long update_id) throws NotFoundException {
        log.info("\nRemoving update {} from task {}", update_id, task_id);
        TaskEntity task = findTaskById(task_id);
        task.removeUpdate(update_id);
        return taskRepo.save(task).getUpdates();
    }

    @Override
    public List<TaskUpdate> updateTaskUpdate(UUID task_id, TaskUpdate taskUpdate) throws NotFoundException {
        log.info("\nUpdating update {} from task {}", taskUpdate.getUpdate_id(), task_id);
        TaskEntity task = findTaskById(task_id);
        task.updateUpdate(taskUpdate.getUpdate_id(), taskUpdate.getCreator_user_id(), taskUpdate.getCreator_username(), taskUpdate.getDescription());
        return taskRepo.save(task).getUpdates();
    }

    @Override
    public List<TaskDTO> getTaskFromWorkspaceById(Long workspace_id) throws NotFoundException {
        log.info("\nFetching all task from workspace {}", workspace_id);
        List<TaskEntity> tasks = taskRepo.findTasksByWorkspaceId(workspace_id);
        return taskMapper.toArrayTaskDTO(tasks);
    }


    @Override
    public TaskDTO updateTaskProgress(UUID task_id, String progress) throws NotFoundException {
        log.info("\nUpdating task {} progress ({})", task_id, progress);
        TaskEntity task = findTaskById(task_id);
        ProgressEnum progEnum = ProgressEnum.valueOf(progress);
        task.setProgress(progEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskPriority(UUID task_id, String priority) throws NotFoundException {
        log.info("\nUpdating task {} priority ({})", task_id, priority);
        TaskEntity task = findTaskById(task_id);
        PriorityEnum priorEnum = PriorityEnum.valueOf(priority);
        task.setPriority(priorEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskStatus(UUID task_id, String status) throws NotFoundException {
        log.info("\nUpdating task {} with Status ({})", task_id, status);
        TaskEntity task = findTaskById(task_id);
        Status statusEnum = Status.valueOf(status);
        task.setStatus(statusEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskEffort(UUID task_id, String effort) throws NotFoundException {
        log.info("\nUpdating task {} with effort ({})", task_id, effort);
        TaskEntity task = findTaskById(task_id);
        EffortEnum effortEnum = EffortEnum.valueOf(effort);
        task.setEffort(effortEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskType(UUID task_id, String type) throws NotFoundException {
        log.info("\nUpdating task {} with type ({})", task_id, type);
        TaskEntity task = findTaskById(task_id);
        TaskType typeEnum = TaskType.valueOf(type);
        task.setTask_type(typeEnum);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskTitle(UUID task_id, String title) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        log.info("\nUpdating task {} title from '{}'to '{}'", task_id, task.getTitle(), title);
        task.setTitle(title);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskSubtitle(UUID task_id, String subtitle) throws NotFoundException {
        TaskEntity task = findTaskById(task_id);
        log.info("\nUpdating task {} subtitle from '{}'to '{}'", task_id, task.getSubtitle(), subtitle);
        task.setSubtitle(subtitle);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateDescription(UUID task_id, String description) throws NotFoundException {
        log.info("\nUpdating task {} description", task_id);
        TaskEntity task = findTaskById(task_id);
        task.setDescription(description);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO updateTaskIssue(UUID task_id, ProgressItem issue) throws NotFoundException {
        log.info("\nUpdating task {} issue {}", task_id, issue.getIssue_id());
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
    public TaskDTO createTaskIssue(UUID task_id, ProgressItem issue) throws NotFoundException {
        log.info("\nCreating task issue for task {}",task_id);
        TaskEntity task = findTaskById(task_id);
        issue.setIssue_id(UUID.randomUUID().getLeastSignificantBits()  & 0x1FFFFFFFFFFFFFL);
        List<ProgressItem> issues = task.getProgressItems();
        issues.add(issue);
        task.setProgressItems(issues);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }

    @Override
    public TaskDTO deleteTaskIssue(UUID task_id, Long issue_id) throws NotFoundException {
        log.info("\nDeleting issue {} from task {} ", issue_id, task_id);
        TaskEntity task = findTaskById(task_id);
        List<ProgressItem> issues = task.getProgressItems();
        issues = issues.stream().filter(i-> !i.getIssue_id().equals(issue_id)).collect(Collectors.toList());
        task.setProgressItems(issues);
        return taskMapper.toTaskDTO(taskRepo.save(task));
    }
}
