package com.aferrari.trailead.home.viewmodel.leader.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.material.Link
import com.aferrari.trailead.home.viewmodel.StatusErrorType
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import kotlinx.coroutines.launch

class LinkMaterialViewModel(private val homeViewModel: LeaderViewModel) : ViewModel() {

    private val materialRepository = homeViewModel.materialRepository

    private val urlRegex =
        Regex("^(https://|http://)?[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*([/?]\\S*)?\$")

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
        if (titleLinkInput.value.isNullOrEmpty() || linkInput.value.isNullOrEmpty()) {
            errorType = StatusErrorType.EMPTY
            status.value = StatusUpdateInformation.FAILED
            return
        }
        if (!isValidLink()) {
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

    private fun saveLinkToDB(title: String, link: String, id: Int, leaderId: Int) {
        viewModelScope.launch {
            materialRepository.insertLink(Link(0, title, link, id, leaderId))
            status.value = StatusUpdateInformation.SUCCESS
            return@launch
        }
    }

    private fun isValidLink(): Boolean = urlRegex.matches(linkInput.value.toString())

}