package com.aaa.api.config.security.jwt;

import com.aaa.api.domain.Users;
import com.aaa.api.service.dto.response.JwtToken;
import com.aaa.api.repository.UsersRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
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
public class JwtTokenReIssueProvider {

    private final SecretKey key;
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenReIssueProvider(@Value("${jwt.secretKey}")final String secretKey,
                                   final UsersRepository usersRepository,
                                   final JwtTokenProvider jwtTokenProvider) {

        this.usersRepository = usersRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String reIssueAccessToken(final String username) {
        final Users users = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 회원입니다."));

        final UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(users);
        final JwtToken jwtToken = jwtTokenProvider.generateToken(authenticationToken);

        return jwtToken.getAccessToken();
    }

    public String validateRefreshToken(final String refreshToken){

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

    private static UsernamePasswordAuthenticationToken getAuthentication(final Users users) {
        User user = new User(
                users.getEmail(),
                "",
                List.of(new SimpleGrantedAuthority(users.getRoles().value())));

        return new UsernamePasswordAuthenticationToken(
                user,
                "",
                List.of(new SimpleGrantedAuthority("ROLE_"+users.getRoles().value())));
    }
}
