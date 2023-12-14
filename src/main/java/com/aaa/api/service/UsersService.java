package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.dto.request.CreateUsersRequest;
import com.aaa.api.exception.DuplicateEmail;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;


    @Transactional
    public long createUser(CreateUsersRequest createUsersRequest) {
        duplicationEmailValidation(createUsersRequest);

        Users users = Users.of(createUsersRequest);
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
