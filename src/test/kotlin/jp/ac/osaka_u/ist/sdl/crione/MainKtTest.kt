package jp.ac.osaka_u.ist.sdl.crione

import org.junit.Test

class MainKtTest {

    @Test
    fun testMain1() {
        main(arrayOf("-p", "sample/refactoring-toy-example", "-c", "https://github.com/danilofes/refactoring-toy-example.git", "-s", "src"))
    }

    @Test
    fun testMain2() {
        main(arrayOf("-p", "sample/refactoring-toy-example", "-c", "https://github.com/danilofes/refactoring-toy-example.git", "-s", "source"))
    }
}
