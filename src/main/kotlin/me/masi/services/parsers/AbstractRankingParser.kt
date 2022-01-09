package me.masi.services.parsers

import me.masi.services.parsers.api.RankingParser
import java.io.File
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

abstract class AbstractRankingParser<out T> : RankingParser<T> {

    protected fun parseEpochNumber(epochNumberLine: String): Int {
        return REGEX_EPOCH_NUMBER.toRegex().find(epochNumberLine)?.value?.toInt() ?: throw IllegalArgumentException("Missing epoch number.")
    }

    protected fun getEpochFilesFromResources(rankingsFolder: String): List<File> {
        val resource = javaClass.classLoader.getResource(rankingsFolder)
        return Files.walk(Paths.get(resource.toURI()))
            .filter(Files::isRegularFile)
            .map { it.toFile() }
            .toList()
    }

    protected fun getEpochPathsFromResources(rankingsFolder: String): List<Path> {
        val jarPath = javaClass.protectionDomain
            .codeSource
            .location
            .toURI()
            .path
        val uri = URI.create("jar:file:$jarPath")
        return FileSystems.newFileSystem(uri, mutableMapOf<String, Any>())
            .use { fs ->
                Files.walk(fs.getPath(rankingsFolder))
                    .filter(Files::isRegularFile)
                    .toList()
            }
    }
}

private const val REGEX_EPOCH_NUMBER = "\\d+"
