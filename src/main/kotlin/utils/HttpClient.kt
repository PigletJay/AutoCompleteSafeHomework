package utils

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import utils.ApiService.userInfo
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

object HttpClient {

    //默认Json
    private val defJson = Json { ignoreUnknownKeys = true }

    /**
     * GET
     */
    suspend inline fun <reified T> get(url: String, params: String = ""): T =
        doRequest("$url$params", null, serializer())

    /**
     * POST
     */
    suspend inline fun <reified T> post(url: String, params: Any): T =
        doRequest(url, params, serializer())

    /**
     * 请求
     */
    @Suppress("UNCHECKED_CAST", "BlockingMethodInNonBlockingContext")
    suspend fun <T> doRequest(url: String, params: Any?, serializer: KSerializer<T>): T = withContext(IO) {
        var urlConn: HttpURLConnection? = null
        try {
            //替换域名
            val requestUrl = url
                .replace("{__weburl__}", "${userInfo?.webUrl}")
                .replace("{__serviceurl__}", "${userInfo?.serviceUrl}")
            //打开连接
            urlConn = URL(requestUrl).openConnection() as HttpURLConnection
            urlConn.apply {
                //设置读取超时10s
                readTimeout = 10_000
                //设置连接超时10s
                connectTimeout = 10_000
                //设置请求属性
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Cookie", "UserID=${userInfo?.accessCookie}")
                setRequestProperty("Authorization", "Bearer ${userInfo?.accessToken}")
                //判断请求方法
                if (params != null) {
                    doOutput = true
                    requestMethod = "POST"
                    //转换为Json参数
                    val jsonParams = defJson.encodeToString(params::class.serializer() as KSerializer<Any>, params)
                    //写入数据
                    outputStream.use { it.write(jsonParams.toByteArray()) }
                }
            }
            //转话响应数据
            val response = if (urlConn.contentEncoding == "gzip") {
                GZIPInputStream(urlConn.inputStream)
            } else {
                urlConn.inputStream
            }.reader().use {
                it.readText()
            }
            //按类型返回数据
            if (serializer.descriptor.serialName == "kotlin.String") {
                response as T
            } else {
                defJson.decodeFromString(serializer, response)
            }
        } finally {
            urlConn?.disconnect()
        }
    }
}