package ru.kc4kt4.students.test.model.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.kc4kt4.students.test.model.entity.User

interface UserRepository : MongoRepository<User, String> {
//    fun findById(login: String): Optional<User>
}