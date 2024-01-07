package com.aaa.api.service;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.Users;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 이메일입니다."));
        return new CustomUserPrincipal(users);
    }
}
