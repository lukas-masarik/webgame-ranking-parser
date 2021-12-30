package me.masi.services.processors

import me.masi.dto.LandsRankingRow
import me.masi.dto.LandsRanking
import me.masi.enums.EFilteringParameter
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.parsers.LandsRankingParser
import me.masi.services.parsers.api.RankingParser

/**
 * Returns filtered stats.
 *
 * Features:
 *  - specify filtering parameter (player, alliance, state system, land number)
 *  - specify filtering query (string based on filtering parameter)
 *  - specify sort attribute (prestige or area)
 *  - specify sort direction
 *  - specify returned rows
 *  - specify epoch start
 *  - specify epoch end
 *  - specify rank start
 *  - specify rank end
 */
class FilterLandsRankingProcessor(
    inputReader: InputReader,
    private val parser: RankingParser<LandsRanking> = LandsRankingParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val filteringParameter = inputReader.selectFilteringParameterFromInput()
        val filteringQuery = when (filteringParameter) {
            EFilteringParameter.PLAYER -> inputReader.selectFilterPlayerQueryFromInput()
            EFilteringParameter.ALLIANCE -> inputReader.selectFilterAllianceQueryFromInput()
            EFilteringParameter.STATE_SYSTEM -> inputReader.selectFilterStateSystemQueryFromInput()
            EFilteringParameter.LAND_NUMBER -> inputReader.selectFilterLandNumberQueryFromInput()
        }
        val sortAttribute = inputReader.selectSortAttributeFromInput()
        val SortDirection = inputReader.selectSortDirectionFromInput()
        val landsCount = inputReader.selectReturnCountFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()
        val rankStart = inputReader.selectStartRankFromInput()
        val rankEnd = inputReader.selectEndRankFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)
        val filteredRanks = filterRankings(filteredEpochs, rankStart, rankEnd)

        val rankedLands = filteredRanks.flatMap { it.landsRankingRows }
            .let {
                when (filteringParameter) {
                    EFilteringParameter.PLAYER -> it.filter { it.playerName.lowercase() == filteringQuery!!.lowercase() }
                    EFilteringParameter.ALLIANCE -> it.filter { it.alliance?.lowercase() == filteringQuery?.ifBlank { null }?.lowercase() }
                    EFilteringParameter.STATE_SYSTEM -> it.filter { it.stateSystem.lowercase() == filteringQuery!!.lowercase() }
                    EFilteringParameter.LAND_NUMBER -> it.filter { it.landNumber == (filteringQuery!!.toIntOrNull() ?: 0) }
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
                if (landsCount != 0) {
                    it.take(landsCount)
                } else {
                    it.toList()
                }
            }

        processOutput(rankedLands, filteringParameter, filteringQuery)
    }

    private fun processOutput(landsRankingRows: List<LandsRankingRow>, filteringParameter: EFilteringParameter, filteringQuery: String?) {
        println("${filteringParameter.value}: $filteringQuery")
        if (landsRankingRows.isEmpty()) {
            println("Zadne vysledky.")
            return
        }

        when (filteringParameter) {
            EFilteringParameter.PLAYER -> processPlayerOutput(landsRankingRows)
            EFilteringParameter.ALLIANCE -> processAllianceOutput(landsRankingRows)
            EFilteringParameter.STATE_SYSTEM -> processStateSystemOutput(landsRankingRows)
            EFilteringParameter.LAND_NUMBER -> processLandNumberOutput(landsRankingRows)
        }
    }

    private fun processPlayerOutput(landsRankingRows: List<LandsRankingRow>) {
        println("#\tPrestiz\tRozloha\tVlada\tAliance\tVek\tUmisteni")
        var i = 1
        landsRankingRows.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.stateSystem}\t${rankedLand.alliance}\t${rankedLand.epochNumber}\t" +
                        "${rankedLand.ranking}."
            )
        }
    }

    private fun processAllianceOutput(landsRankingRows: List<LandsRankingRow>) {
        println("#\tHrac\tPrestiz\tRozloha\tVlada\tVek\tUmisteni")
        var i = 1
        landsRankingRows.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.stateSystem}\t${rankedLand.epochNumber}\t" +
                        "${rankedLand.ranking}."
            )
        }
    }

    private fun processStateSystemOutput(landsRankingRows: List<LandsRankingRow>) {
        println("#\tHrac\tPrestiz\tRozloha\tAliance\tVek\tUmisteni")
        var i = 1
        landsRankingRows.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.alliance}\t${rankedLand.epochNumber}\t" +
                        "${rankedLand.ranking}."
            )
        }
    }

    private fun processLandNumberOutput(landsRankingRows: List<LandsRankingRow>) {
        println("#\tHrac\tPrestiz\tRozloha\tVlada\tAliance\tVek\tUmisteni")
        var i = 1
        landsRankingRows.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.stateSystem}\t${rankedLand.alliance}\t" +
                        "${rankedLand.epochNumber}\t${rankedLand.ranking}."
            )
        }
    }
}
