package ru.kc4kt4.students.test.model

import javax.validation.constraints.NotBlank

data class StudentDto(
        @field:NotBlank(message = "givenName must not be blank") val givenName: String,
        @field:NotBlank(message = "surname must not be blank") val surname: String,
        val middleName: String = "",
        val age: Int,
        val course: Int
)