package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R

class Navigator(private val context: Context) {
    fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.course_link))
        }
        context.startActivity(shareIntent)
    }
    fun openLink() {
        val url = Uri.parse(context.getString(R.string.agreement_link))
        val agreementIntent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(agreementIntent)
    }

    fun openEmail() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.dev_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_text))
        }
        context.startActivity(supportIntent)
    }
}
