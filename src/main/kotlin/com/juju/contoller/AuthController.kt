package com.juju.contoller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.juju.data.Cities
import com.juju.dto.request.auth.AuthenticateRequest
import com.juju.dto.response.ApiResponse
import com.juju.dto.response.CityResponse
import com.juju.dto.response.auth.AuthenticateResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.authRouting() {


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
            transaction {

            }
            val accessToken = generateAccessToken(userName)
            val refreshToken = generateRefreshToken(userName)
            call.response.cookies.append("juju-auth", refreshToken, httpOnly = true, domain = domain)
            call.respond(HttpStatusCode.OK, ApiResponse(AuthenticateResponse(accessToken)))
        }
    }
}