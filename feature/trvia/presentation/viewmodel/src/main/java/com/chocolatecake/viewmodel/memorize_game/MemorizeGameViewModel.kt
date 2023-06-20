package com.chocolatecake.viewmodel.memorize_game

import androidx.lifecycle.viewModelScope
import com.chocolatecake.bases.BaseViewModel
import com.chocolatecake.entities.BoardEntity
import com.chocolatecake.entities.UserEntity
import com.chocolatecake.usecase.GetCurrentUserUseCase
import com.chocolatecake.usecase.game.GetCurrentBoardUseCase
import com.chocolatecake.usecase.game.UpdateUserPointsUseCase
import com.chocolatecake.usecase.game.levelup.LevelUpMemorizeUseCase
import com.chocolatecake.viewmodel.common.model.GameType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemorizeGameViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getCurrentBoardUseCase: GetCurrentBoardUseCase,
    private val updateUserPointsUseCase: UpdateUserPointsUseCase,
    private val levelUpMemorizeUseCase: LevelUpMemorizeUseCase
) : BaseViewModel<MemorizeGameUIState, MemorizeGameUIEvent>(MemorizeGameUIState()),
    MemorizeListener {

    init {
        getData()
    }

    private fun getData() {
        tryToExecute(
            getCurrentUserUseCase::invoke,
            ::onSuccessUser,
            ::onError
        )
        tryToExecute(
            getCurrentBoardUseCase::invoke,
            ::onSuccessBoard,
            ::onError
        )
    }

    private fun onSuccessBoard(boardEntity: BoardEntity) {
        _state.update {
            val board = boardEntity.itemsEntity.toUIState()
            it.copy(
                boardSize = boardEntity.itemsEntity.size,
                CorrectPairPositions = boardEntity.pairOfCorrectPositions,
                board = board,
                initialBoard = board,
                isLoading = false,
                isError = false
            )
        }
        initTimer()
    }

    private fun onSuccessUser(user: UserEntity) {
        _state.update {
            it.copy(
                level = user.memorizeGameLevel,
                points = user.totalPoints,
                isError = false,
                isLoading = false
            )
        }
    }


    private var timerJob: Job? = null
    private fun initTimer() {
        _state.update { it.copy(countDownTimer = 30) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                if (_state.value.countDownTimer == 0) {
                    onUserLose()
                    timerJob = null
                    cancel()
                }
                delay(1000)
                _state.update { it.copy(countDownTimer = it.countDownTimer - 1) }
            }
        }
    }

    private fun onUserLose() {
        sendEvent(MemorizeGameUIEvent.NavigateToLoserScreen)
    }

    override fun onItemClick(itemGameImageUiState: ItemGameImageUiState) {
        toggleGameItem(itemGameImageUiState)
    }

    private fun toggleGameItem(itemGameImageUiState: ItemGameImageUiState) {
        viewModelScope.launch {
            val modifyItem =
                itemGameImageUiState.copy(isSelected = !itemGameImageUiState.isSelected)
            val modifyBoard = (_state.value.board - itemGameImageUiState).toMutableList()
            modifyBoard.add(modifyItem.position, modifyItem)
            _state.update { it.copy(board = modifyBoard) }
            delay(300)
            saveUserPosition(itemGameImageUiState.position)
        }
    }

    private fun saveUserPosition(position: Int) {
        if (state.value.firstUserPosition == null) {
            saveFirstPosition(position)
        } else {
            saveSecondPosition(position)
        }
    }

    private fun saveFirstPosition(position: Int) {
        _state.update { it.copy(firstUserPosition = position) }
    }

    private fun saveSecondPosition(position: Int) {
        val firstPosition = _state.value.firstUserPosition!!
        if (_state.value.CorrectPairPositions.equalsIgnoreOrder(Pair(firstPosition, position))) {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        secondUserPosition = position,
                        points = it.points + (it.level * 20)
                    )
                }
                updateUserPointsUseCase(_state.value.points)
                levelUpMemorizeUseCase()
                sendEvent(MemorizeGameUIEvent.NavigateToWinnerScreen(GameType.MEMORIZE))
            }
        } else {
            _state.update {
                it.copy(
                    firstUserPosition = null,
                    secondUserPosition = null,
                    board = it.initialBoard
                )
            }
        }
    }

    private fun onError(throwable: Throwable) {
        _state.update { it.copy(isError = true) }
        sendEvent(MemorizeGameUIEvent.ShowSnackbar(throwable.message.toString()))
    }
}

private fun <A : Comparable<A>> Pair<A, A>.equalsIgnoreOrder(pair: Pair<A, A>): Boolean {
    return listOf(first, second).sorted() == setOf(pair.first, pair.second).sorted()
}


private fun List<BoardEntity.ItemBoardEntity>.toUIState(): List<ItemGameImageUiState> {
    return this.map {
        ItemGameImageUiState(
            imageUrl = it.imageUrl,
            position = it.position,
        )
    }
}
