package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import jp.ac.osaka_u.ist.sdl.crione.testEntity.TestInstance
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jgit.lib.Repository
import org.junit.Test
import kotlin.test.assertEquals

class RefactoringMinerKtTest {
    @Test
    fun testMining() {
        val repository: Repository = TestInstance.repositoryInstance
        val results: Set<String> = mining(repository, "master")
        assertEquals(2, results.size)
        assertThat(results).contains("int s3=3;\n" +
                "int s4=4;\n" +
                "int s5=5;\n" +
                "int s6=6;\n" +
                "int s7=7;\n")
        assertThat(results).contains("System.out.println(\"...\");\n")
    }
}
