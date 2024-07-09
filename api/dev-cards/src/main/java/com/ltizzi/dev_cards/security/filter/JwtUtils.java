package com.ltizzi.dev_cards.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalUnit;
import java.util.stream.Collectors;

/**
 * @author Leonardo Terlizzi
 */
@Service
public class JwtUtils {

    private final JwtEncoder encoder;
    @Autowired
    private JwtDecoder jwtDecoder;

    public JwtUtils(JwtEncoder encoder){
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication, String username){
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));
        System.out.println(scope);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofMinutes(60)))
                .subject(username)
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String extractUsername(String token){
        return jwtDecoder.decode(token).getClaimAsString("sub");
    }

}
