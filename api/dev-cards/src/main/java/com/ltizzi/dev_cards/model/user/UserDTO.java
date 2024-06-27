package com.ltizzi.dev_cards.model.user;

import com.ltizzi.dev_cards.model.task.TaskLiteDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class UserDTO {

    private Long user_id;
    private String username;
    //private String password;
    private String email;
    private String avatar;
    private String about;
    private String githubProfile;
    private List<WorkspaceLiteDTO> workspaces = new ArrayList<>();
    private List<TaskLiteDTO> created_tasks = new ArrayList<>();
    private List<TaskLiteDTO> designated_tasks = new ArrayList<>();
    private Timestamp created_at;
    private Timestamp updated_at;
}
