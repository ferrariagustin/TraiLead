package com.aferrari.trailead.app.ui.leader.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.configurer.NetworkManager
import com.aferrari.trailead.app.ui.RefreshListener
import com.aferrari.trailead.app.ui.leader.home.adapter.MaterialListAdapter.Companion.LINK
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.StringUtils.ERROR_WEBVIEW_PATH
import com.aferrari.trailead.databinding.LinkFragmentBinding


class LinkFragment : Fragment(), RefreshListener {

    private lateinit var binding: LinkFragmentBinding

    private val viewmodel: LeaderViewModel by activityViewModels()

    private val link: String by lazy {
        arguments?.getString(LINK) ?: ERROR_WEBVIEW_PATH
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.link_fragment, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupWebView()
    }

    private fun setupWebView() {
        if (!NetworkManager.isOnline()) {
            binding.wvLinkId.loadUrl(ERROR_WEBVIEW_PATH)
            return
        }
        binding.wvLinkId.loadUrl(link)
        refresh()
    }

    private fun setupToolbar() {
        binding.linkToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    override fun refresh() {
        Log.e("LinkFragment", "init refresh")
        viewmodel.refresh.observe(viewLifecycleOwner) {
            Log.e("LinkFragment", "refresh: $it")
            if (it) binding.wvLinkId.loadUrl(link)
        }
    }
}
