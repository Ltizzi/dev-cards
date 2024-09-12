package com.ltizzi.dev_cards.model.customConfiguration.utils;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@Entity
@Data
@NoArgsConstructor
public class TagPool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tag_pool_id;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "configuration_tags"))
    private List<UITag> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "special_tags", joinColumns = @JoinColumn(name = "configuration_special_tags"))
    private List<SpecialTag> specialTags = new ArrayList<>();

}
