package com.aaa.api.config.security.jwt;

import com.aaa.api.domain.Users;
import com.aaa.api.service.dto.response.JwtToken;
import com.aaa.api.repository.UsersRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenReIssueProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    public String reIssueAccessToken(String username) {
        Users users = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 회원입니다."));


        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(users);


        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticationToken);

        return jwtToken.getAccessToken();
    }

    public String validateRefreshToken(String refreshToken){
        byte[] decodeKey = Base64.getDecoder().decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(decodeKey);
        
        if (StringUtils.hasText(refreshToken)){
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(refreshToken)
                        .getPayload();

                return claims.getSubject();
            } catch (ExpiredJwtException e){
                log.error("RefreshToken is Expired",e);
                throw e;
            } catch (SecurityException | MalformedJwtException e) {
                log.info("Invalid RefreshToken", e);
                throw e;
            }
        }
        throw new JwtException("Invalid RefreshToken");
    }

    private static UsernamePasswordAuthenticationToken getAuthentication(Users users) {
        User user = new User(users.getEmail(), "", List.of(new SimpleGrantedAuthority(users.getRoles().value())));

        return new UsernamePasswordAuthenticationToken(user, "", List.of(new SimpleGrantedAuthority("ROLE_"+users.getRoles().value())));
    }
}
