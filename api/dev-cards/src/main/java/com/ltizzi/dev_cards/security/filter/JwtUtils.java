package com.ltizzi.dev_cards.security.filter;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.ltizzi.dev_cards.exception.NotFoundException;
import com.ltizzi.dev_cards.model.task.TaskEntity;
import com.ltizzi.dev_cards.model.user.UserEntity;
import com.ltizzi.dev_cards.model.user.utils.Role;
import com.ltizzi.dev_cards.model.utils.UserWorkspacesRoles;
import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import com.ltizzi.dev_cards.repository.TaskRepository;
import com.ltizzi.dev_cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

//import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class JwtUtils {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TaskRepository taskRepo;

    private final JwtEncoder encoder;
 //   @Autowired
    private final JwtDecoder jwtDecoder;//private JwtDecoder jwtDecoder;

    public JwtUtils(JwtEncoder encoder, JwtDecoder decoder){
        this.encoder = encoder;
        this.jwtDecoder = decoder;

    }

    public String generateToken(Authentication authentication, String username, List<UserWorkspacesRoles> roles){
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));
        //System.out.println(scope);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofDays(7)))
                .subject(username)
                .claim("scope", scope)
                .claim( "roles", roles)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String regenerateToken(String token) throws NotFoundException {
        Instant now = Instant.now();
        String scope = getScope(token);
        UserEntity user = getUserByToken(token);
        String username = user.getUsername();
        List<UserWorkspacesRoles> roles = getUserRoles(user);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofDays(7)))
                .subject(username)
                .claim("scope", scope)
                .claim("roles", roles)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public String extractUsername(String token){
        return jwtDecoder.decode(token).getClaimAsString("sub");
    }

    public List<UserWorkspacesRoles> getRoles(String token){
        Map<String, Object> rolesClaim = jwtDecoder.decode(token).getClaims();
        ObjectMapper objectMapper = new ObjectMapper();
        Object rolesObject = rolesClaim.get("roles");

        List<UserWorkspacesRoles> uwrs = objectMapper.convertValue(rolesObject, new TypeReference<List<UserWorkspacesRoles>>() {});

//        Type listType = new TypeToken<List<Map<String,Object>>>() {}.getType();
//        Gson gson = new Gson();
//        List<Map<String,Object>>rolesList = gson.fromJson(gson.toJson(rolesObject), listType);
//        Type userWorkspaceRolesType = new TypeToken<List<UserWorkspacesRoles>>() {}.getType();
//        List<UserWorkspacesRoles> uwrs = gson.fromJson(gson.toJson(rolesList), userWorkspaceRolesType);
        return uwrs;
    }

    public String getScope(String token){
        Map<String, Object> scopeCLaim = jwtDecoder.decode(token).getClaims();
        ObjectMapper objMapper = new ObjectMapper();
        Object scopeObj = scopeCLaim.get("scope");
        return  objMapper.convertValue(scopeObj, new TypeReference<String>() {});
    }

    public UserEntity getUserByToken(String token) throws NotFoundException {
        List<UserEntity> user = userRepo.findByUsername(extractUsername(token));
        if(user.get(0)!= null){
            return user.get(0);
        }
        else throw  new NotFoundException("Username is not valid");
    }

    public boolean checkUserCanAccessTask(String token, Long task_id) throws NotFoundException {
        UserEntity user = getUserByToken(token);
        TaskEntity task = taskRepo.findById(task_id).orElseThrow(()->new NotFoundException("Task not found"));
        return user.getWorkspaces().stream().filter(ws->ws.getWorkspace_id().equals(task.getWorkspace().getWorkspace_id())).toList().size()>0;
    }

    public boolean checkUserCanModifyTask(String token, Long task_id) throws NotFoundException {
        //UserEntity user = getUserByToken(token);
        boolean canAccess = false;
        List<UserWorkspacesRoles> uwrs = getRoles(token);
        String username = extractUsername(token);
        TaskEntity task = taskRepo.findById(task_id).orElseThrow(()->new NotFoundException("Task not found!"));
        List<UserEntity> user = userRepo.findByUsername(username);
        if(user.get(0)!=null && task.getOwner().getUser_id().equals(user.get(0).getUser_id())){
            return true;
        }

        for(UserWorkspacesRoles uwr: uwrs){
            for(Long id: uwr.getAssigned_tasks_ids()){
                if(id.equals(task_id)){
                    canAccess = true;
                    break;
                }
            }
            if(task.getWorkspace().getWorkspace_id().equals(uwr.getWorkspace_id())
                    && (uwr.getRole().equals(Role.ROLE_MODERATOR)
                    || uwr.getRole().equals(Role.ROLE_OWNER)) ){
                canAccess = true;
            }
        }

        return canAccess;
  }

  public boolean checkUserIsTaskOwner(String token, Long task_id) throws NotFoundException {
        String username = extractUsername(token);
        TaskEntity task = taskRepo.findById(task_id).orElseThrow(()->new NotFoundException("Task not found"));
        List<UserEntity> users = userRepo.findByUsername(username);
        return users.get(0) != null && users.get(0).getUser_id().equals(task.getOwner().getUser_id());
  }

    public boolean checkIsModerator(Long ws_id, String token){
        return getRoles(token).stream()
                .filter(uwr->uwr.getWorkspace_id().equals(ws_id) && uwr.getRole().equals(Role.ROLE_MODERATOR))
                .toList().size()>0;
    }

    public boolean checkIsOwner(Long ws_id, String token){
        return getRoles(token).stream()
                .filter(uwr->uwr.getWorkspace_id().equals(ws_id) && uwr.getRole().equals(Role.ROLE_OWNER))
                .toList().size()>0;
    }

    public boolean checkIsMember(Long ws_id, String token){
        return getRoles(token).stream()
                .filter(uwr->uwr.getWorkspace_id().equals(ws_id))
                .toList().size()>0;
    }

    public boolean checkIsOwnerOrModerator(Long ws_id, String token){
        return checkIsOwner(ws_id,token) || checkIsModerator(ws_id,token);
    }

    public List<UserWorkspacesRoles> getUserRoles(UserEntity user){
        List<UserWorkspacesRoles> roles = new ArrayList<>();
        for(WorkspaceEntity ws: user.getWorkspaces()){
            UserWorkspacesRoles user_roles = new UserWorkspacesRoles();
            user_roles.setWorkspace_id(ws.getWorkspace_id());
            if(ws.getOwner().getUser_id().equals(user.getUser_id())){
                user_roles.setRole(Role.ROLE_OWNER);
            } else if (ws.getModerators().stream()
                    .filter(mod -> mod.getUser_id().equals(user.getUser_id()))
                    .collect(Collectors.toList()).size() >0) {
                user_roles.setRole(Role.ROLE_MODERATOR);
            }
            else{
                user_roles.setRole(Role.ROLE_USER);
            }
            List<Long> tasks_ids = user.getDesignated_tasks().stream()
                    .map(t->t.getWorkspace().getWorkspace_id().equals(ws.getWorkspace_id()) ? t.getTask_id() : null)
                    .filter(out-> out!=null)
                    .collect(Collectors.toList());
            user_roles.setAssigned_tasks_ids(tasks_ids);
            roles.add(user_roles);
        }
        return roles;
    }

}
