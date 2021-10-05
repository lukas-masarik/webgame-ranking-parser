package me.masi.services.inputreaders

import me.masi.enums.*
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.processors.AggregateRankedLandsProcessor
import me.masi.services.processors.FilterRankedLandsProcessor
import me.masi.services.processors.ListRankedLandsProcessor
import me.masi.services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Dostupné programy:
                    (1) Prochazet zebricek zemi
                    (2) Filtrovat zebricek zemi (podle hrace, aliance, vlady, ...)
                    (3) Seskupovat zebricek zemi (podle hracu, alianci, vlad, ...)
                    (0) Ukoncit program
                
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
                Dostupne razeni:
                    (1) sestupne
                    (2) vzestupne
                
                Vyber razeni (defaultne 1): 
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
                Dostupne atributy pro razeni:
                    (1) prestiz
                    (2) rozloha
                
                Vyber radici atribut (defaulnte 1): 
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
                Kolik radku vysledku chces vratit (0 pro vsechny, defaultne 10)? 
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
                Vyber pocatecni vek (nech prazdne pro nezadani limitu): 
            """.trimIndent()
        )
        val input = readLine()
        return extractEpochNumberFromInput(input)
    }

    override fun selectEndEpochFromInput(): Int? {
        print(
            """
                Vyber konecný vek (nech prazdne pro nezadani limitu): 
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
                Vyber pocatecni poradí zemi (nech prazdne pro nezadani limitu): 
            """.trimIndent()
        )
        val input = readLine()
        return extractRankNumberFromInput(input)
    }

    override fun selectEndRankFromInput(): Int? {
        print(
            """
                Vyber konecne poradi zemi (nech prazdne pro nezadani limitu): 
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
                Dostupne agregacni parametry:
                    (1) ucast
                    (2) prestiz
                    (3) rozloha
                
                Vyber agregacni parametr (defaultne 1): 
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
                Dostupne seskupujici parametry:
                    (1) hrac
                    (2) aliance
                    (3) vlada
                    (4) císlo zeme
                
                Vyber seskupujici parametr (defaultne 1): 
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
                Dostpune filtrujici parametry:
                    (1) hrac
                    (2) aliance
                    (3) vlada
                    (4) cislo zeme
                
                Vyber filtrujici parametr (defaultne 1): 
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
                Priklady jmena hrace: [mara8|MAFline|thordevil]
                
                Napis jmeno hrace: 
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
                Priklady jmena aliance: [M, Anarchy, **CQR**]
                
                Napis jmeno aliance: 
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
                Dostupne vlady: [anar|demo|dikt|feud|fund|kom|rep|robo|tech|utop]
                
                Zvol vladu: 
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
                Priklady cisla zeme: [42|111|94]
                
                Napis cislo zeme: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterLandNumberQueryFromInput(input)
    }

    private fun extractFilterLandNumberQueryFromInput(input: String?): String {
        return if (input?.toIntOrNull() is Number) input else selectFilterLandNumberQueryFromInput()
    }
}
