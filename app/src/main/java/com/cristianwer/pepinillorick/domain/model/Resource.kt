package com.cristianwer.pepinillorick.domain.model

/**
 * A generic class that holds a value with its loading status.
 *
 * @param T The type of the data.
 * @property data The data held by this resource.
 * @property message The error message if any.
 */
internal sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful operation with data.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents a loading state, optionally with cached data.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Represents an error state, optionally with cached data.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
