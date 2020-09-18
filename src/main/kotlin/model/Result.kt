package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 登录返回结果
 */
@Serializable
data class LoginResult(
    @SerialName("err_code")
    val errCode: Int,
    @SerialName("err_desc")
    val errDesc: String,
    @SerialName("data")
    val data: UserInfo?
)

/**
 * 专题活动返回结果
 */
@Serializable
data class SpecialResult(
    @SerialName("result")
    val result: Boolean
)

/**
 * 假期作业返回结果
 */
@Serializable
data class HolidayResult(
    @SerialName("result")
    val result: Boolean
)

/**
 * 家校互动返回结果
 */
@Serializable
data class SchoolHomeInteractionResult(
    @SerialName("d")
    val result: Boolean
)