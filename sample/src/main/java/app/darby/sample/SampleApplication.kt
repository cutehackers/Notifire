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
            // smallIconResId()
            // channelId()
        }
    }
}