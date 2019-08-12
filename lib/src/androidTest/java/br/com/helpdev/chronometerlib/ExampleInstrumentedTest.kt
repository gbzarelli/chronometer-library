package br.com.helpdev.chronometerlib

import android.os.SystemClock
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("br.com.helpdev.chronometerlib", appContext.packageName)
    }

    @Test
    fun testUsability() {
        val manager = ChronometerManager { SystemClock.elapsedRealtime() }
        manager.start()
        Thread.sleep(1000) // +1s
        manager.lap()
        manager.pause()
        Thread.sleep(1000) // 1s paused
        manager.start()
        Thread.sleep(1000) // +1s
        manager.stop()

        print(ChronometerUtils.getFormattedTime(manager.getChronometerTime()))

        val obChronometer = manager.chronometer
        println(obChronometer)

        manager.reset()
    }
}
