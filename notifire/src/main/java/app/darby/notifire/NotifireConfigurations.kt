package app.darby.notifire

import androidx.annotation.DrawableRes

/**
 * Notification configurations for default values
 */
data class NotifireConfigurations internal constructor(
    // Default small icon resource id
    @DrawableRes var smallIconResId: Int,
    // The constructed Notification will be posted on this NotificationChannel. default channel id
    val channelId: String,
) {
    class Builder {
        @DrawableRes private var smallIconResId: Int? = null
        private var channelId: String? = null

        fun smallIconResId(@DrawableRes smallIconResId: Int) = apply {
            this.smallIconResId = smallIconResId
        }

        fun channelId(channelId: String) = apply {
            this.channelId = channelId
        }

        fun build(): NotifireConfigurations {
            return NotifireConfigurations(
                smallIconResId ?: throw IllegalArgumentException(),
                channelId ?: throw IllegalArgumentException()
            )
        }
    }
    companion object {
        fun builder() = Builder()
    }
}