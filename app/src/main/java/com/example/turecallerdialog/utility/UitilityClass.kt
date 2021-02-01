package com.example.turecallerdialog.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.turecallerdialog.R
import com.example.turecallerdialog.ui.activity.DialogCheckActivity
import com.google.android.material.appbar.CollapsingToolbarLayout

class UitilityClass {




}

//Set CollapsingToolbarLayout title
fun CollapsingToolbarLayout.setCollapsingToolbarLayoutTitle(
    title: String,
    isTitleEnabled: Boolean
) {
    this.title = title
    this.isTitleEnabled = isTitleEnabled

}


fun String.removeCountryCode(countryCode : String) : String{
    return  when {
        this.contains(countryCode) -> this.replace(countryCode,"")
        else -> {
            this
        }
    }

}

private const val NOTIF_ID = 1337
public const val CHANNEL_WHATEVER = "whatever"

class StartActivityWorker(
        private val appContext: Context,
        workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        appContext.startActivity(
                Intent(
                        appContext,
                        DialogCheckActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

        return Result.success()
    }
}

class ShowNotificationWorker(
        private val appContext: Context,
        workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun doWork(): Result {
        val pi = PendingIntent.getActivity(
                appContext,
                0,
                Intent(appContext, DialogCheckActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(appContext, CHANNEL_WHATEVER)
                .setSmallIcon(R.drawable.ic_baseline_local_phone_24)
                .setContentTitle("Call")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pi, true)

        val mgr = appContext.getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && mgr.getNotificationChannel(CHANNEL_WHATEVER) == null
        ) {
            mgr.createNotificationChannel(
                    NotificationChannel(
                            CHANNEL_WHATEVER,
                            "Whatever",
                            NotificationManager.IMPORTANCE_HIGH
                    )
            )
        }

        mgr.notify(NOTIF_ID, builder.build())

        return Result.success()
    }
}
