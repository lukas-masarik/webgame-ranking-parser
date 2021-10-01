package services.processors

import dto.RankedLandsEpoch
import enums.EOrderAttribute
import enums.EOrderDirection
import services.inputreaders.api.InputReader
import services.parsers.RankedLandsParser
import services.parsers.api.EpochsParser

/**
 * Returns winner players' stats.
 *
 * Features:
 *  - specify returned rows
 *  - specify order attribute (prestige or area)
 *  - specify order direction
 *  - specify epoch start
 *  - specify epoch end
 *  - specify rank start
 *  - specify rank end
 */
class CompareLandsThroughEpochsProcessor(
    inputReader: InputReader,
    private val parser: EpochsParser<RankedLandsEpoch> = RankedLandsParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val landsCount = inputReader.selectReturnCountFromInput()
        val orderAttribute = inputReader.selectOrderAttributeFromInput()
        val orderDirection = inputReader.selectOrderDirectionFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()
        val rankStart = inputReader.selectStartRankFromInput()
        val rankEnd = inputReader.selectEndRankFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)
        val filteredRanks = filterRanks(filteredEpochs, rankStart, rankEnd)

        val rankedLands = filteredRanks.flatMap { it.rankedLands }
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
            .take(landsCount)

        for (i in rankedLands.indices) {
            val rankedLand = rankedLands[i]
            println("${i+1}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.area}km2\t${rankedLand.epochNumber}")
        }
    }
}