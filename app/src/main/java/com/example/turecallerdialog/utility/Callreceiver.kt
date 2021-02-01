package com.example.turecallerdialog.utility

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.turecallerdialog.R
import com.example.turecallerdialog.database.AppDatabase
import com.example.turecallerdialog.ui.activity.DialogCheckActivity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit


class Callreceiver : PhonecallReceiver()
{

    private val workManager by lazy { WorkManager.getInstance() }
    //private const val CHANNEL_WHATEVER = "whatever"


    override fun onIncomingCallStarted(ctx: Context?, number: String?, start: Date?) {
      // Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()


        ctx.let {ctxs ->
            number?.let {
                callDialog(ctxs,it)
            }
        }



      //  Log.e("TAG", "--->onIncomingCallStarted")
    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {

       //Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()
       // Log.e("TAG", "--->onOutgoingCallStarted")
        number?.let {
            callDialog(ctx,it)

        }
    }

    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        //Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()
        //Log.e("TAG", "--->onIncomingCallEnded")
        number?.let {
            callDialog(ctx,it)

        }

    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {

        //Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()
       // Log.e("TAG", "--->onOutgoingCallEnded")

       /* number?.let {
            callDialog(ctx,it)

        }*/
    }

    override fun onMissedCall(ctx: Context?, number: String?, start: Date?) {
        //Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()
       // Log.e("TAG", "--->onMissedCallonMissedCall")

        number?.let {
            callDialog(ctx,it)

        }

    }


    @SuppressLint("RestrictedApi")
    fun callDialog(ctx: Context?, number: String){

        ctx?.let { context ->

            val database = AppDatabase.getInstance(context).contactDao()


            GlobalScope.launch (Dispatchers.IO){

               val search = "%${number.removeCountryCode("+91")}%";


                Log.e("number","-->$search")
                database.getCheckedNumber(true,search).let {

                    Log.e("dabase-->","-->${it.size}")


                    if(it.size>0) {

                        delay(2000L)

                       // withContext(Dispatchers.Main){

                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


                           // val compressionWork = OneTimeWorkRequest.Builder(StartActivityWorker::class.java)
                            val data = Data.Builder()
//Add parameter in Data class. just like bundle. You can also add Boolean and Number in parameter.
                            data.put("model", it.get(0))
//Set Input Data
                           // compressionWork.setInputData(data.build())
//enque worker
                           // WorkManager.getInstance().enqueue(compressionWork.build())


                            workManager.enqueue(OneTimeWorkRequest.Builder(StartActivityWorker::class.java).setInitialDelay(10, TimeUnit.SECONDS).setInputData(data.build()).build())
                        }else{

                        }*/


                        Log.e("Version","-->${Build.VERSION.SDK_INT}")


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            /*val fullScreenIntent = Intent(context, DialogCheckActivity::class.java)
                            val fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                            val notificationBuilder =
                                    NotificationCompat.Builder(context, "sdd")
                                            .setSmallIcon(R.drawable.ic_baseline_local_phone_24)
                                            .setContentTitle("Call")
                                            .setContentText(number)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setCategory(NotificationCompat.CATEGORY_CALL)

                                            // Use a full-screen intent only for the highest-priority alerts where you
                                            // have an associated activity that you would like to launch after the user
                                            // interacts with the notification. Also, if your app targets Android 10
                                            // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                                            // order for the platform to invoke this notification.
                                            .setFullScreenIntent(fullScreenPendingIntent, true)

                            val incomingCallNotification = notificationBuilder.build()

                            val mgr = context.getSystemService(NotificationManager::class.java)

                            mgr.notify(121, incomingCallNotification)*/
                            val i = Intent(context, DialogCheckActivity::class.java)
                            i.putExtra("model",it.get(0))
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            val pi = PendingIntent.getActivity(
                                    context,
                                    0,
                                    i,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            val builder = NotificationCompat.Builder(context, CHANNEL_WHATEVER)
                                    .setSmallIcon(R.drawable.ic_baseline_local_phone_24)
                                    .setContentTitle("Call")
                                    .setContentText(it.get(0).dummyName +"\n"+number)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    //.setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_CALL)
                                    .setFullScreenIntent(pi, true)

                            val mgr = context.getSystemService(NotificationManager::class.java)

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

                            mgr.notify(123, builder.build())




                        }else{
                            val i = Intent(context, DialogCheckActivity::class.java)
                            i.putExtra("model",it.get(0))
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            // i.flags = FLAG_ACTIVITY_SINGLE_TOP
                            context.startActivity(i)
                        }



                      /* val pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)

                        try{
                        pi.send(context, 0, i);

                    } catch (e : PendingIntent.CanceledException) {
                        // the stack trace isn't very helpful here.  Just log the exception message.
                        System.out.println( "Sending contentIntent failed: " );
                    }*/


                        // }


                    }






                }
            }






        }


    }


}