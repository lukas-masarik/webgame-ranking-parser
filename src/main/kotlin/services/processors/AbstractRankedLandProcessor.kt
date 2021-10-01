package services.processors

import dto.Epoch

abstract class AbstractRankedLandProcessor {

    fun filterEpochs(epochs: List<Epoch>, epochStart: Int? = null, epochEnd: Int? = null): List<Epoch> {
        val epochsWithoutFirstN = if (epochStart != null) epochs.filter { it.number >= epochStart } else epochs
        return if (epochEnd != null) epochsWithoutFirstN.filter { it.number <= epochEnd } else epochsWithoutFirstN
    }
}