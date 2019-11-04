package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import gr.uom.java.xmi.decomposition.UMLOperationBodyMapper
import gr.uom.java.xmi.diff.ExtractOperationRefactoring
import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.getTokenizedStatement
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.api.GitHistoryRefactoringMiner
import org.refactoringminer.api.Refactoring
import org.refactoringminer.api.RefactoringHandler
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RefactoringMiner

val logger: Logger = LoggerFactory.getLogger(RefactoringMiner::class.java)

fun mining(repository: Repository, trackingBranch: String): Set<String> {
    val miner: GitHistoryRefactoringMiner = GitHistoryRefactoringMinerImpl()
    val extractedCode: MutableSet<String> = mutableSetOf()
    miner.detectAll(repository, trackingBranch, object : RefactoringHandler() {
        override fun handle(commitId: String?, refactorings: List<Refactoring>?) {
            val extractedMethods: Set<String> = refactorings!!.asSequence().filter(::isExtractMethodRefactoring)
                    .map { formatStatements(it as ExtractOperationRefactoring) }
                    .filter { it.isNotEmpty() }
                    .distinct()
                    .toSet()

            extractedCode.addAll(extractedMethods)
        }

        override fun handleException(commitId: String?, e: Exception?) {
            logger.error(e.toString())
        }
    })

    return extractedCode
}

private fun isExtractMethodRefactoring(refactoring: Refactoring): Boolean {
    return refactoring.name == "Extract Method"
}

private fun formatStatements(refactoring: ExtractOperationRefactoring): String {
    val mapper: UMLOperationBodyMapper = refactoring.bodyMapper
    println(getTokenizedStatement(mapper.mappings.joinToString("") { it.fragment1.toString() }))
    return getTokenizedStatement(mapper.mappings.joinToString("") { it.fragment1.toString() })
}
