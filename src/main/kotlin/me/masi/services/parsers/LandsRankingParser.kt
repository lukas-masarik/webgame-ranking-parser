package me.masi.services.parsers

import me.masi.dto.lands.LandsRanking
import me.masi.dto.lands.LandsRankingRow
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class LandsRankingParser : AbstractRankingParser<LandsRanking>() {

    override fun parse(): List<LandsRanking> {
        // use from IDE
        val epochFiles = getEpochFilesForLandsFromResources()
        return parseLandsRankingFiles(epochFiles)
        // use to generate jar
        /*val epochFiles = getEpochPathsFromResourcesInJar(RANKING_LANDS_FOLDER)
        return parseRankedLandsPaths(epochFiles)*/
    }

    private fun parseLandsRankingFiles(epochFiles: List<File>): List<LandsRanking> {
        val landsRankings = mutableListOf<LandsRanking>()
        epochFiles.forEach {
            val epochContent = it.readLines()
            val epochNumber = parseEpochNumber(epochContent.first())

            val landsRankingRows = mutableListOf<LandsRankingRow>()
            // skip first 2 lines without land data
            epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
                landsRankingRows.add(parseLandsRankingRow(it, epochNumber))
            }

            landsRankings.add(LandsRanking(epochNumber = epochNumber, landsRankingRows = landsRankingRows))
        }
        return landsRankings
    }

    private fun parseRankedLandsPaths(epochFiles: List<Path>): List<LandsRanking> {
        val rankedLandsEpochs = mutableListOf<LandsRanking>()
        epochFiles.forEach {
            var filePath = it.absolutePathString()
            // need to convert windows files
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1, filePath.length)
            }

            val inputStream = javaClass.classLoader.getResourceAsStream(filePath)
            InputStreamReader(inputStream, StandardCharsets.UTF_8).use {
                val reader = BufferedReader(it)
                val epochContent = reader.readLines()
                val epochNumber = parseEpochNumber(epochContent.first())

                val landsRankingRows = mutableListOf<LandsRankingRow>()
                // skip first 2 lines without land data
                epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
                    landsRankingRows.add(parseLandsRankingRow(it, epochNumber))
                }

                rankedLandsEpochs.add(LandsRanking(epochNumber = epochNumber, landsRankingRows = landsRankingRows))
            }
        }
        return rankedLandsEpochs
    }

    fun parseLandsRankingRow(landsRankingRow: String, epochNumber: Int): LandsRankingRow {
        val landsRankingRowMatchResult =
            REGEX_LANDS_RANKING_ROW.toRegex().find(landsRankingRow)?.groupValues ?: throw IllegalArgumentException("Invalid lands ranking row.")
        return LandsRankingRow(
            ranking = landsRankingRowMatchResult[1].toInt(),
            landName = landsRankingRowMatchResult[2],
            landNumber = landsRankingRowMatchResult[3].toInt(),
            playerName = landsRankingRowMatchResult[4],
            area = landsRankingRowMatchResult[5].toInt(),
            prestige = landsRankingRowMatchResult[6].toLong(),
            alliance = landsRankingRowMatchResult[7].ifBlank { null },
            stateSystem = landsRankingRowMatchResult[8],
            rounds = landsRankingRowMatchResult[9].toInt(),
            epochNumber = epochNumber
        )
    }
}

private const val REGEX_LANDS_RANKING_ROW = "(\\d+)\\.\\t(.*)\\(#(\\d+)\\)\\s-\\s(.*)\\t(\\d+)km2\\t(\\d+)\\t(.*)\\t([a-zA-Z]{3,4})\\t(\\d+)"
