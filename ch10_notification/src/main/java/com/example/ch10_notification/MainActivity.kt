package com.example.ch10_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import com.example.ch10_notification.databinding.ActivityMainBinding
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //퍼미션 허용 요청 확인
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted ->
            if(isGranted){
                Log.d("park", "callback, granted..")
            } else{
                Log.d("park","callback, denied..")
            }
        }
        requestPermissionLauncher.launch("android.permission.ACCESS_FINE_LOCATION")
        requestPermissionLauncher.launch("android.permission.ACCESS_COARSE_LOCATION")
        //퍼미션 허용 확인
        val status1 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
        val status2 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION")
        if(status1==PackageManager.PERMISSION_GRANTED && status2 == PackageManager.PERMISSION_GRANTED ){
            Log.d("park","permission granted")
        }else{
            Log.d("park", "permission denied")
        }
        //이벤트 연결
        binding.notificationButton.setOnClickListener{
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                //26버전 이상
                val channelId="one-channel"
                val channelName="My Channel One"
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "My Channel One Description"
                    setShowBadge(true)
                    //링톤 객체 얻기
                    val uri: Uri = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                    //알림음 재생
                    setSound(uri, audioAttributes)
                    //진동 울릴 지 여부
                    enableVibration(true)

                }
                //채널을 NotificationManager에 등록
                manager.createNotificationChannel(channel)
                //채널 이용하여 빌더 생성
                builder=NotificationCompat.Builder(this, channelId)
        } else{
            //26버전 이하
            builder = NotificationCompat.Builder(this)
            }
            builder.run {
                //알림의 기본 정보
                setSmallIcon(R.drawable.small)
                setWhen(System.currentTimeMillis())
                setContentTitle("박시윤")
                setContentText("글로벌미디어학부입니다.")
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
            }
            val KEY_TEXT_REPLY = "key_text_reply"
            var replyLabel:String = "답장"
            var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
                setLabel(replyLabel)
                build()
            }
            val replyIntent = Intent(this, ReplyReceiver::class.java)
            val replyPendingIntent = PendingIntent.getBroadcast(
                this, 30, replyIntent, PendingIntent.FLAG_MUTABLE
            )
            builder.addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.send,
                    "답장",
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )
            manager.notify(11, builder.build())
        }
    }
}