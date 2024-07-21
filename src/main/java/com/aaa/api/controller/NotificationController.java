package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.UpdateCommentNotice;
import com.aaa.api.service.CommentNotificationService;
import com.aaa.api.service.SseService;
import com.aaa.api.service.dto.NoticeMessageDatas;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class NotificationController {

    private final CommentNotificationService commentNotificationService;
    private final SseService sseService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> sseConnect(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal) throws IOException {
        SseEmitter sseEmitter = sseService.connectToSSE(userPrincipal.getUserId());
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/comment/notice")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<NoticeMessageDatas> getStoredNotification(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        NoticeMessageDatas storedNotifications = commentNotificationService.getUnreadNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(storedNotifications);
    }

    @PatchMapping("/comment/notice/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") //permissionEvaluator 추가하기
    public ResponseEntity<Void> updateToNoticeReadStatus(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                         UpdateCommentNotice updateCommentNotice) throws JsonProcessingException {
        commentNotificationService.updateMarkAsRead(userPrincipal.getUserId(), updateCommentNotice.getNoticeId());
        return ResponseEntity.noContent().build();
    }
}
