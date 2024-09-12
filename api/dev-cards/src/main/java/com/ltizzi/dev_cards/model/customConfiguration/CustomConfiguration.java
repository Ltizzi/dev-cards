package com.ltizzi.dev_cards.model.customConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.model.customConfiguration.utils.CustomGlosary;
import com.ltizzi.dev_cards.model.customConfiguration.utils.SpecialTag;
import com.ltizzi.dev_cards.model.task.MiniTaskDTO;
import com.ltizzi.dev_cards.model.task.TaskLiteDTO;
import com.ltizzi.dev_cards.model.utils.RandomIdGenerator;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Leonardo Terlizzi
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "configurations")
public class CustomConfiguration {

    private static final RandomIdGenerator idGenerator = new RandomIdGenerator();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long config_id;

    @OneToOne
    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id")
    private WorkspaceEntity workspace;

//    @ElementCollection
//    @CollectionTable(name="custom_glosaries",
//                    joinColumns = @JoinColumn(name = "workspace_glosary"))
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workspace_glosary_id")
    private List<CustomGlosary> customGlosaries = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "special_tags", joinColumns = @JoinColumn(name = "workspace_special_tags"))
    private List<SpecialTag> specialTags = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name ="flagged_tasks", joinColumns = @JoinColumn(name = "workspace_flagged_tasks"))
    private List<MiniTaskDTO> flagged_tasks = new ArrayList<>();


    public List<MiniTaskDTO> addFlaggedTask(MiniTaskDTO task){
        if(!flagged_tasks.contains(task)){
            flagged_tasks.add(task);
        }
        return flagged_tasks;
    }

    public List<MiniTaskDTO> removeFlaggedTask(MiniTaskDTO task){
        if(flagged_tasks.contains(task)){
            flagged_tasks.remove(task);
        }
        return flagged_tasks;
    }

    public List<CustomGlosary> addGlosary(CustomGlosary glosary) throws JsonProcessingException {
        if(!customGlosaries.contains(glosary)){
            customGlosaries.add(glosary);
        }
        return customGlosaries;
    }

    public List<CustomGlosary> removeGlosary(CustomGlosary glosary){
        if(customGlosaries.stream().filter(g->g.getId().equals(glosary.getId())).collect(Collectors.toList()).size()>0){
            customGlosaries = customGlosaries.stream()
                    .filter(g->g.getId().equals(glosary.getId())).collect(Collectors.toList());
        }
        return customGlosaries;
    }

    public List<CustomGlosary> updateGlosary(Long id, CustomGlosary glosary){
            return customGlosaries.stream()
                    .map(g->{
                        if(g.getId().equals(id)){
                            g = glosary;
                        }
                        return g;
                    }).collect(Collectors.toList());
    }



    public boolean addSpecialTag(SpecialTag tag){
        tag.setId(idGenerator.generateRandomLong());
        specialTags.add(tag);
        return specialTags.contains(tag);
    }

    public boolean removeSpecialTag(Long id){
        if(specialTags.stream().filter(t->t.getId().equals(id)).collect(Collectors.toList()).size()>0){
            specialTags = specialTags.stream()
                    .filter(t->!t.getId().equals(id)).collect(Collectors.toList());
            return true;
        }
        else return false;
    }

    public boolean updateSpecialTag(Long id, SpecialTag tag){
        if(specialTags.contains(tag)){
            specialTags = specialTags.stream()
                    .map(t->{
                        if(t.getId().equals(id)){
                            t = tag;
                        }
                        return t;
                    }).collect(Collectors.toList());
            return true;
        }
        return false;
    }


}
