package me.masi.services.processors.lands

import me.masi.dto.lands.LandsRanking
import me.masi.dto.lands.LandsRankingRow
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.enums.lands.EFilteringParameterForLands
import me.masi.services.inputreaders.api.InputReader
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
    private val inputReader: InputReader,
    private val parser: RankingParser<LandsRanking>,
) : AbstractLandsRankingProcessor() {

    override fun process() {
        val filteringParameter = inputReader.selectFilteringParameterForLandsFromInput()
        val filteringQuery = when (filteringParameter) {
            EFilteringParameterForLands.PLAYER -> inputReader.selectFilterPlayerQueryFromInput()
            EFilteringParameterForLands.ALLIANCE -> inputReader.selectFilterAllianceQueryFromInput()
            EFilteringParameterForLands.STATE_SYSTEM -> inputReader.selectFilterStateSystemQueryFromInput()
            EFilteringParameterForLands.LAND_NUMBER -> inputReader.selectFilterLandNumberQueryFromInput()
        }
        val sortAttribute = inputReader.selectSortAttributeFromInput()
        val sortDirection = inputReader.selectSortDirectionFromInput()
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
                    EFilteringParameterForLands.PLAYER -> it.filter { it.playerName.lowercase() == filteringQuery!!.lowercase() }
                    EFilteringParameterForLands.ALLIANCE -> it.filter { it.alliance?.lowercase() == filteringQuery?.ifBlank { null }?.lowercase() }
                    EFilteringParameterForLands.STATE_SYSTEM -> it.filter { it.stateSystem.lowercase() == filteringQuery!!.lowercase() }
                    EFilteringParameterForLands.LAND_NUMBER -> it.filter { it.landNumber == (filteringQuery!!.toIntOrNull() ?: 0) }
                }
            }
            .let {
                when (sortDirection) {
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

    private fun processOutput(landsRankingRows: List<LandsRankingRow>, filteringParameter: EFilteringParameterForLands, filteringQuery: String?) {
        println("${filteringParameter.value}: $filteringQuery")
        if (landsRankingRows.isEmpty()) {
            println("Zadne vysledky.")
            return
        }

        when (filteringParameter) {
            EFilteringParameterForLands.PLAYER -> processPlayerOutput(landsRankingRows)
            EFilteringParameterForLands.ALLIANCE -> processAllianceOutput(landsRankingRows)
            EFilteringParameterForLands.STATE_SYSTEM -> processStateSystemOutput(landsRankingRows)
            EFilteringParameterForLands.LAND_NUMBER -> processLandNumberOutput(landsRankingRows)
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
