package jp.ac.osaka_u.ist.sdl.crione.repositoryMining

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffAlgorithm
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.Edit
import org.eclipse.jgit.diff.RawText
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.AbbreviatedObjectId
import org.eclipse.jgit.lib.ConfigConstants
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.ObjectLoader
import org.eclipse.jgit.lib.ObjectReader
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevTree
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

class MyRepository(private val repository: Repository) {
    private val git: Git = Git(repository)
    private val reader: ObjectReader = repository.newObjectReader()
    private val diffAlgorithm: DiffAlgorithm = DiffAlgorithm.getAlgorithm(repository.config.getEnum(ConfigConstants.CONFIG_DIFF_SECTION, null, ConfigConstants.CONFIG_KEY_ALGORITHM, DiffAlgorithm.SupportedAlgorithm.HISTOGRAM))

    fun checkout(commitId: String) {
        git.checkout().setName(commitId).call()
    }

    fun getSourceCodes(srcDir: Path): List<Pair<String, String>> {
        return Files.walk(srcDir)
                .filter { path -> path.toString().endsWith(".java") }
                .map { path -> path.toString() to String(Files.readAllBytes(path)) }
                .toList()
    }

    fun getDeletedDiffs(): List<String> {
        val headObjectId: ObjectId = repository.resolve(Constants.HEAD)
        val previousHeadObjectId: ObjectId = repository.resolve("${Constants.HEAD}^")
        val headTreeParser: AbstractTreeIterator = getTreeParser(headObjectId)
        val previousHeadTreeParser: AbstractTreeIterator = getTreeParser(previousHeadObjectId)

        return git.diff()
                .setNewTree(headTreeParser)
                .setOldTree(previousHeadTreeParser)
                .call()
                .flatMap { diffEntry -> calculateDeletedDiffs(diffEntry, diffAlgorithm, reader) }
                .toList()
    }

    private fun getTreeParser(objectId: ObjectId): AbstractTreeIterator {
        val walk = RevWalk(repository)
        val commit: RevCommit = walk.parseCommit(objectId)
        val tree: RevTree = walk.parseTree(commit.tree.id)

        val treeParser = CanonicalTreeParser()
        treeParser.reset(reader, tree.id)
        walk.dispose()

        return treeParser
    }

    private fun calculateDeletedDiffs(diffEntry: DiffEntry, diffAlgorithm: DiffAlgorithm, reader: ObjectReader): List<String> {
        val oldText: RawText = readText(diffEntry.oldId, reader)
        val newText: RawText = readText(diffEntry.newId, reader)

        return diffAlgorithm.diff(RawTextComparator.DEFAULT, oldText, newText)
                .filter { it.type == Edit.Type.DELETE }
                .map { oldText.getString(it.beginA, it.endA, false) }
                .toList()
    }

    private fun readText(blobId: AbbreviatedObjectId, reader: ObjectReader): RawText {
        val loader: ObjectLoader = reader.open(blobId.toObjectId(), Constants.OBJ_BLOB)
        return RawText(loader.cachedBytes)
    }
}
