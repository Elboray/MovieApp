package com.chocolatecake.viewmodel.search

import com.chocolatecake.viewmodel.common.model.MovieHorizontalUIState
import com.chocolatecake.viewmodel.common.model.PeopleUIState

sealed class SearchItem(val type: SearchItemType){
    data class MediaItem(val movieHorizontalUIState: List<MovieHorizontalUIState>): SearchItem(SearchItemType.MEDIA)
    data class PeopleItem(val peopleItem: List<PeopleUIState>): SearchItem(SearchItemType.PEOPLE)
}
enum class SearchItemType{
    MEDIA,
    PEOPLE
}

