package com.juju.contoller

import com.juju.data.Cities
import com.juju.dto.response.ApiResponse
import com.juju.dto.response.CityResponse
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.cityRouting() {
    routing {
        authenticate("juju") {
            get("/") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }

        post("/apis/cities") {
            var list: List<CityResponse>? = null
            transaction {
                addLogger(StdOutSqlLogger)
                // insert new city. SQL: INSERT INTO Cities (name) VALUES ('Seoul')
                val seoulId = Cities.insert {
                    it[name] = "Seoul"
                } get Cities.id

                log.info("seoul id : $seoulId")
                list = Cities.select { Cities.id eq seoulId }.withDistinct()
                    .map { CityResponse(it[Cities.id].value, it[Cities.name]) }
                log.info("city : $list")
            }

            val data = list!![0]
            call.respond(HttpStatusCode.Created, ApiResponse(data))
        }
    }
}