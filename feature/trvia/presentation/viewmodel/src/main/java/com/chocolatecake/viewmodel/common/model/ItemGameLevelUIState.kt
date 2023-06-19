package com.chocolatecake.viewmodel.common.model

import kotlin.math.roundToInt

data class ItemGameLevelUIState(
    val level: Int = 1,
    val questionsCount: Int = 0,
    val max: Int = 5,
    val isOpenLevel: Boolean,
) {
    val missedQuestionsCount
        get() = max - questionsCount

    val progress: Int
        get() = ((questionsCount.toDouble() / max) * 100.0).roundToInt()

    val formattedProgress
        get() = "$progress%"

    val isPassed: Boolean
        get() = progress == 100

    val title
        get() = when (level) {
            1 -> "Easy"
            2 -> "Medium"
            else -> "Hard"
        }
}
