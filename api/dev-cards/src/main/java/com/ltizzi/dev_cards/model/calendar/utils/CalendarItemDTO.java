package com.ltizzi.dev_cards.model.calendar.utils;

import com.ltizzi.dev_cards.model.task.utils.Color;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */
@Data
@NoArgsConstructor
public class CalendarItemDTO {

    private UUID id;
    private UserLiteDTO owner;
    private String title;
    private String description;
    private String location;
    private Color color;
    private HourRange hourRange;
    private String date;
    private Timestamp created_at;
    private Timestamp updated_at;

}
