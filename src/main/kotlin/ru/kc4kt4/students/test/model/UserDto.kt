package ru.kc4kt4.students.test.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserDto(
        @field:NotBlank @field:Size(min = 3) val login: String,
        @field:NotBlank @field:Size(min = 6) val password: String
)