package com.ltizzi.dev_cards.model.task;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class TaskDTOWithReference {

    private TaskDTO task;
    private MiniTaskDTO reference;
}
