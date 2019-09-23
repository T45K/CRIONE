package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import jp.ac.osaka_u.ist.sdl.crione.testEntity.TestInstance
import org.junit.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

class MyRepositoryTest {
    @Test
    fun testGetDeletedDiff() {
        val myRepository = TestInstance.myRepositoryInstance
        myRepository.checkout("master")
        val deletedDiffs: List<String> = myRepository.getDeletedDiffs()

        assertEquals("import org.DogManager;\r\n", deletedDiffs[0])
        assertEquals("\tpublic void barkBark(DogManager manager) {\r\n" +
                "\t\tSystem.out.println(\"ruff\");\r\n" +
                "\t\tSystem.out.println(\"ruff\");\r\n" +
                "\t\ttakeABreath();\r\n" +
                "\t\tSystem.out.println(\"ruff\");\r\n" +
                "\t\tSystem.out.println(\"ruff\");\r\n" +
                "\t\tSystem.out.println(\"ruff\");\r\n" +
                "\t}\r\n" +
                "\r\n", deletedDiffs[1])
    }

    @Test
    fun testGetSourceCodes() {
        val myRepository = TestInstance.myRepositoryInstance
        myRepository.checkout("master")
        val sourceCodes: List<Pair<String, String>> = myRepository.getSourceCodes(Paths.get("sample/refactoring-toy-example/src"))

        assertEquals(18, sourceCodes.size)
    }

}
