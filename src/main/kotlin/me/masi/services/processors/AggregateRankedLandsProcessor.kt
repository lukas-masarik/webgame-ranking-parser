package me.masi.services.processors

import me.masi.dto.RankedLand
import me.masi.dto.RankedLandsEpoch
import me.masi.enums.EAggregatingParameter
import me.masi.enums.EGroupingParameter
import me.masi.enums.ESortDirection
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.parsers.RankedLandsParser
import me.masi.services.parsers.api.EpochsParser

/**
 * Returns aggregated stats.
 *
 * Features:
 *  - specify grouping parameter (player, alliance, state system, land number)
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
        val sortDirection = inputReader.selectSortDirectionFromInput()
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
                when (sortDirection) {
                    ESortDirection.ASCENDING -> {
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
                    ESortDirection.DESCENDING -> {
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
            println("Žádné výsledky.")
            return
        }

        println("#\t${groupingParameter.value}\tÚčast\tPrestiž suma\tRozloha suma")
        var i = 1
        resultMap.forEach { (groupingParameter, rankedResults) ->
            val occurrenceCount = rankedResults.size
            val prestigeSum = rankedResults.sumOf { it.prestige }
            val areaSum = rankedResults.sumOf { it.area }
            println("${i++}.\t${groupingParameter ?: ""}\t$occurrenceCount\t$prestigeSum\t${areaSum}km2")
        }
    }
}
