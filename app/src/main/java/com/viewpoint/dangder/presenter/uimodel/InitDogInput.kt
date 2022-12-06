package com.viewpoint.dangder.presenter.uimodel

import android.net.Uri

data class InitDogInput(
    val age : Int,
    val description: String,
    val interests : Array<String>? = emptyArray(),
    val characters : Array<String>? = emptyArray(),
    val images : Array<Uri>,
    val userId : String,
    val lat : Double,
    val lng : Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InitDogInput

        if (age != other.age) return false
        if (description != other.description) return false
        if (interests != null) {
            if (other.interests == null) return false
            if (!interests.contentEquals(other.interests)) return false
        } else if (other.interests != null) return false
        if (characters != null) {
            if (other.characters == null) return false
            if (!characters.contentEquals(other.characters)) return false
        } else if (other.characters != null) return false
        if (!images.contentEquals(other.images)) return false
        if (userId != other.userId) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false

        return true
    }

    override fun hashCode(): Int {
        var result = age
        result = 31 * result + description.hashCode()
        result = 31 * result + (interests?.contentHashCode() ?: 0)
        result = 31 * result + (characters?.contentHashCode() ?: 0)
        result = 31 * result + images.contentHashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        return result
    }
}
