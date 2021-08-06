package ua.dev.foregroundservice

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import java.text.DateFormat
import java.util.*


class ForegroundService : Service() {


    override fun onCreate() {
        Log.e("ZAZ", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)



//            Log.e("ZAZ", Calendar.getInstance().toString())

            Thread {
                while (true) {
                    showNotification(
                        DateFormat.getDateTimeInstance(
                            DateFormat.LONG,
                            DateFormat.LONG,
                            Locale.getDefault()
                        ).format(Calendar.getInstance().time)
                    )
                    Thread.sleep(5000)
                }
            }.start();
        //stopSelf();
        return START_STICKY
    }

    private fun showNotification(messageText: String) {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(messageText)
            .setPriority(PRIORITY_MAX)
            .setSmallIcon(R.drawable.arrow_down_float)
            .build()

        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        notificationManager.cancel("notificationTag", 5)
        notificationManager.notify("notificationTag", 5, notification)
    }

    override fun onDestroy() {
        Log.e("ZAZ", "onDestroy")
        super.onDestroy()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        Log.e("ZAZ", "onBind")
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }
}