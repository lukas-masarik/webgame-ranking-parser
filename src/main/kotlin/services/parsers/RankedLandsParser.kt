package services.parsers

import dto.Epoch
import dto.RankedLand
import services.parsers.api.EpochsParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class RankedLandsParser : EpochsParser {

    override fun parse(): List<Epoch> {
        val epochFiles: List<File> = getEpochFilesFromResources()
        val epochs = mutableListOf<Epoch>()
        epochFiles.forEach {
            val epochContent = it.readLines()
            val epochNumber = parseEpochNumber(epochContent.first())

            val rankedLands = mutableListOf<RankedLand>()
            // skip first 2 lines without land data
            epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
                rankedLands.add(parseRankedLand(it, epochNumber))
            }

            epochs.add(Epoch(number = epochNumber, rankedLands = rankedLands))
        }

        return epochs
    }

    private fun getEpochFilesFromResources(): List<File> {
        val resource = javaClass.classLoader.getResource(EPOCHS_FOLDER)
        return Files.walk(Paths.get(resource.toURI()))
            .filter(Files::isRegularFile)
            .map { it.toFile() }
            .toList()
    }

    private fun parseEpochNumber(epochNumberLine: String): Int {
        return REGEX_EPOCH_NUMBER.toRegex().find(epochNumberLine)?.value?.toInt() ?: throw IllegalArgumentException("Missing epoch number.")
    }

    fun parseRankedLand(rankedLandLine: String, epochNumber: Int): RankedLand {
        val rankedLandMatchResult = REGEX_RANKED_LAND.toRegex().find(rankedLandLine)?.groupValues ?: throw IllegalArgumentException("Invalid ranked land line.")
        return RankedLand(
            order = rankedLandMatchResult[1].toInt(),
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

private const val EPOCHS_FOLDER = "epochs"
private const val REGEX_EPOCH_NUMBER = "\\d+"
private const val REGEX_RANKED_LAND = "(\\d+)\\.\\t(.*)\\(\\#(\\d+)\\)\\s\\-\\s(.*)\\t(\\d+)km2\\t(\\d+)\\t(.*)\\t([a-zA-Z]{3,4})\\t(\\d+)"