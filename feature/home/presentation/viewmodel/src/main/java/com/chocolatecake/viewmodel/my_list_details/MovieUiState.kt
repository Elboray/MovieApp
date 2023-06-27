package com.chocolatecake.viewmodel.my_list_details

data class MovieUiState(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val genres: String,
    val year: String,
    val rating: Double,
){
    val rate: Double
        get() = (rating * 100.0).toInt() / 100.0
}