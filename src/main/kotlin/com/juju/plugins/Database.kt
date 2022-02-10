package com.juju.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = appConfig.property("db.jdbcUrl").getString()
    private val dbDriver = appConfig.property("db.dbDriver").getString()
    private val dbUser = appConfig.property("db.dbUser").getString()
    private val dbPassword = appConfig.property("db.dbPassword").getString()

    fun init() {
        Database.connect(dbUrl, driver = dbDriver, user = dbUser, password = dbPassword)
    }

}