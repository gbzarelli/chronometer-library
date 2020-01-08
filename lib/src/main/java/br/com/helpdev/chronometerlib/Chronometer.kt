package br.com.helpdev.chronometerlib

import java.io.Serializable
import java.util.*

class Chronometer : Serializable {
    companion object {
        class Builder(
            var dateTime: Date,
            var accumulatedStartTime: Long,
            var startTime: Long,
            var endTime: Long,
            var pausedTime: Long,
            var laps: List<Chronometer> = mutableListOf()
        ) {
            fun build(): Chronometer = Chronometer(this)
        }
    }

    constructor()

    private constructor(builder: Builder) {
        this.accumulatedStartTime = builder.accumulatedStartTime
        this.dateTime = builder.dateTime
        this.startTime = builder.startTime
        this.endTime = builder.endTime
        this.pausedTime = builder.pausedTime
        this.laps.addAll(builder.laps)
    }

    var accumulatedStartTime = 0L
        private set
    var dateTime: Date? = null
        private set
    var startTime = 0L
        private set
    var endTime = 0L
        private set
    var pausedTime = 0L
        private set

    private val laps: MutableList<Chronometer> = mutableListOf()

    fun getLap(position: Int): Chronometer? {
        if (laps.size == 0) throw IllegalStateException("Chronometer not started")
        if (position <= 0 || position > laps.size) throw IllegalArgumentException("Only accepted values in 1-" + laps.size)
        return laps[position - 1]
    }

    fun getLaps() = Collections.unmodifiableList(laps)

    fun startTime(startTime: Long, createLap: Boolean = true): Boolean {
        if (this.startTime > 0) return false
        this.startTime = startTime
        this.dateTime = Date()
        if (createLap) {
            return newLap(startTime)
        }
        return true
    }

    fun addPausedTime(pausedTime: Long) {
        this.pausedTime = this.pausedTime.plus(pausedTime)
        laps.last().pausedTime = laps.last().pausedTime.plus(pausedTime)
    }

    fun setEndTime(endTime: Long) {
        if (endTime < laps.last().getStartBase()) throw IllegalArgumentException("End time can't be lower than last lap start base")
        this.endTime = endTime
        laps.last().endTime = endTime
    }

    fun newLap(currentTime: Long): Boolean {
        if (laps.size > 0) {
            if (currentTime < laps.last().getStartBase()) throw IllegalArgumentException("Current time can't be lower than last lap start base")
            laps.last().endTime = currentTime
        }
        return laps.add(Chronometer().apply {
            startTime(currentTime, false)
        }.also {
            it.accumulatedStartTime = getRunningTime(currentTime)
        })
    }

    fun removeLap(lapNumber: Int): Long {
        if (!isAcceptedValueToRemove(lapNumber)) throw IllegalArgumentException("Only finished laps can be removed")

        val index = lapNumber - 1
        val lapRemoved = laps.removeAt(index)
        val pausedTimeRemoved = lapRemoved.pausedTime
        val runningTimeRemoved = lapRemoved.getRunTime()

        this.pausedTime = this.pausedTime.minus(pausedTimeRemoved)
        this.startTime = this.startTime.plus(runningTimeRemoved)
        laps.forEachIndexed { forIndex, lap ->
            lap.incrementTime(runningTimeRemoved)
            if (forIndex >= index) {
                lap.accumulatedStartTime = lap.accumulatedStartTime.minus(runningTimeRemoved)
            }
        }

        return runningTimeRemoved
    }

    fun getChronometerTime(currentTime: Long = -1) = when {
        endTime > 0 -> getRunTime()
        currentTime > 0 -> getRunningTime(currentTime)
        else -> -1
    }

    fun getRunTime(): Long {
        if (endTime <= 0) return -1
        return endTime - getStartBase()
    }

    fun getRunningTime(currentTime: Long): Long {
        return currentTime - getStartBase()
    }

    fun getStartBase() = startTime + pausedTime

    private fun isAcceptedValueToRemove(lapNumber: Int): Boolean {
        if (lapNumber <= 0) return false
        return if (laps.last().endTime > 0L) {
            lapNumber <= laps.size
        } else {
            lapNumber < laps.size
        }
    }

    private fun incrementTime(runningTimeRemoved: Long) {
        startTime = startTime.plus(runningTimeRemoved)
        if (endTime > 0L) endTime = endTime.plus(runningTimeRemoved)
    }

    fun lastLap() = laps.last()

    fun getAccumulatedTime(): Long {
        return accumulatedStartTime + getRunTime()
    }

    override fun toString(): String {
        return "Chronometer(dateTime=$dateTime, startTime=$startTime, endTime=$endTime, pausedTime=$pausedTime, laps=$laps)"
    }


}
