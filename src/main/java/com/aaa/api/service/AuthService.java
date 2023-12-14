package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.exception.InvalidSignInInfomation;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;

    @Transactional
    public Long login(LoginRequest request) {
        Users users = usersRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSignInInfomation::new);

        return users.getId();
    }
}
