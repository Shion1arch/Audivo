package com.mardous.booming.mvvm

import com.mardous.booming.database.PlayCountEntity

data class PlayInfoResult(
    val playCount: Int,
    val skipCount: Int,
    val lastPlayDate: Long,
    val mostPlayedTracks: List<PlayCountEntity>
)