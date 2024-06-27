package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.model.task.utils.*;
import com.ltizzi.dev_cards.model.user.UserEntity;
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

}
