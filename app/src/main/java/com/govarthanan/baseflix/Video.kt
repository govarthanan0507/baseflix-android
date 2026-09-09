package com.govarthanan.baseflix

data class Video(
    val id: Long,
    val name: String,
    val folder: String,
    val streamUrl: String?
)
