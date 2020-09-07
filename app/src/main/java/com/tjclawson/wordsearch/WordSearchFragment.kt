package com.tjclawson.wordsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_word_search.*

class WordSearchFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        grid_view_word_search.numColumns = gameViewModel.gridSizeToSet

        // init adapter and pass onItemClicked listener with lambda expression
        val wordSearchAdapter = WordSearchAdapter(requireActivity()) { p0, p1->
            val message: String = if (gameViewModel.checkIfValidMove(p0, p1)) {
                getString(R.string.word_found)
            } else {
                getString(R.string.not_word_found)
            }
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }

        grid_view_word_search.adapter = wordSearchAdapter

        // Set observers
        gameViewModel.currentGameList.observe(viewLifecycleOwner, Observer {
            wordSearchAdapter.submitList(it)
        })

        gameViewModel.wordsFoundCount.observe(viewLifecycleOwner, Observer {
            text_view_words_found_count.text = it.toString()
        })

        gameViewModel.gameWon.observe(viewLifecycleOwner, Observer {
            if (it) {
                onGameWon()
            }
        })

        gameViewModel.wordsLeftCount.observe(viewLifecycleOwner, Observer {
            text_view_words_left_count.text = it.toString()
        })
    }

    private fun onGameWon() {
        gameViewModel.onGameWon()
        button_new_game.visibility = View.VISIBLE
        button_new_game.setOnClickListener {
            gameViewModel.initNewGame()
            button_new_game.visibility = View.GONE
        }
    }
}
