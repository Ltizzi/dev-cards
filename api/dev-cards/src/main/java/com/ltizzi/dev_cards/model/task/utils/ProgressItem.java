package com.ltizzi.dev_cards.model.task.utils;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressItem {


    private Long issue_id;
    private String sentence;
    private boolean isCompleted;

    public boolean getIsCompleted(){
        return this.isCompleted;
    }
}
