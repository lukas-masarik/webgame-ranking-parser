package services.processors

import dto.RankedLand
import enums.EOrderDirection
import enums.EStateSystem
import services.inputreaders.api.InputReader
import services.parsers.SimpleEpochsParser
import services.parsers.api.EpochsParser
import services.processors.api.Processor

class PrestigeByStateSystemProcessor(
    private val inputReader: InputReader,
    private val parser: EpochsParser = SimpleEpochsParser(),
) : Processor {

    override fun apply() {
        val stateSystem = inputReader.selectStateSystemFromInput()
        val orderDirection = inputReader.selectOrderDirectionFromInput()
        val landsCount = inputReader.selectReturnCountFromInput()

        val epochs = parser.parse()
        val rankedLands = epochs.flatMap { it.rankedLands }
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