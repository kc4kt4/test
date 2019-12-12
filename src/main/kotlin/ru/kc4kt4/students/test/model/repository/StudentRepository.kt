package ru.kc4kt4.students.test.model.repository

import org.springframework.data.repository.CrudRepository
import ru.kc4kt4.students.test.model.entity.Student

interface StudentRepository : CrudRepository<Student, String> {
    override fun findAll(): List<Student>
    fun getById(id: String): Student
}