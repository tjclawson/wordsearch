package com.tjclawson.wordsearch


class WordSearchGame(private val words: ArrayList<String>,
                     private val gridSize: Int) {

    private val numberOfWords = words.size
    private val winningMoves = HashSet<Pair<Int, Int>>()
    private val grid = arrayListOf<LetterNode>()
    private val foundWordsSet = HashSet<Pair<Int, Int>>()

    init {
        populateEmptyGrid()
        generate()
    }

    /**
     * Checks if selected nodes create a valid word that has not been found
     * Sets nodes to be highlighted if so
     */
    fun checkVector(p0: Int, p1: Int): Boolean {
        if ((winningMoves.contains(Pair(p0, p1)) || winningMoves.contains(Pair(p1, p0))) &&
            (!foundWordsSet.contains(Pair(p0, p1)) && !foundWordsSet.contains(Pair(p1, p0)))) {
            foundWordsSet.add(Pair(p0, p1))
            setLettersToBeHighlighted(p0, p1)
            return true
        }
        return false
    }

    /**
     * Loops from smaller to bigger position
     * Determine increment/decrement to next node based on whether the node is vertical, horizontal,
     * positive slope or negative slope
     */
    private fun setLettersToBeHighlighted(p0: Int, p1: Int) {
        val bigger = p0.coerceAtLeast(p1)
        val smaller = p0.coerceAtMost(p1)
        var current = smaller
        var adjustment = 0
        when {
            // horizontal
            bigger / gridSize == smaller / gridSize -> { adjustment = 1 }
            // vertical
            smaller % gridSize == bigger % gridSize -> { adjustment = gridSize }
            // neg slope
            bigger % gridSize > smaller % gridSize -> { adjustment = gridSize + 1 }
            // pos slope
            bigger % gridSize < smaller % gridSize -> { adjustment = gridSize - 1 }
        }
        while (current <= bigger) {
            grid[current].isFound = true
            current += adjustment
        }
    }

    fun isGameWon(): Boolean {
        return foundWordsSet.size == numberOfWords
    }

    fun getWordsFoundCount() = foundWordsSet.size

    fun getWordsLeftCount() = words.size - foundWordsSet.size

    fun getGrid() = grid

    private fun populateEmptyGrid() {
        for (i in 0 until gridSize * gridSize) {
            grid.add(LetterNode(false, '_'))
        }
    }

    /**
     * Places words on grid randomly
     * Finds an appropriate start index based on word length and direction
     * Tries to place word, if not successful, picks a new starting index and repeats
     */
    private fun generate() {
        for (word in words) {
            var didPlaceWord = false

            while (!didPlaceWord) {

                var increment = 1
                var currentIndex = 0

                // determine start index and increment
                when ((0..3).random()) {
                    0 -> {
                        currentIndex = (0..gridSize - word.length).random()
                        + (gridSize * (0 until gridSize).random())
                    }
                    1 -> {
                        increment = gridSize
                        currentIndex = (0 until grid.size - (increment * (word.length - 1))).random()
                    }
                    2 -> {
                        increment = (gridSize - 1) * -1
                        currentIndex = ((gridSize - (0 until gridSize - (word.length - 1))
                            .random() - 1) * gridSize) + (0 until gridSize - (word.length - 1)).random()
                    }
                    3 -> {
                        increment = gridSize + 1
                        currentIndex = ((0 until gridSize - (word.length - 1)).random() * gridSize)
                        + (0 until gridSize - (word.length - 1)).random()
                    }
                }

                // Set to keep track of path
                val wordPath = arrayListOf<Int>()

                // try to find path for word
                for (letter in word) {
                    if (grid[currentIndex].letter == '_' || grid[currentIndex].letter == letter) {
                        wordPath.add(currentIndex)
                        currentIndex += increment
                    } else {
                        wordPath.clear()
                        break
                    }
                }

                // write word to grid and add winning move
                if (wordPath.size == word.length) {
                    didPlaceWord = true
                    wordPath.forEachIndexed { wordIndex, gridIndex ->
                        grid[gridIndex].letter = word[wordIndex]
                    }
                    winningMoves.add(Pair(wordPath[0], wordPath[wordPath.size - 1]))
                }
            }
        }

        // populate empty nodes with random letters
        val allowedChars = ('a'..'z')

        for (letterNode in grid) {
            if (letterNode.letter == '_') letterNode.letter = allowedChars.random()
        }
    }

    fun gameWon() {
        val wonMessage = "YOU WON!!!"
        var pointer = 0
        for (letterNode in grid) {
            letterNode.letter = wonMessage[pointer]
            letterNode.isFound = false
            pointer++
            if (pointer == wonMessage.length - 1) pointer = 0
        }
    }
}