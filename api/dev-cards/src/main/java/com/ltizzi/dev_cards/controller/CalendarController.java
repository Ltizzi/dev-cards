package com.ltizzi.dev_cards.controller;

import com.ltizzi.dev_cards.exception.NotAllowedException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.calendar.UserCalendarDTO;
import com.ltizzi.dev_cards.model.calendar.WorkspaceCalendarDTO;
import com.ltizzi.dev_cards.model.calendar.utils.CalendarItemDTO;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Leonardo Terlizzi
 */
@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calServ;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/userById")
    @ResponseBody
    public ResponseEntity<UserCalendarDTO> getUserCalendarById(@RequestParam UUID id,
                                                               @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        UserEntity user = jwtUtils.getUserByToken(token);
        UserCalendarDTO dto = calServ.getUserCalendarById(id);
        if(!user.getUser_id().equals(dto.getOwner().getUser_id())){
            throw  new NotAllowedException("User can't access other user calendar");
        }
        else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    @GetMapping("/WsById")
    @ResponseBody
    public ResponseEntity<WorkspaceCalendarDTO> getWorkspaceCalendarById(@RequestParam UUID id,
                                                                         @RequestHeader("Authorization") String token,
                                                                         @RequestParam Long ws_id,
                                                                         @RequestParam Long user_id) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkUserIsInSameGroup(ws_id,user_id,token)){
            throw  new NotAllowedException("User can't check calendar from workspace because isn't a member");
        }
        else{
            return new ResponseEntity<>(calServ.getWorkspaceCalendarById(id), HttpStatus.OK);
        }
    }

    @GetMapping("/byUserId")
    @ResponseBody
    public ResponseEntity<UserCalendarDTO> getCalendarByUserId(@RequestParam Long user_id,
                                                               @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserIsSelf(user_id, token)){
            throw  new NotAllowedException("User can't check other user calendar");
        }
        else{
            return new ResponseEntity<>(calServ.getUserCalendarByUserId(user_id), HttpStatus.OK);
        }
    }

    @GetMapping("/byWsId")
    @ResponseBody
    public ResponseEntity<WorkspaceCalendarDTO> getCalendarByWsId(@RequestParam Long ws_id,
                                                                  @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsMember(ws_id, token)){
            throw  new NotAllowedException("User can't access workspace Calendar (isnt a member)");
        } else{
            return new ResponseEntity<>(calServ.getWorkspaceCalendarByWsId(ws_id), HttpStatus.OK);
        }
    }

    @PostMapping("/userAddItem")
    @ResponseBody
    public ResponseEntity<UserCalendarDTO> addItemToUserCalendar(@RequestParam UUID calendar_id,
                                                                 @RequestBody CalendarItemDTO item,
                                                                 @RequestParam Long user_id,
                                                                 @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkUserIsSelf(user_id, token)){
            throw new NotAllowedException("User can't access other user calendar");
        }else{
            return new ResponseEntity<>(calServ.addItemToUserCalendar(calendar_id, item), HttpStatus.OK);
        }
    }

    @PatchMapping("/userUpdateItem")
    @ResponseBody
    public ResponseEntity<CalendarItemDTO> updateUserCalendarItem(@RequestParam Long user_id,
                                                                  @RequestParam UUID calendar_id,
                                                                  @RequestBody CalendarItemDTO item,
                                                                  @RequestHeader("Authorization")String token
                                                                  ) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkUserIsSelf(user_id, token)){
            throw  new NotAllowedException("User is not calendar's owner");
        }else{
            return new ResponseEntity<>(calServ.updateCalendarItem(calendar_id, item), HttpStatus.OK)
        }
    }

    @DeleteMapping("/userRemoveItem")
    @ResponseBody
    public ResponseEntity<APIResponse> removeUserCalendarItem(@RequestParam Long user_id,
                                                              @RequestParam UUID calendar_id,
                                                              @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkUserIsSelf(user_id, token)){
            throw  new NotAllowedException("User cannot modify or delete other user's calendars");
        }else{
            return new ResponseEntity<>(calServ.removeCalendarItem(calendar_id), HttpStatus.OK);
        }
    }

    @PostMapping("/wsAddItem")
    @ResponseBody
    public ResponseEntity<WorkspaceCalendarDTO> addItemToWsCalendar(@RequestParam Long ws_id,
                                                                    @RequestParam UUID ws_calendar_id,
                                                                    @RequestBody CalendarItemDTO item,
                                                                    @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id,token)){
            throw  new NotAllowedException("User can't modifiy workspace's calendar");
        }else{
            return new ResponseEntity<>(calServ.addItemToWorkspaceCalendar(ws_calendar_id, item), HttpStatus.OK);
        }
    }

    @PatchMapping("/wsUpdateItem")
    @ResponseBody
    public ResponseEntity<CalendarItemDTO> updateWorkspaceCalendarItem(@RequestParam Long ws_id,
                                                                       @RequestParam UUID calendar_id,
                                                                       @RequestBody CalendarItemDTO item,
                                                                       @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User doesn't have permissions to modify workspace's calendar");
        }else{
            return new ResponseEntity<>(calServ.updateCalendarItem(calendar_id,item), HttpStatus.OK);
        }
    }

    @DeleteMapping("/wsRemoveItem")
    @ResponseBody
    public ResponseEntity<APIResponse> deleteWorkspaceCalendarItem(@RequestParam Long ws_id,
                                                                   @RequestParam UUID calendar_id,
                                                                   @RequestHeader("Authorization")String token) throws NotAllowedException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User doesn't have credentials to modify or remove workspace's calendar items ");
        }else{
            return new ResponseEntity<>(calServ.removeCalendarItem(calendar_id), HttpStatus.OK);
        }
    }
}
