package app.darby.sample

import android.app.Application
import app.darby.notifire.Notifire

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpNotifire()
    }

    private fun setUpNotifire() {
        Notifire.initialize {
            smallIconResId(R.drawable.ic_notification_small_white_24dp)
            channelId(getString(R.string.notification_channel_id, "default"))
        }
    }
}