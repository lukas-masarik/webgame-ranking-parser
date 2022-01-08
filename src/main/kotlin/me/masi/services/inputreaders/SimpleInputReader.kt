package me.masi.services.inputreaders

import me.masi.enums.EAggregatingParameter
import me.masi.enums.ERankingType
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.enums.alliances.EFilteringParameterForAlliances
import me.masi.enums.alliances.EGroupingParameterForAlliances
import me.masi.enums.lands.EFilteringParameterForLands
import me.masi.enums.lands.EGroupingParameterForLands
import me.masi.services.inputreaders.api.InputReader
import me.masi.services.processors.alliances.AggregateAlliancesRankingProcessor
import me.masi.services.processors.alliances.FilterAlliancesRankingProcessor
import me.masi.services.processors.alliances.ListAlliancesRankingProcessor
import me.masi.services.processors.api.Processor
import me.masi.services.processors.lands.AggregateLandsRankingProcessor
import me.masi.services.processors.lands.FilterLandsRankingProcessor
import me.masi.services.processors.lands.ListLandsRankingProcessor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        return when (selectRankingType()) {
            ERankingType.LANDS -> selectLandsRankingsProcessor()
            ERankingType.ALLIANCES -> selectAlliancesRankingsProcessor()
        }
    }

    private fun selectRankingType(): ERankingType {
        print(
            """
                Dostupne zebricky:
                    (1) Zebricek zemi
                    (2) Zebricek alianci
                
                Vyber zebricek (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractRankingTypeFromInput(input)
    }

    private fun extractRankingTypeFromInput(input: String?): ERankingType {
        return when (input?.toIntOrNull()) {
            2 -> ERankingType.ALLIANCES
            else -> ERankingType.LANDS
        }
    }

    private fun selectLandsRankingsProcessor(): Processor {
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
        return extractLandsRankingsProcessorFromInput(input)
    }

    private fun extractLandsRankingsProcessorFromInput(input: String?): Processor {
        return when (input?.toIntOrNull()) {
            0 -> exitProcess(1)
            1 -> ListLandsRankingProcessor(this)
            2 -> FilterLandsRankingProcessor(this)
            3 -> AggregateLandsRankingProcessor(this)
            else -> ListLandsRankingProcessor(this)
        }
    }

    private fun selectAlliancesRankingsProcessor(): Processor {
        print(
            """
                Dostupne programy:
                    (1) Prochazet zebricek alianci
                    (2) Filtrovat zebricek alianci (podle aliance, clenu, predsedy)
                    (3) Seskupovat zebricek alianci (podle aliance, clenu, predsedy)
                
                Vyber program (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractAlliancesRankingsProcessorFromInput(input)
    }

    private fun extractAlliancesRankingsProcessorFromInput(input: String?): Processor {
        return when (input?.toIntOrNull()) {
            1 -> ListAlliancesRankingProcessor(this)
            2 -> FilterAlliancesRankingProcessor(this)
            3 -> AggregateAlliancesRankingProcessor(this)
            else -> ListAlliancesRankingProcessor(this)
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

    override fun selectGroupingParameterForLandsFromInput(): EGroupingParameterForLands {
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
        return extractGroupingParameterForLandsFromInput(input)
    }

    private fun extractGroupingParameterForLandsFromInput(input: String?): EGroupingParameterForLands {
        return when (input?.toIntOrNull()) {
            1 -> EGroupingParameterForLands.PLAYER
            2 -> EGroupingParameterForLands.ALLIANCE
            3 -> EGroupingParameterForLands.STATE_SYSTEM
            4 -> EGroupingParameterForLands.LAND_NUMBER
            else -> EGroupingParameterForLands.PLAYER
        }
    }

    override fun selectGroupingParameterForAlliancesFromInput(): EGroupingParameterForAlliances {
        print(
            """
                Dostupne seskupujici parametry:
                    (1) aliance
                    (2) predseda
                    (3) clenu
                
                Vyber seskupujici parametr (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractGroupingParameterForAlliancesFromInput(input)
    }

    private fun extractGroupingParameterForAlliancesFromInput(input: String?): EGroupingParameterForAlliances {
        return when (input?.toIntOrNull()) {
            1 -> EGroupingParameterForAlliances.TAG
            2 -> EGroupingParameterForAlliances.CHAIRMAN
            3 -> EGroupingParameterForAlliances.MEMBERS_COUNT
            else -> EGroupingParameterForAlliances.TAG
        }
    }

    override fun selectFilteringParameterForLandsFromInput(): EFilteringParameterForLands {
        print(
            """
                Dostupne filtrujici parametry:
                    (1) hrac
                    (2) aliance
                    (3) vlada
                    (4) cislo zeme
                
                Vyber filtrujici parametr (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilteringParameterForLandsFromInput(input)
    }

    private fun extractFilteringParameterForLandsFromInput(input: String?): EFilteringParameterForLands {
        return when (input?.toIntOrNull()) {
            1 -> EFilteringParameterForLands.PLAYER
            2 -> EFilteringParameterForLands.ALLIANCE
            3 -> EFilteringParameterForLands.STATE_SYSTEM
            4 -> EFilteringParameterForLands.LAND_NUMBER
            else -> EFilteringParameterForLands.PLAYER
        }
    }

    override fun selectFilteringParameterForAlliancesFromInput(): EFilteringParameterForAlliances {
        print(
            """
                Dostupne filtrujici parametry:
                    (1) aliance
                    (2) predseda
                    (3) clenu
                
                Vyber filtrujici parametr (defaultne 1): 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilteringParameterForAlliancesFromInput(input)
    }

    private fun extractFilteringParameterForAlliancesFromInput(input: String?): EFilteringParameterForAlliances {
        return when (input?.toIntOrNull()) {
            1 -> EFilteringParameterForAlliances.TAG
            2 -> EFilteringParameterForAlliances.CHAIRMAN
            3 -> EFilteringParameterForAlliances.MEMBERS_COUNT
            else -> EFilteringParameterForAlliances.TAG
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

    override fun selectFilterMembersCountQueryFromInput(): String {
        print(
            """
                Zadej velikost aliance (defaultne 10): 
            """.trimIndent()
        )
        val input = readLine()
        return extractFilterMembersCountrQueryFromInput(input)
    }

    private fun extractFilterMembersCountrQueryFromInput(input: String?): String {
        return input?.toIntOrNull()?.toString() ?: "10"
    }
}
