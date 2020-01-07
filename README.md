![Issues](https://img.shields.io/github/issues/gbzarelli/chronometer-library.svg) ![Forks](https://img.shields.io/github/forks/gbzarelli/chronometer-library.svg) ![Stars](https://img.shields.io/github/stars/gbzarelli/chronometer-library.svg) ![Release Version](https://img.shields.io/github/release/gbzarelli/chronometer-library.svg)

# Chronometer Library [![](https://jitpack.io/v/gbzarelli/chronometer-library.svg)](https://jitpack.io/#gbzarelli/chronometer-library)
 
 The Chronometer Library is designed to make it easy to use a stopwatch, 
 has features that help count turns, and a custom Widget to display 
 the milliseconds.
 
## Add Chronometer Library with dependency:

- Add it in your `build.gradle` (root) at the end of repositories:

```gradle
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

- Add the dependency in your `build.gradle` project

```gradle
	dependencies {
	        implementation 'com.github.gbzarelli:chronometer-library:2.0.0'
	}
```

## How to use the ChronometerWidget
 
#### Layout representation: 00:00.0
 
- Put in your layout xml

```xml
<br.com.helpdev.chronometerlib.widget.ChronometerWidget
        android:id="@+id/chronometer_widget"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_horizontal"/>
```

- The code 

```kotlin
    fun start(){
        chronometer_widget.start()
    }
    
    fun reset(){
        chronometer_widget.base = SystemClock.elapsedRealtime()
        stop() 
    }
    
    fun stop(){
        chronometer_widget.stop()
    }
```

## How to use the ChronometerManager class

- See `ChronometerManager.kt` and `Chronometer.kt`

```kotlin
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
```

## Project Sample

- Play Store: [Swimming Stopwatch (Lap counter)](https://play.google.com/store/apps/details?id=br.com.helpdev.lapscounter.swimming&hl=en)
- Github: [LapsCounter](https://github.com/gbzarelli/LapsCounter)

# Licence

[MIT](https://choosealicense.com/licenses/mit/)