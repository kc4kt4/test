package ru.kc4kt4.students.test.model.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.kc4kt4.students.test.model.entity.Student

interface StudentRepository : MongoRepository<Student, String> {
    override fun findAll(): List<Student>
    fun getById(id: String): Student
}