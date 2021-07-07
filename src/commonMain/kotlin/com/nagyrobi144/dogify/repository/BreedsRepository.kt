package com.nagyrobi144.dogify.repository

import com.nagyrobi144.dogify.repository.model.Breed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

class BreedsRepository internal constructor(
    private val remoteSource: BreedsRemoteSource,
    private val localSource: BreedsLocalSource
) {

    val breeds = localSource.breeds

    suspend fun get() = localSource.selectAll().isNullOrEmpty() ?: fetch()

    suspend fun fetch() = supervisorScope {
        remoteSource.getBreeds().map { breedName ->
            async { Breed(breedName, remoteSource.getBreedImage(breedName)) }
        }.map {
            try {
                it.await()
            } catch (e: Throwable) {
                null
            }
        }.also { breeds ->
            breeds.mapNotNull { async { localSource.insert(it ?: return@async) } }.awaitAll()
        }
    }

    suspend fun update(breed: Breed) = localSource.update(breed)

    suspend fun clear() = localSource.clear()
}