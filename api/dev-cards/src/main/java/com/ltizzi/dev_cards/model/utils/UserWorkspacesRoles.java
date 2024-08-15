package com.ltizzi.dev_cards.model.utils;

import com.ltizzi.dev_cards.model.user.utils.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class UserWorkspacesRoles {

    private Long workspace_id;
    private Role role;
    private List<Long> assigned_tasks_ids;



}
