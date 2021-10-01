package services.inputreaders.api

import enums.*
import services.processors.api.Processor

interface InputReader {
    fun selectProcessorFromInput(): Processor
    fun selectOrderDirectionFromInput(): EOrderDirection
    fun selectOrderAttributeFromInput(): EOrderAttribute
    fun selectReturnCountFromInput(): Int
    fun selectStateSystemFromInput(): EStateSystem
    fun selectStartEpochFromInput(): Int?
    fun selectEndEpochFromInput(): Int?
    fun selectStartRankFromInput(): Int?
    fun selectEndRankFromInput(): Int?
    fun selectAggregatingParameterFromInput(): EAggregatingParameter
    fun selectGroupingParameterFromInput(): EGroupingParameter
}
