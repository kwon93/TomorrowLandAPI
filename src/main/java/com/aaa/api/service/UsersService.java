package com.aaa.api.service;

import com.aaa.api.domain.Users;
import com.aaa.api.exception.DuplicateEmail;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public void reward(Long questionUserId, Long rewardUserId) {
        final Users questionUser = usersRepository.findById(questionUserId)
                .orElseThrow(UserNotFound::new);
        questionUser.decreasePoint();

        final Users rewardUser = usersRepository.findById(rewardUserId)
                .orElseThrow(UserNotFound::new);
        rewardUser.increasePoint();
    }


    private void duplicationEmailValidation(final CreateUsersServiceRequest serviceRequest) {
        Optional<Users> duplicateEmail
                = usersRepository.findByEmail(serviceRequest.getEmail());
        if (duplicateEmail.isPresent()){
            throw new DuplicateEmail();
        }
    }

}
