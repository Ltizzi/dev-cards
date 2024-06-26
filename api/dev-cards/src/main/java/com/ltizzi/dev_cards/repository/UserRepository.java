package com.ltizzi.dev_cards.repository;

import com.ltizzi.dev_cards.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leonardo Terlizzi
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {

    Long countBy();
}
