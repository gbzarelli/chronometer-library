package br.com.helpdev.chronometerlib

import java.io.Serializable

class ChronometerManager(
    chronometer: Chronometer = Chronometer(),
    val currentSystemTime: () -> Long
) : Serializable {

    var chronometer: Chronometer private set
    private var pauseBaseTime: Long = 0L

    init {
        this.chronometer = chronometer
    }

    fun start(): Long {
        verifyAndRestorePausedTime()

        if (isFinished()) reset()

        if (0L == chronometer.startTime) {
            chronometer.startTime(currentSystemTime(), true)
        }

        return chronometer.getRunningTime(currentSystemTime())
    }


    private fun verifyAndRestorePausedTime(): Boolean {
        if (pauseBaseTime <= 0) return false
        val pausedTime = currentSystemTime() - pauseBaseTime
        chronometer.addPausedTime(pausedTime)
        pauseBaseTime = 0L
        return true
    }

    fun pause(): Boolean {
        if (!isStarted() || isFinished()) return false
        pauseBaseTime = currentSystemTime()
        return true
    }

    private fun isStarted() = chronometer.startTime > 0

    private fun isFinished() = chronometer.endTime > 0

    private fun isPaused() = pauseBaseTime > 0L

    fun stop(): Chronometer {
        if (isFinished()) return chronometer
        verifyAndRestorePausedTime()
        chronometer.setEndTime(currentSystemTime())
        return chronometer
    }

    fun reset() {
        chronometer = Chronometer()
        pauseBaseTime = 0L
    }

    fun lap() = chronometer.newLap(currentSystemTime())

    fun removeLap(lapPosition: Int) = chronometer.removeLap(lapPosition)

    fun getChronometerTime() = chronometer.getChronometerTime(currentSystemTime())

    fun getCurrentBase(): Long {
        return getCurrentBase(chronometer)
    }

    fun getBaseLastLap(): Long {
        return getCurrentBase(chronometer.lastLap())
    }

    fun getCurrentBase(chronometer: Chronometer): Long {
        return when {
            pauseBaseTime > 0 -> getBaseWithPause(chronometer)
            chronometer.endTime > 0 -> getBaseWithFinishedTime(chronometer)
            else -> chronometer.getStartBase()
        }
    }

    private fun getBaseWithFinishedTime(chronometer: Chronometer): Long {
        return currentSystemTime() - chronometer.getRunTime()
    }

    private fun getBaseWithPause(chronometer: Chronometer): Long {
        return currentSystemTime() - chronometer.getStartBase() - (currentSystemTime() - pauseBaseTime)
    }

    fun getPauseBaseTime() = when {
        isFinished() -> currentSystemTime() - chronometer.pausedTime
        else -> pauseBaseTime
    }

    fun getLastLapBasePause() = when (pauseBaseTime) {
        0L -> currentSystemTime() - chronometer.lastLap().pausedTime
        else -> pauseBaseTime - chronometer.lastLap().pausedTime
    }
}