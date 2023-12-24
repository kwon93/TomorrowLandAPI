package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usersRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 이메일입니다."));

    }

    private UserDetails createUserDetails(Users users) {
        return User.builder()
                .username(users.getEmail())
                .password(passwordEncoder.encode(users.getPassword()))
                .roles(users.getRoles().toArray(new String[0]))
                .build();
    }
}
