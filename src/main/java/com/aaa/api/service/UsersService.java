package com.aaa.api.service;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.IsRewarded;
import com.aaa.api.exception.*;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import com.aaa.api.service.dto.response.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    @Transactional
    public String createUser(final CreateUsersServiceRequest serviceRequest) {
        duplicationEmailValidation(serviceRequest);
        
        String encodedPassword = passwordHashing(serviceRequest);
        Users users = persistUser(serviceRequest, encodedPassword);
        return users.getName();
    }

    @Transactional
    public void reward(Long questionUserId, Long rewardUserId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);
        rewardValidation(questionUserId, rewardUserId, comment);

        final Users questionUser = usersRepository.findById(questionUserId)
                .orElseThrow(UserNotFound::new);
        questionUser.decreasePoint();

        final Users rewardUser = usersRepository.findById(rewardUserId)
                .orElseThrow(UserNotFound::new);
        rewardUser.increasePoint();
        comment.updateRewardState();
    }

    public UserInfo getUsersInfo(Long userId) {
        Users users = usersRepository.findById(userId).orElseThrow(UserNotFound::new);
        List<Comment> commentsByUser = commentRepository.findByUserId(users.getId());

        return UserInfo.builder()
                .entity(users)
                .userAnswer(commentsByUser.size())
                .build();
    }

    private Users persistUser(CreateUsersServiceRequest serviceRequest, String encodedPassword) {
        Users users = serviceRequest.toEntity(encodedPassword);
        usersRepository.save(users);
        return users;
    }
    
    private String passwordHashing(CreateUsersServiceRequest serviceRequest) {
        return passwordEncoder.encode(serviceRequest.getPassword());
    }

    private void rewardValidation(Long questionUserId, Long rewardUserId, Comment comment) {
        if(doseUserAlreadyRewarded(comment)){
            throw new DuplicateReward();
        }

        if(doseUserSelfReward(questionUserId, rewardUserId)){
            throw new InvalidReward();
        }
    }

    private void duplicationEmailValidation(final CreateUsersServiceRequest serviceRequest) {
        Optional<Users> duplicateEmail
                = usersRepository.findByEmail(serviceRequest.getEmail());
        if (duplicateEmail.isPresent()){
            throw new DuplicateEmail();
        }
    }

    private boolean doseUserSelfReward(Long questionUserId, Long rewardUserId) {
        return questionUserId.equals(rewardUserId);
    }

    private boolean doseUserAlreadyRewarded(Comment comment) {
        return comment.getIsRewarded().equals(IsRewarded.True);
    }

}
