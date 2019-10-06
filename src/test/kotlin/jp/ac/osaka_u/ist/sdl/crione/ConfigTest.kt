package jp.ac.osaka_u.ist.sdl.crione

import org.junit.Test
import kotlin.test.assertEquals

class ConfigTest {

    @Test
    fun configTest1() {
        val (projectDir, cloneURL, srcDir, trackingBranch) = buildFromArgs(listOf("-p", "sample/refactoring-toy-example", "-c", "https://github.com/danilofes/refactoring-toy-example.git", "-s", "src"))
        assertEquals("sample/refactoring-toy-example", projectDir)
        assertEquals("https://github.com/danilofes/refactoring-toy-example.git", cloneURL)
        assertEquals(listOf("src"), srcDir)
        assertEquals("master", trackingBranch)
    }

    @Test
    fun configTest2() {
        val (projectDir, cloneURL, srcDir, trackingBranch) = buildFromArgs(listOf("-p", "sample/refactoring-toy-example", "-c", "https://github.com/danilofes/refactoring-toy-example.git", "-s", "src", "-t", "trunk"))
        assertEquals("sample/refactoring-toy-example", projectDir)
        assertEquals("https://github.com/danilofes/refactoring-toy-example.git", cloneURL)
        assertEquals(listOf("src"), srcDir)
        assertEquals("trunk", trackingBranch)
    }

    @Test
    fun configTest3() {
        val (_, _, srcDir, _) = buildFromArgs(listOf("-p", "sample/refactoring-toy-example", "-s", "src", "-s", "source"))
        assertEquals(listOf("src", "source"), srcDir)
    }
}