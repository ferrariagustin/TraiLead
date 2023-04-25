package com.aferrari.trailead.home.viewmodel

import com.aferrari.login.data.material.YouTubeVideo

interface IMaterial {
    /**
     * is selected Material clicked
     */
    fun setSelectedMaterial(youTubeVideo: YouTubeVideo)

    /**
     * Delete material selected
     */
    fun deleteMaterialSelected()
}
