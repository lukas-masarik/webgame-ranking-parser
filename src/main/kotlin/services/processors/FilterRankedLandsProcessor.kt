package services.processors

import dto.RankedLand
import dto.RankedLandsEpoch
import enums.*
import services.inputreaders.api.InputReader
import services.parsers.RankedLandsParser
import services.parsers.api.EpochsParser
import java.time.temporal.TemporalQuery

class FilterRankedLandsProcessor(
    inputReader: InputReader,
    private val parser: EpochsParser<RankedLandsEpoch> = RankedLandsParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val filteringParameter = inputReader.selectFilteringParameterFromInput()
        val filteringQuery = inputReader.selectFilteringQueryFromInput()
        val orderAttribute = inputReader.selectOrderAttributeFromInput()
        val orderDirection = inputReader.selectOrderDirectionFromInput()
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
                when (filteringParameter) {
                    EFilteringParameter.PLAYER -> it.filter { it.playerName.lowercase() == filteringQuery.lowercase() }
                    EFilteringParameter.ALLIANCE -> it.filter { it.alliance?.lowercase() == filteringQuery.lowercase() }
                    EFilteringParameter.STATE_SYSTEM -> it.filter { it.stateSystem.lowercase() == filteringQuery.lowercase() }
                    EFilteringParameter.LAND_NUMBER -> it.filter { it.landNumber == (filteringQuery.toIntOrNull() ?: 0) }
                }
            }
            .let {
                when (orderDirection) {
                    EOrderDirection.ASCENDING -> {
                        when (orderAttribute) {
                            EOrderAttribute.PRESTIGE -> it.sortedBy { it.prestige }
                            EOrderAttribute.AREA -> it.sortedBy { it.area }
                        }
                    }
                    EOrderDirection.DESCENDING -> {
                        when (orderAttribute) {
                            EOrderAttribute.PRESTIGE -> it.sortedByDescending { it.prestige }
                            EOrderAttribute.AREA -> it.sortedByDescending { it.area }
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

    private fun processOutput(rankedLands: List<RankedLand>, filteringParameter: EFilteringParameter, filteringQuery: String) {
        println("${filteringParameter.value}: $filteringQuery")
        if (rankedLands.isEmpty()) {
            println("No results.")
            return
        }

        when (filteringParameter) {
            EFilteringParameter.PLAYER -> processPlayerOutput(rankedLands)
            EFilteringParameter.ALLIANCE -> processAllianceOutput(rankedLands)
            EFilteringParameter.STATE_SYSTEM -> processStateSystemOutput(rankedLands)
            EFilteringParameter.LAND_NUMBER -> processLandNumberOutput(rankedLands)
        }
    }

    private fun processPlayerOutput(rankedLands: List<RankedLand>) {
        println("#\tPrestige\tArea\tState system\tAlliance\tEpoch\tRank")
        var i = 1
        rankedLands.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.stateSystem}\t${rankedLand.alliance}\t${rankedLand.epochNumber}\t" +
                        "${rankedLand.rank}."
            )
        }
    }

    private fun processAllianceOutput(rankedLands: List<RankedLand>) {
        println("#\tPrestige\tArea\tState system\tPlayer\tEpoch\tRank")
        var i = 1
        rankedLands.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.stateSystem}\t${rankedLand.playerName}\t${rankedLand.epochNumber}\t" +
                        "${rankedLand.rank}."
            )
        }
    }

    private fun processStateSystemOutput(rankedLands: List<RankedLand>) {
        println("#\tPrestige\tArea\tPlayer\tAlliance\tEpoch\tRank")
        var i = 1
        rankedLands.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.playerName}\t${rankedLand.alliance}\t${rankedLand.epochNumber}\t" +
                        "${rankedLand.rank}."
            )
        }
    }

    private fun processLandNumberOutput(rankedLands: List<RankedLand>) {
        println("#\tPrestige\tArea\tState System\tPlayer\tAlliance\tEpoch\tRank")
        var i = 1
        rankedLands.forEach { rankedLand ->
            println(
                "${i++}.\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.stateSystem}\t${rankedLand.playerName}\t${rankedLand.alliance}\t" +
                        "${rankedLand.epochNumber}\t${rankedLand.rank}."
            )
        }
    }
}
