package jp.ac.osaka_u.ist.sdl.crione.cloneSearch

import org.apache.commons.codec.digest.DigestUtils.md5Hex
import java.nio.file.Path

fun search(query: String, sourceCodes: List<Pair<Path, String>>) =
        sourceCodes.flatMap { sourceCode: Pair<Path, String> -> searchFromSingleSourceCode(query, sourceCode) }

private fun searchFromSingleSourceCode(query: String, sourceCode: Pair<Path, String>): List<Clone> {
    val tokens: List<Triple<String, Int, Int>> = getTokenList(sourceCode.second)
    val (querySize: Int, hashedQuery: String) = analyzeQuery(query)

    val clones: MutableList<Clone> = mutableListOf()
    for (i in querySize..tokens.size) {
        val tokenSequence = tokens.subList(i - querySize, i).joinToString(" ") { token -> token.first }
        if (md5Hex(tokenSequence) != hashedQuery) {
            continue
        }

        val beginIndex = tokens[i - querySize].second
        val endIndex = tokens[i - 1].third
        clones.add(Clone(sourceCode.first, beginIndex, endIndex, sourceCode.second.substring(beginIndex, endIndex + 1)))
    }

    return clones
}

private fun analyzeQuery(query: String): Pair<Int, String> {
    val tokens: List<Triple<String, Int, Int>> = getTokenList(query)
    val normalizedCode = tokens.joinToString(" ") { token -> token.first }
    return tokens.size to md5Hex(normalizedCode)
}
