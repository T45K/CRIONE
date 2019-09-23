package jp.ac.osaka_u.ist.sdl.crione.testEntity

import jp.ac.osaka_u.ist.sdl.crione.repositoryMining.MyRepository
import org.eclipse.jgit.lib.Repository
import org.refactoringminer.util.GitServiceImpl

object TestInstance {
    val repositoryInstance: Repository = GitServiceImpl().cloneIfNotExists("sample/refactoring-toy-example", "https://github.com/danilofes/refactoring-toy-example.git")
    val myRepositoryInstance: MyRepository = MyRepository(repositoryInstance)
}