package com.aferrari.trailead.home.viewmodel.leader.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.trailead.home.Utils.UrlUtils
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import kotlinx.coroutines.launch

class EditMaterialViewModel(private val homeViewModel: LeaderViewModel) : ViewModel() {

    val statusUpdateEditMaterial = MutableLiveData<StatusUpdateInformation>()
    var youTubeVideoSelected: YouTubeVideo?

    init {
        statusUpdateEditMaterial.value = StatusUpdateInformation.NONE
        youTubeVideoSelected = homeViewModel.youTubeVideoSelected
    }


    /**
     * The url and title is updated.
     * If the url or title is empty, it doesn't updated but if the url is mal formatted, the user
     * is notified
     * @param newUrl new url to updated
     * @param newTitle new title to updated
     */
    fun editMaterial(newUrl: String, newTitle: String) {
        youTubeVideoSelected?.let {

            if (newUrl.isNotEmpty()) {
                if (!UrlUtils().isYoutubeUrl(newUrl)) {
                    statusUpdateEditMaterial.value = StatusUpdateInformation.FAILED
                    return
                }
                val youtubeId: String? = UrlUtils().getYouTubeId(newUrl)
                if (youtubeId.isNullOrEmpty()) {
                    statusUpdateEditMaterial.value = StatusUpdateInformation.FAILED
                    return
                }
                updateUrlMaterial(it, youtubeId)
            }
            if (newTitle.isNotEmpty()) {
                updateTitleMaterial(it, newTitle)
            }
            statusUpdateEditMaterial.value = StatusUpdateInformation.SUCCESS
            return
        }

    }


    private fun updateTitleMaterial(youTubeVideoSelected: YouTubeVideo, newTitle: String) =
        viewModelScope.launch {
            homeViewModel.repository.updateTitleMaterial(youTubeVideoSelected.id, newTitle)
        }

    private fun updateUrlMaterial(
        youTubeVideoSelected: YouTubeVideo,
        youtubeId: String
    ) = viewModelScope.launch {
        homeViewModel.repository.updateUrlMaterial(youTubeVideoSelected.id, youtubeId)
    }
}