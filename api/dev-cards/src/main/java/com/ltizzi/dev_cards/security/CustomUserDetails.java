package com.ltizzi.dev_cards.security;

import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.utils.Role;
import com.ltizzi.dev_cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password = null;
        List<GrantedAuthority> authorities = null;
        List<UserEntity> users = userRepo.findByUsername(username);
        if(users.isEmpty()){
            throw new UsernameNotFoundException("User details not found for the user: " + username);
        }
        else{
            UserEntity user = users.get(0);
            userName = user.getUsername();
            password = user.getPassword();
            authorities = new ArrayList<>();

            for (Role role: user.getRoles()){
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }

        }
        return new User(userName, password, authorities);
    }
}
