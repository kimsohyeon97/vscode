package com.example.chat.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery();
        System.out.println("URI 쿼리: " + query);

        Map<String, String> paramMap = parseQuery(query);
        String roomId = paramMap.get("roomId");

        if (roomId == null) {
            System.out.println("roomId 누락! 등록 실패: " + session.getId());
            return;
        }

        roomSessions.putIfAbsent(roomId, new ArrayList<>());
        roomSessions.get(roomId).add(session);

        System.out.println("[" + roomId + "] 연결됨: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        Map<String, String> map = objectMapper.readValue(payload, new TypeReference<>() {});
        String roomId = map.get("roomId");
        String sender = map.get("sender");
        String content = map.get("message");

        List<WebSocketSession> sessions = roomSessions.get(roomId);

        // 디버깅 로그
        System.out.println("받은 메시지: " + payload);
        System.out.println("전송 대상 방: " + roomId);
        System.out.println("현재 세션 수: " + (sessions != null ? sessions.size() : 0));

        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(sender + ": " + content));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        roomSessions.values().forEach(s -> s.remove(session));
        System.out.println(" 연결 종료: " + session.getId());
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query != null) {
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    map.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
                }
            }
        }
        return map;
    }
}
