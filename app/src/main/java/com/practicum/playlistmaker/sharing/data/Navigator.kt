package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R

class Navigator(private val context: Context) {
    fun shareLink() : Intent {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.course_link))
        }
        return  shareIntent
    }
    fun openLink() : Intent {
        val url = Uri.parse(context.getString(R.string.agreement_link))
        val agreementIntent = Intent(Intent.ACTION_VIEW, url)
        return agreementIntent
    }

    fun openEmail() : Intent{
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.dev_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_text))
        }
        return supportIntent
    }
}
