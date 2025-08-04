package com.ltizzi.dev_cards.service;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.calendar.CalendarEntity;
import com.ltizzi.dev_cards.model.calendar.UserCalendarDTO;
import com.ltizzi.dev_cards.model.calendar.WorkspaceCalendarDTO;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.utils.APIResponse;

import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */


public interface CalendarService {

    public UserCalendarDTO getUserCalendarById(UUID id) throws NotFoundException;

    public WorkspaceCalendarDTO getWorkspaceCalendarById(UUID id) throws NotFoundException;

    public UserCalendarDTO getUserCalendarByUserId(Long id) throws NotFoundException;

    public WorkspaceCalendarDTO getWorkspaceCalendarByWsId(Long id) throws NotFoundException;

    public UserCalendarDTO addItemToUserCalendar(UUID calendar_id, CalendarItemDTO item) throws NotFoundException;

    public WorkspaceCalendarDTO addItemToWorkspaceCalendar(UUID ws_calendar_id, CalendarItemDTO item) throws NotFoundException;

    public CalendarItemDTO updateCalendarItem(UUID id, CalendarItemDTO item) throws NotFoundException;

    public APIResponse removeCalendarItem(UUID id);


}
