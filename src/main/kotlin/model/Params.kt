package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 登录参数
 */
@Serializable
data class LoginParams(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)

/**
 * 专题作业签到参数
 */
@Serializable
data class SpecialParams(
    @SerialName("step")
    val step: Int,
    @SerialName("specialId")
    val specialId: String
)

/**
 * 假期作业签到参数
 */
@Serializable
data class HolidayParams(
    @SerialName("step")
    val step: Int,
    @SerialName("semester")
    val semester: Int,
    @SerialName("schoolYear")
    val schoolYear: Int
)

/**
 * 知识测试签到参数
 */
@Serializable
data class KnowledgeTestParams(
    @SerialName("fid")
    val fid: String,
    @SerialName("workid")
    val wid: String,
    @SerialName("CourseID")
    val cid: String,
    @SerialName("title")
    val title: String = "",
    @SerialName("require")
    val require: String = "",
    @SerialName("purpose")
    val purpose: String = "",
    @SerialName("contents")
    val contents: String = "",
    @SerialName("testinfo")
    val testInfo: String = "",
    @SerialName("testMark")
    val testMark: Int = 100,
    @SerialName("testReulst")
    val testResult: Int = 1,
    @SerialName("testwanser")
    val testWanSer: String = "",
    @SerialName("WatchTime")
    val watchTime: String = "",
    @SerialName("SiteName")
    val siteName: String = "",
    @SerialName("SiteAddrees")
    val siteAddress: String = ""
)

/**
 * 家校互动签到参数
 */
@Serializable
data class SchoolHomeInteractionParams(
    @SerialName("gid")
    val gid: String,
    @SerialName("vId")
    val vid: String,
    @SerialName("courseid")
    val cid: String,
    @SerialName("userid")
    val userId: String,
    @SerialName("cityCode")
    val cityCode: String,
    @SerialName("comeFrom")
    val comeFrom: String
)