package services.inputreaders

import enums.*
import services.inputreaders.api.InputReader
import services.processors.AggregateRankedLandsProcessor
import services.processors.FilterRankedLandsProcessor
import services.processors.ListRankedLandsProcessor
import services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Available processes:
                    (1) Compare lands through epochs
                    (2) Filter ranked lands stats (filter specific player, alliance, ...)
                    (3) Aggregate ranked lands stats (aggregate stats for players, alliances, ...)
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
            1 -> ListRankedLandsProcessor(this)
            3 -> FilterRankedLandsProcessor(this)
            4 -> AggregateRankedLandsProcessor(this)
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
        if (inputInt!! < 0) selectReturnCountFromInput()
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

    override fun selectAggregatingParameterFromInput(): EAggregatingParameter {
        print(
            """
                Available aggregating parameters:
                    (1) OCCURRENCE
                    (2) PRESTIGE
                    (3) AREA
                
                Choose aggregation parameter (1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractAggregatingParameterFromInput(input)
    }

    private fun extractAggregatingParameterFromInput(input: String?): EAggregatingParameter {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectAggregatingParameterFromInput()

        return when (userInput!!.toInt()) {
            1 -> EAggregatingParameter.OCCURRENCE
            2 -> EAggregatingParameter.PRESTIGE
            3 -> EAggregatingParameter.AREA
            else -> selectAggregatingParameterFromInput()
        }
    }

    override fun selectGroupingParameterFromInput(): EGroupingParameter {
        print(
            """
                Available grouping parameters:
                    (1) PLAYER
                    (2) ALLIANCE
                    (3) STATE SYSTEM
                    (4) LAND NUMBER
                
                Choose aggregation parameter (1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractGroupingParameterFromInput(input)
    }

    private fun extractGroupingParameterFromInput(input: String?): EGroupingParameter {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectGroupingParameterFromInput()

        return when (userInput!!.toInt()) {
            1 -> EGroupingParameter.PLAYER
            2 -> EGroupingParameter.ALLIANCE
            3 -> EGroupingParameter.STATE_SYSTEM
            4 -> EGroupingParameter.LAND_NUMBER
            else -> selectGroupingParameterFromInput()
        }
    }

    override fun selectFilteringParameterFromInput(): EFilteringParameter {
        print(
            """
                Available filtering parameters:
                    (1) PLAYER
                    (2) ALLIANCE
                    (3) STATE SYSTEM
                    (4) LAND NUMBER
                
                Choose filtering parameter (1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilteringParameterFromInput(input)
    }

    private fun extractFilteringParameterFromInput(input: String?): EFilteringParameter {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectFilteringParameterFromInput()

        return when (userInput!!.toInt()) {
            1 -> EFilteringParameter.PLAYER
            2 -> EFilteringParameter.ALLIANCE
            3 -> EFilteringParameter.STATE_SYSTEM
            4 -> EFilteringParameter.LAND_NUMBER
            else -> selectFilteringParameterFromInput()
        }
    }

    override fun selectFilteringQueryFromInput(): String {
        print(
            """
                Filter examples:
                    For player: [mara8|MAFline|thordevil]
                    For alliance: [SB|M|**CQR**]
                    For state system: [anar|demo|dikt|feud|fund|kom|rep|robo|tech|utop]
                    For land number: [42|984]
                
                Choose filter parameter: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilteringQueryFromInput(input)
    }

    private fun extractFilteringQueryFromInput(input: String?): String {
        return input ?: selectFilteringQueryFromInput()
    }
}
