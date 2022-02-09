package com.juju.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.juju.dto.request.auth.AuthenticateRequest
import com.juju.dto.response.ApiErrorResponse
import com.juju.dto.response.ApiResponse
import com.juju.dto.response.auth.AuthenticateResponse
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import java.util.*

fun Application.configureRouting() {
    routing {
        val jwtAudience = environment.config.property("jwt.audience").getString()
        val secret = environment.config.property("jwt.secret").getString()
        val domain = environment.config.property("jwt.domain").getString()
        fun generateAccessToken(username: String): String {
            return JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(domain)
                .withClaim("username", username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 15))
                .sign(Algorithm.HMAC256(secret))
        }

        fun generateRefreshToken(username: String): String {
            return JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(domain)
                .withClaim("username", username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 60 * 12))
                .sign(Algorithm.HMAC256(secret))
        }

        post("/apis/authentication") {
            val request = call.receive<AuthenticateRequest>()
            log.info("$request")
            val userName = request.username
            val accessToken = generateAccessToken(userName)
            val refreshToken = generateRefreshToken(userName)
            call.response.cookies.append("juju-auth", refreshToken, httpOnly = true, domain = domain)
            call.respond(HttpStatusCode.OK, ApiResponse(AuthenticateResponse(accessToken)))
        }

        install(StatusPages) {
            exception<MismatchedInputException> { cause ->
                call.respond(HttpStatusCode.BadRequest, ApiErrorResponse(cause.message?: "json format 이 이상합니다."))
            }
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse(cause.message?: "인증이 필요합니다."))
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden, ApiErrorResponse(cause.message?: "권한이 없습니다."))
            }
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
