package com.viewpoint.dangder.util.socket

import com.viewpoint.dangder.domain.entity.Dog

data class ContentsData(
    val meetAt: String? = null,
    val message: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
)

data class OnData(
    val type: String? = null,
    val data: ContentsData? = null,
    val dog: Dog? = null
)

data class EmitData(
    val type: String? = null,
    val data: ContentsData? = null,
    val roomId: String? = null,
    val dog: Dog? = null
)
