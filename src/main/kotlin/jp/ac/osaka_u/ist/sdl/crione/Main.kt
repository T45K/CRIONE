package jp.ac.osaka_u.ist.sdl.crione

import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.Clone
import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.search
import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.MyRepository
import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.mining
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.util.GitServiceImpl
import java.nio.file.Paths

fun main(args: Array<String>) {
    val (projectDir, cloneURL, srcDir) = args
    val repository: Repository = GitServiceImpl().cloneIfNotExists(projectDir, cloneURL)
    val miningResult: List<String> = mining(repository)

    val myRepository = MyRepository(repository)
    for (commitId: String in miningResult) {
        myRepository.checkout(commitId)
        val sourceCodes: List<Pair<String, String>> = myRepository.getSourceCodes(Paths.get(projectDir, srcDir))
        val deletedDiffs: List<String> = myRepository.getDeletedDiffs()

        val clones: List<List<Clone>> = deletedDiffs.map { query -> search(query, sourceCodes) }
        println(clones)
    }
}
