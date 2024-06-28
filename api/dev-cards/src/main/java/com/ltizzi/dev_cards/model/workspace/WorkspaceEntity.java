package com.ltizzi.dev_cards.model.workspace;

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
import java.util.Iterator;
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

    @Column(columnDefinition = "TEXT", length = 5000)
    private String description;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private UserEntity owner;


    @OneToMany(mappedBy = "task_id", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
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
            name = "worksace_users",
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


    public void addUser(UserEntity user){
        users.add(user);
        user.getWorkspaces().remove(this);
    }

    public void removeUser(UserEntity user){
        users.remove(user);
        user.getWorkspaces().remove(this);
    }
}
