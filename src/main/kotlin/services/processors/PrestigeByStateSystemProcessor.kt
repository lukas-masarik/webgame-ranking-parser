package services.processors

import dto.RankedLand
import dto.RankedLandsEpoch
import enums.EOrderDirection
import enums.EStateSystem
import services.inputreaders.api.InputReader
import services.parsers.RankedLandsParser
import services.parsers.api.EpochsParser

class PrestigeByStateSystemProcessor(
    inputReader: InputReader,
    private val parser: EpochsParser<RankedLandsEpoch> = RankedLandsParser(),
) : AbstractRankedLandProcessor(inputReader) {

    override fun process() {
        val stateSystem = inputReader.selectStateSystemFromInput()
        val orderDirection = inputReader.selectOrderDirectionFromInput()
        val landsCount = inputReader.selectReturnCountFromInput()
        val epochStart = inputReader.selectStartEpochFromInput()
        val epochEnd = inputReader.selectEndEpochFromInput()

        val epochs = parser.parse()
        val filteredEpochs = filterEpochs(epochs, epochStart, epochEnd)

        val rankedLands = filteredEpochs.flatMap { it.rankedLands }
            .filter { EStateSystem.fromRawValue(it.stateSystem) == stateSystem }
            .let {
                when (orderDirection) {
                    EOrderDirection.ASCENDING -> it.sortedBy { it.prestige }
                    EOrderDirection.DESCENDING -> it.sortedByDescending { it.prestige }
                }
            }
            .take(landsCount)

        processOutput(rankedLands, stateSystem)
    }

    private fun processOutput(rankedLands: List<RankedLand>, stateSystem: EStateSystem) {
        println(stateSystem.name)
        for (i in rankedLands.indices) {
            val rankedLand = rankedLands[i]
            println("${i+1}.\t${rankedLand.playerName}\t${rankedLand.prestige}\t${rankedLand.epochNumber}")
        }
    }
}