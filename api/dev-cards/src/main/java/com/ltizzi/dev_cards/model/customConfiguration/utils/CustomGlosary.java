package com.ltizzi.dev_cards.model.customConfiguration.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
public class CustomGlosary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;

    @ElementCollection
    @CollectionTable(name = "glosary_items",
            joinColumns = @JoinColumn(name = "custom_glosary_id"))
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "custom_glosary_id")
    private List<GlosaryItem> items = new ArrayList<>();

    public CustomGlosary addItem(GlosaryItem item){
        if(!items.contains(item)){
            items.add(item);
        }
        return this;
    }

    public CustomGlosary removeItem(int id){
        if(items.stream().filter(i->i.getId() == id).collect(Collectors.toList()).size()>0){
            items = items.stream().filter(i->i.getId() != id).collect(Collectors.toList());
        }
        return this;
    }

    public CustomGlosary updateItem(int id, GlosaryItem item){
        if(items.stream().filter(i->i.getId()== id).collect(Collectors.toList()).size()>0){
            items = items.stream()
                    .map(i-> {
                        if(i.getId()== id){
                            i.setValue(item.getValue());
                        }
                        return i;
                    }).collect(Collectors.toList());
        }
        return this;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(items);
    }

}
