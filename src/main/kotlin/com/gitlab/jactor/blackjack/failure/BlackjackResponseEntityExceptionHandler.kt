package com.gitlab.jactor.blackjack.failure

import com.gitlab.jactor.blackjack.dto.ErrorDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class BlackjackResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ResponseBody
    @ExceptionHandler
    fun handleHttClientErrorException(httpClientErrorException: HttpClientErrorException): ResponseEntity<*>? {
        val exSimpleName = httpClientErrorException::class.simpleName
        val origin = fetchInternalStackOrFirstElement(httpClientErrorException.stackTrace)
        val message = "$exSimpleName: ${httpClientErrorException.message}"

        return ResponseEntity.status(httpClientErrorException.statusCode)
            .header(HttpHeaders.WARNING, message)
            .body(ErrorDto(message = message, provider = origin))
    }

    @ResponseBody
    @ExceptionHandler
    fun handleException(exception: Exception): ResponseEntity<*>? {
        val exSimpleName = exception::class.simpleName
        val origin = fetchInternalStackOrFirstElement(exception.stackTrace)
        val message = "$exSimpleName: ${exception.message}"

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .header(HttpHeaders.WARNING, message)
            .body(ErrorDto(message = message, provider = origin))
    }

    @ResponseBody
    @ExceptionHandler
    fun handleUnknownPlayerException(unknownPlayerException: UnknownPlayerException): ResponseEntity<*>? {
        val exSimpleName = unknownPlayerException::class.simpleName
        val origin = fetchInternalStackOrFirstElement(unknownPlayerException.stackTrace)
        val message = "$exSimpleName: ${unknownPlayerException.message}"

        return ResponseEntity.badRequest()
            .header(HttpHeaders.WARNING, message)
            .body(ErrorDto(message = message, provider = origin))
    }

    private fun fetchInternalStackOrFirstElement(stackTrace: Array<StackTraceElement>): String {
        return stackTrace.toList().stream()
            .filter { it.isNativeMethod }
            .map { it.toString() }
            .findFirst()
            .orElseGet { stackTrace[0].toString() }
    }
}
