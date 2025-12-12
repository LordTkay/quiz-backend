package de.tkay.quiz.player

import org.slf4j.LoggerFactory
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

class PlayerQueryHandshakeInterceptor : HandshakeInterceptor {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val servletRequest = (request as ServletServerHttpRequest).servletRequest
        val username = servletRequest.getParameter("username")

        if (username.isEmpty()) {
            logger.warn("Connection attempt was aborted because no username was provided!")
            return false
        }

        attributes["username"] = username
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // NOOP
    }
}
