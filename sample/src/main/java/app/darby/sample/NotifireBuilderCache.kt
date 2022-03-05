package app.darby.sample

import android.annotation.SuppressLint
import app.darby.notifire.Notifire

/**
 * We use a Singleton for a global copy of the NotificationCompat.Builder to update active
 * Notifications from other Services/Activities.
 *
 * You have two options for updating your notifications:
 *
 *  1. Use a new NotificationCompatBuilder to create the Notification. This approach requires you
 *  to get *ALL* the information and pass it to the builder. We get all the information from a Mock
 *  Database and this is the approach used in the MainActivity.
 *
 *  2. Use an existing NotificationCompatBuilder to create a Notification. This approach requires
 *  you to store a reference to the original builder. The benefit is you only need the new/updated
 *  information for an existing notification. We use this approach in the IntentService handlers to
 *  update existing notifications.
 *
 *  IMPORTANT NOTE 1: You shouldn't save/modify the resulting Notification object using
 *  its member variables and/or legacy APIs. If you want to retain anything from update
 *  to update, retain the Builder as option 2 outlines.
 *
 *  IMPORTANT NOTE 2: If the global Notification Builder is lost because the process is killed, you
 *  should have a way to recreate the Notification Builder from a persistent state. (We do this as
 *  well in the sample, check the IntentServices.)
 */
object NotifireBuilderCache {
    @SuppressLint("StaticFieldLeak")
    var builder: Notifire.Builder? = null
}