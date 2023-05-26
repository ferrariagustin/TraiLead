package com.aferrari.trailead.common

import java.util.regex.Pattern

class UrlUtils {
    fun isYoutubeUrl(youTubeURl: String): Boolean {
        val pattern = Regex("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+")
        return youTubeURl.isNotEmpty() && youTubeURl.matches(pattern)
    }

    fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            null
        }
    }
}