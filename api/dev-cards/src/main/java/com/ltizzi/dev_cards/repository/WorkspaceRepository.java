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
}
