package me.masi.services.processors.alliances

import me.masi.dto.alliances.AlliancesRanking
import me.masi.services.processors.api.Processor

abstract class AbstractAlliancesRankingProcessor : Processor {

    fun filterEpochs(alliancesRankings: List<AlliancesRanking>, firstEpoch: Int? = null, lastEpoch: Int? = null): List<AlliancesRanking> {
        val epochsWithoutFirstN = if (firstEpoch != null) alliancesRankings.filter { it.epochNumber >= firstEpoch } else alliancesRankings
        return if (lastEpoch != null) epochsWithoutFirstN.filter { it.epochNumber <= lastEpoch } else epochsWithoutFirstN
    }

    fun filterRankings(alliancesRankings: List<AlliancesRanking>, firstRanking: Int? = null, lastRanking: Int? = null): List<AlliancesRanking> {
        val alliancesRankingsWithFirstRankingsFiltered = if (firstRanking != null) {
            alliancesRankings.map { alliancesRanking ->
                AlliancesRanking(
                    epochNumber = alliancesRanking.epochNumber,
                    alliancesRankingRows = alliancesRanking.alliancesRankingRows.filter { it.ranking >= firstRanking }
                )
            }
        } else {
            alliancesRankings
        }

        val alliancesRankingsWithLastRankingsFiltered = if (lastRanking != null) {
            alliancesRankingsWithFirstRankingsFiltered.map { alliancesRanking ->
                AlliancesRanking(
                    epochNumber = alliancesRanking.epochNumber,
                    alliancesRankingRows = alliancesRanking.alliancesRankingRows.filter { it.ranking <= lastRanking }
                )
            }
        } else {
            alliancesRankingsWithFirstRankingsFiltered
        }

        return alliancesRankingsWithLastRankingsFiltered
    }
}
