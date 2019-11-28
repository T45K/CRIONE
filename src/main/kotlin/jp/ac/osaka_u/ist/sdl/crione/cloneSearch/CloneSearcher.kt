package jp.ac.osaka_u.ist.sdl.crione.cloneSearch

import jp.ac.osaka_u.ist.sdl.crione.db.Query
import org.apache.commons.codec.digest.DigestUtils.md5Hex

fun search(query: Query, sourceCodes: List<Pair<String, String>>) =
        sourceCodes.flatMap { sourceCode: Pair<String, String> -> searchFromSingleSourceCode(query.code, sourceCode) }

private fun searchFromSingleSourceCode(query: String, sourceCode: Pair<String, String>): List<Clone> {
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

/**
 * queryがすでに正規化されている形になっているので，そのトークン数は空白の数+1になる
 */
private fun analyzeQuery(query: String): Pair<Int, String> {
    val querySize: Int = query.toCharArray()
            .filter { it == ' ' }
            .count() + 1
    return querySize to md5Hex(query)
}
