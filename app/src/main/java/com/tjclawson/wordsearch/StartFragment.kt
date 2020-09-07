package com.tjclawson.wordsearch

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_start_screen.*

class StartFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        container?.removeAllViews()
        return inflater.inflate(R.layout.fragment_start_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        if (gameViewModel.isGameInProgress) {
            button_resume.visibility = View.VISIBLE
        }

        button_start.setOnClickListener {
            if (gameViewModel.isGameInProgress) {
                displayAlertDialog()
            } else {
                gameViewModel.initNewGame()
                navigateToWordSearchFragment()
            }
        }

        button_resume.setOnClickListener {
            navigateToWordSearchFragment()
        }
    }

    private fun displayAlertDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.game_in_progress_message)
                .setTitle(R.string.game_in_progress_title)
                .setPositiveButton(R.string.yes) {_, _ ->
                    gameViewModel.initNewGame()
                    navigateToWordSearchFragment()
                }
                .setNegativeButton(R.string.no) {_, _ -> }
                .create()
                .show()
        }
    }

    private fun navigateToWordSearchFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frame_container, WordSearchFragment())
            .addToBackStack(getString(R.string.start_fragment_tag))
            .commit()
    }
}
