package com.ltizzi.dev_cards.security.filter;

import com.ltizzi.dev_cards.model.utils.UserWorkspacesRoles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @author Leonardo Terlizzi
 */

public class JwtValidaionFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        validateToken(request);

        filterChain.doFilter(request, response);
    }

    private void validateToken(HttpServletRequest request){
        //UserWorkspacesRoles roles = jwtUtils.getRoles(request.getHeader("Authorization"));


        Map<String, String[]> parameters = request.getParameterMap();
        String pathInfo = request.getPathInfo();


//        for(UserWorkspacesRoles role: roles){
//
//        }
    }

}
