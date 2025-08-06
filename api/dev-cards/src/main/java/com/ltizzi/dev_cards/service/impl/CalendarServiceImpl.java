package com.ltizzi.dev_cards.service.impl;

import com.ltizzi.dev_cards.exception.InvalidUserException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.calendar.*;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemEntity;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemMapper;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.repository.*;
import com.ltizzi.dev_cards.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */

@Slf4j
@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private UserCalendarRepository userCalRepo;

    @Autowired
    private WorkspaceCalendarRepository wsCalRepo;

    @Autowired
    private CalendarItemRepository itemRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WorkspaceRepository wsRepo;

    @Autowired
    private UserCalendarMapper userCalMapper;

    @Autowired
    private WorkspaceCalendarMapper wsCalMapper;

    @Autowired
    private CalendarItemMapper itemMapper;

    @Override
    public UserCalendarDTO getUserCalendarById(UUID id) throws NotFoundException {
        log.info("\nGetting user's calendar with id: {}", id);
        return userCalMapper.toUserCalendarDTO(userCalRepo.findById(id).orElseThrow(()-> new NotFoundException("Calendar not found!")));
    }

    @Override
    public WorkspaceCalendarDTO getWorkspaceCalendarById(UUID id) throws NotFoundException {
        log.info("\nGetting Workspace's calendar with id: {}", id);
        return wsCalMapper.toWorkspaceCalendarDTO(wsCalRepo.findById(id).orElseThrow(()-> new NotFoundException("Calendar not found!")));
    }

    @Override
    public UserCalendarDTO getUserCalendarByUserId(Long id) throws NotFoundException {
        log.info("\nGetting User's Calendar by User id: {}", id);
        UserCalendarEntity userCalendar = userCalRepo.findCalendarByUserId(id).getFirst();

        if(userCalendar == null){
            //throw  new NotFoundException("Calendar not found!");
            UserCalendarEntity newCal = new UserCalendarEntity();
            UserEntity user = userRepo.findById(id).orElse(null);
            if(user == null){
                log.error("\nUser with id {} not found!", id);
                throw  new NotFoundException("User not found!");
            } else {
                log.info("\nUser's has not a calendar, adding new one...");
                newCal.setCalendar_id(UUID.randomUUID());
                newCal.setOwner(user);
                return userCalMapper.toUserCalendarDTO(userCalRepo.save(newCal));
            }
        }
        return userCalMapper.toUserCalendarDTO(userCalendar);
    }

    @Override
    public WorkspaceCalendarDTO getWorkspaceCalendarByWsId(Long id) throws NotFoundException {
        WorkspaceCalendarEntity wsCalendar = wsCalRepo.findCalendarByWorkspaceId(id).getFirst();
        log.info("\nGetting the calendar of the Workspace with id: {}", id);
        if(wsCalendar == null){
            //throw  new NotFoundException("Calendar not found!");
            WorkspaceCalendarEntity wsCal = new WorkspaceCalendarEntity();
            WorkspaceEntity ws = wsRepo.findById(id).orElse(null);
            if(ws == null){
                log.error("\nWorkspaces with id {} not found!", id);
                throw  new NotFoundException("Workspace not found");
            } else{
                log.info("\nWorkspace's doesnt have a calendar, adding one...");
                wsCal.setCalendar_id(UUID.randomUUID());
                wsCal.setWorkspace(ws);
                return wsCalMapper.toWorkspaceCalendarDTO(wsCalRepo.save(wsCal));
            }
        }
        return wsCalMapper.toWorkspaceCalendarDTO(wsCalendar);
    }

    @Override
    public UserCalendarDTO addItemToUserCalendar(UUID calendar_id, CalendarItemDTO item) throws NotFoundException {
        log.info("\nAdding new calendar item to calendar with id {}", calendar_id);
        UserCalendarEntity uc = userCalRepo.findById(calendar_id).orElseThrow(()-> new NotFoundException("Calendar not found!"));
        return userCalMapper.toUserCalendarDTO((uc.addItemToCalendar(itemMapper.toCalendarItemEntity(item))));
    }

    @Override
    public WorkspaceCalendarDTO addItemToWorkspaceCalendar(UUID ws_calendar_id, CalendarItemDTO item) throws NotFoundException {
        log.info("\nAdding new calendar item to calendar with id {}", ws_calendar_id);
        WorkspaceCalendarEntity ws_cal = wsCalRepo.findById(ws_calendar_id).orElseThrow(()-> new NotFoundException("Calendar not found!!"));
        return wsCalMapper.toWorkspaceCalendarDTO(ws_cal.addItemToCalendar(itemMapper.toCalendarItemEntity(item)));
    }

    @Override
    public CalendarItemDTO updateCalendarItem(UUID id, CalendarItemDTO item) throws NotFoundException {
        log.info("\nUpdating calendar item with id {} to calendar id {}", item.getId(), id);
        CalendarItemEntity ci = itemRepo.findById(id).orElseThrow(()-> new NotFoundException("Calendar item not found!"));
        ci = itemMapper.toCalendarItemEntity(item);
        return itemMapper.toCalendarItemDTO(itemRepo.save(ci));
    }

    @Override
    public APIResponse removeCalendarItem(UUID id) {
        log.info("\nDeleting calendar item with id {}", id);
        APIResponse res = new APIResponse();
        res.setHttp_method("DELETE");
        CalendarItemEntity ci = itemRepo.findById(id).orElse(null);
        if(ci== null){
            log.error("\nCalendar item with id {} not found!", id);
            res.setMessage("FAIL-Calendar Item not Found!");
            return res;
        }
        else{
            itemRepo.deleteById(id);
            log.info("\nRemoved!");
            res.setMessage("OK-Calendar Item "+ id.toString()+" deleted!");
            return res;
        }
    }
}
