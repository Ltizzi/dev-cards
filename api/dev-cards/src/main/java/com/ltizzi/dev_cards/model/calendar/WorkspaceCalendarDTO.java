package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.calendar.utils.HourRange;
import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */
@Data
@NoArgsConstructor
public class WorkspaceCalendarDTO {

    private UUID calendar_id;
    private WorkspaceLiteDTO workspace;
    private Map<String, Map<String, CalendarItemDTO>> items;
}
