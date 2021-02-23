package com.example.jokerway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.jokerway.R

class FinishFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val arg = arguments!!
        val resultTextImage = activity?.findViewById<ImageView>(R.id.result_text_image)
        if (arg.getBoolean("win")){
            resultTextImage?.setImageResource(R.drawable.text_you_win)
        }

        val restartButton = activity?.findViewById<ImageView>(R.id.button_restart)

        restartButton?.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, GameScreenFragment())
                    .commit()
        }
    }
}