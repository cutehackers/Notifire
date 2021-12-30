package app.darby.notifire.style

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

/**
 * BigPictureStyle notification builder
 */
class BigPictureStyleBuilder(context: Context, _builder: NotificationCompat.Builder) :
    StyleBuilder<NotificationCompat.BigPictureStyle>(
        context,
        _builder,
        { NotificationCompat.BigPictureStyle() }
    ) {

    /**
     * Overrides ContentTitle in the big form of the template.
     */
    fun bigContentTitle(contentTitle: CharSequence) = apply {
        style.setBigContentTitle(contentTitle)
    }

    /**
     * Summary line after the detail section in the big form of the template.
     * Note: To improve readability, don't overload the user with info. If Summary Text
     * doesn't add critical information, you should skip it.
     */
    fun summaryText(summaryText: CharSequence) = apply {
        style.setSummaryText(summaryText)
    }

    /**
     * Provides the bitmap for the BigPicture notification.
     */
    fun bigPicture(picture: Bitmap) = apply {
        style.bigPicture(picture)
    }

    /**
     * Override the large icon when the big notification is shown.
     */
    fun bigLargeIcon(icon: Bitmap) = apply {
        style.bigLargeIcon(icon)
    }

    /**
     * When set, the big picture of this style will be promoted and shown in place of the large icon
     * in the collapsed state of this notification.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun showBigPictureWhenCollapsed(show: Boolean) = apply {
        style.showBigPictureWhenCollapsed(show)
    }
}
