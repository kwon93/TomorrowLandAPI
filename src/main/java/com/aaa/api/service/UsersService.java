package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.exception.DuplicateEmail;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String  createUser(final CreateUsersServiceRequest serviceRequest) {
        duplicationEmailValidation(serviceRequest);

        final String encodedPassword = passwordEncoder.encode(serviceRequest.getPassword());
        final Users users = serviceRequest.toEntity(encodedPassword);
        usersRepository.save(users);

        return users.getRoles().toString();
    }

    //Optional return 안하는 방향으로 TODO
    private void duplicationEmailValidation(final CreateUsersServiceRequest serviceRequest) {
        Optional<Users> duplicateEmail
                = usersRepository.findByEmail(serviceRequest.getEmail());
        if (duplicateEmail.isPresent()){
            throw new DuplicateEmail();
        }
    }
}
