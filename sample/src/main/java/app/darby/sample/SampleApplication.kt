package app.darby.sample

import android.app.Application
import app.darby.notifire.Notifire
import app.darby.notifire.creator.notificationChannel

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpNotifire()
    }

    private fun setUpNotifire() {
        // create a default notification channel if not found
        val channel = notificationChannel(getString(R.string.notification_channel_id, "default")) {
            setName(getString(R.string.notification_default_channel_name))
            setDescription(getString(R.string.notification_default_channel_description))
        }

        // setup notification configurations
        Notifire.initialize {
            smallIconResId(R.drawable.ic_notification_small_white_24dp)
            channelId(channel.id)
        }
    }
}