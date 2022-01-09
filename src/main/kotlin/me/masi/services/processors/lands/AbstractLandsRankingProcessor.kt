package me.masi.services.processors.lands

import me.masi.dto.lands.LandsRanking
import me.masi.services.processors.api.Processor

abstract class AbstractLandsRankingProcessor : Processor {

    fun filterEpochs(landsRankings: List<LandsRanking>, firstEpoch: Int? = null, lastEpoch: Int? = null): List<LandsRanking> {
        val epochsWithoutFirstN = if (firstEpoch != null) landsRankings.filter { it.epochNumber >= firstEpoch } else landsRankings
        return if (lastEpoch != null) epochsWithoutFirstN.filter { it.epochNumber <= lastEpoch } else epochsWithoutFirstN
    }

    fun filterRankings(landsRankings: List<LandsRanking>, firstRanking: Int? = null, lastRanking: Int? = null): List<LandsRanking> {
        val landsRankingsWithFirstRankingsFiltered = if (firstRanking != null) {
            landsRankings.map { landsRanking ->
                LandsRanking(
                    epochNumber = landsRanking.epochNumber,
                    landsRankingRows = landsRanking.landsRankingRows.filter { it.ranking >= firstRanking }
                )
            }
        } else {
            landsRankings
        }

        val landsRankingsWithLastRankingsFiltered = if (lastRanking != null) {
            landsRankingsWithFirstRankingsFiltered.map { landsRanking ->
                LandsRanking(
                    epochNumber = landsRanking.epochNumber,
                    landsRankingRows = landsRanking.landsRankingRows.filter { it.ranking <= lastRanking }
                )
            }
        } else {
            landsRankingsWithFirstRankingsFiltered
        }

        return landsRankingsWithLastRankingsFiltered
    }
}
