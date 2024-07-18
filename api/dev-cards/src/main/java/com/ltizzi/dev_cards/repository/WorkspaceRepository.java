package com.ltizzi.dev_cards.repository;


import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



/**
 * @author Leonardo Terlizzi
 */

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long>, PagingAndSortingRepository<WorkspaceEntity, Long> {

    Long countBy();

//    @Query("SELECT t FROM tasks t WHERE t.workspace.workspace_id = :workspace_id")
//    Optional<List<TaskEntity>> findTasksByWorkspaceId(@Param("workspace_id") Long workspace_id);


//
//    @Query("SELECT w.users FROM workspaces W WHERE w.workspace_id = :workspace_id")
//    Optional<List<UserEntity>> findUsersByWorkspaceId(@Param("workspace_id") Long workspace_id);
}
