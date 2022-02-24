package com.juju.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database

fun Application.database() {
    val dbUrl = environment.config.property("db.jdbcUrl").getString()
    val dbDriver = environment.config.property("db.dbDriver").getString()
    val dbUser = environment.config.property("db.dbUser").getString()
    val dbPassword = environment.config.property("db.dbPassword").getString()


    val config = HikariConfig().apply {
        jdbcUrl = dbUrl
        driverClassName = dbDriver
        username = dbUser
        password = dbPassword
        maximumPoolSize = 10
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}