package br.com.helpdev.chronometerlib

import java.text.DecimalFormat

object ChronometerUtils {

    fun getFormattedTime(timeElapsed: Long): String {
        val df = DecimalFormat("00")

        val hours = (timeElapsed / (3600 * 1000)).toInt()
        var remaining = (timeElapsed % (3600 * 1000)).toInt()

        val minutes = remaining / (60 * 1000)
        remaining %= (60 * 1000)

        val seconds = remaining / 1000
        remaining %= 1000

        val milliseconds = timeElapsed.toInt() % 1000 / 100

        var text = ""

        if (hours > 0) {
            text += df.format(hours.toLong()) + ":"
        }

        text += df.format(minutes.toLong()) + ":"
        text += df.format(seconds.toLong()) + "."
        if (hours <= 0) {
            text += Integer.toString(milliseconds)
        }
        return text
    }
}