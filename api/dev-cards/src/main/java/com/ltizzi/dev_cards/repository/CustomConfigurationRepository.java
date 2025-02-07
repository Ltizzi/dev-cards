package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.customConfiguration.CustomConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * @author Leonardo Terlizzi
 */

@Repository
public interface CustomConfigurationRepository  extends JpaRepository<CustomConfiguration, Long> {

    @Query("SELECT t FROM CustomConfiguration t WHERE t.workspace.workspace_id = :workspace_id")
    CustomConfiguration findConfigurationByWorkspaceId(@Param("workspace_id") Long workspace_id);
}
