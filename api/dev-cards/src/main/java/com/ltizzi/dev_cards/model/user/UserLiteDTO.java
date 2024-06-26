package com.ltizzi.dev_cards.model.user;

import com.ltizzi.dev_cards.model.workspace.WorkspaceLiteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class UserLiteDTO {

    private Long user_id;
    private String username;
    private String email;
    private String avatar;


}
