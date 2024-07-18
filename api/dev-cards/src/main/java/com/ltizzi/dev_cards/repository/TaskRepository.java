package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>,
        PagingAndSortingRepository<TaskEntity,Long> {

    Long countBy();

    @Query("SELECT t FROM TaskEntity t WHERE t.workspace = :workspace_id")
    List<TaskEntity> findTasksByWorkspaceId(@Param("workspace_id") Long workspace_id);
}
