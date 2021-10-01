package services.processors

import dto.RankedLandsEpoch
import services.inputreaders.api.InputReader
import services.processors.api.Processor

abstract class AbstractRankedLandProcessor(
    protected val inputReader: InputReader,
) : Processor {

    fun filterEpochs(rankedLandsEpoches: List<RankedLandsEpoch>, epochStart: Int? = null, epochEnd: Int? = null): List<RankedLandsEpoch> {
        val epochsWithoutFirstN = if (epochStart != null) rankedLandsEpoches.filter { it.number >= epochStart } else rankedLandsEpoches
        return if (epochEnd != null) epochsWithoutFirstN.filter { it.number <= epochEnd } else epochsWithoutFirstN
    }
}