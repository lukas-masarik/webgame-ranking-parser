package services.inputreaders.api

import services.processors.api.Processor

interface InputReader {
    fun selectProcessorFromInput(): Processor
}