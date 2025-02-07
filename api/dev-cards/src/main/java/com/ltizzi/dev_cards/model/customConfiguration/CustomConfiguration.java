package com.ltizzi.dev_cards.model.customConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.model.customConfiguration.utils.*;
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

//    @ElementCollection
////    @CollectionTable(name = "special_tags", joinColumns = @JoinColumn(name = "workspace_special_tags"))
////    private List<SpecialTag> specialTags = new ArrayList<>();

    @OneToOne(cascade= CascadeType.PERSIST)
    @JoinColumn(name = "tag_pool", referencedColumnName = "tag_pool_id")
    private TagPool tagPool;

    @ElementCollection
    @CollectionTable(name ="flagged_tasks", joinColumns = @JoinColumn(name = "workspace_flagged_tasks"))
    private List<MiniTaskDTO> flagged_tasks = new ArrayList<>();


    //private ThemeData themeData;


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

    public void createTagPool(){
        this.setTagPool(new TagPool());
    }


//TODO: quizas cambiar void por algún tipo de retorno (aunuqe en el service se devuelve la lista completa)
    public void addSpecialTag(SpecialTag tag){
        tag.setId(idGenerator.generateRandomLong());
        List<SpecialTag>tags = new ArrayList<>();
        if(this.getTagPool() != null){
            tags = tagPool.getSpecialTags();
        }
        else {
            createTagPool();
        }
        //List<SpecialTag> tags = tagPool.getSpecialTags();
        boolean already_exist = tags
                .stream()
                .filter(t->t.getName().equalsIgnoreCase(tag.getName()))
                .collect(Collectors.toList())
                .size() > 0;
        if(!already_exist){
            tags.add(tag);
            tagPool.setSpecialTags(tags);
        }

//        return specialTags.contains(tag);
    }

    public void removeSpecialTag(Long id){
        if(tagPool.getSpecialTags().stream().filter(t->t.getId().equals(id)).collect(Collectors.toList()).size()>0){
            List<SpecialTag> tags = tagPool.getSpecialTags();
            tags = tags.stream()
                    .filter(t->!t.getId().equals(id)).collect(Collectors.toList());
            tagPool.setSpecialTags(tags);
            //return true;
        }
        //else return false;
    }

    public void updateSpecialTag(Long id, SpecialTag tag){
        if(tagPool.getSpecialTags().contains(tag)){
            List<SpecialTag>tags = tagPool.getSpecialTags().stream()
                    .map(t->{
                        if(t.getId().equals(id)){
                            t = tag;
                        }
                        return t;
                    }).collect(Collectors.toList());
            tagPool.setSpecialTags(tags);
            //return true;
        }
        //return false;
    }

    public void addTagToPool(UITag tag){
        List<UITag> tags = new ArrayList<>();
        if(this.getTagPool()!=null){
            tags = tagPool.getTags();
        }
        boolean alreadyAdded = tags
                .stream()
                .filter(t->t.getName().equalsIgnoreCase(tag.getName()))
                .collect(Collectors.toList())
                .size() >0;
        if(!alreadyAdded) {
            tags.add(tag);
            tagPool.setTags(tags);
        }
    }

    public void removeTagFromPool(UITag tag){
        List<UITag> tags = tagPool
                .getTags()
                .stream()
                .filter(t->
                        !t.getName()
                                .equalsIgnoreCase(tag.getName()))
                .collect(Collectors.toList());
        tagPool.setTags(tags);
    }

    public void updateTagFromPool(UITag tag){
        List<UITag> tags = tagPool.getTags().stream().map(t->{
            if(t.getName().equalsIgnoreCase(tag.getName())){
                t = tag;
            }
            return t;
        }).collect(Collectors.toList());
        tagPool.setTags(tags);
    }


}
