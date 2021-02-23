package com.example.jokerway.fragments.asist

abstract class Side {

    abstract fun turn(): Float
    abstract fun move(): Int
    abstract fun coordinate(): Boolean       //  X- TRUE   Y- FALSE
    abstract fun nextElement(): Int
    abstract fun previousElement(): Int
}

class Left : Side() {
    override fun turn(): Float {
        return -90.0f
    }

    override fun move(): Int {
        return -10
    }

    override fun coordinate(): Boolean {
        return true
    }

    override fun nextElement(): Int {
        return -1
    }

    override fun previousElement(): Int {
        return 1
    }

}

class Up : Side() {
    override fun turn(): Float {
        return 0.0f
    }

    override fun move(): Int {
        return -10
    }

    override fun coordinate(): Boolean {
        return false
    }

    override fun nextElement(): Int {
        return -1
    }

    override fun previousElement(): Int {
        return 1
    }

}

class Right : Side() {
    override fun turn(): Float {
        return 90.0f
    }

    override fun move(): Int {
        return 10
    }

    override fun coordinate(): Boolean {
        return true
    }

    override fun nextElement(): Int {
        return +1
    }

    override fun previousElement(): Int {
        return -1
    }

}

class Down: Side() {
    override fun turn(): Float {
        return 180.0f
    }

    override fun move(): Int {
        return 10
    }

    override fun coordinate(): Boolean {
        return false
    }

    override fun nextElement(): Int {
        return 1
    }

    override fun previousElement(): Int {
        return -1
    }

}