package com.aferrari.trailead.domain.models

import java.io.Serializable

interface Material : Serializable {
    val id: Int
    val title: String
    val url: String
    var categoryId: Int?
    val leaderMaterialId: Int
}