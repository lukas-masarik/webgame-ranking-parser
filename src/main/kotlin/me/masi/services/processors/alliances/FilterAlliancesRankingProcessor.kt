package me.masi.services.processors.alliances

import me.masi.dto.alliances.AlliancesRanking
import me.masi.dto.alliances.AlliancesRankingRow
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.enums.alliances.EFilteringParameterForAlliances
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.parsers.api.RankingParser

class FilterAlliancesRankingProcessor(
    private val inputReader: InputReader,
    private val parser: RankingParser<AlliancesRanking>,
) : AbstractAlliancesRankingProcessor() {

    override fun process() {
        val filteringParameter = inputReader.selectFilteringParameterForAlliancesFromInput()
        val filteringQuery = when (filteringParameter) {
            EFilteringParameterForAlliances.TAG -> inputReader.selectFilterAllianceQueryFromInput()
            EFilteringParameterForAlliances.CHAIRMAN -> inputReader.selectFilterPlayerQueryFromInput()
            EFilteringParameterForAlliances.MEMBERS_COUNT -> inputReader.selectFilterMembersCountQueryFromInput()
        }
        val sortAttribute = inputReader.selectSortAttributeFromInput()
        val SortDirection = inputReader.selectSortDirectionFromInput()
        val alliancesCount = inputReader.selectReturnCountFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()
        val rankStart = inputReader.selectStartRankFromInput()
        val rankEnd = inputReader.selectEndRankFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)
        val filteredRanks = filterRankings(filteredEpochs, rankStart, rankEnd)

        val rankedAlliances = filteredRanks.flatMap { it.alliancesRankingRows }
            .let {
                when (filteringParameter) {
                    EFilteringParameterForAlliances.TAG -> it.filter { it.allianceTag.lowercase() == filteringQuery!!.lowercase() }
                    EFilteringParameterForAlliances.MEMBERS_COUNT -> it.filter { it.membersCount == (filteringQuery!!.toIntOrNull() ?: 10) }
                    EFilteringParameterForAlliances.CHAIRMAN -> it.filter { it.chairmanPlayerName.lowercase() == filteringQuery!!.lowercase() }
                }
            }
            .let {
                when (SortDirection) {
                    ESortDirection.ASCENDING -> {
                        when (sortAttribute) {
                            ESortAttribute.PRESTIGE -> it.sortedBy { it.prestige }
                            ESortAttribute.AREA -> it.sortedBy { it.area }
                            ESortAttribute.EPOCH_NUMBER -> it.sortedBy { it.epochNumber }
                        }
                    }
                    ESortDirection.DESCENDING -> {
                        when (sortAttribute) {
                            ESortAttribute.PRESTIGE -> it.sortedByDescending { it.prestige }
                            ESortAttribute.AREA -> it.sortedByDescending { it.area }
                            ESortAttribute.EPOCH_NUMBER -> it.sortedByDescending { it.epochNumber }
                        }
                    }
                }
            }
            .let {
                if (alliancesCount != 0) {
                    it.take(alliancesCount)
                } else {
                    it.toList()
                }
            }

        processOutput(rankedAlliances, filteringParameter, filteringQuery)
    }

    private fun processOutput(alliancesRankingRows: List<AlliancesRankingRow>, filteringParameter: EFilteringParameterForAlliances, filteringQuery: String?) {
        println("${filteringParameter.value}: $filteringQuery")
        if (alliancesRankingRows.isEmpty()) {
            println("Zadne vysledky.")
            return
        }

        when (filteringParameter) {
            EFilteringParameterForAlliances.TAG -> processAllianceOutput(alliancesRankingRows)
            EFilteringParameterForAlliances.CHAIRMAN -> processChairmanOutput(alliancesRankingRows)
            EFilteringParameterForAlliances.MEMBERS_COUNT -> processMembersCountOutput(alliancesRankingRows)
        }
    }

    private fun processAllianceOutput(alliancesRankingRows: List<AlliancesRankingRow>) {
        println("#\tPrestiz\tRozloha\tClenu\tPredseda\tVek\tUmisteni")
        var i = 1
        alliancesRankingRows.forEach { rankedAlliance ->
            println(
                "${i++}.\t${rankedAlliance.prestige}\t${rankedAlliance.area}km2\t${rankedAlliance.membersCount}\t${rankedAlliance.chairmanPlayerName}\t" +
                        "${rankedAlliance.epochNumber}\t${rankedAlliance.ranking}."
            )
        }
    }

    private fun processChairmanOutput(alliancesRankingRows: List<AlliancesRankingRow>) {
        println("#\tAliance\tPrestiz\tRozloha\tClenu\tVek\tUmisteni")
        var i = 1
        alliancesRankingRows.forEach { rankedAlliance ->
            println(
                "${i++}.\t${rankedAlliance.allianceTag}\t${rankedAlliance.prestige}\t${rankedAlliance.area}km2\t${rankedAlliance.membersCount}\t" +
                        "${rankedAlliance.epochNumber}\t${rankedAlliance.ranking}."
            )
        }
    }

    private fun processMembersCountOutput(alliancesRankingRows: List<AlliancesRankingRow>) {
        println("#\tAliance\tPrestiz\tRozloha\tPreseda\tVek\tUmisteni")
        var i = 1
        alliancesRankingRows.forEach { rankedAlliance ->
            println(
                "${i++}.\t${rankedAlliance.allianceTag}\t${rankedAlliance.prestige}\t${rankedAlliance.area}km2\t${rankedAlliance.chairmanPlayerName}\t" +
                        "${rankedAlliance.epochNumber}\t${rankedAlliance.ranking}."
            )
        }
    }
}
