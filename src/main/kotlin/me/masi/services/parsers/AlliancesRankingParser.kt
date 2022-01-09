package me.masi.services.parsers

import me.masi.dto.alliances.AlliancesRanking
import me.masi.dto.alliances.AlliancesRankingRow
import me.masi.enums.EAppTrigger
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class AlliancesRankingParser(
    private val appTrigger: EAppTrigger = EAppTrigger.IDE,
) : AbstractRankingParser<AlliancesRanking>() {

    override fun parse(): List<AlliancesRanking> {
        return when (appTrigger) {
            EAppTrigger.IDE -> parseRankingsFromFiles()
            EAppTrigger.JAR -> parseRankingsFromPaths()
        }
    }

    private fun parseRankingsFromFiles(): List<AlliancesRanking> {
        val epochFiles = getEpochFilesFromResources(RANKING_ALLIANCES_FOLDER)
        return parseAlliancesRankingFiles(epochFiles)
    }

    private fun parseRankingsFromPaths(): List<AlliancesRanking> {
        val epochPaths = getEpochPathsFromResources(RANKING_ALLIANCES_FOLDER)
        return parseAlliancesRakingPaths(epochPaths)
    }

    private fun parseAlliancesRankingFiles(epochFiles: List<File>): List<AlliancesRanking> {
        val alliancesRankings = mutableListOf<AlliancesRanking>()
        epochFiles.forEach {
            val epochContent = it.readLines()
            alliancesRankings.add(parseAlliancesRankingContent(epochContent))
        }
        return alliancesRankings
    }

    private fun parseAlliancesRakingPaths(epochPaths: List<Path>): List<AlliancesRanking> {
        val alliancesRankings = mutableListOf<AlliancesRanking>()
        epochPaths.forEach {
            var filePath = it.absolutePathString()
            // need to convert windows files
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1, filePath.length)
            }

            val inputStream = javaClass.classLoader.getResourceAsStream(filePath)
            InputStreamReader(inputStream, StandardCharsets.UTF_8).use {
                val reader = BufferedReader(it)
                val epochContent = reader.readLines()
                alliancesRankings.add(parseAlliancesRankingContent(epochContent))
            }
        }
        return alliancesRankings
    }

    private fun parseAlliancesRankingContent(epochContent: List<String>): AlliancesRanking {
        val epochNumber = parseEpochNumber(epochContent.first())

        val alliancesRankingRows = mutableListOf<AlliancesRankingRow>()
        // skip first 2 lines without land data
        epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
            alliancesRankingRows.add(parseAlliancesRankingRow(it, epochNumber))
        }

        return AlliancesRanking(epochNumber = epochNumber, alliancesRankingRows = alliancesRankingRows)
    }

    fun parseAlliancesRankingRow(alliancesRankingRow: String, epochNumber: Int): AlliancesRankingRow {
        val alliancesRankingRowMatchResult =
            REGEX_ALLIANCES_RANKING_ROW.toRegex().find(alliancesRankingRow)?.groupValues ?: throw IllegalArgumentException("Invalid alliances ranking row.")
        return AlliancesRankingRow(
            ranking = alliancesRankingRowMatchResult[1].toInt(),
            allianceTag = alliancesRankingRowMatchResult[2],
            membersCount = alliancesRankingRowMatchResult[3].toInt(),
            area = alliancesRankingRowMatchResult[4].toInt(),
            prestige = alliancesRankingRowMatchResult[5].toLong(),
            chairmanPlayerName = alliancesRankingRowMatchResult[6],
            epochNumber = epochNumber
        )
    }
}

private const val RANKING_ALLIANCES_FOLDER = "rankings/alliances"
private const val REGEX_ALLIANCES_RANKING_ROW = "(\\d+)\\.\\t\\[(.*)\\].*\\t(\\d+)\\t(\\d+)km2\\t(\\d+)\\t.*#\\d+\\)\\s-\\s(.*)"
