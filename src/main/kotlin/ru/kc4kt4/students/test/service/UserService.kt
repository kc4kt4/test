package ru.kc4kt4.students.test.service

import org.springframework.stereotype.Service
import ru.kc4kt4.students.test.model.UserDto
import ru.kc4kt4.students.test.model.entity.User
import ru.kc4kt4.students.test.model.exception.LoginExistException
import ru.kc4kt4.students.test.model.exception.UserNotFoundException
import ru.kc4kt4.students.test.model.repository.UserRepository

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(dto: UserDto) {
        if (userRepository.existsById(dto.login)) {
            throw LoginExistException()
        }
        val user = User(dto.login, dto.password)
        userRepository.save(user)
    }

    fun getAllUsers(): List<String> {
        return userRepository.findAll().map { it.id }.toList()
    }

    fun findUser(login: String): User {
        return userRepository.findById(login).orElseThrow { UserNotFoundException("user not found with login - $login") }
    }
}