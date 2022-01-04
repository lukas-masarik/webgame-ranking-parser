package me.masi.services.processors.lands

import me.masi.dto.lands.LandsRanking
import me.masi.dto.lands.LandsRankingRow
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.parsers.LandsRankingParser
import me.masi.services.parsers.api.RankingParser

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
class ListLandsRankingProcessor(
    private val inputReader: InputReader,
    private val parser: RankingParser<LandsRanking> = LandsRankingParser(),
) : AbstractLandsRankingProcessor() {

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
        val filteredRanks = filterRankings(filteredEpochs, rankStart, rankEnd)

        val rankedLands = filteredRanks.flatMap { it.landsRankingRows }
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

        processOutput(rankedLands)
    }

    private fun processOutput(landsRankingRows: List<LandsRankingRow>) {
        if (landsRankingRows.isEmpty()) {
            println("Zadne vysledky.")
            return
        }

        println("#\tHrac\tPrestiz\tRozloha\tVek\tUmisteni")
        var i = 1
        landsRankingRows.forEach { rankedLand ->
            println("${i++}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.epochNumber}\t${rankedLand.ranking}.")
        }
    }
}
