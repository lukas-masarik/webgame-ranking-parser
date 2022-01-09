package me.masi.services.processors.alliances

import me.masi.dto.alliances.AlliancesRanking
import me.masi.dto.alliances.AlliancesRankingRow
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.parsers.api.RankingParser

class ListAlliancesRankingProcessor(
    private val inputReader: InputReader,
    private val parser: RankingParser<AlliancesRanking>,
) : AbstractAlliancesRankingProcessor() {

    override fun process() {
        val sortAttribute = inputReader.selectSortAttributeFromInput()
        val sortDirection = inputReader.selectSortDirectionFromInput()
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
                if (alliancesCount != 0) {
                    it.take(alliancesCount)
                } else {
                    it.toList()
                }
            }

        processOutput(rankedAlliances)
    }

    private fun processOutput(alliancesRankingRows: List<AlliancesRankingRow>) {
        if (alliancesRankingRows.isEmpty()) {
            println("Zadne vysledky.")
            return
        }

        println("#\tAliance\tPrestiz\tRozloha\tVek\tClenu\tPredseda\tUmisteni")
        var i = 1
        alliancesRankingRows.forEach { rankedAlliance ->
            println(
                "${i++}.\t${rankedAlliance.allianceTag}\t${rankedAlliance.prestige}\t${rankedAlliance.area}km2\t${rankedAlliance.epochNumber}" +
                    "\t${rankedAlliance.membersCount}\t${rankedAlliance.chairmanPlayerName}\t${rankedAlliance.ranking}."
            )
        }
    }
}
