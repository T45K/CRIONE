package jp.ac.osaka_u.ist.sdl.crione

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.test.assertEquals

class ConfigTest {

    @Test
    fun configTest1() {
        val (mode: Mode, projectDir, cloneURL, srcDir, trackingBranch) = buildFromArgs(listOf("-m", "s", "-p", "sample/refactoring-toy-example", "-c", "https://github.com/danilofes/refactoring-toy-example.git", "-s", "src"))
        assertThat(mode == Mode.SEARCH)
        assertEquals("sample/refactoring-toy-example", projectDir)
        assertEquals("https://github.com/danilofes/refactoring-toy-example.git", cloneURL)
        assertEquals("src", srcDir)
        assertEquals("master", trackingBranch)
    }

    @Test
    fun configTest2() {
        val (mode: Mode, projectDir, cloneURL, srcDir, trackingBranch) = buildFromArgs(listOf("-m", "m", "-p", "sample/refactoring-toy-example", "-c", "https://github.com/danilofes/refactoring-toy-example.git", "-s", "src", "-t", "trunk"))
        assertThat(mode == Mode.MINING)
        assertEquals("sample/refactoring-toy-example", projectDir)
        assertEquals("https://github.com/danilofes/refactoring-toy-example.git", cloneURL)
        assertEquals("src", srcDir)
        assertEquals("trunk", trackingBranch)
    }

    @Test(expected = java.lang.RuntimeException::class)
    fun configTest3() {
        buildFromArgs(listOf("-m", "hoge", "-p", "sample/refactoring-toy-example", "-s", "src", "-s", "source"))
    }

    @Test(expected = RuntimeException::class)
    fun configTest4() {
        buildFromArgs(listOf())
    }
}