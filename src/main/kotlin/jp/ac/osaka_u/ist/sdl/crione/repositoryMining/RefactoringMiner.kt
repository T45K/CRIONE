package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import gr.uom.java.xmi.diff.ExtractOperationRefactoring
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.api.GitHistoryRefactoringMiner
import org.refactoringminer.api.Refactoring
import org.refactoringminer.api.RefactoringHandler
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl

fun mining(repository: Repository, trackingBranch: String): List<Pair<String, String>> {
    val miner: GitHistoryRefactoringMiner = GitHistoryRefactoringMinerImpl()
    val extractedCodeAndCommitHashes: MutableList<Pair<String, String>> = mutableListOf()
    miner.detectAll(repository, trackingBranch, object : RefactoringHandler() {
        override fun handle(commitId: String?, refactorings: List<Refactoring>?) {
            extractedCodeAndCommitHashes.addAll(refactorings!!.filter(::isExtractMethodRefactoring)
                    .map { flatStatements(it as ExtractOperationRefactoring) to commitId!! }
                    .toList())
        }
    })

    return extractedCodeAndCommitHashes
}

private fun isExtractMethodRefactoring(refactoring: Refactoring): Boolean {
    return refactoring.name == "Extract Method"
}

private fun flatStatements(refactoring: ExtractOperationRefactoring): String {
    return refactoring.extractedOperation
            .body
            .compositeStatement
            .statements
            .joinToString("") { it.toString() }
}
