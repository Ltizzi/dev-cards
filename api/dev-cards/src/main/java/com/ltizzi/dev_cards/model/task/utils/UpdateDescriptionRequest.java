package com.ltizzi.dev_cards.model.task.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDescriptionRequest {

    private String description;
}
