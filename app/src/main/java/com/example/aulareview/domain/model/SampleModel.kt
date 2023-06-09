package com.example.aulareview.domain.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleModel(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
