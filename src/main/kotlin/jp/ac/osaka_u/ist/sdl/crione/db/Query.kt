package jp.ac.osaka_u.ist.sdl.crione.db

data class Query(val code: String, val locations: List<Location>)

data class Location(val commitHash: String, val projectName: String)
