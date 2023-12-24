package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.dto.request.CreateUsersRequest;
import com.aaa.api.exception.DuplicateEmail;
import com.aaa.api.repository.UsersRepository;
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
    public long createUser(CreateUsersRequest request) {
        duplicationEmailValidation(request);

        String encryptPassword = passwordEncoder.encode(request.getPassword());

        Users users = Users.of(request, encryptPassword);
        usersRepository.save(users);

        return users.getId();
    }

    private void duplicationEmailValidation(CreateUsersRequest createUsersRequest) {
        Optional<Users> duplicateEmail
                = usersRepository.findByEmail(createUsersRequest.getEmail());
        if (duplicateEmail.isPresent()){
            throw new DuplicateEmail();
        }
    }
}
