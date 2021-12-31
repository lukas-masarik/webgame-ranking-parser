package me.masi.services.inputreaders

import me.masi.enums.*
import me.masi.enums.lands.EFilteringParameter
import me.masi.enums.lands.EGroupingParameter
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.processors.lands.AggregateLandsRankingProcessor
import me.masi.services.processors.lands.FilterLandsRankingProcessor
import me.masi.services.processors.lands.ListLandsRankingProcessor
import me.masi.services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Dostupne programy:
                    (1) Prochazet zebricek zemi
                    (2) Filtrovat zebricek zemi (podle hrace, aliance, vlady, ...)
                    (3) Seskupovat zebricek zemi (podle hracu, alianci, vlad, ...)
                    (0) Ukoncit program
                
                Vyber program (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractProcessorFromInput(input)
    }

    private fun extractProcessorFromInput(input: String?): Processor {
        return when (input?.toIntOrNull()) {
            0 -> exitProcess(1)
            1 -> ListLandsRankingProcessor(this)
            2 -> FilterLandsRankingProcessor(this)
            3 -> AggregateLandsRankingProcessor(this)
            else -> ListLandsRankingProcessor(this)
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
        return when (input?.toIntOrNull()) {
            1 -> ESortDirection.DESCENDING
            2 -> ESortDirection.ASCENDING
            else -> ESortDirection.DESCENDING
        }
    }

    override fun selectSortAttributeFromInput(): ESortAttribute {
        print(
            """
                Dostupne atributy pro razeni:
                    (1) prestiz
                    (2) rozloha
                    (3) vek
                
                Vyber radici atribut (defaulnte 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractOrderAttributeFromInput(input)
    }

    private fun extractOrderAttributeFromInput(input: String?): ESortAttribute {
        return when (input?.toIntOrNull()) {
            1 -> ESortAttribute.PRESTIGE
            2 -> ESortAttribute.AREA
            3 -> ESortAttribute.EPOCH_NUMBER
            else -> ESortAttribute.PRESTIGE
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
        val userInput = input?.toIntOrNull() ?: 10
        return if (userInput < 0) {
            10
        } else {
            userInput
        }
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
                Vyber konecny vek (nech prazdne pro nezadani limitu): 
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
                Vyber pocatecni poradi zemi (nech prazdne pro nezadani limitu): 
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
        return when (input?.toIntOrNull()) {
            1 -> EAggregatingParameter.OCCURRENCE
            2 -> EAggregatingParameter.PRESTIGE
            3 -> EAggregatingParameter.AREA
            else -> EAggregatingParameter.OCCURRENCE
        }
    }

    override fun selectGroupingParameterFromInput(): EGroupingParameter {
        print(
            """
                Dostupne seskupujici parametry:
                    (1) hrac
                    (2) aliance
                    (3) vlada
                    (4) cislo zeme
                
                Vyber seskupujici parametr (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractGroupingParameterFromInput(input)
    }

    private fun extractGroupingParameterFromInput(input: String?): EGroupingParameter {
        return when (input?.toIntOrNull()) {
            1 -> EGroupingParameter.PLAYER
            2 -> EGroupingParameter.ALLIANCE
            3 -> EGroupingParameter.STATE_SYSTEM
            4 -> EGroupingParameter.LAND_NUMBER
            else -> EGroupingParameter.PLAYER
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
        return when (input?.toIntOrNull()) {
            1 -> EFilteringParameter.PLAYER
            2 -> EFilteringParameter.ALLIANCE
            3 -> EFilteringParameter.STATE_SYSTEM
            4 -> EFilteringParameter.LAND_NUMBER
            else -> EFilteringParameter.PLAYER
        }
    }

    override fun selectFilterPlayerQueryFromInput(): String {
        print(
            """
                Priklady jmena hrace: [mara8|MAFline|thordevil]
                
                Zadej jmeno hrace: 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterPlayerQueryFromInput(input)
    }

    private fun extractFilterPlayerQueryFromInput(input: String?): String {
        return input ?: ""
    }

    override fun selectFilterAllianceQueryFromInput(): String? {
        print(
            """
                Priklady jmena aliance: [M, Anarchy, **CQR**]
                
                Zadej jmeno aliance: 
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
                
                Zadej vladu (defaultne anar): 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterStateSystemQueryFromInput(input)
    }

    private fun extractFilterStateSystemQueryFromInput(input: String?): String {
        return input ?: "anar"
    }

    override fun selectFilterLandNumberQueryFromInput(): String {
        print(
            """
                Priklady cisla zeme: [42|111|94]
                
                Zadej cislo zeme (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterLandNumberQueryFromInput(input)
    }

    private fun extractFilterLandNumberQueryFromInput(input: String?): String {
        return input?.toIntOrNull()?.toString() ?: "1"
    }
}
