package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.repository.ShareLinksOpenerRepository

class ShareLinksOpenerRepositoryImpl(private val context: Context) : ShareLinksOpenerRepository {

    override fun shareApp() {
        val shareMessage = context.getString(R.string.share_link)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(shareIntent)
    }

    override fun openTerms() {
        val settingsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.assignment_link)))
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(settingsIntent)
    }

    override fun openSupport() {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_message_address)))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_message_theme))
        supportIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message_text))
        context.startActivity(supportIntent)
    }

}