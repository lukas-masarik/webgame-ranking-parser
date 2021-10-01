package services.parsers

import dto.RankedLand
import dto.RankedLandsEpoch
import services.parsers.api.EpochsParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class RankedLandsParser : EpochsParser<RankedLandsEpoch> {

    override fun parse(): List<RankedLandsEpoch> {
        val epochFiles: List<File> = getEpochFilesFromResources()
        val rankedLandsEpochs = mutableListOf<RankedLandsEpoch>()
        epochFiles.forEach {
            val epochContent = it.readLines()
            val epochNumber = parseEpochNumber(epochContent.first())

            val rankedLands = mutableListOf<RankedLand>()
            // skip first 2 lines without land data
            epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
                rankedLands.add(parseRankedLand(it, epochNumber))
            }

            rankedLandsEpochs.add(RankedLandsEpoch(number = epochNumber, rankedLands = rankedLands))
        }

        return rankedLandsEpochs
    }

    private fun getEpochFilesFromResources(): List<File> {
        val resource = javaClass.classLoader.getResource(EPOCHS_LANDS_FOLDER)
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

private const val EPOCHS_LANDS_FOLDER = "epochs/lands"
private const val REGEX_EPOCH_NUMBER = "\\d+"
private const val REGEX_RANKED_LAND = "(\\d+)\\.\\t(.*)\\(\\#(\\d+)\\)\\s\\-\\s(.*)\\t(\\d+)km2\\t(\\d+)\\t(.*)\\t([a-zA-Z]{3,4})\\t(\\d+)"
