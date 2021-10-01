package services.processors

import dto.RankedLand
import dto.RankedLandsEpoch
import enums.EAggregatingParameter
import enums.EOrderDirection
import services.inputreaders.api.InputReader
import services.parsers.RankedLandsParser
import services.parsers.api.EpochsParser

/**
 * Returns aggregated stats for players
 *
 * Features:
 *  - specify aggregating parameter (occurrence, prestige, area)
 *  - specify order direction
 *  - specify returned rows
 *  - specify epoch start
 *  - specify epoch end
 *  - specify rank start
 *  - specify rank end
 */
class AggregatePlayersProcessor(
    inputReader: InputReader,
    private val parser: EpochsParser<RankedLandsEpoch> = RankedLandsParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val aggregatingParameter = inputReader.selectAggregatingParameterFromInput()
        val orderDirection = inputReader.selectOrderDirectionFromInput()
        val landsCount = inputReader.selectReturnCountFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()
        val rankStart = inputReader.selectStartRankFromInput()
        val rankEnd = inputReader.selectEndRankFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)
        val filteredRanks = filterRanks(filteredEpochs, rankStart, rankEnd)

        val playersWithResults = filteredRanks.flatMap { it.rankedLands }
            .groupBy { it.playerName }
            .let {
                when (orderDirection) {
                    EOrderDirection.ASCENDING -> {
                        when (aggregatingParameter) {
                            EAggregatingParameter.OCCURRENCE -> it.toList()
                                .sortedBy { (_, values) -> values.size }
                                .toMap()
                            EAggregatingParameter.PRESTIGE -> it.toList()
                                .sortedBy { (_, values) -> values.sumOf { it.prestige } }
                                .toMap()
                            EAggregatingParameter.AREA -> it.toList()
                                .sortedBy { (_, values) -> values.sumOf { it.area } }
                                .toMap()
                        }
                    }
                    EOrderDirection.DESCENDING -> {
                        when (aggregatingParameter) {
                            EAggregatingParameter.OCCURRENCE -> it.toList()
                                .sortedByDescending { (_, values) -> values.size }
                                .toMap()
                            EAggregatingParameter.PRESTIGE -> it.toList()
                                .sortedByDescending { (_, values) -> values.sumOf { it.prestige } }
                                .toMap()
                            EAggregatingParameter.AREA -> it.toList()
                                .sortedByDescending { (_, values) -> values.sumOf { it.area } }
                                .toMap()
                        }
                    }
                }
            }
            .toList()
            .take(landsCount)
            .toMap()

        processOutput(playersWithResults)
    }

    private fun processOutput(playersWithResults: Map<String, List<RankedLand>>) {
        if (playersWithResults.isEmpty()) {
            println("No results.")
            return
        }

        println("#\tPlayer\tOccurrence\tPrestige sum\tArea sum")
        var i = 1
        playersWithResults.forEach { playerName, rankedResults ->
            val occurrenceCount = rankedResults.size
            val prestigeSum = rankedResults.sumOf { it.prestige }
            val areaSum = rankedResults.sumOf { it.area }
            println("${i++}.\t$playerName\t$occurrenceCount\t$prestigeSum\t${areaSum}km2")
        }
    }
}