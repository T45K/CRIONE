package jp.ac.osaka_u.ist.sdl.crione.cloneSearch

import java.nio.file.Path

data class Clone(val filePath: Path, val beginIndex: Int, val endIndex: Int, val code: String)
