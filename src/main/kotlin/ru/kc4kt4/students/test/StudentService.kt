package ru.kc4kt4.students.test

import org.springframework.stereotype.Service
import ru.kc4kt4.students.test.model.StudentDto
import ru.kc4kt4.students.test.model.entity.Student
import ru.kc4kt4.students.test.model.repository.StudentRepository
import java.util.*

@Service
class StudentService(private val studentRepository: StudentRepository) {

    fun saveOrUpdate(dto: StudentDto): String {
        return saveOrUpdate(dto, UUID.randomUUID().toString())
    }

    fun saveOrUpdate(dto: StudentDto, id: String): String {
        val student = Student(id, dto.givenName, dto.surname, dto.middleName, dto.age, dto.course)
        val entity = studentRepository.save(student)
        return entity.id
    }

    fun delete(id: String) {
        studentRepository.deleteById(id)
    }

    fun findById(id: String): Student {
        return studentRepository.getById(id)
    }

    fun getStudents(): List<Student> {
        return studentRepository.findAll()
    }

    fun toDto(student: Student): StudentDto {
        return StudentDto(student.givenName,
                student.surname, student.middleName,
                student.age, student.course)
    }
}