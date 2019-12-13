package ru.kc4kt4.students.test

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import ru.kc4kt4.students.test.model.StudentDto
import ru.kc4kt4.students.test.model.entity.Student
import ru.kc4kt4.students.test.model.repository.StudentRepository

class StudentControllerTest : TestApplicationTests() {
    private val url = "http://localhost:8081/students"

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var studentRepository: StudentRepository

    @Test
    fun createUser_success() {
        val response = restTemplate.postForEntity("$url/", HttpEntity(dto), String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(studentRepository.findAll())
                .hasSize(1)
                .element(0)
                .matches { it.id == response.body!! }
        clean()
    }

    @Test
    fun createUser_badRequest() {
        val badDto = StudentDto(dto.givenName, "", dto.middleName, dto.age, dto.course)
        val response = restTemplate.postForEntity("$url/", HttpEntity(badDto), String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun delete_success() {
        initStudent()
        assertThat(studentRepository.findAll()).hasSize(1)
        restTemplate.delete("$url/$id")
        assertThat(studentRepository.findAll()).hasSize(0)
    }

    @Test
    fun delete_unknownId() {
        initStudent()
        assertThat(studentRepository.findAll()).hasSize(1)
        restTemplate.exchange("$url/ewq", HttpMethod.DELETE, HttpEntity.EMPTY, Unit::class.java)
        assertThat(studentRepository.findAll()).hasSize(1)
        clean()
    }

    @Test
    fun getById_success() {
        initStudent()
        val response = restTemplate.getForEntity("$url/$id", StudentDto::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(studentRepository.findAll())
                .hasSize(1)
                .element(0)
                .matches { it.id == id }
        clean()
    }

    @Test
    fun getById_unknownId_noContent() {
        initStudent()
        val response = restTemplate.getForEntity("$url/ewq", StudentDto::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        clean()
    }

    private fun initStudent() {
        studentRepository.save(Student(id, "name", "surname", "mname", 2, 3))
    }

    private fun clean() {
        studentRepository.deleteAll()
    }

    companion object {
        private const val id = "qwe"
        private const val givenName = "given"
        private const val surname = "surname"
        private const val middleName = "middle"
        private const val age = 1
        private const val course = 2

        private val dto: StudentDto = StudentDto(givenName, surname, middleName, age, course)
    }
}