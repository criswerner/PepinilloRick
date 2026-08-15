package com.cristianwer.pepinillorick.domain.model

/**
 * Domain representation of a Rick & Morty character.
 *
 * @property id The unique identifier of the character.
 * @property name The name of the character.
 * @property status The survival status of the character (Alive, Dead or unknown).
 * @property species The species of the character.
 * @property type The type or subspecies of the character.
 * @property gender The gender of the character (Female, Male, Genderless or unknown).
 * @property origin Name and link to the character's origin location.
 * @property location Name and link to the character's last known location endpoint.
 * @property imageUrl Link to the character's image.
 * @property episodes List of episodes in which this character appeared.
 */
internal data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: Location,
    val location: Location,
    val imageUrl: String,
    val episodes: List<String>
)

/**
 * Represents the possible states of a character's life.
 *
 * @property value The raw string representation from the API.
 */
internal enum class CharacterStatus(val value: String) {
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("unknown");

    companion object {
        /**
         * Maps a string value to a [CharacterStatus].
         *
         * @param value The string to map.
         * @return The corresponding [CharacterStatus] or [UNKNOWN] if no match is found.
         */
        fun fromString(value: String): CharacterStatus {
            return entries.find { it.value.lowercase() == value.lowercase() } ?: UNKNOWN
        }
    }
}

/**
 * Represents the possible genders of a character.
 *
 * @property value The raw string representation from the API.
 */
internal enum class CharacterGender(val value: String) {
    FEMALE("Female"),
    MALE("Male"),
    GENDERLESS("Genderless"),
    UNKNOWN("unknown");

    companion object {
        /**
         * Maps a string value to a [CharacterGender].
         *
         * @param value The string to map.
         * @return The corresponding [CharacterGender] or [UNKNOWN] if no match is found.
         */
        fun fromString(value: String): CharacterGender {
            return entries.find { it.value.lowercase() == value.lowercase() } ?: UNKNOWN
        }
    }
}

/**
 * Domain representation of a location or origin.
 *
 * @property name The name of the location.
 * @property url Link to the location's endpoint.
 */
internal data class Location(
    val name: String,
    val url: String
)
