package me.masi.services.parsers

import me.masi.dto.alliances.AlliancesRankingRow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AlliancesRankingParserTest {
    private val rankingParser = AlliancesRankingParser()

    @Test
    fun `parse alliance ranking row with alliance name`() {
        val alliancesRankingRowRaw = "27.\t[**SNK**] SmeNaKašu\t9\t117774km2\t29599780\tLiška Bystrouška na Kašu(#263) - Jitus21"
        val alliancesRankingRow = rankingParser.parseAlliancesRankingRow(alliancesRankingRowRaw, 1)

        assertEquals(
            alliancesRankingRow,
            AlliancesRankingRow(
                ranking = 27,
                allianceTag = "**SNK**",
                membersCount = 9,
                area = 117774,
                prestige = 29599780,
                chairmanPlayerName = "Jitus21",
                epochNumber = 1
            )
        )
    }

    @Test
    fun `parse alliance ranking row without alliance name`() {
        val alliancesRankingRowRaw = "19.\t[Anarchy]\t10\t140790km2\t24751703\tTouha je zázrak, kámo...(#2707) - thordevil"
        val alliancesRankingRow = rankingParser.parseAlliancesRankingRow(alliancesRankingRowRaw, 1)

        assertEquals(
            alliancesRankingRow,
            AlliancesRankingRow(
                ranking = 19,
                allianceTag = "Anarchy",
                membersCount = 10,
                area = 140790,
                prestige = 24751703,
                chairmanPlayerName = "thordevil",
                epochNumber = 1
            )
        )
    }
}
