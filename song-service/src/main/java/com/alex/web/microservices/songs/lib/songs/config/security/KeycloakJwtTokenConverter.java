package com.alex.web.microservices.songs.lib.songs.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;

/**
 * This class is converter from Jwt token to AuthToken using access-token by OAUTh2.0
 * @see Jwt
 * @see JwtAuthenticationToken
 */
@RequiredArgsConstructor
public class KeycloakJwtTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";
    private final TokenProperties properties;

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        final var resourceAccess = (Map<String, Object>) jwt.getClaims().getOrDefault(RESOURCE_ACCESS, Map.of());
        final var clientAccess = (Map<String, Object>) resourceAccess.getOrDefault(properties.getResourceId(), Map.of());
        final var clientRoles = (List<String>) clientAccess.getOrDefault(ROLES, List.of());


        List<SimpleGrantedAuthority> authorities = clientRoles.stream().map(SimpleGrantedAuthority::new).toList();

        String principalClaimName = properties.getPrincipalAttribute()
                .map(jwt::getClaimAsString)
                .orElse(jwt.getClaimAsString(JwtClaimNames.SUB));

        return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
    }
}
