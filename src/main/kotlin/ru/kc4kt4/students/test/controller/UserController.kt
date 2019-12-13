package ru.kc4kt4.students.test.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kc4kt4.students.test.model.UserDto
import ru.kc4kt4.students.test.service.UserService
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/users"],
        produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userService: UserService) {

    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody @Valid dto: UserDto): ResponseEntity<Unit> {
        userService.createUser(dto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping(value = ["/"])
    fun getAllUsers(): ResponseEntity<List<String>> {
        val users = userService.getAllUsers()
        return ResponseEntity.status(HttpStatus.OK).body(users)
    }
}