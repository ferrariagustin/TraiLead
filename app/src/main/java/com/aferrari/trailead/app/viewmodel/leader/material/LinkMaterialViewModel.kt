package com.aferrari.trailead.app.viewmodel.leader.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.viewmodel.StatusErrorType
import com.aferrari.trailead.viewmodel.StatusUpdateInformation
import kotlinx.coroutines.launch

class LinkMaterialViewModel(private val homeViewModel: LeaderViewModel) : ViewModel() {

    private val materialRepository = homeViewModel.materialRepository

    private val urlRegex =
        Regex("^(http|https)://[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(/.*)?\$")


    var linkSelected: Link? = null

    val titleLinkInput = MutableLiveData<String>()
    val linkInput = MutableLiveData<String>()

    val status = MutableLiveData<StatusUpdateInformation>()

    lateinit var errorType: StatusErrorType

    init {
        initStatus()
        linkSelected = homeViewModel.materialSelected as? Link
    }

    private fun initStatus() {
        status.value = StatusUpdateInformation.NONE
        errorType = StatusErrorType.NONE
    }

    fun saveLink() {
        initStatus()
        if (isEmptyOrNull()) {
            errorType = StatusErrorType.EMPTY
            status.value = StatusUpdateInformation.FAILED
            return
        }
        if (!isValidLink(linkInput.value)) {
            errorType = StatusErrorType.INVALID
            status.value = StatusUpdateInformation.FAILED
            return
        }
        if (homeViewModel.categorySelected == null) {
            status.value = StatusUpdateInformation.FAILED
            return
        }
        saveLinkToDB(
            titleLinkInput.value.toString(),
            linkInput.value.toString(),
            homeViewModel.categorySelected!!.id,
            homeViewModel.getLeaderId()
        )
    }

    private fun isEmptyOrNull() =
        titleLinkInput.value.isNullOrEmpty() || linkInput.value.isNullOrEmpty()

    /**
     * Update title and url to link.
     */
    fun updateLink(newTitle: String, newLink: String) {
        initStatus()
        if (newTitle.isNullOrEmpty() and newLink.isNullOrEmpty()) {
            status.value = StatusUpdateInformation.SUCCESS
            return
        }
        if (newLink.isNotEmpty() and !isValidLink(newLink)) {
            status.value = StatusUpdateInformation.FAILED
            return
        }
        updateNewLink(newTitle, newLink)
    }

    private fun updateNewLink(newTitle: String, newLink: String) {
        viewModelScope.launch {
            linkSelected?.let { link ->
                if (newTitle.isNotEmpty()) {
                    materialRepository.updateTitleLink(link.id, newTitle)
                }
                if (newLink.isNotEmpty()) {
                    materialRepository.updateUrlLink(link.id, newLink)
                }
                status.value = StatusUpdateInformation.SUCCESS
            }
        }
    }

    private fun saveLinkToDB(title: String, link: String, id: Int, leaderId: Int) {
        viewModelScope.launch {
            materialRepository.insertLink(
                Link(
                    IntegerUtils().createObjectId(),
                    title,
                    link,
                    id,
                    leaderId
                )
            )
            status.value = StatusUpdateInformation.SUCCESS
            return@launch
        }
    }

    private fun isValidLink(link: String?): Boolean = urlRegex.matches(link.toString())

}