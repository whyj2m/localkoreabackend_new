package com.study.springboot.config.webSocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessionList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결이 성공하면 세션을 리스트에 추가
        sessionList.add(session);
        System.out.println("WebSocket Connection Established: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결이 종료되면 세션을 리스트에서 제거
        sessionList.remove(session);
        System.out.println("WebSocket Connection Closed: " + session.getId());
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 메시지 수신
        String payload = message.getPayload();
        System.out.println(session.getId() + "에서 메시지 수신: " + message.getPayload());

        // 여기서 받은 메시지를 다른 세션들에게 전송
        for (WebSocketSession webSocketSession : sessionList) {
            if (webSocketSession.isOpen() && session.getId().equals(webSocketSession.getId())) {
                try {
                    webSocketSession.sendMessage(new TextMessage(payload));
                    System.out.println("메시지 전송 성공: " + payload + " -> " + webSocketSession.getId());
                } catch (Exception e) {
                	  e.printStackTrace();
                }
            }
        }
    }
}