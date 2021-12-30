package app.darby.notifire.provider

import app.darby.notifire.MessagingStyleBuilder

fun interface MessagingStyleBuilderProvider : (MessagingStyleBuilder) -> Unit
