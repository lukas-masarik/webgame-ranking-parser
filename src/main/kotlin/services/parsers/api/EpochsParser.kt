package services.parsers.api

import dto.Epoch

interface EpochsParser {
    fun parse(): List<Epoch>
}