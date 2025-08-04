package com.ltizzi.dev_cards.model.calendar.utils;

import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.UserMapper;
import com.ltizzi.dev_cards.repository.CalendarItemRepository;
import com.ltizzi.dev_cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author Leonardo Terlizzi
 */

@Component
public class CalendarItemMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CalendarItemRepository ciRepo;

    public CalendarItemDTO toCalendarItemDTO(CalendarItemEntity ci){
        CalendarItemDTO dto = new CalendarItemDTO();
        dto.setId(ci.getId());
        dto.setTitle(ci.getTitle());
        dto.setDescription(ci.getDescription());
        dto.setColor(ci.getColor());
        dto.setLocation(ci.getLocation());
        dto.setDate(ci.getDate());
        dto.setCreated_at(ci.getCreated_at());
        dto.setUpdated_at(ci.getUpdated_at());
        dto.setHourRange(ci.getHourRange());
        dto.setOwner(userMapper.toUserLiteDTO(ci.getOwner()));

        return dto;
    }

    public CalendarItemEntity toCalendarItemEntity(CalendarItemDTO dto) throws NotFoundException {
        CalendarItemEntity ci = new CalendarItemEntity();
        if(dto.getId() != null){
            ci = ciRepo.findById(dto.getId()).orElseThrow(()-> new NotFoundException("Calendar Item not found!"));
            if(ci!=null){
                ci.setId(dto.getId());
            }
        }
        ci.setTitle(dto.getTitle());
        ci.setDescription(dto.getDescription());
        ci.setColor(dto.getColor());
        ci.setLocation(dto.getLocation());
        ci.setDate(dto.getDate());
        ci.setHourRange(dto.getHourRange());
        ci.setCreated_at(dto.getCreated_at());
        ci.setUpdated_at(dto.getUpdated_at());

        UserEntity user = userRepo.findById(dto.getOwner().getUser_id()).orElseThrow(()-> new NotFoundException("User not found!"));
        ci.setOwner(user);
        return ci;
    }
}
