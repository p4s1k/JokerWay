package com.example.jokerway.fragments.asist

import android.util.Log

class LevelGenerator {
    var line =0
    var column =0


    fun generateMap(): Array<Array<Boolean>> {
        var tableArray = Array(5) { Array(5) { false } }
        var finish = false

        while (!finish) {
            line = (0..4).random()
            column = (0..4).random()
            tableArray = Array(5) { Array(5) { false } }
            tableArray[line][column] = true
//            val sideArray = arrayOf(0, 1, 2, 3)
            val sideArray = arrayOf(Left(), Up(), Right(), Down())
            val countCards = (10..20).random()
            Log.i("aaa", "начало $line $column")
            Log.i("aaa", "карт должно быть $countCards")

            generationFor@ for (i in 1 until countCards) {
                sideArray.shuffle()
                for (side in sideArray) {
                    nextElement(side)
                    if (line in 0..4 && column in 0..4) {
                        if (!tableArray[line][column]) {
                            tableArray[line][column] = true
                            for (arrBool in tableArray) {
                                var text = ""
                                for (value in arrBool) {
                                    text += " $value"
                                }
                                Log.i("aaa", text)
                            }
                            break
                        } else {
                            previousElement(side)
                        }
                    } else {
                        Log.i("aaa", "не существует")
                       previousElement(side)
                    }
                    if (sideArray.indexOf(side) == 3 && i >= 10) {
                        finish = true
                        Log.i("aaa", "закончилось досрочною ожидалось $countCards создано $i")
                        Log.i("moska", "закончилось досрочною ожидалось $countCards создано $i")
                        break@generationFor
                    }
                    if (sideArray.indexOf(side) == 3 && i < 10) {
                        Log.i("aaa", "не получилось ожидалось $countCards создано $i")
                        Log.i("moska", "не получилось ожидалось $countCards создано $i")
                        break@generationFor
                    }
                }
            }
        }
        return tableArray
    }

    private fun nextElement(side: Side){
        if (side.coordinate()){
            column+=side.nextElement()
        }else line+=side.nextElement()
    }

    private fun previousElement(side: Side){
        if (side.coordinate()){
            column+=side.previousElement()
        }else{
            line+=side.previousElement()
        }
    }
}