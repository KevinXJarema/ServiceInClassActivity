package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import java.util.Timer

class MainActivity : AppCompatActivity() {

    lateinit var timerTextView : TextView

    lateinit var  timerBinder: TimerService.TimerBinder
    var isConnected = false
    val timerHandler = Handler(Looper.getMainLooper()){
        timerTextView.text = it.what.toString()
        true
    }

    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected=false
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{

        when(item.itemId){

            R.id.action_start -> if(isConnected) timerBinder.start(10)
            R.id.action_pause -> if(isConnected) timerBinder.pause()
            R.id.action_stop -> if(isConnected) timerBinder.stop()
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)




        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE

        )

    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }

}