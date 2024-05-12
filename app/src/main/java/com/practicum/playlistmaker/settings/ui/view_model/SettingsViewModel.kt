package com.practicum.playlistmaker.settings.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import com.practicum.playlistmaker.sharing.domain.ShareLinksOpenerInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent{

    private val shareLinksOpenerInteractor: ShareLinksOpenerInteractor by inject()
    private val themeSwitcherInteractor: ThemeSwitcherInteractor by inject()

    fun shareApp(){
        shareLinksOpenerInteractor.shareApp()
    }

    fun openTerms(){
        shareLinksOpenerInteractor.openTerms()
    }

    fun openSupport(){
        shareLinksOpenerInteractor.openSupport()
    }

    fun getTheme() : Boolean{
        return themeSwitcherInteractor.getTheme()
    }

    fun switchTheme(){
        themeSwitcherInteractor.switchTheme()
    }

    fun coroutineTest(){
        viewModelScope.launch{
            ourNumbersFlow().collect {
                value -> Log.d("value", "$value")
            }
        }
    }


    private suspend fun ourNumbersFlow(): Flow<Int> = flow{
        (0..10).forEach {
            delay(1000)
            emit(it)
        }

        }.filter{ it % 2 == 0}
        . map{value -> value * value}


    private suspend fun calculateFactorial(value: Int): Int {
        var result = 1
        // 3
        for (i in 1..value) {
            result *= i
            // 4
            Log.d("coroutine_test", "Факториал числа $i = $result")
            delay(timeMillis = 1000)
        }
        return result
    }

}