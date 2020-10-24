package com.esaku.sekolahsiswa.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.esaku.sekolahsiswa.DetailMapelActivity
import com.esaku.sekolahsiswa.MainActivity
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.models.ModelProfile
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.ArrayList
import kotlin.random.Random

private const val CHANNEL_ID = "siswa_channel"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var intent: Intent
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
//
//        }
//
//        if (remoteMessage.notification!=null) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.notification}")
//
//            try {
//                val title=remoteMessage.notification?.title
//                val message=remoteMessage.notification?.body
//                val clickAction=remoteMessage.notification?.clickAction
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }

        val data = remoteMessage.data
        var key: Map<String, Any> = HashMap()
        key = Gson().fromJson(remoteMessage.data["key"], key.javaClass)
        val onlykey= key["kode_matpel"].toString()

        Log.d(TAG, "KEY: $onlykey")


        val myCustomKey = data["my_custom_key"]
        Log.d(TAG, "From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            val parameterclick=it.clickAction
            Log.d(TAG, "Click Action: ${it.clickAction}")
            when (parameterclick) {
                "detail_matpel" -> {
                    intent = Intent(this, DetailMapelActivity::class.java)
                    intent.putExtra("kode_mapel", onlykey)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                "notifikasi" -> {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("open_notifikasi",true)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                "informasi" -> {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("open_informasi",true)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                else -> {
                    intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }
            val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = Random.nextInt()

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                createNotificationChannel(notificationManager)

//                val channelName = "approval_notification"
//                val channel = NotificationChannel(CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
//                    description = "Approval"
//                    enableLights(true)
//                    lightColor = Color.BLUE
//                }
////                notificationManager.createNotificationChannel(channel)
//
//                val pendingIntent=PendingIntent.getActivity(this,0,intent, FLAG_ONE_SHOT)
//                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle(it.title)
//                    .setAutoCancel(true)
//                    .setContentText(it.body)
//                    .setSmallIcon(R.drawable.approval_icon)
//                    .setContentIntent(pendingIntent)
//                    .setChannelId(CHANNEL_ID)
//                    .build()
//                notificationManager.notify(notificationID,notification)
//                val mNotificationManager =
//                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                mNotificationManager.createNotificationChannel(channel)
//                mNotificationManager.notify(notificationID , notification);
//                createNotificationChannel(notificationManager)
            }

                val pendingIntent=PendingIntent.getActivity(this,0,intent, FLAG_ONE_SHOT)
                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(it.title)
                    .setAutoCancel(true)
                    .setContentText(it.body)
//                    .setContentText(it.body)
                    .setSmallIcon(R.drawable.ic_stucore)
                    .setContentIntent(pendingIntent)
                    .build()
                notificationManager.notify(notificationID,notification)
            }

//            PendingIntent.getActivity(this, 0, Intent(this, MyActivity::class.java), 0)




    }
//
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "stucore"
        val channel = NotificationChannel(CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "stucore notification manager"
            enableLights(true)
            lightColor = Color.BLUE
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)

    }
}