package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemEntity;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemMapper;
import com.ltizzi.dev_cards.model.calendar.utils.HourRange;
import com.ltizzi.dev_cards.model.workspace.WorkspaceMapper;
import com.ltizzi.dev_cards.repository.WorkspaceCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */

@Component
public class WorkspaceCalendarMapper {

    @Autowired
    private WorkspaceCalendarRepository wsCalendarRepo;

    @Autowired
    @Lazy
    private WorkspaceMapper wsMapper;

    @Autowired
    @Lazy
    private CalendarItemMapper itemMapper;

    public WorkspaceCalendarDTO toWorkspaceCalendarDTO(WorkspaceCalendarEntity wsCalendar){
        WorkspaceCalendarDTO dto = new WorkspaceCalendarDTO();
        dto.setCalendar_id(wsCalendar.getCalendar_id());
        dto.setWorkspace(wsMapper.toWorkspaceLiteDTO(wsCalendar.getWorkspace()));

        Map<String, Map<HourRange, CalendarItemDTO>> items =  wsCalendar.getItems()
                .stream()
                .collect(Collectors.groupingBy(
                        CalendarItemEntity::getDate,
                        Collectors.toMap(
                                CalendarItemEntity::getHourRange,
                                item->itemMapper.toCalendarItemDTO(item)
                        )
                ));
        dto.setItems(items);
        return dto;
    }

    public WorkspaceCalendarEntity toWorkspaceCalendarEntity(WorkspaceCalendarDTO dto) throws NotFoundException {
        WorkspaceCalendarEntity wc = new WorkspaceCalendarEntity();
        if(dto.getCalendar_id() != null){
            wc = wsCalendarRepo.findById(dto.getCalendar_id()).orElseThrow(()-> new NotFoundException("Calendar not found!"));
        }
        wc.setWorkspace(wsMapper.toWorkSpaceEntity(dto.getWorkspace()));
        List<CalendarItemEntity> items = dto.getItems()
                .entrySet()
                .stream()
                .flatMap(entry->{
                    String date = entry.getKey();
                    Map<HourRange, CalendarItemDTO> innermap = entry.getValue();
                    return innermap.entrySet()
                            .stream()
                            .map(innerEntry->{
                                CalendarItemDTO ciDTO = innerEntry.getValue();
                                try{
                                    return itemMapper.toCalendarItemEntity(ciDTO);
                                } catch (NotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }).toList();
    wc.setItems(items);
    return wc;
    }
}
