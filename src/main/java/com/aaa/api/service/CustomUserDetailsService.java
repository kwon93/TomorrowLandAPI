package com.aaa.api.service;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.Users;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Integer userId = (Integer) redisTemplate.opsForHash().get("userInfo", "userId");
        String userEmail = (String) redisTemplate.opsForHash().get("userInfo", "username");
        String userRole = (String) redisTemplate.opsForHash().get("userInfo", "userRole");

        if (userEmail == null || userRole == null){
            final Users users = usersRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 이메일입니다."));

            return new CustomUserPrincipal(users.getEmail(), users.getRoles().toString(), users.getId());
        }

        return new CustomUserPrincipal(username, userRole, userId.longValue());
    }
}
