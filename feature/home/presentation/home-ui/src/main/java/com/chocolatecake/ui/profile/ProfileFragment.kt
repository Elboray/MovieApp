package com.chocolatecake.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chocolatecake.bases.BaseFragment
import com.chocolatecake.ui.home.R
import com.chocolatecake.ui.home.databinding.FragmentProfileBinding
import com.chocolatecake.viewmodel.profile.ProfileUIState
import com.chocolatecake.viewmodel.profile.ProfileUiEvent
import com.chocolatecake.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileUIState, ProfileUiEvent>() {

    override val layoutIdFragment: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.FavoriteEvent -> TODO()
            ProfileUiEvent.LogoutEvent -> {
                showSnackBar("Logout!")
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
            }

            ProfileUiEvent.MyListsEvent -> TODO()
            ProfileUiEvent.PopcornPuzzlesEvent -> {
                 findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
            }

            ProfileUiEvent.RatingEvent -> TODO()
            ProfileUiEvent.ThemeEvent -> TODO()
            ProfileUiEvent.WatchHistoryEvent -> TODO()
            ProfileUiEvent.WatchlistEvent -> TODO()
        }

    }
}