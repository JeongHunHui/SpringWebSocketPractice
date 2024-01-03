package com.hunhui.websocket.handler

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@Component
class SocketHandler : TextWebSocketHandler() {
    private val sessionList: MutableList<WebSocketSession> = mutableListOf()
    private val sessionIds: MutableMap<String, Int> = mutableMapOf()
    private val clientIdCounter = AtomicInteger(0)
    private val randomNumber = Random.nextInt(1, 11)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val clientId = clientIdCounter.incrementAndGet()
        sessionIds[session.id] = clientId
        sessionList.add(session)
        broadcast("클라이언트 $clientId 님이 연결에 성공했습니다.")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val clientId = sessionIds[session.id]
        sessionList.remove(session)
        sessionIds.remove(session.id)
        broadcast("클라이언트 $clientId 님이 연결을 끊었습니다.")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val messageText = message.payload
        val clientId = sessionIds[session.id]
        broadcast("클라이언트 $clientId: $messageText")
        // 메시지가 숫자이고, 무작위로 생성된 숫자와 일치할 때
        if (messageText.toIntOrNull() == randomNumber) {
            broadcast("클라이언트 $clientId 님이 정답을 맞췄습니다.")
        }
    }

    private fun broadcast(text: String) {
        sessionList.forEach { it.sendMessage(TextMessage(text)) }
    }
}