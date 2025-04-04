package com.ltizzi.dev_cards.model.task.utils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

//@Entity //@Embedded
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdate {



    private Long update_id;

    private Long creator_user_id;
    private String creator_username;
    private List<Long> editors_id = new ArrayList<>();
    private List<String> editors_usernames = new ArrayList<>();

    @Column(length = 2000)
    private String description;

    private List<String> old_descriptions = new ArrayList<>();

    @Builder.Default
    private Date created_at = new Date();

    @Builder.Default
    private Date updated_at = new Date();

    public TaskUpdate update(Long editor_id, String editor_username, String new_description){
        this.editors_usernames.add(editor_username);
        this.old_descriptions.add(this.description);
        this.description = new_description;
        this.updated_at = new Date();
        return this;
    }


}
