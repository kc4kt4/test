package ru.kc4kt4.students.test.model.entity

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Id

@Document(collection = "user")
data class User(
        @Id val id: String,
        val password: String
)