package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemEntity;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemMapper;
import com.ltizzi.dev_cards.model.calendar.utils.HourRange;
import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.repository.UserCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */

@Component
public class UserCalendarMapper {

    @Autowired
    private UserCalendarRepository  ucRepo;

    @Autowired
    @Lazy
    private UserMapper userMapper;

    @Autowired
    @Lazy
    private CalendarItemMapper itemMapper;

    public UserCalendarDTO toUserCalendarDTO(UserCalendarEntity uc){
        UserCalendarDTO dto = new UserCalendarDTO();
        dto.setCalendar_id(uc.getCalendar_id());
        dto.setOwner(userMapper.toUserLiteDTO(uc.getOwner()));

        Map<String, Map<HourRange, CalendarItemDTO>> items = new HashMap<>();

        items = uc.getItems()
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

    public UserCalendarEntity toUserCalendarEntity(UserCalendarDTO dto) throws NotFoundException {
        UserCalendarEntity uc = new UserCalendarEntity();
        if(dto.getCalendar_id() != null){
            uc = ucRepo.findById(dto.getCalendar_id()).orElseThrow(()->new NotFoundException("Calendar not found"));
        }
        uc.setOwner(userMapper.toUserEntity(dto.getOwner()));
        List<CalendarItemEntity> items = dto.getItems()
                .entrySet()
                .stream()
                .flatMap(entry->{
                    String date = entry.getKey();
                    Map<HourRange, CalendarItemDTO> innermap = entry.getValue();
                    return innermap.entrySet()
                            .stream()
                            .map(innerEntry->{
                                CalendarItemDTO ciDto = innerEntry.getValue();
                                try {
                                    return itemMapper.toCalendarItemEntity(ciDto);
                                } catch (NotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }).toList();
        uc.setItems(items);
        return uc;
    }
}
