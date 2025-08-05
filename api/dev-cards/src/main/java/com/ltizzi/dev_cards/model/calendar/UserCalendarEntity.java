package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Entity
@Table(name="user_calendars")
public class UserCalendarEntity extends CalendarEntity{


    @OneToOne
    @JoinColumn(name = "owner_id", unique = true)
    private UserEntity owner;


    public void setOwner(UserEntity owner){
        this.owner = owner;
    }

    public UserEntity getOwner(){
        return this.owner;
    }
}
