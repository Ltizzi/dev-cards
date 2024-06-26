package com.ltizzi.dev_cards.model.user;

import com.ltizzi.dev_cards.model.task.TaskEntity;
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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> created_tasks = new ArrayList<>();



    @ElementCollection
    private List<TaskEntity> designated_tasks = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    private boolean soft_delete = Boolean.FALSE;
}
