package jp.ac.osaka_u.ist.sdl.crione.cloneSearch

import org.apache.commons.codec.digest.DigestUtils.md5Hex

fun search(query: String, sourceCodes: List<SourceCode>): List<Clone> {
    return sourceCodes.flatMap { sourceCode: SourceCode -> searchFromSingleSourceCode(query, sourceCode) }
}

private fun searchFromSingleSourceCode(query: String, sourceCode: SourceCode): List<Clone> {
    val tokens: List<Triple<String, Int, Int>> = getTokenList(sourceCode.contents)
    val (querySize: Int, hashedQuery: String) = analyzeQuery(query)

    val clones: MutableList<Clone> = mutableListOf()
    for (i in querySize..tokens.size) {
        val tokenSequence = tokens.subList(i - querySize, i - 1).joinToString(" ") { token -> token.first }
        if (md5Hex(tokenSequence) != hashedQuery) {
            continue
        }

        clones.add(Clone(sourceCode.filePath, tokens[i - querySize].second, tokens[i - 1].third, tokenSequence))
    }

    return clones
}

private fun analyzeQuery(query: String): Pair<Int, String> {
    val tokens: List<Triple<String, Int, Int>> = getTokenList(query)
    val normalizedCode = tokens.joinToString(" ") { token -> token.first }
    return tokens.size to normalizedCode
}
