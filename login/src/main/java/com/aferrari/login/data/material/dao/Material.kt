package com.aferrari.login.data.material.dao

import java.io.Serializable

interface Material : Serializable {
    val id: Int
    val title: String
    val url: String
    var categoryId: Int?
    val leaderMaterialId: Int
}