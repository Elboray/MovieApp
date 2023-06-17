package com.chocolatecake.ui.type_game

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chocolatecake.bases.BaseFragment
import com.chocolatecake.bases.BaseViewModel
import com.chocolatecake.ui.trivia.R
import com.chocolatecake.ui.trivia.databinding.FragmentTypeGamesBinding
import com.chocolatecake.viewmodel.game_type.GameTypeUIEvent
import com.chocolatecake.viewmodel.game_type.GameTypeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TypeGameFragment: BaseFragment<FragmentTypeGamesBinding,Unit,GameTypeUIEvent>() {
    override val layoutIdFragment: Int = R.layout.fragment_type_games
    override val viewModel: BaseViewModel<Unit, GameTypeUIEvent> by viewModels<GameTypeViewModel>()


    override fun onEvent(event: GameTypeUIEvent) {
        when(event){
            GameTypeUIEvent.NavigateToMemorizeBoard -> {
                findNavController().navigate(TypeGameFragmentDirections.actionTypeGameFragmentToMemorizeGameFragment())
            }
            GameTypeUIEvent.NavigateToMovieGame -> {
                findNavController().navigate(TypeGameFragmentDirections.actionTypeGameFragmentToMovieGuessingFragment())
            }
            GameTypeUIEvent.NavigateToPeopleGame -> {
                findNavController().navigate(TypeGameFragmentDirections.actionTypeGameFragmentToPeopleGuessingFragment())
            }
            GameTypeUIEvent.NavigateToTvGame -> {
                showSnackBar("Not Supported Yet !🤷‍")
               // findNavController().navigate(TypeGameFragmentDirections.actionTypeGameFragmentToTvShowGuessingFragment())
            }
            is GameTypeUIEvent.ShowSnackbar -> {
                showSnackBar("Coming Soon!!!")
            }
        }
    }
}