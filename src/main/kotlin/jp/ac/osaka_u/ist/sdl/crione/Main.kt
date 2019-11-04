package jp.ac.osaka_u.ist.sdl.crione

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.Clone
import jp.ac.osaka_u.ist.sdl.crione.cloneSearch.search
import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.mining
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.util.GitServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.streams.toList

class Main

val logger: Logger = LoggerFactory.getLogger(Main::class.java)
val JSON_FILE_PATH: Path = Paths.get("src/main/resources/queryCode.json")

fun main(args: Array<String>) {
    val (mode: Mode, projectDir, cloneURL, srcDir, trackingBranch) = buildFromArgs(args.toList())
    val mapper: ObjectMapper = jacksonObjectMapper()
    val queryCodes: MutableSet<String>
    queryCodes = try {
        mapper.readValue(JSON_FILE_PATH.toFile())
    } catch (e: MismatchedInputException) {
        mutableSetOf()
    }

    val repository: Repository = GitServiceImpl().cloneIfNotExists(projectDir, cloneURL)
    when (mode) {
        Mode.SEARCH -> {
            val sourceCodes: List<Pair<String, String>> = Files.walk(Paths.get(projectDir, srcDir))
                    .filter { it.toString().endsWith(".java") }
                    .map { it.toString() to String(Files.readAllBytes(it)) }
                    .toList()

            val clones: List<List<Clone>> = queryCodes.map { search(it, sourceCodes) }
                    .toList()
        }
        Mode.MINING -> {
            val extractedCodes: Set<String> = mining(repository, trackingBranch)
            queryCodes.addAll(extractedCodes)
            val objectMapper = ObjectMapper()
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
            val contents: ByteArray = objectMapper.writeValueAsBytes(queryCodes)
            Files.write(JSON_FILE_PATH, contents, StandardOpenOption.WRITE)
        }
    }
}
