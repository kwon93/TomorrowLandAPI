package com.aaa.api.config;

import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyInitializer {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private Users dummyUser;

//    @PostConstruct
//    public void userInit(){
//         dummyUser = Users.builder()
//                .id(1L)
//                .email("dummy@test.com")
//                .password(passwordEncoder.encode("abc123"))
//                .name("foo")
//                .role(Role.USER)
//                .point(200)
//                .build();
//        usersRepository.save(dummyUser);
//    }

}
