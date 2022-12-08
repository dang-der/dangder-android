package com.viewpoint.dangder.presenter.uimodel

data class Review(
    val id : String,
    val reviewMessage: String,
    val reviewDetail: List<String>,
    val senderImageUrl : String
    )
