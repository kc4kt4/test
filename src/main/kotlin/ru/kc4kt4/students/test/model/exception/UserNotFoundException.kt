package ru.kc4kt4.students.test.model.exception

class UserNotFoundException(override val message: String) : RuntimeException(message)