package com.example.jokerway.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.jokerway.R
import com.example.jokerway.fragments.asist.*
import java.util.*
import kotlin.math.abs

class GameScreenFragment : Fragment(), View.OnTouchListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gamescreen, container, false)
    }

    private var endGame = false

    private lateinit var gridLayout: GridLayout
    private val levelGenerator = LevelGenerator()

    private lateinit var tableArray: Array<Array<Boolean>>
    private val left = Left()
    private val up = Up()
    private val right = Right()
    private val down = Down()

    private lateinit var asteroid: ImageView

    private var actualRow = 0
    private var actualColumn = 0

    private var side: Side = up
    private var score = 0

    val handler = Handler()
    private var timer = Timer()
    private val touchCoordinates = TouchCoordinates()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val button = activity?.findViewById<Button>(R.id.button)
//        button?.setOnClickListener(this)
        gridLayout = activity!!.findViewById(R.id.grid_layout)
        gridLayout.setOnTouchListener(this)
        tableArray = levelGenerator.generateMap()
        drawLevel(tableArray)
        asteroid = activity!!.findViewById(R.id.asteroid)
        handler.post { spawnAsteroid() }
    }

//    override fun onClick(v: View?) {
//        if (v != null) {
//            when (v.id) {
//                R.id.button -> restart()
//            }
//        }
//    }

    override fun onResume() {
        if (!endGame) startGame()
        super.onResume()
    }

    override fun onPause() {
        stopGame()
        super.onPause()
    }

    private fun startGame() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post { move() }
            }
        }, 2000, 40)

        handler.postDelayed({
            asteroid.setImageResource(R.drawable.aster)
            asteroid.animate()?.apply {
                scaleX(2.0f)
                scaleY(2.0f)
            }
            side.turn()
        }, 1000)
    }

    private fun turnAsteroid() {
        asteroid.animate().apply {
            duration = 50
            rotation(side.turn())
        }
    }

    private fun stopGame() {
        timer.cancel()
        timer = Timer()
    }

    private fun drawLevel(tableArray: Array<Array<Boolean>>) {
        for ((row, rowArray) in tableArray.withIndex()) {
            for ((column, value) in rowArray.withIndex()) {
                if (value) {
                    gridLayout[row * gridLayout.columnCount + column].visibility = View.VISIBLE
                    score++
                    Log.i("aaa", "score = $score")
                }
            }
        }
    }

//    private fun restart() {
//        score = 0
//        stopGame()
//
//        for (i in 0 until gridLayout.columnCount * gridLayout.rowCount) {
//            gridLayout[i].alpha = 1f
//            gridLayout[i].visibility = View.INVISIBLE
//        }
//
//        tableArray = levelGenerator.generateMap()
//        drawLevel(tableArray)
//        spawnAsteroid()
//        when (side) {
//            left -> side = up
//            up -> side = right
//            right -> side = down
//            down -> side = left
//        }
//        startGame()
//    }

    private fun move() {
        if(side.coordinate()) {
            asteroid.x += side.move()
        }else asteroid.y += side.move()
        findCollision()
    }

    private fun findCollision() {
        val column = ((asteroid.x + asteroid.width / 2) / gridLayout[0].height).toInt()
        val row = ((asteroid.y + asteroid.height / 2) / gridLayout[0].width).toInt()
        Log.i("pos", "row = $row column = $column")
        val cardNum = row * gridLayout.columnCount + column
        val card = gridLayout[actualRow * gridLayout.columnCount + actualColumn]
        Log.i("pos", "$cardNum")
        if (row != actualRow || column != actualColumn) {
            if (row !in tableArray.indices || column !in tableArray.indices) {
                lose()
                return
            }
            if (tableArray[row][column]) {
                tableArray[actualRow][actualColumn] = false
                checkWin()
                hideCard(card)
                actualRow = row
                actualColumn = column
                Log.i("pos", "ПЕРЕШЕЛ")
            } else {
                lose()
            }
        }
    }


    private fun hideCard(card: View) {
        card.animate().apply {
            duration = 1000
            alpha(0.0f)
        }.withEndAction {
        }.startDelay = 200
    }

    private fun lose() {
        stopGame()
        openFinishFragment(false)
//        handler.postDelayed({
//            restart()
//        }, 1000)
    }

    private fun checkWin() {
        score--
        if (score <= 1) {
            stopGame()
            openFinishFragment(true)
        }
    }

    private fun openFinishFragment(boolean: Boolean) {
        endGame = true
        val finishFragment = FinishFragment()
        val bundle = Bundle()
        bundle.putBoolean("win", boolean)
        if (!finishFragment.isAdded) {
            finishFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.add(R.id.fragment_container, finishFragment)
                    ?.commit()
        }
    }

    private fun spawnAsteroid() {
        actualRow = levelGenerator.line
        actualColumn = levelGenerator.column

        val x = gridLayout[1].width
        val y = gridLayout[0].x
        val z = gridLayout[2].x

        val card = gridLayout[levelGenerator.line * gridLayout.columnCount + levelGenerator.column]

        asteroid.x = card.width / 2 + card.x
        asteroid.y = card.height / 2 + card.y
        asteroid.x = asteroid.x - asteroid.width / 2
        asteroid.y = asteroid.y - asteroid.height / 4

        Log.i("pos", "x = $x y = $y z = $z")
        Log.i("pos", "астероид х = ${asteroid.x} y = ${asteroid.y}")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchCoordinates.x1 = event.x
                touchCoordinates.y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                touchCoordinates.x2 = event.x
                touchCoordinates.y2 = event.y
                touchCoordinates.x3 = touchCoordinates.x2 - touchCoordinates.x1
                touchCoordinates.y3 = touchCoordinates.y2 - touchCoordinates.y1
                // RIGHT OR LEFT
                if (abs(touchCoordinates.x3) > touchCoordinates.minDistance) {
                    side = if (touchCoordinates.x2 > touchCoordinates.x1) {
                        right
                    } else left
                    // DOWN OR UP
                } else if (abs(touchCoordinates.y3) > touchCoordinates.minDistance) {
                    side = if (touchCoordinates.y2 > touchCoordinates.y1) {
                        down
                    } else up
                }
                turnAsteroid()
            }
        }
        return true
    }
}