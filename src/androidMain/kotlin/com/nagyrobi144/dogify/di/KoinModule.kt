package com.nagyrobi144.dogify.di

import com.nagyrobi144.dogify.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers

internal actual fun getDispatcherProvider(): DispatcherProvider = AndroidDispatcherProvider()

private class AndroidDispatcherProvider: DispatcherProvider{
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val unconfined = Dispatchers.Unconfined
}