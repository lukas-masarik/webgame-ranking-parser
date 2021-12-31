package me.masi.dto.lands

data class LandsRanking(
    val epochNumber: Int,
    val landsRankingRows: List<LandsRankingRow>,
)
