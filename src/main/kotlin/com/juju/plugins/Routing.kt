package com.juju.plugins

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.juju.dto.response.ApiErrorResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<MismatchedInputException> { cause ->
            call.respond(HttpStatusCode.BadRequest, ApiErrorResponse(cause.message ?: "json format 이 이상합니다."))
        }
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse(cause.message ?: "인증이 필요합니다."))
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden, ApiErrorResponse(cause.message ?: "권한이 없습니다."))
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
