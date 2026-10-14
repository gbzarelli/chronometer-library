package br.com.helpdev.chronometerlib

import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.helpdev.chronometerlib.widget.ChronometerWidget
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChronometerWidgetInstrumentedTest {

    @Test
    fun widgetInitializesAndDisplaysItsBaseTime() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            val context = instrumentation.targetContext
            val widget = ChronometerWidget(context)

            assertNotNull(widget.text)
            widget.base = SystemClock.elapsedRealtime() - 1_250L

            assertEquals("00:01.2", widget.text.toString())
            assertEquals(1_250L, widget.timeElapsed)
        }
    }
}
