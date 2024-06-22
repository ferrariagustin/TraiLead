package com.aferrari.trailead.app.viewmodel

import com.aferrari.trailead.domain.models.Material

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
