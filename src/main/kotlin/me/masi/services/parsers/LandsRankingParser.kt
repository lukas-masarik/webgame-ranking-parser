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
        val epochFiles = getEpochFilesFromResources(RANKING_LANDS_FOLDER)
        return parseRankedLandsFiles(epochFiles)
        // use to generate jar
        /*val epochFiles = getEpochPathsFromResourcesInJar(RANKING_LANDS_FOLDER)
        return parseRankedLandsPaths(epochFiles)*/
    }

    private fun parseRankedLandsFiles(epochFiles: List<File>): List<LandsRanking> {
        val rankedLandsEpochs = mutableListOf<LandsRanking>()
        epochFiles.forEach {
            val epochContent = it.readLines()
            val epochNumber = parseEpochNumber(epochContent.first())

            val landsRankingRows = mutableListOf<LandsRankingRow>()
            // skip first 2 lines without land data
            epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
                landsRankingRows.add(parseLandsRankingRow(it, epochNumber))
            }

            rankedLandsEpochs.add(LandsRanking(epochNumber = epochNumber, landsRankingRows = landsRankingRows))
        }
        return rankedLandsEpochs
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

    private fun parseEpochNumber(epochNumberLine: String): Int {
        return REGEX_EPOCH_NUMBER.toRegex().find(epochNumberLine)?.value?.toInt() ?: throw IllegalArgumentException("Missing epoch number.")
    }

    fun parseLandsRankingRow(landsRankingRow: String, epochNumber: Int): LandsRankingRow {
        val rankedLandMatchResult =
            REGEX_RANKED_LAND.toRegex().find(landsRankingRow)?.groupValues ?: throw IllegalArgumentException("Invalid ranked land line.")
        return LandsRankingRow(
            ranking = rankedLandMatchResult[1].toInt(),
            landName = rankedLandMatchResult[2],
            landNumber = rankedLandMatchResult[3].toInt(),
            playerName = rankedLandMatchResult[4],
            area = rankedLandMatchResult[5].toInt(),
            prestige = rankedLandMatchResult[6].toLong(),
            alliance = rankedLandMatchResult[7].ifBlank { null },
            stateSystem = rankedLandMatchResult[8],
            rounds = rankedLandMatchResult[9].toInt(),
            epochNumber = epochNumber
        )
    }
}

private const val RANKING_LANDS_FOLDER = "rankings/lands"
private const val REGEX_EPOCH_NUMBER = "\\d+"
private const val REGEX_RANKED_LAND = "(\\d+)\\.\\t(.*)\\(#(\\d+)\\)\\s-\\s(.*)\\t(\\d+)km2\\t(\\d+)\\t(.*)\\t([a-zA-Z]{3,4})\\t(\\d+)"
