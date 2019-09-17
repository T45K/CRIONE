package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.lib.Repository
import org.refactoringminer.api.GitHistoryRefactoringMiner
import org.refactoringminer.api.GitService
import org.refactoringminer.api.Refactoring
import org.refactoringminer.api.RefactoringHandler
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl
import org.refactoringminer.util.GitServiceImpl

fun mining(repositoryPath: String): List<String> {
    val gitService: GitService = GitServiceImpl()
    val repository: Repository = gitService.cloneIfNotExists(repositoryPath, "https://github.com/danilofes/refactoring-toy-example.git")
    val miner: GitHistoryRefactoringMiner = GitHistoryRefactoringMinerImpl()

    val extractMethodCommitHashes: MutableList<String> = mutableListOf()
    miner.detectAll(repository, "master", object : RefactoringHandler() {
        override fun handle(commitId: String?, refactorings: List<Refactoring>?) {
            val containsExtractMethod = refactorings!!.any { isExtractMethodRefactoring(it) }
            if (containsExtractMethod) {
                extractMethodCommitHashes.add(commitId!!)
            }
        }
    })

    return extractMethodCommitHashes
}

private fun isExtractMethodRefactoring(refactoring: Refactoring): Boolean {
    return refactoring.toString().contains("Extract Method")
}
