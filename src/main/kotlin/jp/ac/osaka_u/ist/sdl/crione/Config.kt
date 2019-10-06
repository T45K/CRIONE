package jp.ac.osaka_u.ist.sdl.crione

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import kotlin.system.exitProcess

data class Config(val projectDir: String,
                  val cloneURL: String,
                  val srcDir: List<String>,
                  val trackingBranch: String)

fun buildFromArgs(args: List<String>): Config {
    val builder = Builder()
    val parser = CmdLineParser(builder)

    try {
        parser.parseArgument(args)
        return builder.build()
    } catch (e: CmdLineException) {
        parser.printUsage(System.out)
        exitProcess(0)
    }
}

class Builder {
    private var projectDir: String = ""
    private var cloneURL: String = ""
    private var srcDirs: MutableList<String> = mutableListOf()
    private var trackingBranch: String = "master"

    fun build(): Config {
        return Config(projectDir, cloneURL, srcDirs, trackingBranch)
    }

    @Option(name = "-p", aliases = ["--project-dir"], usage = "Target project directory", required = true)
    private fun setProjectDir(projectDir: String) {
        this.projectDir = projectDir
    }

    @Option(name = "-c", aliases = ["--clone-url"], usage = "Remote repository url")
    private fun setCloneURL(cloneURL: String) {
        this.cloneURL = cloneURL
    }

    @Option(name = "-s", aliases = ["--src-dir"], usage = "Source directory", required = true)
    private fun setSrcDir(srcDir: String) {
        this.srcDirs.add(srcDir)
    }

    @Option(name = "-t", aliases = ["--tracking-branch"], usage = "Target tracking branch")
    private fun setTrackingBranch(trackingBranch: String) {
        this.trackingBranch = trackingBranch
    }
}
