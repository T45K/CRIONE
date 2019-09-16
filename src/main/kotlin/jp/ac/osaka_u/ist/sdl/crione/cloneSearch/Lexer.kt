package jp.ac.osaka_u.ist.sdl.crione.cloneSearch

import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.compiler.IScanner
import org.eclipse.jdt.core.compiler.ITerminalSymbols.*

fun getTokenList(code: String): List<Triple<String, Int, Int>> {
    val scanner: IScanner = ToolFactory.createScanner(false, false, true, false)
    scanner.source = code.toCharArray()

    val tokens: MutableList<Triple<String, Int, Int>> = mutableListOf()
    var tokenType: Int = scanner.nextToken
    while (tokenType != TokenNameEOF) {
        val token: String = if (isIdentifier(tokenType)) "$" else scanner.currentTokenSource.toString()
        tokens.add(Triple(token, scanner.currentTokenStartPosition, scanner.currentTokenEndPosition))

        tokenType = scanner.nextToken
    }

    return tokens
}

fun isIdentifier(tokenType: Int): Boolean {
    @Suppress("DEPRECATION")
    when (tokenType) {
        TokenNameIdentifier,
        TokenNameCharacterLiteral,
        TokenNameDoubleLiteral,
        TokenNameIntegerLiteral,
        TokenNameLongLiteral,
        TokenNameStringLiteral,
        TokenNameFloatingPointLiteral -> return true
    }

    return false
}