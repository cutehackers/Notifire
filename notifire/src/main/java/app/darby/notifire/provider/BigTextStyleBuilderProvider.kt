package app.darby.notifire.provider

import app.darby.notifire.style.BigTextStyleBuilder

fun interface BigTextStyleBuilderProvider : (BigTextStyleBuilder) -> Unit
