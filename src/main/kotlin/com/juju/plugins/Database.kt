package com.juju.plugins

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.*
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = appConfig.property("db.jdbcUrl").getString()
    private val dbDriver = appConfig.property("db.dbDriver").getString()
    private val dbUser = appConfig.property("db.dbUser").getString()
    private val dbPassword = appConfig.property("db.dbPassword").getString()

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl         = dbUrl
            driverClassName = dbDriver
            username        = dbUser
            password        = dbPassword
            maximumPoolSize = 10
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }

}