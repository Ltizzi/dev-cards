package com.ltizzi.dev_cards.model.user.utils;

import com.ltizzi.dev_cards.model.user.UserDTO;
import lombok.*;

/**
 * @author Leonardo Terlizzi
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponse {

   private UserDTO user;
   private String token;
}
