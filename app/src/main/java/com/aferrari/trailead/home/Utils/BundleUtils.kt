package com.aferrari.trailead.home.Utils

import android.os.Bundle

class BundleUtils {

    fun getBundleTab(tabId: Int): Bundle = Bundle().apply { this.putInt(StringUtils.TAB_ID, tabId) }
}