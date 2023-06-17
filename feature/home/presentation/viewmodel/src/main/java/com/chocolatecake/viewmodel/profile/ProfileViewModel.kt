package com.chocolatecake.viewmodel.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.chocolatecake.bases.BaseViewModel
import com.chocolatecake.usecase.profile.GetAccountDetailsUseCase
import com.chocolatecake.usecase.profile.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val profileUiMapper: ProfileUiMapper
) : BaseViewModel<ProfileUIState, ProfileUiEvent>(ProfileUIState()), ProfileListener {

    init {
        getAccountDetails()
    }

    private fun getAccountDetails() {
        viewModelScope.launch {
            val profileEntity = profileUiMapper.map(getAccountDetailsUseCase())
            _state.update {
                it.copy(
                    username = profileEntity.username,
                    avatarUrl = profileEntity.avatarUrl,
                    error = null,
                    isLogout = false
                )
            }
        }
    }

    override fun onClickFavorite() {
        sendEvent(ProfileUiEvent.FavoriteEvent)
    }

    override fun onClickWatchlist() {
        sendEvent(ProfileUiEvent.WatchlistEvent)
    }

    override fun onClickWatchHistory() {
        sendEvent(ProfileUiEvent.WatchHistoryEvent)
    }

    override fun onClickMyLists() {
        sendEvent(ProfileUiEvent.MyListsEvent)
    }

    override fun onClickRating() {
        sendEvent(ProfileUiEvent.RatingEvent)
    }

    override fun onClickPopcornPuzzles() {
        sendEvent(ProfileUiEvent.PopcornPuzzlesEvent)
    }

    override fun onClickTheme() {
        sendEvent(ProfileUiEvent.ThemeEvent)
    }

    override fun onClickLogout() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLogout = true
            ) }
                if (_state.value.isLogout){
                    Log.d("123123123", "onClickLogout: ${_state.value.isLogout}")
                    logoutUseCase()
                    sendEvent(ProfileUiEvent.LogoutEvent)
                }

        }
    }
}