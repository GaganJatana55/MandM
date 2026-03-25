package org.example.mandm.share

import android.content.Context
import android.content.Intent

fun shareText(context: Context, text: String, title: String = "Share bill") {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, title))
}




