package app.darby.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.darby.notifire.Notifire
import app.darby.notifire.creator.notification
import app.darby.notifire.creator.notificationAsBigTextStyle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // method 2.
        notification {

        }

        notificationAsBigTextStyle(applicationContext) {

        }
    }

    // method 1.
    private fun notifyAsBigTextStyle() {
        val notifire = Notifire.builder(this, "default_sample_channel_id")
            .smallIcon(R.drawable.ic_launcher_foreground)
            .contentTitle("Sender")
            .contentText("Subject")
            //.largeIcon()
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

        notifire.cancel()
    }
}