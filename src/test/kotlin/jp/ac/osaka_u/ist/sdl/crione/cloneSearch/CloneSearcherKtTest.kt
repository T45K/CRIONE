package jp.ac.osaka_u.ist.sdl.crione.cloneSearch

import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList
import kotlin.test.assertEquals

class CloneSearcherKtTest {

    @Test
    fun testSearch() {
        val query = "int a = 0; char b = '0'; double c = 0d; float d = 0f; long e = 0l; String f = \"0\";"
        val fileList: List<Pair<Path, String>> = listOf(getSourceCode(Paths.get("sample/cloneSearch/File1.java")), getSourceCode(Paths.get("sample/cloneSearch/File2.java")), getSourceCode(Paths.get("sample/cloneSearch/File3.java")))

        val clones: List<Clone> = search(query, fileList)
        assertEquals(3, clones.size)

        val (filePath0, beginIndex0, endIndex0, code0) = clones[0]
        assertEquals(Paths.get("sample/cloneSearch/File1.java"), filePath0)
        assertEquals(85, beginIndex0)
        assertEquals(206, endIndex0)
        assertEquals("int a = 0;\n" +
                "        char b = '0';\n" +
                "        double c = 0d;\n" +
                "        float d = 0f;\n" +
                "        long e = 0l;\n" +
                "        String f = \"0\";", code0)

        val (filePath1, beginIndex1, endIndex1, code1) = clones[1]
        assertEquals(Paths.get("sample/cloneSearch/File1.java"), filePath1)
        assertEquals(264, beginIndex1)
        assertEquals(380, endIndex1)
        assertEquals("int g = 1;\n" +
                "        char h = 1;\n" +
                "        double i = 1;\n" +
                "        float j = 1;\n" +
                "        long k = 1;\n" +
                "        String l = \"1\";", code1)

        val (filePath2, beginIndex2, endIndex2, code2) = clones[2]
        assertEquals(Paths.get("sample/cloneSearch/File2.java"), filePath2)
        assertEquals(84, beginIndex2)
        assertEquals(205, endIndex2)
        assertEquals("int g = 1;\n" +
                "        char h = '1';\n" +
                "        double i = 1d;\n" +
                "        float j = 1f;\n" +
                "        long k = 1l;\n" +
                "        String l = \"1\";", code2)
    }

    private fun getSourceCode(filePath: Path): Pair<Path, String> {
        return filePath to String(Files.readAllBytes(filePath))
    }
}
