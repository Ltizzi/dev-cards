package com.ltizzi.dev_cards.model.calendar.utils;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Leonardo Terlizzi
 */
@Embeddable
@Data
@NoArgsConstructor
public class HourRange {

    private String start_at;
    private String end_at;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof  HourRange)) return false;
        HourRange that = (HourRange) o;
        return Objects.equals(start_at, that.start_at) && Objects.equals(end_at, that.end_at);
    }

    @Override
    public int hashCode(){
        return Objects.hash(start_at, end_at);
    }


}
