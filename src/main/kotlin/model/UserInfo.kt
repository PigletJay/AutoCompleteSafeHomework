package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * 用户信息
 */
@Serializable
data class UserInfo(
    @SerialName("cityId")
    val cityId: Int,
    @SerialName("comefrom")
    val comefrom: Int,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("accessCookie")
    val accessCookie: String,
    @SerialName("webUrl")
    val webUrl: String,
    @Transient
    @SerialName("serviceUrl")
    var serviceUrl: String? = null
)