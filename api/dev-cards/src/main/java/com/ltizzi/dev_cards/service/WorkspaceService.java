package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.InvalidWorkspaceException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */


public interface WorkspaceService {

    public List<WorkspaceDTO> getWorkspaces(int page, int limit);

    public WorkspaceDTO getWorkspaceDTOById(Long id) throws NotFoundException;

    public WorkspaceDTO saveWorkspace(WorkspaceDTO workspace) throws InvalidWorkspaceException;

    public WorkspaceDTO updateWorkspace(Long workspace_id, WorkspaceDTO workspace) throws NotFoundException, InvalidWorkspaceException;

    public APIResponse deleteWorkspace(Long workspace_id) throws  NotFoundException;
}
