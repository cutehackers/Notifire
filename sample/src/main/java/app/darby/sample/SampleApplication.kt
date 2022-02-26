package app.darby.sample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService
import app.darby.notifire.Notifire

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpNotifire()
    }

    private fun setUpNotifire() {
        newChannelIfNotExists()

        Notifire.initialize {
            smallIconResId(R.drawable.ic_notification_small_white_24dp)
            channelId(getString(R.string.notification_channel_id, "default"))
        }
    }

    /**
     * Sample notification channel, this will be edited in a library form.
     */
    private fun newChannelIfNotExists() {
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