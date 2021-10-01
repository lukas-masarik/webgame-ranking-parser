package services.inputreaders

import enums.EOrderAttribute
import enums.EOrderDirection
import enums.EStateSystem
import services.inputreaders.api.InputReader
import services.processors.WinnersByPrestigeOrAreaProcessor
import services.processors.ParseOnlyProcessor
import services.processors.PrestigeByStateSystemProcessor
import services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Available processes:
                    (1) Parse only
                    (2) Winners by prestige or area
                    (3) Prestige by state system
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
            1 -> ParseOnlyProcessor()
            2 -> WinnersByPrestigeOrAreaProcessor(this)
            3 -> PrestigeByStateSystemProcessor(this)
            else -> selectProcessorFromInput()
        }
    }

    override fun selectOrderDirectionFromInput(): EOrderDirection {
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
        if (input?.toIntOrNull() == null) selectOrderDirectionFromInput()

        return when (input!!.toInt()) {
            1 -> EOrderDirection.ASCENDING
            2 -> EOrderDirection.DESCENDING
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