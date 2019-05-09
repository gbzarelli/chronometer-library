# Chronometer Library
 
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
	        implementation 'com.github.gbzarelli:chronometer-library:1.0.0'
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

## How to use the Chronometer class

- See `Chronometer.kt` and `ObChronometer.kt`

```kotlin
    val chronometer = Chronometer()
    chronometer.start()
    Thread.sleep(1000)
    chronometer.lap()
    chronometer.pause()
    Thread.sleep(1000)
    chronometer.start()
    Thread.sleep(1000)
    chronometer.stop()
    print( Chronometer.getFormattedTime(chronometer.getRunningTime()) )
    val obChronometer = chronometer.obChronometer
    chronometer.reset()
```

## Project Sample

- Play Store: [Swimming Stopwatch (Lap counter)](https://play.google.com/store/apps/details?id=br.com.helpdev.lapscounter.swimming&hl=en)
- Github: [LapsCounter](https://github.com/gbzarelli/LapsCounter)

# Licence