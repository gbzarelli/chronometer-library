package br.com.helpdev.chronometerlib

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChronometerManagerTest {

    private class MutableClock(var now: Long = 1_000L) {
        fun currentTime() = now
        fun advanceBy(milliseconds: Long) {
            now += milliseconds
        }
    }

    private fun manager(clock: MutableClock) =
        ChronometerManager(currentSystemTime = clock::currentTime)

    @Test
    fun startAndStopMeasureElapsedTimeWithoutWaiting() {
        val clock = MutableClock()
        val manager = manager(clock)

        assertEquals(0L, manager.start())
        clock.advanceBy(1_250L)
        assertEquals(1_250L, manager.getChronometerTime())
        assertEquals(1_250L, manager.stop().getChronometerTime())
        assertTrue(manager.isFinished())
    }

    @Test
    fun pauseFreezesTimeAndResumeExcludesPausedDuration() {
        val clock = MutableClock()
        val manager = manager(clock)
        manager.start()
        clock.advanceBy(1_000L)

        assertTrue(manager.pause())
        assertEquals(1_000L, manager.getChronometerTime())
        clock.advanceBy(10_000L)
        assertEquals(1_000L, manager.getChronometerTime())

        manager.start()
        clock.advanceBy(500L)
        assertEquals(1_500L, manager.stop().getChronometerTime())
    }

    @Test
    fun stopWhilePausedDoesNotAddThePausedInterval() {
        val clock = MutableClock()
        val manager = manager(clock)
        manager.start()
        clock.advanceBy(750L)
        manager.pause()
        clock.advanceBy(5_000L)

        assertEquals(750L, manager.stop().getChronometerTime())
    }

    @Test
    fun lapRemovalRetainsElapsedTimeAndLapBoundaries() {
        val clock = MutableClock()
        val manager = manager(clock)
        manager.start()
        clock.advanceBy(1_000L)
        manager.lap()
        clock.advanceBy(2_000L)
        manager.lap()
        clock.advanceBy(3_000L)
        manager.stop()

        assertEquals(6_000L, manager.chronometer.getChronometerTime())
        assertEquals(1_000L, manager.chronometer.getLap(1)!!.getChronometerTime())
        assertEquals(2_000L, manager.chronometer.getLap(2)!!.getChronometerTime())
        assertEquals(3_000L, manager.chronometer.getLap(3)!!.getChronometerTime())

        manager.removeLap(2)

        assertEquals(4_000L, manager.chronometer.getChronometerTime())
        assertEquals(1_000L, manager.chronometer.getLap(1)!!.getChronometerTime())
        assertEquals(3_000L, manager.chronometer.getLap(2)!!.getChronometerTime())
    }

    @Test
    fun statusReflectsStartedPausedAndFinishedStates() {
        val clock = MutableClock()
        val manager = manager(clock)

        manager.start()
        assertTrue(manager.isStarted())
        assertFalse(manager.isFinished())
        assertFalse(manager.isPaused())

        manager.pause()
        assertFalse(manager.isStarted())
        assertTrue(manager.isPaused())

        manager.stop()
        assertFalse(manager.isStarted())
        assertTrue(manager.isFinished())
        assertFalse(manager.isPaused())
    }
}
