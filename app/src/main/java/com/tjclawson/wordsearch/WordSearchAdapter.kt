package com.tjclawson.wordsearch

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WordSearchAdapter(private val context: Context,
                        private val onItemClicked: (p0: Int, p1: Int) -> Unit): BaseAdapter() {

    var hasSelected = false
    var selectedPosition = -1
    private val gameList = ArrayList<LetterNode>()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        // initialize view
        val textView = TextView(context)
        textView.textSize = 20F
        textView.text = gameList[position].letter.toString()
        textView.setTypeface(null, Typeface.BOLD)
        textView.gravity = Gravity.CENTER
        textView.background = context.getDrawable(R.drawable.default_circle_text_view)

        if (gameList[position].isFound) {
            textView.background = context.getDrawable(R.drawable.found_circle_text_view)
        }

        // set listener to change UI and invoke onItemClicked
        textView.setOnClickListener {
            if (hasSelected) {
                if (position == selectedPosition) {
                    if (gameList[position].isFound) {
                        textView.background = context.getDrawable(R.drawable.found_circle_text_view)
                    } else {
                        textView.background = context.getDrawable(R.drawable.default_circle_text_view)
                    }
                } else {
                    notifyDataSetChanged()
                    onItemClicked.invoke(selectedPosition, position)
                }
                hasSelected = false
                selectedPosition = -1
            } else {
                hasSelected = true
                selectedPosition = position
                textView.background = context.getDrawable(R.drawable.selected_circle_text_view)
            }
        }
        return textView
    }

    // unused
    override fun getItem(p0: Int): Any? {
        return null
    }

    // unused
    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return gameList.size
    }

    fun submitList(newList: ArrayList<LetterNode>){
        gameList.clear()
        gameList.addAll(newList)
        notifyDataSetChanged()
    }
}