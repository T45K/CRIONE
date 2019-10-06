package jp.ac.osaka_u.ist.sdl.crione

import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.Clone
import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.search
import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.MyRepository
import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.mining
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.util.GitServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths

class Main

val logger: Logger = LoggerFactory.getLogger(Main::class.java)

fun main(args: Array<String>) {
    val (projectDir, cloneURL, srcDirs, trackingBranch) = buildFromArgs(args.toList())
    val repository: Repository = GitServiceImpl().cloneIfNotExists(projectDir, cloneURL)
    val miningResult: List<String> = mining(repository, trackingBranch)

    val myRepository = MyRepository(repository)
    for (commitId: String in miningResult) {
        myRepository.checkout(commitId)
        val srcDir: String = srcDirs.find { Files.exists(Paths.get(it)) }!!
        val sourceCodes: List<Pair<String, String>> = myRepository.getSourceCodes(Paths.get(projectDir, srcDir))
        val deletedDiffs: List<String> = myRepository.getDeletedDiffs()

        val clones: List<List<Clone>> = deletedDiffs.map { query -> search(query, sourceCodes) }
        logger.info(clones.size.toString())
    }
}
