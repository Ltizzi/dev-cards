package com.ltizzi.dev_cards.security.utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltizzi.dev_cards.model.utils.UserWorkspacesRoles;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */

public class CustomJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt token) {
        ObjectMapper mapper = new ObjectMapper();
        List<UserWorkspacesRoles> roles = null;//token.getClaim("roles");

        try{
            String json = mapper.writeValueAsString(token.getClaim("roles"));
            roles = mapper.readValue(json, new TypeReference<List<UserWorkspacesRoles>>() {});
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(roles);
        return roles.stream()
                .map(role->new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());
    }
}
