package services.inputreaders

import enums.EOrderAttribute
import enums.EOrderDirection
import enums.EStateSystem
import services.inputreaders.api.InputReader
import services.processors.PrestigeByStateSystemProcessor
import services.processors.WinnersByPrestigeOrAreaProcessor
import services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Available processes:
                    (1) Winners by prestige or area
                    (2) Prestige by state system
                    (0) Exit program
                
                Choose processor: 
            """.trimIndent()
        )
        val input = readLine()
        return extractProcessorFromInput(input)
    }

    private fun extractProcessorFromInput(input: String?): Processor {
        if (input?.toIntOrNull() == null) selectProcessorFromInput()

        return when (input!!.toInt()) {
            0 -> exitProcess(1)
            1 -> WinnersByPrestigeOrAreaProcessor(this)
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
                
                Choose attribute: 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderAttributeFromInput(input)
    }

    private fun extractOrderAttributeFromInput(input: String?): EOrderAttribute {
        if (input?.toIntOrNull() == null) selectOrderAttributeFromInput()

        return when (input!!.toInt()) {
            1 -> EOrderAttribute.PRESTIGE
            2 -> EOrderAttribute.AREA
            else -> selectOrderAttributeFromInput()
        }
    }

    override fun selectReturnCountFromInput(): Int {
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
}