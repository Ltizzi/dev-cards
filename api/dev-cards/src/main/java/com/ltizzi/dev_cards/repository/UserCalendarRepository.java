package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.calendar.UserCalendarEntity;
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
public interface UserCalendarRepository extends JpaRepository<UserCalendarEntity, UUID> {

    @Query("SELECT uc FROM UserCalendarEntity uc WHERE uc.owner = :user_id")
    List<UserCalendarEntity> findCalendarByUserId(@Param("user_Id")Long user_id);
}
