package me.masi.services.parsers

import me.masi.dto.lands.LandsRankingRow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LandsRankingParserTest {
    private val rankingParser = LandsRankingParser()

    @Test
    fun `parse lands ranking row with alliance`() {
        val landsRankingRowRaw = "2.\tBlack Stronghold(#209) - Black Warrior\t28331km2\t16692649\tUFD\tRep\t3811"
        val landsRankingRow = rankingParser.parseLandsRankingRow(landsRankingRowRaw, 1)

        assertEquals(
            landsRankingRow,
            LandsRankingRow(
                ranking = 2,
                landName = "Black Stronghold",
                landNumber = 209,
                playerName = "Black Warrior",
                area = 28331,
                prestige = 16692649,
                alliance = "UFD",
                stateSystem = "Rep",
                rounds = 3811,
                epochNumber = 1
            )
        )
    }

    @Test
    fun `parse lands ranking row without alliance`() {
        val landsRankingRowRaw = "1.\tBorobudur(#521) - Lone Wolf\t31581km2\t16810415\t\tUtop\t3836"
        val landsRankingRow = rankingParser.parseLandsRankingRow(landsRankingRowRaw, 1)

        assertEquals(
            landsRankingRow,
            LandsRankingRow(
                ranking = 1,
                landName = "Borobudur",
                landNumber = 521,
                playerName = "Lone Wolf",
                area = 31581,
                prestige = 16810415,
                alliance = null,
                stateSystem = "Utop",
                rounds = 3836,
                epochNumber = 1
            )
        )
    }
}
