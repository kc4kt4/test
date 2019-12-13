package ru.kc4kt4.students.test

import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import ru.kc4kt4.students.test.model.StudentDto
import ru.kc4kt4.students.test.model.entity.Student
import ru.kc4kt4.students.test.model.entity.User
import ru.kc4kt4.students.test.model.repository.StudentRepository
import ru.kc4kt4.students.test.model.repository.UserRepository

class StudentControllerTest : TestApplicationTests() {
    private val url = "http://localhost:8081/students"

    @Autowired
    lateinit var studentRepository: StudentRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun initAdmin() {
        userRepository.deleteAll()
        studentRepository.deleteAll()
        userRepository.save(User(admin, adminPwd))
    }

    @AfterEach
    fun cleanAdmin() {
        studentRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun createStudent_success() {
        val response = restTemplate.exchange("$url/", HttpMethod.POST, createRequest(dto), String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(studentRepository.findAll())
                .hasSize(1)
                .element(0)
                .matches { it.id == response.body!! }
    }

    @Test
    fun createStudent_badRequest() {
        val badDto = StudentDto(dto.givenName, "", dto.middleName, dto.age, dto.course)
        val response = restTemplate.exchange("$url/", HttpMethod.POST, createRequest(badDto), String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun delete_success() {
        initStudent()
        assertThat(studentRepository.findAll()).hasSize(1)
        val response = restTemplate.exchange("$url/$id", HttpMethod.DELETE, createRequest(), Unit::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(studentRepository.findAll()).hasSize(0)
    }

    @Test
    fun delete_unknownId() {
        initStudent()
        assertThat(studentRepository.findAll()).hasSize(1)
        restTemplate.exchange("$url/ewq", HttpMethod.DELETE, createRequest(), Unit::class.java)
        assertThat(studentRepository.findAll()).hasSize(1)
    }

    @Test
    fun getById_success() {
        initStudent()
        val response = restTemplate.exchange("$url/$id", HttpMethod.GET, createRequest(), StudentDto::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!)
                .matches { it.middleName == mname }
    }

    @Test
    fun getAllStudents_success() {
        initStudent()
        val response = restTemplate.exchange("$url/", HttpMethod.GET, createRequest(), typeReference<List<StudentDto>>())
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!)
                .hasSize(1)
                .element(0)
                .matches { it.middleName == mname }
    }

    @Test
    fun withoutAuth_error() {
        val response = restTemplate.exchange("$url/", HttpMethod.GET, HttpEntity.EMPTY, Any::class.java)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun getById_unknownId_noContent() {
        initStudent()
        val response = restTemplate.exchange("$url/ewq", HttpMethod.GET, createRequest(), StudentDto::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    private fun createRequest(dto: StudentDto): HttpEntity<StudentDto> {
        val headers = HttpHeaders().apply {
            setBasicAuth(admin, adminPwd, Charsets.UTF_8)
        }
        return HttpEntity(dto, headers)
    }

    private fun createRequest(): HttpEntity<*> {
        val headers = HttpHeaders().apply {
            setBasicAuth(admin, adminPwd, Charsets.UTF_8)
        }
        return HttpEntity(Unit, headers)
    }

    private fun initStudent() {
        studentRepository.save(Student(id, "name", "surname", mname, 2, 3))
    }

    companion object {
        private const val admin = "admin"
        private const val adminPwd = "password"

        private const val id = "qwe"
        private const val givenName = "given"
        private const val surname = "surname"
        private const val middleName = "middle"
        private const val mname = "mname"
        private const val age = 1
        private const val course = 2

        private val dto: StudentDto = StudentDto(givenName, surname, middleName, age, course)
    }
}