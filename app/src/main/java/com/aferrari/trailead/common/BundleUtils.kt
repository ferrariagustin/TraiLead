package com.aferrari.trailead.common

import android.os.Bundle

class BundleUtils {

    fun getBundleTab(tabId: Int): Bundle = Bundle().apply { this.putInt(StringUtils.TAB_ID, tabId) }
}