package me.masi.services.inputreaders.api

import me.masi.enums.*
import me.masi.enums.lands.EFilteringParameter
import me.masi.enums.lands.EGroupingParameter
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
    fun selectGroupingParameterFromInput(): EGroupingParameter
    fun selectFilteringParameterFromInput(): EFilteringParameter
    fun selectFilterPlayerQueryFromInput(): String
    fun selectFilterAllianceQueryFromInput(): String?
    fun selectFilterStateSystemQueryFromInput(): String
    fun selectFilterLandNumberQueryFromInput(): String
}
