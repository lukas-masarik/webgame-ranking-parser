package me.masi

import me.masi.enums.EAppTrigger
import me.masi.services.inputreaders.SimpleInputReader

object WebgameRankingParser {
    @JvmStatic
    fun main(args: Array<String>) {
        val appTrigger = getApplicationTrigger()
        val inputReader = SimpleInputReader(appTrigger)
        val processor = inputReader.selectProcessorFromInput()
        processor.process()
    }

    private fun getApplicationTrigger(): EAppTrigger {
        val resourceUrl = WebgameRankingParser::class.java.getResource("WebgameRankingParser.class")
        return if (resourceUrl == null) {
            throw IllegalStateException("Unable to find initial class file.")
        } else if (resourceUrl.toString().startsWith("file")) {
            EAppTrigger.IDE
        } else if (resourceUrl.toString().startsWith("jar")) {
            EAppTrigger.JAR
        } else {
            throw IllegalStateException("Unknown file prefix.")
        }
    }
}
