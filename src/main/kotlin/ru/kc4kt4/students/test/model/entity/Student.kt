package ru.kc4kt4.students.test.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "student")
data class Student(
        @Id @Column(name = "id") val id: String,
        @Column(name = "given_name") val givenName: String,
        @Column(name = "surname") val surname: String,
        @Column(name = "middle_name") val middleName: String = "",
        @Column(name = "age") val age: Int,
        @Column(name = "course") val course: Int
)