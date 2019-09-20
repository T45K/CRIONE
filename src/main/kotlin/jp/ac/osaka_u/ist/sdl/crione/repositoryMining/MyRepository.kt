package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffAlgorithm
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.Edit
import org.eclipse.jgit.diff.EditList
import org.eclipse.jgit.diff.RawText
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.AbbreviatedObjectId
import org.eclipse.jgit.lib.ConfigConstants
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectLoader
import org.eclipse.jgit.lib.ObjectReader
import org.eclipse.jgit.lib.Repository
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class MyRepository(private val repository: Repository) {
    private val git: Git = Git(repository)
    private val reader: ObjectReader = repository.newObjectReader()
    private val diffAlgorithm: DiffAlgorithm = DiffAlgorithm.getAlgorithm(repository.config.getEnum(ConfigConstants.CONFIG_DIFF_SECTION, null, ConfigConstants.CONFIG_KEY_ALGORITHM, DiffAlgorithm.SupportedAlgorithm.HISTOGRAM))

    fun checkout(commitId: String) {
        git.checkout().setName(commitId).call()
    }

    fun getSourceCodes(srcDir: String): List<Pair<String, String>> {
        return Files.walk(Paths.get(srcDir))
                .filter { path -> path.toString().endsWith(".java") }
                .map { path -> path.toString() to String(Files.readAllBytes(path)) }
                .toList()
    }

    fun getDeletedDiff(): List<String> {
        return git.diff()
                .setDestinationPrefix("HEAD^")
                .call()
                .flatMap { diffEntry -> getDiff(diffEntry, diffAlgorithm, reader) }
                .filter { edit -> edit.type == Edit.Type.DELETE }
                .map { it.toString() }
                .toList()
    }

    private fun getDiff(diffEntry: DiffEntry, diffAlgorithm: DiffAlgorithm, reader: ObjectReader): EditList {
        val oldText: RawText = readText(diffEntry.oldId, reader)
        val newText: RawText = readText(diffEntry.newId, reader)

        return diffAlgorithm.diff(RawTextComparator.DEFAULT, oldText, newText)
    }

    private fun readText(blobId: AbbreviatedObjectId, reader: ObjectReader): RawText {
        val loader: ObjectLoader = reader.open(blobId.toObjectId(), Constants.OBJ_BLOB)
        return RawText(loader.cachedBytes)
    }
}
