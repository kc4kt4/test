package ru.kc4kt4.students.test

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import ru.kc4kt4.students.test.model.UserDto
import ru.kc4kt4.students.test.model.entity.User
import ru.kc4kt4.students.test.model.repository.UserRepository

class UserControlletTest : TestApplicationTests() {
    private val url = "http://localhost:8081/users"

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun cleanBefore() {
        userRepository.deleteAll()
    }

    @AfterEach
    fun cleanAfter() {
        userRepository.deleteAll()
    }

    @Test
    fun getAllUsers_success() {
        userRepository.save(User(login, password))
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val response = restTemplate.exchange("$url/", HttpMethod.GET, HttpEntity(Unit, headers), typeReference<List<String>>())
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body)
                .hasSize(1)
                .element(0)
                .isEqualTo(login)
        userRepository.deleteAll()
    }

    @Test
    fun createUser_success() {
        Assertions.assertThat(userRepository.findAll()).hasSize(0)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val dto = UserDto(login, password)
        val response = restTemplate.exchange("$url/", HttpMethod.POST, HttpEntity(dto, headers), Unit::class.java)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(userRepository.findAll())
                .hasSize(1)
                .element(0)
                .matches { it.id == login }
    }

    companion object {
        private const val login = "omen"
        private const val password = "some pwd"
    }
}