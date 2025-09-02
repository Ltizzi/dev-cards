package com.ltizzi.dev_cards.repository;


import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Repository
public interface CalendarItemRepository extends JpaRepository<CalendarItemEntity, UUID> {

    @Query("SELECT ci FROM CalendarItemEntity ci WHERE ci.owner.user_id = :user_id")
    List<CalendarItemEntity> findCalendarItemByUserId(@Param("user_id")Long user_id);
}
