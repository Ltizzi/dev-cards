package com.ltizzi.dev_cards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ltizzi.dev_cards.exception.InvalidConfigurationException;
import com.ltizzi.dev_cards.exception.NotAllowedException;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.customConfiguration.ConfigurationDTO;
import com.ltizzi.dev_cards.model.customConfiguration.CustomConfiguration;
import com.ltizzi.dev_cards.model.customConfiguration.utils.*;
import com.ltizzi.dev_cards.model.utils.APIResponse;
import com.ltizzi.dev_cards.security.filter.JwtUtils;
import com.ltizzi.dev_cards.service.CustomConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leonardo Terlizzi
 */
@RestController
@RequestMapping("/config")
public class CustomConfigurationController {

    @Autowired
    private CustomConfigurationService configServ;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/byId")
    @ResponseBody
    public ResponseEntity<ConfigurationDTO> getConfigById(@RequestParam Long id,
                                                            @RequestParam Long ws_id,
                                                            @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't access workspace configuration");
        }
        else {
            return new ResponseEntity<>(configServ.getConfigurationById(id), HttpStatus.OK);
        }
    }

    @GetMapping("/byWorkspaceId")
    @ResponseBody
    public ResponseEntity<ConfigurationDTO> getConfigByWsId(@RequestParam Long ws_id,
                                                               @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw new NotAllowedException("User can't access workspace configuration");
        }
        else {
            return new ResponseEntity<>(configServ.getConfigurationByWorkspaceId(ws_id), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ConfigurationDTO> saveConfig(@RequestParam Long ws_id,
                                                          @RequestBody ConfigurationDTO config,
                                                          @RequestHeader("Authorization")String token) throws NotAllowedException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwner(ws_id, token)){
            throw new NotAllowedException("User can't access or modify workspace's configuration");
        }
        else {
            return new ResponseEntity<>(configServ.saveConfiguration(config), HttpStatus.OK);
        }
    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<ConfigurationDTO> updateConfig(@RequestParam Long ws_id,
                                                            @RequestParam Long config_id,
                                                            @RequestBody ConfigurationDTO config,
                                                            @RequestHeader("Authorization")String token) throws NotFoundException, InvalidConfigurationException, NotAllowedException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't access or modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.updateConfiguration(config_id,config), HttpStatus.OK);
        }
    }

    @DeleteMapping("/remove")
    @ResponseBody
    public ResponseEntity<APIResponse> removeConfigurationById(@RequestParam Long ws_id,
                                                               @RequestParam Long id,
                                                               @RequestHeader("Authorization")String token) throws NotFoundException, NotAllowedException {
        if(!jwtUtils.checkIsOwner(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.removeConfigurationById(id), HttpStatus.OK);
        }
    }

    @PostMapping("/addGlosary")
    @ResponseBody
    public ResponseEntity<List<CustomGlosary>> addCustomGlosary(@RequestParam Long ws_id,
                                                                @RequestParam Long config_id,
                                                                @RequestBody CustomGlosary glosary,
                                                                @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, JsonProcessingException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.addCustomGlosary(config_id, glosary), HttpStatus.OK);
        }
    }

    @DeleteMapping("/removeGlosary")
    @ResponseBody
    public ResponseEntity<List<CustomGlosary>> removeGlosary(@RequestParam Long ws_id,
                                                             @RequestParam Long config_id,
                                                             @RequestBody CustomGlosary glosary,
                                                             @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.removeCustomGlosary(config_id, glosary), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateGlosary")
    @ResponseBody
    public ResponseEntity<List<CustomGlosary>> updateGlosary(@RequestParam Long ws_id,
                                                             @RequestParam Long config_id,
                                                             @RequestParam Long id,
                                                             @RequestBody CustomGlosary glosary,
                                                             @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.updateGlosary(config_id, id, glosary), HttpStatus.OK);
        }
    }

    @PostMapping("/addSpecialTag")
    @ResponseBody
    public ResponseEntity<TagPool> addSpecialTag(@RequestParam  Long ws_id,
                                                 @RequestParam Long config_id,
                                                 @RequestBody SpecialTag tag,
                                                 @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration.");
        }
        else{
            return new ResponseEntity<>(configServ.addSpecialTag(config_id, tag), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteSpecialTag")
    @ResponseBody
    public ResponseEntity<TagPool> removeSpecialTag(@RequestParam Long ws_id,
                                                                @RequestParam Long config_id,
                                                                @RequestParam Long id,
                                                                @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else {
            return new ResponseEntity<>(configServ.removeSpecialTag(config_id, id ), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateSpecialTag")
    @ResponseBody
    public ResponseEntity<TagPool> updateSpecialTag(@RequestParam Long ws_id,
                                                                @RequestParam Long config_id,
                                                                @RequestParam Long id,
                                                                @RequestBody SpecialTag tag,
                                                                @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.updateSpecialTag(config_id, id, tag), HttpStatus.OK);
        }
    }

    @PostMapping("/addTag")
    @ResponseBody
    public ResponseEntity<TagPool> addTag(@RequestParam  Long ws_id,
                                                 @RequestParam Long config_id,
                                                 @RequestBody UITag tag,
                                                 @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration.");
        }
        else{
            return new ResponseEntity<>(configServ.addTagToPool(config_id, tag), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteTag")
    @ResponseBody
    public ResponseEntity<TagPool> removeTag(@RequestParam Long ws_id,
                                                    @RequestParam Long config_id,
                                                    @RequestParam UITag tag,
                                                    @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else {
            return new ResponseEntity<>(configServ.removeTagFromPool(config_id, tag ), HttpStatus.OK);
        }
    }

    @PatchMapping("/updateTag")
    @ResponseBody
    public ResponseEntity<TagPool> updateTag(@RequestParam Long ws_id,
                                                    @RequestParam Long config_id,
                                                    @RequestParam Long id,
                                                    @RequestBody UITag tag,
                                                    @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else{
            return new ResponseEntity<>(configServ.updateTagFromPool(config_id, tag), HttpStatus.OK);
        }
    }

    @PostMapping("/savePool")
    @ResponseBody
    public ResponseEntity<TagPool> saveTagPool(@RequestParam Long ws_id,
                                               @RequestParam Long config_id,
                                               @RequestBody TagPool pool,
                                               @RequestHeader("Authorization")String token) throws NotFoundException, InvalidConfigurationException, NotAllowedException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id, token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else {
            return new ResponseEntity<>(configServ.saveTagPool(config_id, pool), HttpStatus.OK);
        }
    }

    @PatchMapping("/theme")
    @ResponseBody
    public ResponseEntity<ThemeData> updateTheme(@RequestParam Long ws_id,
                                                 @RequestParam Long config_id,
                                                 @RequestBody ThemeData themeData,
                                                 @RequestHeader("Authorization")String token) throws NotAllowedException, NotFoundException, InvalidConfigurationException {
        if(!jwtUtils.checkIsOwnerOrModerator(ws_id,token)){
            throw  new NotAllowedException("User can't modify workspace's configuration");
        }
        else {
            return new ResponseEntity<>(configServ.saveThemeData(config_id,themeData), HttpStatus.OK);
        }
    }
}
