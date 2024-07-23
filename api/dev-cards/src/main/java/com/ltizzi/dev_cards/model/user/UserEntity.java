package com.ltizzi.dev_cards.model.user;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.user.utils.Role;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET soft_delete = true where user_id=?")
@Where(clause = "soft_delete=false")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotEmpty(message = "Username can't be empty")
    @NotNull(message = "Username  can't be null")
    @NotBlank(message = "Username  can't be blank")
    private String username;

    @NotEmpty(message = "Password can't be empty")
    @NotNull(message = "Password can't be null")
    @NotBlank(message = "Password can't be blank")
    private String password;

    @NotEmpty(message = "Email can't be empty")
    @NotNull(message = "Email can't be null")
    @NotBlank(message = "Email can't be blank")
    private String email;

    @NotEmpty(message = "Avatar can't be empty")
    @NotNull(message = "Avatar can't be null")
    @NotBlank(message = "Avatar can't be blank")
    private String avatar;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String about;

    @Column(columnDefinition = "TEXT", length = 100)
    private String githubProfile;

    @ManyToMany
    @JoinTable(
            name = "users_workspaces",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "workspace_id")
    )
    private List<WorkspaceEntity> workspaces = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<TaskEntity> created_tasks = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    //@ElementCollection
    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<TaskEntity> designated_tasks = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    private boolean soft_delete = Boolean.FALSE;

    public void addWorkspace(WorkspaceEntity ws){
        workspaces.add(ws);
        ws.getUsers().add(this);
    }

    public void removeWorkspace(WorkspaceEntity ws) {
        workspaces.remove(ws);
        ws.getUsers().remove(this);
    }

    public void assignTask(TaskEntity task) throws InvalidTaskException {
        if(!designated_tasks.contains(task)){
            designated_tasks.add(task);
        }
        else throw  new InvalidTaskException("User already assigned to task");
    }

    public void deassignTask(TaskEntity task) throws InvalidTaskException {
        if(designated_tasks.contains(task)){
            designated_tasks.remove(task);
        }
        else throw  new InvalidTaskException("can't deassign task because user  is not assigned to it");
    }
}
