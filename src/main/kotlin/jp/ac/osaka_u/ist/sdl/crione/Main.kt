package jp.ac.osaka_u.ist.sdl.crione

import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.Clone
import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.search
import jp.ac.osaka_u.ist.sdl.crione.db.SQL
import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.mining
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.util.GitServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class Main

val logger: Logger = LoggerFactory.getLogger(Main::class.java)

fun main(args: Array<String>) {
    val (mode: Mode, projectDir, cloneURL, srcDir, trackingBranch) = buildFromArgs(args.toList()) ?: return
    val repository: Repository = GitServiceImpl().cloneIfNotExists(projectDir, cloneURL)
    val sql = SQL()
    when (mode) {
        Mode.SEARCH -> {
            val sourceCodes: List<Pair<String, String>> = Files.walk(Paths.get(projectDir, srcDir))
                    .filter { it.toString().endsWith(".java") }
                    .map { it.toString() to String(Files.readAllBytes(it)) }
                    .toList()


            val clones: List<List<Clone>> = sql.findAll().parallelStream()
                    .map { search(it, sourceCodes) }
                    .toList()

            logger.info(clones.toString())
        }

        Mode.MINING -> {
            mining(repository, trackingBranch)
                    .map { sql.insert(it.first, it.second, projectDir) }
        }
    }

    sql.close()
}
