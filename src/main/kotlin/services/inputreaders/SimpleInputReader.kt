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
                Dostupné programy:
                    (1) Procházet žebříček zemí
                    (2) Filtrovat žebříček zemí (podle hráče, aliance, vlády, ...)
                    (3) Seskupovat žebříček zemí (podle hráčů, aliancí, vlád, ...)
                    (0) Ukončit program
                
                Zvol program: 
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
            2 -> FilterRankedLandsProcessor(this)
            3 -> AggregateRankedLandsProcessor(this)
            else -> selectProcessorFromInput()
        }
    }

    override fun selectSortDirectionFromInput(): ESortDirection {
        print(
            """
                Dostupné řazení:
                    (1) sestupně
                    (2) vzestupně
                
                Vyber řazení (defaultně 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderDirectionFromInput(input)
    }

    private fun extractOrderDirectionFromInput(input: String?): ESortDirection {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectSortDirectionFromInput()

        return when (userInput!!.toInt()) {
            1 -> ESortDirection.DESCENDING
            2 -> ESortDirection.ASCENDING
            else -> selectSortDirectionFromInput()
        }
    }

    override fun selectSortAttributeFromInput(): ESortAttribute {
        print(
            """
                Dostupné atributy pro řazení:
                    (1) prestiž
                    (2) rozloha
                
                Vyber řadící atribut (defaulntě 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderAttributeFromInput(input)
    }

    private fun extractOrderAttributeFromInput(input: String?): ESortAttribute {
        val userInput = if (input?.isEmpty() == true) "1" else input
        if (userInput?.toIntOrNull() == null) selectSortAttributeFromInput()

        return when (userInput!!.toInt()) {
            1 -> ESortAttribute.PRESTIGE
            2 -> ESortAttribute.AREA
            else -> selectSortAttributeFromInput()
        }
    }

    override fun selectReturnCountFromInput(): Int {
        print(
            """
                Kolik řádků výsledku chceš vrátit (0 pro všechny, defaultně 10)? 
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

    override fun selectStartEpochFromInput(): Int? {
        print(
            """
                Vyber počáteční věk (nech prázdné pro nezadání limitu): 
            """.trimIndent()
        )
        val input = readLine()
        return extractEpochNumberFromInput(input)
    }

    override fun selectEndEpochFromInput(): Int? {
        print(
            """
                Vyber konečný věk (nech prázdné pro nezadání limitu): 
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
                Vyber počáteční pořadí zemí (nech prázdné pro nezadání limitu): 
            """.trimIndent()
        )
        val input = readLine()
        return extractRankNumberFromInput(input)
    }

    override fun selectEndRankFromInput(): Int? {
        print(
            """
                Vyber konečné pořadí zemí (nech prázdné pro nezadání limitu): 
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
                Dostupné agregační parametry:
                    (1) účast
                    (2) prestiž
                    (3) rozloha
                
                Vyber agregační parametr (defaultně 1): 
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
                Dostupné seskupující parametry:
                    (1) hráč
                    (2) aliance
                    (3) vláda
                    (4) číslo země
                
                Vyber seskupující parametr (defaultně 1): 
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
                Dostpuné filtrující parametry:
                    (1) hráč
                    (2) aliance
                    (3) vláda
                    (4) číslo země
                
                Vyber filtrující parametr (defaultně 1): 
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

    override fun selectFilterPlayerQueryFromInput(): String {
        print(
            """
                Příklady jména hráče: [mara8|MAFline|thordevil]
                
                Napíš jméno hráče: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterPlayerQueryFromInput(input)
    }

    private fun extractFilterPlayerQueryFromInput(input: String?): String {
        return input ?: selectFilterPlayerQueryFromInput()
    }

    override fun selectFilterAllianceQueryFromInput(): String? {
        print(
            """
                Příklady jména aliance: [M, Anarchy, **CQR**]
                
                Napiš jméno aliance: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterAllianceQueryFromInput(input)
    }

    private fun extractFilterAllianceQueryFromInput(input: String?): String? {
        return input?.ifBlank { null }
    }

    override fun selectFilterStateSystemQueryFromInput(): String {
        print(
            """
                Dostupné vlády: [anar|demo|dikt|feud|fund|kom|rep|robo|tech|utop]
                
                Zvol vládu: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterStateSystemQueryFromInput(input)
    }

    private fun extractFilterStateSystemQueryFromInput(input: String?): String {
        return input ?: selectFilterStateSystemQueryFromInput()
    }

    override fun selectFilterLandNumberQueryFromInput(): String {
        print(
            """
                Příklady čísla země: [42|111|94]
                
                Napiš číslo země: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterLandNumberQueryFromInput(input)
    }

    private fun extractFilterLandNumberQueryFromInput(input: String?): String {
        return if (input?.toIntOrNull() is Number) input else selectFilterLandNumberQueryFromInput()
    }
}
