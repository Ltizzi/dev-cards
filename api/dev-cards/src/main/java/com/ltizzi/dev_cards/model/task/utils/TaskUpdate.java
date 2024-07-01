package com.ltizzi.dev_cards.model.task.utils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Entity //@Embedded
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdate {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long update_id;

    private Long creator_user_id;
    private String creator_username;
    private List<Long> editors_id = new ArrayList<>();
    private List<String> editors_usernames = new ArrayList<>();

    private String description;

    private List<String> old_descriptions;

    @Builder.Default
    private Instant created_at = Instant.now();

    @Builder.Default
    private Instant updated_at = Instant.now();

    public TaskUpdate update(Long editor_id, String editor_username, String new_description){
        this.editors_usernames.add(editor_username);
        this.old_descriptions.add(this.description);
        this.description = new_description;
        this.updated_at = Instant.now();
        return this;
    }


}
