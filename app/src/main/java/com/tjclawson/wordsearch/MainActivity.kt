package com.tjclawson.wordsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        if (viewModel.isGameInProgress) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame_container, WordSearchFragment(), null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame_container, StartFragment(), null)
                .commit()
        }
    }
}
