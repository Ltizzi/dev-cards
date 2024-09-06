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
public class SpecialTag {

    private Long id;
    private String value;
    private String name;
    private String description;
}
