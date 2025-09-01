package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.calendar.utils.HourRange;
import com.ltizzi.dev_cards.model.user.UserLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class UserCalendarDTO {

    private UUID calendar_id;
    private UserLiteDTO owner;
    private Map<String,Map<String, CalendarItemDTO> >items;
}
