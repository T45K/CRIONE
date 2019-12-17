package jp.ac.osaka_u.ist.sdl.crione.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class SQL() {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:./code.db")
            ?: throw RuntimeException("bad db connection")
    private var statement: Statement = connection.createStatement()

    init {
        statement.executeUpdate("create table if not exists query (code string, id long primary key)")
        statement.executeUpdate("create table if not exists location (id Long, commitHash string, projectName string)")
    }

    fun insert(code: String, commitHash: String, projectName: String) {
        val id: Long = getId(code, "code", "query")
        if (id != -1L) {
            statement.executeUpdate("insert into location values($id, '$commitHash', '$projectName')")
        } else {
            statement.executeUpdate("insert into query(code) values('$code')")
            statement.executeUpdate("insert into location values(${getId(code, "code", "query")}, '$commitHash', '$projectName')")
        }
    }

    fun findAll(): List<Query> {
        statement = connection.createStatement()
        val queryResultSet: ResultSet = statement.executeQuery("select * from query")
        val queries: MutableList<Query> = mutableListOf()
        while (queryResultSet.next()) {
            val code: String = queryResultSet.getString("code")
            val id: Long = queryResultSet.getLong("id")
            val locations: List<Location> = getLocations(id)
            val query = Query(code, locations)
            println(query)
            queries.add(query)

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

    private fun getId(recordName: String, columnName: String, tableName: String): Long {
        val result: ResultSet = statement.executeQuery("select * from $tableName where $columnName = $recordName")
        return if (result.next()) result.getLong("id") else -1L
    }
}
