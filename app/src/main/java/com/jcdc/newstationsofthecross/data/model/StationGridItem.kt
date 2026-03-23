package com.jcdc.newstationsofthecross.data.model

import java.io.Serializable

sealed class StationGridItem : Serializable {
    abstract val title: String

    data class IntroductionItem(override val title: String, val introduction: Introduction) : StationGridItem()

    data class StationItem(val station: Station) : StationGridItem() {
        override val title: String = station.title
    }

    data class ConclusionItem(val conclusion: Conclusion) : StationGridItem() {
        override val title: String = conclusion.title
    }
}
