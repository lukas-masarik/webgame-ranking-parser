package services.processors

import dto.RankedLand
import dto.RankedLandsEpoch
import enums.ESortAttribute
import enums.ESortDirection
import services.inputreaders.api.InputReader
import services.parsers.RankedLandsParser
import services.parsers.api.EpochsParser

/**
 * Returns overall lands' stats.
 *
 * Features:
 *  - specify sort attribute (prestige or area)
 *  - specify sort direction
 *  - specify returned rows
 *  - specify epoch start
 *  - specify epoch end
 *  - specify rank start
 *  - specify rank end
 */
class ListRankedLandsProcessor(
    inputReader: InputReader,
    private val parser: EpochsParser<RankedLandsEpoch> = RankedLandsParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val sortAttribute = inputReader.selectSortAttributeFromInput()
        val sortDirection = inputReader.selectSortDirectionFromInput()
        val landsCount = inputReader.selectReturnCountFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()
        val rankStart = inputReader.selectStartRankFromInput()
        val rankEnd = inputReader.selectEndRankFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)
        val filteredRanks = filterRanks(filteredEpochs, rankStart, rankEnd)

        val rankedLands = filteredRanks.flatMap { it.rankedLands }
            .let {
                when (sortDirection) {
                    ESortDirection.ASCENDING -> {
                        when (sortAttribute) {
                            ESortAttribute.PRESTIGE -> it.sortedBy { it.prestige }
                            ESortAttribute.AREA -> it.sortedBy { it.area }
                        }
                    }
                    ESortDirection.DESCENDING -> {
                        when (sortAttribute) {
                            ESortAttribute.PRESTIGE -> it.sortedByDescending { it.prestige }
                            ESortAttribute.AREA -> it.sortedByDescending { it.area }
                        }
                    }
                }
            }
            .let {
                if (landsCount != 0) {
                    it.take(landsCount)
                } else {
                    it.toList()
                }
            }

        processOutput(rankedLands)
    }

    private fun processOutput(rankedLands: List<RankedLand>) {
        if (rankedLands.isEmpty()) {
            println("Žádné výsledky.")
            return
        }

        println("#\tHráč\tPrestiž\tRozloha\tVěk\tUmístění")
        var i = 1
        rankedLands.forEach { rankedLand ->
            println("${i++}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.epochNumber}\t${rankedLand.rank}.")
        }
    }
}
