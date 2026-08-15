package com.cristianwer.pepinillorick.domain.model

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

internal enum class CharacterStatus(val value: String) {
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("unknown");

    companion object {
        fun fromString(value: String): CharacterStatus {
            return entries.find { it.value.lowercase() == value.lowercase() } ?: UNKNOWN
        }
    }
}

internal enum class CharacterGender(val value: String) {
    FEMALE("Female"),
    MALE("Male"),
    GENDERLESS("Genderless"),
    UNKNOWN("unknown");

    companion object {
        fun fromString(value: String): CharacterGender {
            return entries.find { it.value.lowercase() == value.lowercase() } ?: UNKNOWN
        }
    }
}

internal data class Location(
    val name: String,
    val url: String
)
