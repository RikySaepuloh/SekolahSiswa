package com.esaku.sekolahsiswa.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.esaku.sekolahsiswa.DetailInformasiActivity
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
private const val channelName = "stucore"
private const val CHANNEL_DESC = "stucore notification manager"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var intent: Intent
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var key: Map<String, Any> = HashMap()
        key = Gson().fromJson(remoteMessage.data["key"], key.javaClass)

        remoteMessage.notification?.let {
            
            when (it.clickAction) {
                "detail_matpel" -> {
                    val onlykey= key["kode_matpel"].toString()
                    intent = Intent(this, DetailMapelActivity::class.java)
                    intent.putExtra("kode_mapel", onlykey)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                "notifikasi" -> {
                    val onlykey= key["no_bukti"].toString()
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("no_bukti", onlykey)
                    intent.putExtra("open_notifikasi",true)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                "informasi" -> {
                    val onlykey= key["no_bukti"].toString()
                    val kodeMatpel= key["kode_matpel"].toString()
                    val nik= key["nik"].toString()
                    intent = Intent(this, DetailInformasiActivity::class.java)
                    intent.putExtra("no_bukti", onlykey)
                    intent.putExtra("kode_matpel", kodeMatpel)
                    intent.putExtra("nik", nik)
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
            }

                val pendingIntent=PendingIntent.getActivity(this,0,intent, FLAG_ONE_SHOT)
                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(it.title)
                    .setAutoCancel(true)
                    .setContentText(it.body)
                    .setSmallIcon(R.drawable.ic_stucore)
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(it.body))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build()
                notificationManager.notify(notificationID,notification)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = CHANNEL_DESC
            enableLights(true)
            importance=NotificationManager.IMPORTANCE_HIGH
            enableVibration(true)
            lightColor = Color.BLUE
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)

    }
}