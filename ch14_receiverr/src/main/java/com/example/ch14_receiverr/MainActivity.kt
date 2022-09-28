package com.example.ch14_receiverr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ch14_receiverr.MyReceiver
import com.example.ch14_receiverr.MyReceiver2
import com.example.ch14_receiverr.databinding.ActivityMainBinding
import com.example.ch14_receiverr.R
import com.example.ch14_receiverr.R.drawable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bootingIntent = Intent(this, MyReceiver2::class.java)
        sendBroadcast(bootingIntent)

        //배터리
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            when (getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when (getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources, R.drawable.usb))
                        }
                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources, R.drawable.ac))
                        }
                    }
                }
                else -> {
                    binding.chargingResultView.text = "Not Plugged"
                }
            }
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            binding.percentResultView.text = "$batteryPct %"
        }
        binding.button.setOnClickListener {
            val intent = Intent(this, MyReceiver::class.java)
            sendBroadcast(intent)
        }

        //화면 켬 끔
        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)

        intentFilter.addAction(Intent.ACTION_SCREEN_ON)

        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent!!.action
                //Log.d("Test", "receive : $action")

                when (action) {
                    Intent.ACTION_SCREEN_ON -> {
                        Log.d("Siyun","screen on")
                    }
                    Intent.ACTION_SCREEN_OFF -> {
                        Log.d("Siyun","screen off")
                    }
                }
            }
        }

        registerReceiver(receiver, intentFilter);
    }




}

