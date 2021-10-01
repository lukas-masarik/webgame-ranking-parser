package services.processors

import dto.Epoch
import services.inputreaders.api.InputReader

abstract class AbstractRankedLandProcessor(
    protected val inputReader: InputReader,
) {

    fun filterEpochs(epochs: List<Epoch>, epochStart: Int? = null, epochEnd: Int? = null): List<Epoch> {
        val epochsWithoutFirstN = if (epochStart != null) epochs.filter { it.number >= epochStart } else epochs
        return if (epochEnd != null) epochsWithoutFirstN.filter { it.number <= epochEnd } else epochsWithoutFirstN
    }
}