package app.darby.notifire.provider

import app.darby.notifire.style.InboxStyleBuilder

fun interface InboxStyleBuilderProvider : (InboxStyleBuilder) -> Unit
