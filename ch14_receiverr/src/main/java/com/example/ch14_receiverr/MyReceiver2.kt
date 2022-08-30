package com.example.ch14_receiverr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

//부팅

class MyReceiver2 : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

            Log.d("kkang","부팅완료")


    }
}