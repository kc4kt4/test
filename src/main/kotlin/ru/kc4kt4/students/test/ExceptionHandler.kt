package ru.kc4kt4.students.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        val message = e.message
        logger.error(message, e)
        val errorBody = ErrorResponse()
        if (message != null && message.isBlank()) {
            errorBody.apply {
                descriptions = listOf(message)
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidatedException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error(e.message, e)
        val errorBody = ErrorResponse().apply {
            descriptions = e.bindingResult.allErrors.map { it.defaultMessage }.toList()
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody)
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultDataAccessExceptionException(e: EmptyResultDataAccessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}

class ErrorResponse {
    var descriptions: List<String?> = listOf("Something wend wrong")
}