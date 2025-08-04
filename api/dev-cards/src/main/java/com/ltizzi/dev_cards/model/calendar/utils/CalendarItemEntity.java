package com.ltizzi.dev_cards.model.calendar.utils;

import com.ltizzi.dev_cards.model.task.utils.Color;
import com.ltizzi.dev_cards.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "calendar_items")
public class CalendarItemEntity {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @PrePersist
    public void generateId(){
        if(this.id == null){
            this.id = UUID.randomUUID();
        }
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    private String title;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private Color color;

    @ElementCollection
    @CollectionTable(name = "calendar_item_hour_range", joinColumns = @JoinColumn(name = "calendar_id"))
    private HourRange hourRange;

    private String date;


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;


    public CalendarItemEntity update(CalendarItemEntity updatedItem){
        this.owner = updatedItem.getOwner();
        this.title = updatedItem.getTitle();
        this.description = updatedItem.getDescription();
        this.location = updatedItem.getLocation();
        this.color = updatedItem.getColor();
        this.hourRange = updatedItem.getHourRange();
        this.date = updatedItem.getDate();
        this.updated_at = new Timestamp(System.currentTimeMillis());
        return this;
    }



}
