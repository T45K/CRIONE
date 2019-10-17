package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import gr.uom.java.xmi.diff.ExtractOperationRefactoring
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.api.GitHistoryRefactoringMiner
import org.refactoringminer.api.Refactoring
import org.refactoringminer.api.RefactoringHandler
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

class RefactoringMiner

val logger: Logger = LoggerFactory.getLogger(RefactoringMiner::class.java)

fun mining(repository: Repository, trackingBranch: String): List<Pair<String, String>> {
    val miner: GitHistoryRefactoringMiner = GitHistoryRefactoringMinerImpl()
    val extractedCodeAndCommitHashes: MutableList<Pair<String, String>> = mutableListOf()
    miner.detectAll(repository, trackingBranch, object : RefactoringHandler() {
        override fun handle(commitId: String?, refactorings: List<Refactoring>?) {
            val extractedMethodList: List<Pair<String, String>> = refactorings!!.asSequence().filter(::isExtractMethodRefactoring)
                    .map { flatStatements(it as ExtractOperationRefactoring) }
                    .distinct()
                    .map { contents -> contents to commitId!! }
                    .toList()

            extractedMethodList.map { it.toString() }
                    .forEach(logger::info)

            extractedCodeAndCommitHashes.addAll(extractedMethodList)
        }

        override fun handleException(commitId: String?, e: Exception?) {
            logger.error(e.toString())
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
