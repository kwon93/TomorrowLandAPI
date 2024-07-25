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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class NotificationController {

    private final CommentNotificationService commentNotificationService;
    private final SseService sseService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<SseEmitter> sseConnect(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal) throws IOException {
        SseEmitter sseEmitter = sseService.connectToSSE(userPrincipal.getUserId());
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/comment/notice")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<NoticeMessageDatas> getStoredNotification(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal) {
        NoticeMessageDatas storedNotifications = commentNotificationService.getUnreadNotifications(userPrincipal.getUserId());
        return ResponseEntity.ok(storedNotifications);
    }

    @PatchMapping("/comment/notice/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> updateToNoticeReadStatus(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal,
                                                          @RequestBody UpdateCommentNotice updateCommentNotice) throws JsonProcessingException {
        commentNotificationService.updateMarkAsRead(userPrincipal.getUserId(), updateCommentNotice.getNoticeId());
        return ResponseEntity.noContent().build();
    }
}
