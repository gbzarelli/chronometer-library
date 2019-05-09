package br.com.helpdev.chronometerlib.objects

import android.os.SystemClock
import java.io.Serializable

class ObLap : Serializable {

    var chronometerTime = 0L

    var startTime = 0L
    var pausedTime = 0L
    var endTime = 0L

    fun getBase() = startTime + pausedTime

    fun getRunningTime() =
            when (endTime) {
                0L -> SystemClock.elapsedRealtime() - (startTime + pausedTime)
                else -> endTime - (startTime + pausedTime)
            }

}