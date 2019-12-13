package ru.kc4kt4.students.test.model.entity

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Id

@Document(collection = "student")
data class Student(
        @Id val id: String,
        val givenName: String,
        val surname: String,
        val middleName: String = "",
        val age: Int,
        val course: Int
)