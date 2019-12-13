package ru.kc4kt4.students.test

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
abstract class TestApplicationTests {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Test
    fun contextLoads() {
    }
}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}