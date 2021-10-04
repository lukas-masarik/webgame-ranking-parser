package app

import services.inputreaders.SimpleInputReader

object WebgameRankingParser {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputReader = SimpleInputReader()
        val processor = inputReader.selectProcessorFromInput()
        processor.process()
    }
}
