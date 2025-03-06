package com.ltizzi.dev_cards.model.customConfiguration.utils;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leonardo Terlizzi
 */
@Embeddable
@Data
@NoArgsConstructor
public class UITag {

    //TODO: implementar id
    //private String id;
    private String name;
    private String color;
}
