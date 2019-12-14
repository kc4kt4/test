package ru.kc4kt4.students.test

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.GenericContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
abstract class TestApplicationTests {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Test
    fun contextLoads() {
    }

    companion object {
        fun mongo(): KGenericContainer {
            return KGenericContainer("mongo:3.6").withExposedPorts(27017).withEnv("MONGO_INITDB_DATABASE", "vasilevskiy")
        }

        @BeforeAll
        @JvmStatic
        fun init() {
            mongo().start()
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            mongo().stop()
        }

    }
}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)