package me.masi.services.parsers

import me.masi.dto.lands.LandsRankingRow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LandsParserTestRankingRecord {
    private val rankingParser = LandsRankingParser()

    @Test
    fun `parse ranked land with alliance`() {
        val rankedLandRaw = "2.\tBlack Stronghold(#209) - Black Warrior\t28331km2\t16692649\tUFD\tRep\t3811"
        val rankedLand = rankingParser.parseLandsRankingRow(rankedLandRaw, 1)

        assertEquals(
            rankedLand,
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
    fun `parse ranked land without alliance`() {
        val rankedLandRaw = "1.\tBorobudur(#521) - Lone Wolf\t31581km2\t16810415\t\tUtop\t3836"
        val rankedLand = rankingParser.parseLandsRankingRow(rankedLandRaw, 1)

        assertEquals(
            rankedLand,
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
