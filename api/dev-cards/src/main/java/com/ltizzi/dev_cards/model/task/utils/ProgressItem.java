package com.ltizzi.dev_cards.model.task.utils;

import jakarta.persistence.Embeddable;
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

    private String sentence;
    private boolean isCompleted = Boolean.FALSE;
}
