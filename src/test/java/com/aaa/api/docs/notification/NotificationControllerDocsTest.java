package com.aaa.api.docs.notification;

import com.aaa.api.controller.dto.request.CommentNotice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NotificationControllerDocsTest {


    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;

    @BeforeEach
    void setUp() {
        ArrayList<Transport> transPorts = new ArrayList<>();
        transPorts.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transPorts);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @DisplayName("commentNotice(): websocket endPoint 연결에 성공해야 한다.")
    void testWebSocketConnection() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CommentNotice> completableFuture = new CompletableFuture<>();

        StompSession session = stompClient
                .connectAsync(String.format("ws://localhost:%d/ws", port), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/notice", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CommentNotice.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete((CommentNotice) payload);
            }
        });

        CommentNotice testNotice = new CommentNotice(/* set your test data here */);
        session.send("/app/ws/notice", testNotice);

        CommentNotice result = completableFuture.get(5, TimeUnit.SECONDS);

        assertEquals(testNotice, result);
    }

}
