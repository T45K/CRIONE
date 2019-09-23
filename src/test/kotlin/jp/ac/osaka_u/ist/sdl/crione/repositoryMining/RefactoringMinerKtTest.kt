package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import jp.ac.osaka_u.ist.sdl.crione.testEntity.TestInstance
import org.eclipse.jgit.lib.Repository
import org.junit.Test
import kotlin.test.assertEquals

class RefactoringMinerKtTest {
    @Test
    fun testMining() {
        val repository: Repository = TestInstance.repositoryInstance
        val hashes: List<String> = mining(repository)
        assertEquals(2, hashes.size)
        assertEquals("f35b2c8eb8c320f173237e44d04eefb4634649a2", hashes[0])
        assertEquals("40950c317bd52ea5ce4cf0d19707fe426b66649c", hashes[1])
    }
}
