package me.masi.dto

data class LandsRanking(
    val epochNumber: Int,
    val landsRankingRows: List<LandsRankingRow>,
)
