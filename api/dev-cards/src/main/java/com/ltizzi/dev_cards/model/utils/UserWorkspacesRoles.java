package com.ltizzi.dev_cards.model.utils;

import com.ltizzi.dev_cards.model.user.utils.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class UserWorkspacesRoles {

    private Long workspace_id;
    private Role role;
    private List<UUID> assigned_tasks_ids;



}
