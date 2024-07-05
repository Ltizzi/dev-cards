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
public interface UserRepository extends JpaRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {

    Long countBy();

//    @Query("SELECT user.workspaces FROM users user WHERE user.user_id = :user_id")
//    Optional<List<WorkspaceEntity>> findWorkspacesByUserId(@Param("user_id") Long user_id);
//
//    @Query("SELECT user.created_tasks FROM users user WHERE user.user_id = :user_id")
//    Optional<List<TaskEntity>> findCreatedTasksByUserId(@Param("user_id") Long user_id);
//
//    @Query("SELECT user.designated_tasks FROM users user WHERE user.user_id = :user_id")
//    Optional<List<TaskEntity>> findDesignatedTasks(@Param("user_id") Long user_id);
}
