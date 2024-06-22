package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.exception.InvalidSignInInfomation;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import com.aaa.api.service.dto.response.SessionDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public long getUserId(final LoginServiceRequest serviceRequest){
        Users users = usersRepository.findByEmail(serviceRequest.getEmail())
                .orElseThrow(UserNotFound::new);
        return users.getId();
    }

    public SessionDataResponse login(LoginServiceRequest request) {
        Users userByEmail =
                usersRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFound::new);

        if(!passwordEncoder.matches(request.getPassword(), userByEmail.getPassword())) {
            throw new InvalidSignInInfomation();
        }

        return SessionDataResponse.of(userByEmail);
    }
}
