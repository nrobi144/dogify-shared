package com.nagyrobi144.dogify.usecase

import com.nagyrobi144.dogify.repository.BreedsRemoteSource
import com.nagyrobi144.dogify.repository.model.Breed
import com.nagyrobi144.dogify.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

/**
 * Gets the names of Breeds and complements it with the breed images
 * The result can either be
 * [Result.Success] if both the breeds and all the images were retrieved
 * [Result.Partial] if getting some of the images weren't successfully retrieved
 * [Result.Error] if the list of breed names can't be retrieved
 */
internal class FetchBreedsUseCase(private val remoteSource: BreedsRemoteSource) {

    suspend operator fun invoke() = Result {
        supervisorScope {
            remoteSource.getBreeds().map { breedName ->
                async { Breed(breedName, remoteSource.getBreedImage(breedName)) }
            }.mapNotNull {
                try {
                    it.await()
                } catch (e: Throwable) {
                    isPartial = true
                    null
                }
            }
        }
    }

}