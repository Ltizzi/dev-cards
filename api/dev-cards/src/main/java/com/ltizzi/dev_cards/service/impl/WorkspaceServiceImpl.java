package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidTaskException;
import com.ltizzi.dev_cards.exception.InvalidWorkspaceException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceDTO;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import com.ltizzi.dev_cards.repository.WorkspaceRepository;
import com.ltizzi.dev_cards.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository wsRepo;

    @Autowired
    private WorkspaceMapper wsMapper;

    @Override
    public List<WorkspaceDTO> getWorkspaces(int page, int limit) {
        PageRequest pageReq = PageRequest.of(page, limit);
        Page<WorkspaceEntity> wsPage = wsRepo.findAll(pageReq);
        return wsMapper.toArrayWorkSpaceDTO(wsPage.getContent());
    }

    @Override
    public WorkspaceDTO getWorkspaceDTOById(Long id) throws NotFoundException {
        return wsMapper.toWorkspaceDTO(wsRepo.findById(id).orElseThrow());
    }

    @Override
    public WorkspaceDTO saveWorkspace(WorkspaceDTO workspace) throws InvalidWorkspaceException {
        return wsMapper.toWorkspaceDTO(wsRepo.save(wsMapper.toWorkspaceEntity(workspace)));
    }

    @Override
    public WorkspaceDTO updateWorkspace(Long workspace_id, WorkspaceDTO workspace) throws NotFoundException, InvalidWorkspaceException {
        WorkspaceDTO old_ws = getWorkspaceDTOById(workspace_id);
        if(old_ws !=null && old_ws.getWorkspace_id().equals(workspace.getWorkspace_id())){
            return wsMapper.toWorkspaceDTO(wsRepo.save(wsMapper.toWorkspaceEntity(workspace)));
        }
        else throw  new InvalidWorkspaceException("Something went wrong");
    }

    @Override
    public APIResponse deleteWorkspace(Long workspace_id) throws NotFoundException {
        APIResponse apiRes = new APIResponse();
        apiRes.setHttp_method("DELETE");
        WorkspaceDTO ws = getWorkspaceDTOById(workspace_id);
        if(ws!=null){
            wsRepo.deleteById(workspace_id);
            apiRes.setMessage("Workspace deleted!");
        }
        else{
            apiRes.setMessage("Workspace not found!");
        }
        return apiRes;
    }
}
