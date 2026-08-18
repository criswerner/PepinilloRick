package com.cristianwer.pepinillorick.domain.model

/**
 * Represents specific types of errors that can be handled in the UI.
 */
internal sealed interface UiError {
    /**
     * Error caused by lack of internet connectivity or timeout.
     */
    data object Connection : UiError

    /**
     * Error returned by the server (e.g., 500, 404).
     * @property code The HTTP status code if available.
     */
    data class Server(val code: Int? = null) : UiError

    /**
     * An unexpected error.
     * @property message Descriptive message of the error.
     */
    data class Unknown(val message: String? = null) : UiError
}

/**
 * A generic class that holds a value with its loading status.
 *
 * @param T The type of the data.
 * @property data The data held by this resource.
 * @property error The structured error if any.
 */
internal sealed class Resource<T>(
    val data: T? = null,
    val error: UiError? = null
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
    class Error<T>(val uiError: UiError, data: T? = null) : Resource<T>(data, uiError)
}
