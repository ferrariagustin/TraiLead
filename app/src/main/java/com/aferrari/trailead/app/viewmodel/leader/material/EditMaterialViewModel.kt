package com.aferrari.trailead.app.viewmodel.leader.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.UrlUtils
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.common_enum.toStatusUpdateInformation
import com.aferrari.trailead.domain.models.YouTubeVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {
            youTubeVideoSelected?.let {
                if (newUrl.isEmpty() || newTitle.isEmpty()) {
                    statusUpdateEditMaterial.value = StatusUpdateInformation.FAILED
                    return@launch
                }
                if (!UrlUtils().isYoutubeUrl(newUrl)) {
                    statusUpdateEditMaterial.value = StatusUpdateInformation.FAILED
                    return@launch
                }
                val youtubeUrl: String? = UrlUtils().getYouTubeId(newUrl)
                if (youtubeUrl.isNullOrEmpty()) {
                    statusUpdateEditMaterial.value = StatusUpdateInformation.FAILED
                    return@launch
                }
                val result = updateUrlVideo(it, youtubeUrl)
                if (result == StatusUpdateInformation.SUCCESS) {
                    statusUpdateEditMaterial.value = updateTitleVideo(it, newTitle)
                } else {
                    statusUpdateEditMaterial.value = result
                }
            }

        }

    }


    private suspend fun updateTitleVideo(youTubeVideoSelected: YouTubeVideo, newTitle: String) =
        withContext(Dispatchers.IO) {
            homeViewModel.materialRepository.updateTitleYoutubeVideo(
                youTubeVideoSelected.id,
                newTitle
            ).toStatusUpdateInformation()
        }

    private suspend fun updateUrlVideo(
        youTubeVideoSelected: YouTubeVideo,
        youtubeUrl: String
    ) = withContext(Dispatchers.IO) {
        homeViewModel.materialRepository.updateUrlYoutubeVideo(youTubeVideoSelected.id, youtubeUrl)
            .toStatusUpdateInformation()
    }
}