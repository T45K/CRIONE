package jp.ac.osaka_u.ist.sdl.crione.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class SQL {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:./code.db")
            ?: throw RuntimeException("bad db connection")
    private val statement: Statement = connection.createStatement()

    init {
        statement.executeUpdate("create table if not exists query (code string, id long primary key)")
        statement.executeUpdate("create table if not exists location (id Long, commitHash string, projectName string)")
    }

    fun insert(code: String, commitHash: String, projectName: String) {
        val id: Long = getIdByCodeFromQuery(code)
        if (id != -1L) {
            statement.executeUpdate("insert into location values($id, '$commitHash', '$projectName')")
        } else {
            statement.executeUpdate("insert into query(code) values('$code')")
            statement.executeUpdate("insert into location values(${getIdByCodeFromQuery(code)}, '$commitHash', '$projectName')")
        }
    }

    fun findAll(): List<Query> {
        val queryResultSet: ResultSet = statement.executeQuery("select * from query")
        val queries: MutableList<Query> = mutableListOf()
        while (queryResultSet.next()) {
            val code: String = queryResultSet.getString("code")
            val id: Long = queryResultSet.getLong("id")
            val locations: List<Location> = getLocations(id)
            queries.add(Query(code, locations))
        }

        return queries
    }

    fun close() = connection.close()

    private fun getLocations(id: Long): List<Location> {
        val locationResultSet: ResultSet = statement.executeQuery("select * from location where id = $id")
        val locations: MutableList<Location> = mutableListOf()
        while (locationResultSet.next()) {
            locations.add(Location(locationResultSet.getString("commitHash"), locationResultSet.getString("projectName")))
        }

        return locations
    }

    private fun getIdByCodeFromQuery(code: String): Long {
        val result: ResultSet = statement.executeQuery("select * from 'query' where 'code' = '$code'")
        return if (result.next()) result.getLong("id") else -1L
    }
}
