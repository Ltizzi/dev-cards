package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.calendar.WorkspaceCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */


public interface WorkspaceCalendarRepository extends JpaRepository<WorkspaceCalendarEntity, UUID> {

    @Query("SELECT wc FROM WorkspaceCalendarEntity wc WHERE wc.workspace = :workspace_id")
    List<WorkspaceCalendarEntity> findCalendarByWorkspaceId(@Param("workspace_id")Long workspace_id);
}
