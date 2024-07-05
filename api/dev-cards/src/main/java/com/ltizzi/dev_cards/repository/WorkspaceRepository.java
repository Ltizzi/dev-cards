package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Leonardo Terlizzi
 */

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long>, PagingAndSortingRepository<WorkspaceEntity, Long> {

    Long countBy();

//    @Query("SELECT w.tasks FROM workspaces w WHERE w.workspace_id = :workspace_id")
//    Optional<List<TaskEntity>> findTasksByWorkspaceId(@Param("workspace_id") Long workspace_id);
//
//    @Query("SELECT w.users FROM workspaces W WHERE w.workspace_id = :workspace_id")
//    Optional<List<UserEntity>> findUsersByWorkspaceId(@Param("workspace_id") Long workspace_id);
}
