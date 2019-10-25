package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.CheckoutConflictException
import org.eclipse.jgit.lib.Repository
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

class MyRepository(repository: Repository) {
    private val git: Git = Git(repository)

    fun checkout(commitId: String) {
        try {
            git.checkout().setName(commitId).call()
        } catch (e: CheckoutConflictException) {
            logger.warn("conflict at $commitId")
            git.checkout().setName(".").call()
            git.checkout().setName(commitId).call()
        }
    }

    fun getSourceCodes(srcDir: Path): List<Pair<String, String>> {
        return Files.walk(srcDir)
                .filter(this::isJavaCode)
                .map(this::convertPathToSourceCode)
                .toList()
    }

    private fun isJavaCode(path: Path): Boolean {
        return path.toString().endsWith(".java")
    }

    private fun convertPathToSourceCode(path: Path): Pair<String, String> {
        return path.toString() to String(Files.readAllBytes(path))
    }
}
