package com.ltizzi.dev_cards.model.utils;

import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceDtoWithJwtResponse {

    private WorkspaceDTO workspace;
    private String token;

}
