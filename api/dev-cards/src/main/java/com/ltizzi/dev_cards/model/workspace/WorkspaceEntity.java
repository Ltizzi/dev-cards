package com.ltizzi.dev_cards.model.workspace;

import com.ltizzi.dev_cards.model.customConfiguration.CustomConfiguration;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.user.UserEntity;
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
@Table(name = "workspaces")
@SQLDelete(sql = "UPDATE workspaces SET soft_delete = true where workspace_id=?")
@Where(clause = "soft_delete=false")
public class WorkspaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workspace_id;

    @NotEmpty(message = "Project name can't be empty")
    @NotNull(message = "Project name can't be null")
    @NotBlank(message = "Project name can't be blank")
    private String project_name;

    @Column(columnDefinition = "TEXT", length = 500)
    private String project_avatar;

    @Column(columnDefinition = "TEXT", length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private UserEntity owner;

    @OneToOne(mappedBy = "workspace", cascade = CascadeType.ALL)
    private CustomConfiguration customConfiguration;


    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TaskEntity> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "workspace_moderators",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> moderators = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name="workspace_collaborators",
            joinColumns =  @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> collaborators = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "workspace_users",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    private boolean soft_delete = Boolean.FALSE;

    public void addTask(TaskEntity task){
        tasks.add(task);
        //task.setProject(this);
    }

    public void addUser(UserEntity user){
        users.add(user);
        user.getWorkspaces().add(this);
    }

    public void removeUser(UserEntity user){
        users.remove(user);
        user.getWorkspaces().remove(this);
    }

    public void addUserAsMod(UserEntity user){
        moderators.add(user);
    }

    public void removeUserAsMod(UserEntity user){
        moderators.remove(user);
    }

    public void addUserAsCollaborator(UserEntity user){
        collaborators.add(user);
    }

    public void removeUserAsCollaborator(UserEntity user){
        collaborators.remove(user);
    }
}
