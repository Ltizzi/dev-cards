package com.ltizzi.dev_cards.model.task;

import com.ltizzi.dev_cards.model.task.utils.Color;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */
@Embeddable
@Data
@NoArgsConstructor
public class MiniTaskDTO {

    private UUID task_id;
    private String title;
    private Color color;
    private Long workspace_id;


}
