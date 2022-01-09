package me.masi.dto.alliances

data class AlliancesRanking(
    val epochNumber: Int,
    val alliancesRankingRows: List<AlliancesRankingRow>,
)
