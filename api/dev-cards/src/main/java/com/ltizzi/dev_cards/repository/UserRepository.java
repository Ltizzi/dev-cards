package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {

    Long countBy();


    List<UserEntity> findByEmail(String email);

    List<UserEntity> findByUsername(String username);

//    @Query("SELECT user.workspaces FROM users user WHERE user.user_id = :user_id")
//    Optional<List<WorkspaceEntity>> findWorkspacesByUserId(@Param("user_id") Long user_id);
//
//    @Query("SELECT user.created_tasks FROM users user WHERE user.user_id = :user_id")
//    Optional<List<TaskEntity>> findCreatedTasksByUserId(@Param("user_id") Long user_id);
//
//    @Query("SELECT user.designated_tasks FROM users user WHERE user.user_id = :user_id")
//    Optional<List<TaskEntity>> findDesignatedTasks(@Param("user_id") Long user_id);
}
