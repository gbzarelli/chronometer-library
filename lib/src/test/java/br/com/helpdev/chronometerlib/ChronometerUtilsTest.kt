package br.com.helpdev.chronometerlib

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class ChronometerUtilsTest {
    @Test
    fun formattedTime() {
        val toMillis = TimeUnit.HOURS.toMillis(2)
            .plus(TimeUnit.MINUTES.toMillis(5))
            .plus(TimeUnit.SECONDS.toMillis(10))
        val formattedTime = ChronometerUtils.getFormattedTime(toMillis)
        assertEquals("02:05:10.", formattedTime)
    }

    @Test
    fun formattedTime2() {
        val toMillis = TimeUnit.MINUTES.toMillis(5)
            .plus(TimeUnit.SECONDS.toMillis(10))
            .plus(TimeUnit.MILLISECONDS.toMillis(300))
        val formattedTime = ChronometerUtils.getFormattedTime(toMillis)
        assertEquals("05:10.3", formattedTime)
    }
}