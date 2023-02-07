package ch.skyfy.ghuperms.config

import ch.skyfy.json5configlib.Defaultable
import ch.skyfy.json5configlib.Validatable
import io.github.xn32.json5k.SerialComment
import kotlinx.serialization.Serializable

@Serializable
data class PermissionsData(
    @SerialComment("A list of all available permission")
    val list: MutableSet<String>
) : Validatable

class DefaultPermissionsData : Defaultable<PermissionsData>{
    override fun getDefault(): PermissionsData {
        return PermissionsData(mutableSetOf())
    }
}