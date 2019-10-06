package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.lib.Repository
import org.refactoringminer.api.GitHistoryRefactoringMiner
import org.refactoringminer.api.Refactoring
import org.refactoringminer.api.RefactoringHandler
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl

fun mining(repository: Repository, trackingBranch: String = "master"): List<String> {
    val miner: GitHistoryRefactoringMiner = GitHistoryRefactoringMinerImpl()
    val extractMethodCommitHashes: MutableList<String> = mutableListOf()
    miner.detectAll(repository, trackingBranch, object : RefactoringHandler() {
        override fun handle(commitId: String?, refactorings: List<Refactoring>?) {
            val containsExtractMethod = refactorings?.any(::isExtractMethodRefactoring) ?: false
            if (containsExtractMethod) {
                extractMethodCommitHashes.add(commitId!!)
            }
        }
    })

    return extractMethodCommitHashes
}

private fun isExtractMethodRefactoring(refactoring: Refactoring): Boolean {
    return refactoring.name == "Extract Method"
}
