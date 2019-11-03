package jp.ac.osaka_u.ist.sdl.crione

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option

data class Config(val mode: Mode,
                  val projectDir: String,
                  val cloneURL: String,
                  val srcDir: String,
                  val trackingBranch: String)

fun buildFromArgs(args: List<String>): Config {
    val builder = Builder()
    val parser = CmdLineParser(builder)

    try {
        parser.parseArgument(args)
        return builder.build()
    } catch (e: CmdLineException) {
        parser.printUsage(System.out)
        throw RuntimeException()
    }
}

class Builder {
    private var mode: Mode = Mode.SEARCH
    private var projectDir: String = ""
    private var cloneURL: String = ""
    private var srcDir: String = ""
    private var trackingBranch: String = "master"

    fun build(): Config {
        return Config(mode, projectDir, cloneURL, srcDir, trackingBranch)
    }

    @Option(name = "-m", aliases = ["--mode"], usage = "mode: SEARCH or MINING", required = true)
    private fun setMode(modeString: String) {
        this.mode = when (modeString) {
            "s" -> Mode.SEARCH
            "m" -> Mode.MINING
            else -> throw RuntimeException()
        }
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
        this.srcDir = srcDir
    }

    @Option(name = "-t", aliases = ["--tracking-branch"], usage = "Target tracking branch")
    private fun setTrackingBranch(trackingBranch: String) {
        this.trackingBranch = trackingBranch
    }
}

enum class Mode {
    SEARCH,
    MINING
}
