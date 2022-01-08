package me.masi.services.processors.alliances

import me.masi.dto.alliances.AlliancesRanking
import me.masi.dto.alliances.AlliancesRankingRow
import me.masi.enums.EAggregatingParameter
import me.masi.enums.ESortDirection
import me.masi.enums.alliances.EGroupingParameterForAlliances
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.parsers.AlliancesRankingParser
import me.masi.services.parsers.api.RankingParser

class AggregateAlliancesRankingProcessor(
    private val inputReader: InputReader,
    private val parser: RankingParser<AlliancesRanking> = AlliancesRankingParser()
) : AbstractAlliancesRankingProcessor() {

    override fun process() {
        val groupingParameter = inputReader.selectGroupingParameterForAlliancesFromInput()
        val aggregatingParameter = inputReader.selectAggregatingParameterFromInput()
        val sortDirection = inputReader.selectSortDirectionFromInput()
        val alliancesCount = inputReader.selectReturnCountFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()
        val rankStart = inputReader.selectStartRankFromInput()
        val rankEnd = inputReader.selectEndRankFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)
        val filteredRanks = filterRankings(filteredEpochs, rankStart, rankEnd)

        val resultMap = filteredRanks.flatMap { it.alliancesRankingRows }
            .let {
                when (groupingParameter) {
                    EGroupingParameterForAlliances.TAG -> it.groupBy { it.allianceTag.lowercase() }
                    EGroupingParameterForAlliances.CHAIRMAN -> it.groupBy { it.chairmanPlayerName }
                    EGroupingParameterForAlliances.MEMBERS_COUNT -> it.groupBy { it.membersCount.toString() }
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
                if (alliancesCount != 0) {
                    it.toList().take(alliancesCount).toMap()
                } else {
                    it.toMap()
                }
            }

        processOutput(resultMap, groupingParameter)
    }

    private fun processOutput(resultMap: Map<String, List<AlliancesRankingRow>>, groupingParameter: EGroupingParameterForAlliances) {
        if (resultMap.isEmpty()) {
            println("Zadne vysledky.")
            return
        }

        println("#\t${groupingParameter.value}\tUcast\tPrestiz suma\tRozloha suma")
        var i = 1
        resultMap.forEach { (groupingParameter, rankedResults) ->
            val occurrenceCount = rankedResults.size
            val prestigeSum = rankedResults.sumOf { it.prestige }
            val areaSum = rankedResults.sumOf { it.area }
            println("${i++}.\t$groupingParameter\t$occurrenceCount\t$prestigeSum\t${areaSum}km2")
        }
    }
}
