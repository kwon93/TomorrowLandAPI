package com.aaa.api.service;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    public static final String USER_INFO = "userInfo";
    private final UsersRepository usersRepository;
    private final RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Integer userId = (Integer) getUserInfoFromRedis("userId");
        String userEmail = (String) getUserInfoFromRedis("username");
        String userRole = (String) getUserInfoFromRedis("userRole");

        if (doseUserSignIn(userEmail, userRole)) return setUserPrincipalFromDatabase(username);

        return new CustomUserPrincipal(username, userRole, userId.longValue());
    }

    private CustomUserPrincipal setUserPrincipalFromDatabase(String username) {
        final Users users = usersRepository.findByEmail(username)
                .orElseThrow(UserNotFound::new);
        return new CustomUserPrincipal(users.getEmail(), users.getRoles().toString(), users.getId());
    }

    private Object getUserInfoFromRedis(String userInfo) {
        return redisTemplate.opsForHash().get(USER_INFO, userInfo);
    }

    private static boolean doseUserSignIn(String userEmail, String userRole) {
        return userEmail == null || userRole == null;
    }
}
