package com.tjclawson.wordsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    var isGameInProgress = false
    var currentGameList = MutableLiveData<ArrayList<LetterNode>>()
    var wordsFoundCount = MutableLiveData<Int>()
    var wordsLeftCount = MutableLiveData<Int>()
    var gameWon = MutableLiveData<Boolean>()
    var gridSizeToSet = 0
    private lateinit var currentGame: WordSearchGame

    fun initNewGame(words: ArrayList<String>
                    = arrayListOf("objectivec", "variable", "mobile", "kotlin", "swift", "java"),
                    gridSize: Int = 10) {
        currentGame = WordSearchGame(words, gridSize)
        updateLiveData()
        isGameInProgress = true
        gridSizeToSet = gridSize
    }

    fun checkIfValidMove(p0: Int, p1: Int): Boolean {
        if (currentGame.checkVector(p0, p1)) {
            updateLiveData()
            return true
        } else {
            return false
        }
    }

    fun onGameWon() {
        currentGame.gameWon()
        isGameInProgress = false
        updateLiveData()
    }

    private fun updateLiveData() {
        currentGameList.postValue(currentGame.getGrid())
        wordsFoundCount.postValue(currentGame.getWordsFoundCount())
        wordsLeftCount.postValue(currentGame.getWordsLeftCount())
        gameWon.postValue(currentGame.isGameWon())
    }
}