package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.lib.Repository
import org.refactoringminer.api.GitHistoryRefactoringMiner
import org.refactoringminer.api.GitService
import org.refactoringminer.api.Refactoring
import org.refactoringminer.api.RefactoringHandler
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl
import org.refactoringminer.util.GitServiceImpl


/**
 * repositoryPath must include ".../.git"
 */
fun mining(repositoryPath: String) {
    val gitService: GitService = GitServiceImpl()
    val miner: GitHistoryRefactoringMiner = GitHistoryRefactoringMinerImpl()
    val repository: Repository = gitService.cloneIfNotExists(repositoryPath, "https://github.com/danilofes/refactoring-toy-example.git")

    miner.detectAll(repository, "master", object : RefactoringHandler() {
        override fun handle(commitId: String?, refactorings: List<Refactoring>?) {
            println("Refactorings at " + commitId!!)
            for (ref in refactorings!!) {
                println(ref.toString())
            }
        }
    })
}