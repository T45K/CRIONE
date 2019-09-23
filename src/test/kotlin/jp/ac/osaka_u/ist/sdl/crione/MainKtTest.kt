package jp.ac.osaka_u.ist.sdl.crione

import org.junit.Test

class MainKtTest {

    @Test
    fun testMain() {
        main(arrayOf("sample/refactoring-toy-example", "https://github.com/danilofes/refactoring-toy-example.git", "src"))
    }
}
