package com.nagyrobi144.dogify.util

/**
 * Wrapper class exposing a given data type of [T] to the UI layer from the Data layer(use-case, repository, source).
 * Can represent multiple states: [Success], [Partial] and [Error]
 */
internal sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Partial<out T>(val value: T) : Result<T>()
    data class Error<out T>(val exception: Throwable) : Result<T>()

    /**
     * Represents the current context of the [Result]
     * The running suspend function can decide at any moment whether the operation
     * was only partially successful by modifying the [isPartial] parameter
     */
    internal class Context {
        var isPartial = false
    }

    companion object {
        /**
         * Wrap a given [function] into a [Result] of either
         * [Result.Success] in case the function succeeds,
         * [Result.Partial] if it's only partially successful, or
         * [Result.Error] in case of a known [Exception]
         */
        suspend operator fun <T> invoke(function: suspend Context.() -> T): Result<T> = try {
            with(Context()) {
                val result = function(this)
                if (isPartial) {
                    Partial(result)
                } else {
                    Success(result)
                }
            }
        } catch (exception: Exception) {
            Error(exception)
        }
    }
}