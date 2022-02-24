package com.juju

import com.juju.contoller.authRouting
import com.juju.contoller.cityRouting
import com.juju.data.Cities
import com.juju.plugins.*
import io.ktor.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
//    DatabaseFactory.init()
    database()
    transaction {
        SchemaUtils.create(Cities)
    }
    cityRouting()
    authRouting()
}

