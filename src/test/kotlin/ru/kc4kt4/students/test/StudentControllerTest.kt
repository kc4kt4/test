package ru.kc4kt4.students.test

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.client.RestTemplate
import ru.kc4kt4.students.test.model.StudentDto
import ru.kc4kt4.students.test.model.repository.StudentRepository

class StudentControllerTest : TestApplicationTests() {
    private val url = "http://localhost:8081/students"

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var studentRepository: StudentRepository

    @Test
    @SqlGroup(value = [
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    ])
    fun createUser_success() {
        val response = restTemplate.postForEntity("$url/", HttpEntity(dto), String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(studentRepository.findAll())
                .hasSize(1)
                .element(0)
                .matches { it.id == response.body!! }
    }

    @Test
    @SqlGroup(value = [
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    ])
    fun createUser_badRequest() {
        val badDto = StudentDto(dto.givenName, "", dto.middleName, dto.age, dto.course)
        val response = restTemplate.postForEntity("$url/", HttpEntity(badDto), String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @SqlGroup(value = [
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into public.student values('$id', 'name', 'surname','mname',2,3)"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    ])
    fun delete_success() {
        assertThat(studentRepository.findAll()).hasSize(1)
        restTemplate.delete("$url/$id")
        assertThat(studentRepository.findAll()).hasSize(0)
    }

    @Test
    @SqlGroup(value = [
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into public.student values('$id', 'name', 'surname','mname',2,3)"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    ])
    fun delete_unknownId() {
        assertThat(studentRepository.findAll()).hasSize(1)
        val response = restTemplate.exchange("$url/ewq", HttpMethod.DELETE, HttpEntity.EMPTY, Unit::class.java)
        assertThat(studentRepository.findAll()).hasSize(1)
    }

    @Test
    @SqlGroup(value = [
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into public.student values('$id', 'name', 'surname', 'mname', 2, 3)"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    ])
    fun getById_success() {
        val response = restTemplate.getForEntity("$url/$id", StudentDto::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(studentRepository.findAll())
                .hasSize(1)
                .element(0)
                .matches { it.id == id }
    }

    @Test
    @SqlGroup(value = [
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into public.student values('$id', 'name', 'surname', 'mname', 2, 3)"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["delete from public.student"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    ])
    fun getById_unknownId_noContent() {
        val response = restTemplate.getForEntity("$url/ewq", StudentDto::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
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