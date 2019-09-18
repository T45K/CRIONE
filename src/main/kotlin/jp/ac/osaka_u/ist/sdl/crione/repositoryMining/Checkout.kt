package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.nio.file.Paths

fun getSourceCodesFromHistory(commitId: String, repositoryPath: String): List<Pair<String, String>> {
    val repository: Repository = FileRepositoryBuilder()
            .setGitDir(Paths.get(repositoryPath).toFile())
            .build()

    val git = Git(repository)

    git.checkout().setStartPoint(repositoryPath).call()

    return mutableListOf()
}