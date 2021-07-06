package com.nagyrobi144.dogify.repository

import com.nagyrobi144.dogify.db.DogifyDatabase
import com.nagyrobi144.dogify.repository.model.Breed
import com.nagyrobi144.dogify.util.DispatcherProvider
import kotlinx.coroutines.withContext

internal class BreedsLocalSource(
    database: DogifyDatabase,
    private val dispatcherProvider: DispatcherProvider
) {
    private val dao = database.breedsQueries

    suspend fun selectAll() = withContext(dispatcherProvider.io) {
        dao.selectAll { name, imageUrl, isFavourite -> Breed(name, imageUrl, isFavourite ?: false) }
    }

    suspend fun insert(breed: Breed) = withContext(dispatcherProvider.io) {
        dao.insert(breed.name, breed.imageUrl, breed.isFavourite)
    }

    suspend fun update(breed: Breed) = withContext(dispatcherProvider.io) {
        dao.update(breed.imageUrl, breed.isFavourite, breed.name)
    }

    suspend fun clear() = withContext(dispatcherProvider.io) {
        dao.clear()
    }
}