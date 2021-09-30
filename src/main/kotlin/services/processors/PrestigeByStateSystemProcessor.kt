package services.processors

import dto.RankedLand
import enums.EOrderDirection
import enums.EStateSystem
import services.parsers.SimpleEpochsParser
import services.processors.api.Processor

class PrestigeByStateSystemProcessor : Processor {
    private val parser = SimpleEpochsParser()

    override fun apply() {
        val stateSystem = askForStateSystem()
        val orderDirection = askForOrderDirection()
        val landsCount = askForReturningCount()

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

    private fun askForStateSystem(): EStateSystem {
        print(
            """
                Available state systems:
                    (1) ANARCHY
                    (2) COMMUNISM
                    (3) DEMOCRACY
                    (4) DICTATORSHIP
                    (5) FEUDALISM
                    (6) FUNDAMENTALISM
                    (7) REPUBLIC
                    (8) ROBOCRACY
                    (9) TECHNOCRACY
                    (10) UTOPIA
                
                Choose state system: 
            """.trimIndent()
        )
        val input = readLine()
        return extractStateSystemFromInput(input)
    }

    private fun extractStateSystemFromInput(input: String?): EStateSystem {
        if (input?.toIntOrNull() == null) askForStateSystem()

        return when (input!!.toInt()) {
            1 -> EStateSystem.ANARCHY
            2 -> EStateSystem.COMMUNISM
            3 -> EStateSystem.DEMOCRACY
            4 -> EStateSystem.DICTATORSHIP
            5 -> EStateSystem.FEUDALISM
            6 -> EStateSystem.FUNDAMENTALISM
            7 -> EStateSystem.REPUBLIC
            8 -> EStateSystem.ROBOCRACY
            9 -> EStateSystem.TECHNOCRACY
            10 -> EStateSystem.UTOPIA
            else -> askForStateSystem()
        }
    }

    private fun askForOrderDirection(): EOrderDirection {
        print(
            """
                Available orders:
                    (1) ASCENDING
                    (2) DESCENDING
                
                Choose order: 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderDirectionFromInput(input)
    }

    private fun extractOrderDirectionFromInput(input: String?): EOrderDirection {
        if (input?.toIntOrNull() == null) askForOrderDirection()

        return when (input!!.toInt()) {
            1 -> EOrderDirection.ASCENDING
            2 -> EOrderDirection.DESCENDING
            else -> askForOrderDirection()
        }
    }

    private fun askForReturningCount(): Int {
        print(
            """
                How many result you want to return? 
            """.trimIndent()
        )
        val input = readLine()
        return extractCountFromInput(input)
    }

    private fun extractCountFromInput(input: String?): Int {
        val inputInt = input?.toIntOrNull()
        if (inputInt == null) askForReturningCount()
        if (inputInt!! < 1) askForReturningCount()
        return inputInt
    }
}