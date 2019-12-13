package ru.kc4kt4.students.test.configuration

import org.apache.tomcat.util.codec.binary.Base64
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import ru.kc4kt4.students.test.model.exception.AuthException
import ru.kc4kt4.students.test.service.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(private val userService: UserService) : HandlerInterceptor {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val auth: String
        try {
            auth = request.getHeader(HttpHeaders.AUTHORIZATION)
        } catch (e: Exception) {
            throw AuthException()
        }
        if (!auth.contains(prefix)) {
            return false
        }
        try {
            val encoded = auth.substring(prefix.length - 1)
            val credentialsAsString = Base64.decodeBase64(encoded.toByteArray(Charsets.UTF_8)).toString(Charsets.UTF_8)
            val credentials = credentialsAsString.split(":")
            if (credentials.size != 2) {
                return false
            }
            val user = userService.findUser(credentials[0])
            if (user.password != credentials[1]) {
                throw AuthException()
            }
            return true
        } catch (e: Exception) {
            logger.error("Error auth with token - $auth, error message - ${e.message}", e)
            throw AuthException()
        }
    }

    companion object {
        private const val prefix = "Basic "
    }
}