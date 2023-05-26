package com.aferrari.trailead.app.viewmodel.leader.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.UrlUtils
import com.aferrari.trailead.domain.models.YouTubeVideo
import com.aferrari.trailead.viewmodel.StatusUpdateInformation
import kotlinx.coroutines.launch

class EditMaterialViewModel(private val homeViewModel: LeaderViewModel) : ViewModel() {

    val statusUpdateEditMaterial = MutableLiveData<StatusUpdateInformation>()
    var youTubeVideoSelected: YouTubeVideo?

    init {
        statusUpdateEditMaterial.value = StatusUpdateInformation.NONE
        youTubeVideoSelected = homeViewModel.materialSelected as? YouTubeVideo
    }


    /**
     * The url and title is updated.
     * If the url or title is empty, it doesn't updated but if the url is mal formatted, the user
     * is notified
     * @param newUrl new url to updated
     * @param newTitle new title to updated
     */
    fun editVideo(newUrl: String, newTitle: String) {
        (youTubeVideoSelected as? YouTubeVideo)?.let {

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
                updateUrlVideo(it, youtubeId)
            }
            if (newTitle.isNotEmpty()) {
                updateTitleVideo(it, newTitle)
            }
            statusUpdateEditMaterial.value = StatusUpdateInformation.SUCCESS
            return
        }

    }


    private fun updateTitleVideo(youTubeVideoSelected: YouTubeVideo, newTitle: String) =
        viewModelScope.launch {
            homeViewModel.materialRepository.updateTitleYoutubeVideo(
                youTubeVideoSelected.id,
                newTitle
            )
        }

    private fun updateUrlVideo(
        youTubeVideoSelected: YouTubeVideo,
        youtubeId: String
    ) = viewModelScope.launch {
        homeViewModel.materialRepository.updateUrlYoutubeVideo(youTubeVideoSelected.id, youtubeId)
    }
}