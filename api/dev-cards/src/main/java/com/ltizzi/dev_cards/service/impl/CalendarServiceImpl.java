package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.calendar.*;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemEntity;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemMapper;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.repository.CalendarItemRepository;
import com.ltizzi.dev_cards.repository.UserCalendarRepository;
import com.ltizzi.dev_cards.repository.WorkspaceCalendarRepository;
import com.ltizzi.dev_cards.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private UserCalendarRepository userCalRepo;

    @Autowired
    private WorkspaceCalendarRepository wsCalRepo;

    @Autowired
    private CalendarItemRepository itemRepo;

    @Autowired
    private UserCalendarMapper userCalMapper;

    @Autowired
    private WorkspaceCalendarMapper wsCalMapper;

    @Autowired
    private CalendarItemMapper itemMapper;

    @Override
    public UserCalendarDTO getUserCalendarById(UUID id) throws NotFoundException {
        return userCalMapper.toUserCalendarDTO(userCalRepo.findById(id).orElseThrow(()-> new NotFoundException("Calendar not found!")));
    }

    @Override
    public WorkspaceCalendarDTO getWorkspaceCalendarById(UUID id) throws NotFoundException {
        return wsCalMapper.toWorkspaceCalendarDTO(wsCalRepo.findById(id).orElseThrow(()-> new NotFoundException("Calendar not found!")));
    }

    @Override
    public UserCalendarDTO getUserCalendarByUserId(Long id) throws NotFoundException {
        UserCalendarEntity userCalendar = userCalRepo.findCalendarByUserId(id).getFirst();
        if(userCalendar == null){
            throw  new NotFoundException("Calendar not found!");
        }
        return userCalMapper.toUserCalendarDTO(userCalendar);
    }

    @Override
    public WorkspaceCalendarDTO getWorkspaceCalendarByWsId(Long id) throws NotFoundException {
        WorkspaceCalendarEntity wsCalendar = wsCalRepo.findCalendarByWorkspaceId(id).getFirst();
        if(wsCalendar == null){
            throw  new NotFoundException("Calendar not found!");
        }
        return wsCalMapper.toWorkspaceCalendarDTO(wsCalendar);
    }

    @Override
    public UserCalendarDTO addItemToUserCalendar(UUID calendar_id, CalendarItemDTO item) throws NotFoundException {
        UserCalendarEntity uc = userCalRepo.findById(calendar_id).orElseThrow(()-> new NotFoundException("Calendar not found!"));
        return userCalMapper.toUserCalendarDTO((uc.addItemToCalendar(itemMapper.toCalendarItemEntity(item))));
    }

    @Override
    public WorkspaceCalendarDTO addItemToWorkspaceCalendar(UUID ws_calendar_id, CalendarItemDTO item) throws NotFoundException {
        WorkspaceCalendarEntity ws_cal = wsCalRepo.findById(ws_calendar_id).orElseThrow(()-> new NotFoundException("Calendar not found!!"));
        return wsCalMapper.toWorkspaceCalendarDTO(ws_cal.addItemToCalendar(itemMapper.toCalendarItemEntity(item)));
    }

    @Override
    public CalendarItemDTO updateCalendarItem(UUID id, CalendarItemDTO item) throws NotFoundException {
        CalendarItemEntity ci = itemRepo.findById(id).orElseThrow(()-> new NotFoundException("Calendar item not found!"));
        ci = itemMapper.toCalendarItemEntity(item);
        return itemMapper.toCalendarItemDTO(itemRepo.save(ci));
    }

    @Override
    public APIResponse removeCalendarItem(UUID id) {
        APIResponse res = new APIResponse();
        res.setHttp_method("DELETE");
        CalendarItemEntity ci = itemRepo.findById(id).orElse(null);
        if(ci== null){
            res.setMessage("FAIL-Calendar Item not Found!");
            return res;
        }
        else{
            itemRepo.deleteById(id);
            res.setMessage("OK-Calendar Item "+ id.toString()+" deleted!");
            return res;
        }
    }
}
