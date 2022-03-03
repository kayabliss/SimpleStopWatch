package com.hfad.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    lateinit var stopWatch: Chronometer// the stopwatch
    var running = false // Is the stopwatch running?
    var offset: Long = 0 // The base offset for the stopwatch

    // We're going to use these three constants as the names for any values we add to the Bundle
    //Add key Strings for use with the Bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {//The onCreate method gets called when the activity is recreated and it takes one parameter, a bundle?
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // if the activity being created from scratch, the Bundle? will be null, but if the activity being recreated , it's the same Bundle object that was used by onSaveInstanceState()


        //We can only instantiate the stopWatch property after the call to setContentView.  Before this point, the chronometer view doesn't exist
        //Get a reference to the stopwatch
        stopWatch = findViewById<Chronometer>(R.id.stopWatch)

        //Restore the previous state, when orientation changes
        if(savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)//Restore the state of offset & running
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopWatch.base = savedInstanceState.getLong(BASE_KEY)
                stopWatch.start()
            }else setBaseTime()
        }

        //The Start Button starts the stopWatch if it's not running
        val startButton = findViewById<Button>(R.id.start)
        startButton.setOnClickListener{
           if(!running) {
               setBaseTime()
               stopWatch.start()
               running = true
           }
        }

        //The Pause button pauses the stopwatch if it's running
        val pauseButton = findViewById<Button>(R.id.stop)
        pauseButton.setOnClickListener {
            if (running){
                saveOffSet()
                stopWatch.stop()
                running = false
            }
        }

        //The reset button sets the offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()//Sets the stopWatch time back to 0
        }
    }

    //override fun onStop() {
     //  super.onStop()
    override fun onPause (){
        super.onPause()
        if (running) {
            saveOffSet()
            stopWatch.stop()
        }
    }

    //override fun onRestart() {
       // super.onRestart()
    override fun onResume(){
        super.onResume()
        if(running){
            setBaseTime()
            stopWatch.start()
            offset = 0
        }
    }
//Use the onSaveInstanceState method to save the state of the offset, running, and the stopwatch base properties
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
       savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY,running)
        savedInstanceState.putLong(BASE_KEY,stopWatch.base)
            super.onSaveInstanceState(savedInstanceState)
    }


    //setBase() and SaveOffSet() are convenience methods to make the code a bit more readable
    //Update the stopwatch.base time, allowing for any offset
    fun setBaseTime(){
        //offset = SystemClock.elapsedRealtime() -stopWatch.base
        stopWatch.base = SystemClock.elapsedRealtime() - offset
    }

    //Record the offSet
    fun saveOffSet(){
        offset = SystemClock.elapsedRealtime() - stopWatch.base
    }
}