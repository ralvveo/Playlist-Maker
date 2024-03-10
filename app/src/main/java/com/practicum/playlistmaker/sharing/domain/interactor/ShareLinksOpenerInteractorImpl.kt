package com.practicum.playlistmaker.sharing.domain.interactor

import com.practicum.playlistmaker.sharing.domain.ShareLinksOpenerInteractor
import com.practicum.playlistmaker.sharing.domain.repository.ShareLinksOpenerRepository

class ShareLinksOpenerInteractorImpl(private val shareLinksOpener : ShareLinksOpenerRepository) :
    ShareLinksOpenerInteractor {

    override fun shareApp() {
        shareLinksOpener.shareApp()
    }

    override fun openTerms() {
        shareLinksOpener.openTerms()
    }

    override fun openSupport() {
        shareLinksOpener.openSupport()
    }
}