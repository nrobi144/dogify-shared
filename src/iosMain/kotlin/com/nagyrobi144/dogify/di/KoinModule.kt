package com.nagyrobi144.dogify.di

import com.nagyrobi144.dogify.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers

internal actual fun getDispatcherProvider(): DispatcherProvider = IosDispatcherProvider()

private class IosDispatcherProvider : DispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.Default
    override val unconfined = Dispatchers.Unconfined
}