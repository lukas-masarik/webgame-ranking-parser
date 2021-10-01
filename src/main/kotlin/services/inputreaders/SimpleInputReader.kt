package services.inputreaders

import enums.EOrderAttribute
import enums.EOrderDirection
import enums.EStateSystem
import services.inputreaders.api.InputReader
import services.processors.PrestigeByStateSystemProcessor
import services.processors.CompareLandsThroughEpochsProcessor
import services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Available processes:
                    (1) Compare lands through epochs
                    (2) Compare lands prestige by state system
                    (0) Exit program
                
                Choose process: 
            """.trimIndent()
        )
        val input = readLine()
        return extractProcessorFromInput(input)
    }

    private fun extractProcessorFromInput(input: String?): Processor {
        if (input?.toIntOrNull() == null) selectProcessorFromInput()

        return when (input!!.toInt()) {
            0 -> exitProcess(1)
            1 -> CompareLandsThroughEpochsProcessor(this)
            2 -> PrestigeByStateSystemProcessor(this)
            else -> selectProcessorFromInput()
        }
    }

    override fun selectOrderDirectionFromInput(): EOrderDirection {
        print(
            """
                Available orders:
                    (1) DESCENDING
                    (2) ASCENDING
                
                Choose order (1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderDirectionFromInput(input)
    }

    private fun extractOrderDirectionFromInput(input: String?): EOrderDirection {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectOrderDirectionFromInput()

        return when (userInput!!.toInt()) {
            1 -> EOrderDirection.DESCENDING
            2 -> EOrderDirection.ASCENDING
            else -> selectOrderDirectionFromInput()
        }
    }

    override fun selectOrderAttributeFromInput(): EOrderAttribute {
        print(
            """
                Available attributes:
                    (1) PRESTIGE
                    (2) AREA
                
                Choose attribute (1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderAttributeFromInput(input)
    }

    private fun extractOrderAttributeFromInput(input: String?): EOrderAttribute {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectOrderAttributeFromInput()

        return when (userInput!!.toInt()) {
            1 -> EOrderAttribute.PRESTIGE
            2 -> EOrderAttribute.AREA
            else -> selectOrderAttributeFromInput()
        }
    }

    override fun selectReturnCountFromInput(): Int {
        print(
            """
                How many result you want to return (10)? 
            """.trimIndent()
        )
        val input = readLine()
        return extractCountFromInput(input)
    }

    private fun extractCountFromInput(input: String?): Int {
        val userInput = if (input?.isEmpty() == true) "10" else input
        val inputInt = userInput?.toIntOrNull()
        if (inputInt == null) selectReturnCountFromInput()
        if (inputInt!! < 1) selectReturnCountFromInput()
        return inputInt
    }

    override fun selectStateSystemFromInput(): EStateSystem {
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
        if (input?.toIntOrNull() == null) selectStateSystemFromInput()

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
            else -> selectStateSystemFromInput()
        }
    }

    override fun selectStartEpochFromInput(): Int? {
        print(
            """
                Choose start epoch (leave blank to skip):
            """.trimIndent()
        )
        val input = readLine()
        return extractEpochNumberFromInput(input)
    }

    override fun selectEndEpochFromInput(): Int? {
        print(
            """
                Choose end epoch (leave blank to skip):
            """.trimIndent()
        )
        val input = readLine()
        return extractEpochNumberFromInput(input)
    }

    private fun extractEpochNumberFromInput(input: String?): Int? {
        return input?.toIntOrNull()
    }

    override fun selectStartRankFromInput(): Int? {
        print(
            """
                Choose start rank (leave blank to skip):
            """.trimIndent()
        )
        val input = readLine()
        return extractRankNumberFromInput(input)
    }

    override fun selectEndRankFromInput(): Int? {
        print(
            """
                Choose end rank (leave blank to skip):
            """.trimIndent()
        )
        val input = readLine()
        return extractRankNumberFromInput(input)
    }

    private fun extractRankNumberFromInput(input: String?): Int? {
        return input?.toIntOrNull()
    }
}