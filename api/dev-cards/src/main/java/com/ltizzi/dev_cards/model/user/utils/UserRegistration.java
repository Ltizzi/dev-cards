package com.ltizzi.dev_cards.model.user.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class UserRegistration {

    private String username;
    private String email;
    private String password;
}
