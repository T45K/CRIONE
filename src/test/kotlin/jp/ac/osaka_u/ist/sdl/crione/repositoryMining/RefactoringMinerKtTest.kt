package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import jp.ac.osaka_u.ist.sdl.crione.testEntity.TestInstance
import org.eclipse.jgit.lib.Repository
import org.junit.Test
import kotlin.test.assertEquals

class RefactoringMinerKtTest {
    @Test
    fun testMining() {
        val repository: Repository = TestInstance.repositoryInstance
        val results: List<Pair<String, String>> = mining(repository, "master")
        assertEquals(2, results.size)
        assertEquals("int s3=3;\n" +
                "int s4=4;\n" +
                "int s5=5;\n" +
                "int s6=6;\n" +
                "int s7=7;\n",results[0].first)
        assertEquals("f35b2c8eb8c320f173237e44d04eefb4634649a2", results[0].second)
        assertEquals("System.out.println(\"...\");\n",results[1].first)
        assertEquals("40950c317bd52ea5ce4cf0d19707fe426b66649c", results[1].second)
    }
}
