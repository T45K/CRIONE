package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.lib.Repository
import org.junit.Test
import org.refactoringminer.api.GitService
import org.refactoringminer.util.GitServiceImpl
import kotlin.test.assertEquals

class RefactoringMinerKtTest {
    companion object {
        private const val projectDir: String = "sample/refactoring-toy-example"
        private const val cloneURL: String = "https://github.com/danilofes/refactoring-toy-example.git"
    }

    @Test
    fun testMining() {
        val gitService: GitService = GitServiceImpl()
        val repository: Repository = gitService.cloneIfNotExists(projectDir, cloneURL)
        val hashes: List<String> = mining(repository)
        assertEquals(2, hashes.size)
        assertEquals("f35b2c8eb8c320f173237e44d04eefb4634649a2", hashes[0])
        assertEquals("40950c317bd52ea5ce4cf0d19707fe426b66649c", hashes[1])
    }
}