package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import jp.ac.osaka_u.ist.sdl.crione.testEntity.TestInstance
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jgit.lib.Repository
import org.junit.Test
import org.refactoringminer.util.GitServiceImpl
import kotlin.test.assertEquals

class RefactoringMinerKtTest {
    @Test
    fun testMining() {
        val repository: Repository = TestInstance.repositoryInstance
        val results: Set<String> = mining(repository, "master", 0)
                .map { it.first }
                .toSet()
        assertEquals(2, results.size)
        assertThat(results).contains("int $ = $ ; int $ = $ ; int $ = $ ; int $ = $ ; int $ = $ ;")
        assertThat(results).contains("$ . $ . $ ( $ ) ;")
    }
}
