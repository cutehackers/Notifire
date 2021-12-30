package app.darby.notifire.provider

import app.darby.notifire.style.MessagingStyleBuilder

fun interface MessagingStyleBuilderProvider : (MessagingStyleBuilder) -> Unit
