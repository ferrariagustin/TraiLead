package com.aferrari.trailead.home.viewmodel

import com.aferrari.login.data.Material

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
