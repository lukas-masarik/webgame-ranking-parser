package services.processors

import enums.EOrderAttribute
import enums.EOrderDirection
import services.parsers.SimpleEpochsParser
import services.processors.api.Processor

class WinnersByPrestigeOrAreaProcessor : Processor {
    private val parser = SimpleEpochsParser()

    override fun apply() {
        val landsCount = askForReturningCount()
        val orderAttribute = askForOrderAttribute()
        val orderDirection = askForOrderDirection()
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

    private fun askForOrderAttribute(): EOrderAttribute {
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
        if (input?.toIntOrNull() == null) askForOrderAttribute()

        return when (input!!.toInt()) {
            1 -> EOrderAttribute.PRESTIGE
            2 -> EOrderAttribute.AREA
            else -> askForOrderAttribute()
        }
    }
}