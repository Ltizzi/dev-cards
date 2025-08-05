package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.exception.InvalidCalendarException;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public abstract class CalendarEntity {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID calendar_id;
    @PrePersist
    public void generateId(){
        if(this.calendar_id == null){
            this.calendar_id = UUID.randomUUID();
        }
    }


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="calendar_id")
    private List<CalendarItemEntity> items = new ArrayList<>();


    public <T extends CalendarEntity> T addItemToCalendar(CalendarItemEntity item){
        if(!items.contains(item)){
            items.add(item);
            return (T) this;
        }
        else throw  new InvalidCalendarException("Calendar already has item");
    }


}
