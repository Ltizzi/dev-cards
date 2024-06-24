package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leonardo Terlizzi
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>,
        PagingAndSortingRepository<TaskEntity,Long> {

    Long countBy();
}
