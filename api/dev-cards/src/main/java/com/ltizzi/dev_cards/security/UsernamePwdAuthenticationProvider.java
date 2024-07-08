package com.ltizzi.dev_cards.security;

import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.utils.Role;
import com.ltizzi.dev_cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Terlizzi
 */

@Component
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<UserEntity> user = userRepo.findByUsername(username);
        if(!user.isEmpty()){
            if(pwdEncoder.matches(pwd, user.get(0).getPassword())){
                return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(user.get(0).getRoles()) );
            }
            else {
                throw new BadCredentialsException("Invalid password!");
            }
        } else {
            throw new BadCredentialsException("No user register with these details");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role: roles){
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        return authorities;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
