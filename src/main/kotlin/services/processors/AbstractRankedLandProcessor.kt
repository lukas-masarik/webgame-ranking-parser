package services.processors

import dto.RankedLandsEpoch
import services.inputreaders.api.InputReader
import services.processors.api.Processor

abstract class AbstractRankedLandProcessor(
    protected val inputReader: InputReader,
) : Processor {

    fun filterEpochs(rankedLandsEpochs: List<RankedLandsEpoch>, epochStart: Int? = null, epochEnd: Int? = null): List<RankedLandsEpoch> {
        val epochsWithoutFirstN = if (epochStart != null) rankedLandsEpochs.filter { it.number >= epochStart } else rankedLandsEpochs
        return if (epochEnd != null) epochsWithoutFirstN.filter { it.number <= epochEnd } else epochsWithoutFirstN
    }

    fun filterRanks(rankedLandsEpochs: List<RankedLandsEpoch>, rankStart: Int? = null, rankEnd: Int? = null): List<RankedLandsEpoch> {
        val epochsLandsWithStartRank = if (rankStart != null) {
            rankedLandsEpochs.map { rankedLandsEpoch ->
                RankedLandsEpoch(
                    number = rankedLandsEpoch.number,
                    rankedLands = rankedLandsEpoch.rankedLands.filter { it.order >= rankStart }
                )
            }
        } else {
            rankedLandsEpochs
        }

        val epochsLandsWithEndRank = if (rankEnd != null) {
            epochsLandsWithStartRank.map { rankedLandsEpoch ->
                RankedLandsEpoch(
                    number = rankedLandsEpoch.number,
                    rankedLands = rankedLandsEpoch.rankedLands.filter { it.order <= rankEnd }
                )
            }
        } else {
            epochsLandsWithStartRank
        }

        return epochsLandsWithEndRank
    }
}