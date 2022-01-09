package app.darby.sample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import app.darby.notifire.Notifire
import app.darby.notifire.creator.notification

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpView()
    }

    private fun setUpView() {
        findViewById<View>(R.id.launchView).setOnClickListener(::onLaunchClick)
    }

    private fun onLaunchClick(view: View) {
        // method 2.
        notification {
            newChannel()

            it.contentTitle("Subject")
            it.contentText("Hello people. It's Notifire!")
        }
    }

    // method 1.
    private fun notifyAsBigTextStyle() {
        val defaultChannelId = getString(R.string.notification_channel_id, "default")
        val notifire = Notifire.builder(this, defaultChannelId)
            .smallIcon(R.drawable.ic_notification_small_white_24dp)
            .contentTitle("Sender")
            .contentText("Subject")
            .largeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_people_black_48dp))
            //.contentIntent()
            .defaults(NotificationCompat.DEFAULT_ALL)
            .color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))
            .category(NotificationCompat.CATEGORY_MESSAGE)
            .isGroupSummary(true, "GROUP_KEY_YOUR_NAME_HERE")
            .priority(NotificationCompat.PRIORITY_DEFAULT)
            .visibility(NotificationCompat.VISIBILITY_SECRET)
            //.addAction {
            //    newAction(R.drawable.ic_launcher_foreground, "", null)
            //}
            .asBigTextStyle()
            .bigText("longer comments")
            .bigContentTitle("content title in the big form")
            .summaryText("content summary text")
            .notify()

        Notifire.cancel(applicationContext, notifire)
    }

    /**
     * Sample notification channel, this will be edited in a library form.
     */
    private fun newChannel() {
        val mgr: NotificationManager = getSystemService() ?: throw IllegalStateException()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.notification_channel_id, "default")
            val channelName = getString(R.string.notification_default_channel_name)
            if (mgr.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = getString(R.string.notification_default_channel_description)
                }

                mgr.createNotificationChannel(channel)
            }
        }
    }
}