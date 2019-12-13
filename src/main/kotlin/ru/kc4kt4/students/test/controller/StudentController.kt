package ru.kc4kt4.students.test.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kc4kt4.students.test.model.StudentDto
import ru.kc4kt4.students.test.model.entity.Student
import ru.kc4kt4.students.test.service.StudentService
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/students"],
        produces = [MediaType.APPLICATION_JSON_VALUE])
class StudentController(private val studentService: StudentService) {

    @GetMapping(value = ["/"])
    fun getStudents(): ResponseEntity<List<StudentDto>> {
        val students: List<Student> = studentService.getStudents()
        val responseBody: List<StudentDto> = students.map { studentService.toDto(it) }.toList()
        return ResponseEntity.ok().body(responseBody)
    }

    @GetMapping(value = ["/{id}"])
    fun getById(@PathVariable("id") id: String): ResponseEntity<StudentDto> {
        val student: Student = studentService.findById(id)
        val responseBody: StudentDto = studentService.toDto(student)
        return ResponseEntity.ok().body(responseBody)
    }

    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody @Valid dto: StudentDto): ResponseEntity<String> {
        val updatedId = studentService.saveOrUpdate(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedId)
    }

    @PostMapping(value = ["/{id}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody @Valid dto: StudentDto, @PathVariable("id") id: String): ResponseEntity<String> {
        val updatedId: String = studentService.saveOrUpdate(dto, id)
        return ResponseEntity.ok(updatedId)
    }

    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable("id") id: String): ResponseEntity<Unit> {
        studentService.delete(id)
        return ResponseEntity.ok().build()
    }
}