package services.processors

import enums.EOrderAttribute
import enums.EOrderDirection
import services.inputreaders.api.InputReader
import services.parsers.SimpleEpochsParser
import services.processors.api.Processor

class WinnersByPrestigeOrAreaProcessor(
    private val inputReader: InputReader,
) : Processor {
    private val parser = SimpleEpochsParser()

    override fun apply() {
        val landsCount = inputReader.selectReturnCountFromInput()
        val orderAttribute = inputReader.selectOrderAttributeFromInput()
        val orderDirection = inputReader.selectOrderDirectionFromInput()
        val epochs = parser.parse()

        val winningLands = epochs.map { it.rankedLands }
            .map { it.first() }
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

        for (i in winningLands.indices) {
            val winningLand = winningLands[i]
            println("${i+1}.\t${winningLand.playerName}\t${winningLand.prestige}\t${winningLand.area}km2\t${winningLand.epochNumber}")
        }
    }
}