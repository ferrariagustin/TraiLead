package com.aferrari.trailead.home.viewmodel

import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.login.data.material.dao.Material

interface IMaterial {
    /**
     * is selected Material clicked
     */
    fun setSelectedMaterial(material: Material)

    /**
     * Delete material selected
     */
    fun deleteMaterialSelected()
}
