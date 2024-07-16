package com.ltizzi.dev_cards.model.workspace;

import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */
@Data
@NoArgsConstructor
public class WorkspaceLiteDTO {

    private Long workspace_id;
    private String project_name;
    private String project_avatar;
    private UserLiteDTO owner;


}
