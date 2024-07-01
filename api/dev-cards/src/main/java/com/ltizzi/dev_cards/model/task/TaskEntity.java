package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */
@Entity
@Data
@NoArgsConstructor
@Table(name= "tasks")
@SQLDelete(sql = "UPDATE tasks SET soft_delete = true where task_id=?")
@Where(clause = "soft_delete=false")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task_id;


    @NotEmpty(message = "title can't be empty")
    @NotNull(message = "title can't be null")
    @NotBlank(message = "title can't be blank")
    private String title;

    @NotEmpty(message = "subtitle can't be empty")
    @NotNull(message = "subtitle can't be null")
    @NotBlank(message = "subtitle can't be blank")
    private String subtitle;

    @Column(columnDefinition = "TEXT", length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    @Enumerated(EnumType.STRING)
    private EffortEnum effort;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private ProgressEnum progress;

    @Enumerated(EnumType.STRING)
    private TaskType task_type;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private WorkspaceEntity project;

    @ElementCollection
    private List<TaskEntity> dependencies = new ArrayList<>();

    @ElementCollection
    private List<String> task_tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "task_updates", joinColumns = @JoinColumn(name = "task_id"))
    private List<TaskUpdate> updates = new ArrayList<>();

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id")
    private UserEntity blocked_by;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> designated_to;



    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    private boolean soft_delete =Boolean.FALSE;

    public void addDependency(TaskEntity dependency) throws InvalidTaskException {
        if(!dependencies.contains(dependency)){
            dependencies.add(dependency);
        }
        else throw new InvalidTaskException("Dependency has already established");
    }

    public void removeDependency(TaskEntity dependency) throws InvalidTaskException {
        if(dependencies.contains(dependency)){
            dependencies.remove(dependency);
        }
        else throw new InvalidTaskException("Failed: task isn't on dependencies list");
    }

    public void assignUser(UserEntity user) throws InvalidUserException, InvalidTaskException {
        if(!designated_to.contains(user)){
            designated_to.add(user);
            user.assignTask(this);
        }
        else throw new InvalidUserException("User has already designated to task");
    }

    public void unassingUser(UserEntity user) throws InvalidUserException, InvalidTaskException {
        if(designated_to.contains(user)){
            designated_to.remove(user);
            user.deassignTask(this);
        }
        else throw new InvalidUserException("User isn't a task's designated user");
    }

    public APIResponse addTag(String tag){
        APIResponse res = new APIResponse();
        res.setHttp_method("PATCH");
        tag = tag.toLowerCase();
        if (!task_tags.contains(tag)) {
            task_tags.add(tag);
            res.setMessage("added");
        }
        else res.setMessage("tag already added");
        return res;
    }

    public APIResponse removeTag(String tag){
        APIResponse res = new APIResponse();
        tag = tag.toLowerCase();
        res.setHttp_method("PATCH");
        if(task_tags.contains(tag)){
            task_tags.remove(tag);
            res.setMessage("removed");
        }
        else res.setMessage("Can remove tag because doesn't exist");
        return res;
    }

    public void addUpdate(TaskUpdate update){
        updates.add(update);
    }

    public  void updateUpdate(Long update_id, Long editor_id, String editor_username, String new_description){
        TaskUpdate oldUpdate = new TaskUpdate();
        for(TaskUpdate ud: updates){
            if(ud.getUpdate_id().equals(update_id)){
                ud.update(editor_id, editor_username, new_description);
            }
        }
    }

    public void removeUpdate(Long update_id) {
        updates = updates.stream().filter(update->!update.getUpdate_id().equals(update_id)).collect(Collectors.toList());
    }

}
