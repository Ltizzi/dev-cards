package com.ltizzi.dev_cards.model.customConfiguration.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */
@NoArgsConstructor
@Data
@Embeddable
@AllArgsConstructor
public class GlosaryItem {


    private int id;
    private String key;
    private String value;

}
