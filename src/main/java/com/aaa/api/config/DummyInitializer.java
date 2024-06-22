package com.aaa.api.config;

import com.aaa.api.domain.Users;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyInitializer {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private Users dummyUser;

}
