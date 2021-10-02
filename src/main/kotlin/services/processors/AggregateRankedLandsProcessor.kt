package services.processors

import dto.RankedLand
import dto.RankedLandsEpoch
import enums.EAggregatingParameter
import enums.EGroupingParameter
import enums.EOrderDirection
import services.inputreaders.api.InputReader
import services.parsers.RankedLandsParser
import services.parsers.api.EpochsParser

/**
 * Returns aggregated stats for players
 *
 * Features:
 *  - specify grouping parameter (player, alliance, state_system)
 *  - specify aggregating parameter (occurrence, prestige, area)
 *  - specify order direction
 *  - specify returned rows
 *  - specify epoch start
 *  - specify epoch end
 *  - specify rank start
 *  - specify rank end
 */
class AggregateRankedLandsProcessor(
    inputReader: InputReader,
    private val parser: EpochsParser<RankedLandsEpoch> = RankedLandsParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val groupingParameter = inputReader.selectGroupingParameterFromInput()
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

        val resultMap = filteredRanks.flatMap { it.rankedLands }
            .let {
                when (groupingParameter) {
                    EGroupingParameter.PLAYER -> it.groupBy { it.playerName }
                    EGroupingParameter.ALLIANCE -> it.groupBy { it.alliance }
                    EGroupingParameter.STATE_SYSTEM -> it.groupBy { it.stateSystem }
                    EGroupingParameter.LAND_NUMBER -> it.groupBy { it.landNumber.toString() }
                }
            }
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
            .let {
                if (landsCount != 0) {
                    it.toList().take(landsCount).toMap()
                } else {
                    it.toMap()
                }
            }

        processOutput(resultMap, groupingParameter)
    }

    private fun processOutput(resultMap: Map<String?, List<RankedLand>>, groupingParameter: EGroupingParameter) {
        if (resultMap.isEmpty()) {
            println("No results.")
            return
        }

        println("#\t${groupingParameter.value}\tOccurrence\tPrestige sum\tArea sum")
        var i = 1
        resultMap.forEach { (groupingParameter, rankedResults) ->
            val occurrenceCount = rankedResults.size
            val prestigeSum = rankedResults.sumOf { it.prestige }
            val areaSum = rankedResults.sumOf { it.area }
            println("${i++}.\t${groupingParameter ?: ""}\t$occurrenceCount\t$prestigeSum\t${areaSum}km2")
        }
    }
}
