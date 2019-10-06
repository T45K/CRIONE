package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import jp.ac.osaka_u.ist.sdl.crione.testEntity.TestInstance
import org.junit.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

class MyRepositoryTest {

    @Test
    fun testGetSourceCodes() {
        val myRepository = TestInstance.myRepositoryInstance
        myRepository.checkout("master")
        val sourceCodes: List<Pair<String, String>> = myRepository.getSourceCodes(Paths.get("sample/refactoring-toy-example/src"))

        assertEquals(18, sourceCodes.size)
    }

}
