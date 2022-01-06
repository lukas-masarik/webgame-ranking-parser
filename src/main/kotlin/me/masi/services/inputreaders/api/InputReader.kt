package me.masi.services.inputreaders.api

import me.masi.enums.EAggregatingParameter
import me.masi.enums.ESortAttribute
import me.masi.enums.ESortDirection
import me.masi.enums.lands.EFilteringParameterForLands
import me.masi.enums.lands.EGroupingParameterForLands
import me.masi.services.processors.api.Processor

interface InputReader {
    fun selectProcessorFromInput(): Processor
    fun selectSortDirectionFromInput(): ESortDirection
    fun selectSortAttributeFromInput(): ESortAttribute
    fun selectReturnCountFromInput(): Int
    fun selectStartEpochFromInput(): Int?
    fun selectEndEpochFromInput(): Int?
    fun selectStartRankFromInput(): Int?
    fun selectEndRankFromInput(): Int?
    fun selectAggregatingParameterFromInput(): EAggregatingParameter
    fun selectGroupingParameterForLandsFromInput(): EGroupingParameterForLands
    fun selectFilteringParameterForLandsFromInput(): EFilteringParameterForLands
    fun selectFilterPlayerQueryFromInput(): String
    fun selectFilterAllianceQueryFromInput(): String?
    fun selectFilterStateSystemQueryFromInput(): String
    fun selectFilterLandNumberQueryFromInput(): String
}
