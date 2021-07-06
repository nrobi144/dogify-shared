package com.nagyrobi144.dogify.di

import com.nagyrobi144.dogify.api.BreedsApi
import com.nagyrobi144.dogify.repository.BreedsRemoteSource
import com.nagyrobi144.dogify.usecase.FetchBreedsUseCase
import com.nagyrobi144.dogify.util.DispatcherProvider
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

internal expect fun getDispatcherProvider(): DispatcherProvider

private val utilityModule = module {
    factory { getDispatcherProvider() }
}

private val apiModule = module {
    factory { BreedsApi() }
}

private val repositoryModule = module {
    factory { BreedsRemoteSource(get(), get()) }
}

private val useCaseModule = module {
    factory { FetchBreedsUseCase(get()) }
}

private val commonModules =
    listOf(utilityModule, apiModule, repositoryModule, useCaseModule)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModules)
}
