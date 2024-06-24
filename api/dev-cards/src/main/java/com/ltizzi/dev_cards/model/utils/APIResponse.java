package com.ltizzi.dev_cards.model.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */

@Data
@NoArgsConstructor
public class APIResponse {

    private String http_method;
    private String message;
}
