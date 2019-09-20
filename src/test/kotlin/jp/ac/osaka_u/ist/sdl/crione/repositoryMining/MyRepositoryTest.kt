package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.lib.Repository
import org.junit.Test
import org.refactoringminer.util.GitServiceImpl
import kotlin.test.assertEquals

class MyRepositoryTest {
    companion object {
        private const val projectPath: String = "sample/refactoring-toy-example"
        private const val cloneURL: String = "https://github.com/danilofes/refactoring-toy-example.git"
    }

    private val repository: Repository = GitServiceImpl().cloneIfNotExists(projectPath, cloneURL)

    @Test
    fun testGetDeletedDiff() {
        val myRepository = MyRepository(repository)
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
        val myRepository = MyRepository(repository)
        myRepository.checkout("master")
        val sourceCodes: List<Pair<String, String>> = myRepository.getSourceCodes("sample/refactoring-toy-example/src")

        assertEquals(18, sourceCodes.size)
    }

}