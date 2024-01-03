package com.hunhui.websocket.config

import com.hunhui.websocket.handler.SocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

// WebSocketConfigurer를 구현하여 웹소켓 핸들러의 매핑 정의
@Configuration
@EnableWebSocket
class WebSocketConfig(
    // 직접 만든 웹소켓 핸들러 클래스
    private val socketHandler: SocketHandler
) : WebSocketConfigurer {
    // /chat 경로로 들어오는 웹소켓 연결 요청을 SocketHandler가 처리하도록 설정
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(socketHandler, "/chat")
            // 모든 도메인에의 웹소켓 연결을 허용
            .setAllowedOrigins("*")
    }
}